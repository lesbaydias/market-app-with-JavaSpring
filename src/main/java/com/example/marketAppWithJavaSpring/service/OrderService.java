package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.dto.OrderResponseDto;
import com.example.marketAppWithJavaSpring.dto.ProductResponseDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.enums.TypeOfPayment;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.*;
import com.example.marketAppWithJavaSpring.repository.OrderRepository;
import com.example.marketAppWithJavaSpring.validator.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final BasketService basketService;
    private final UserService userService;
    private final CardService cardService;
    private final CreditService creditService;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final SaleService saleService;
    private final BasketProductService basketProductService;
    private final SellerService sellerService;

    private final Time time = new Time();


    @Transactional
    public OrderResponseDto addOrder(UserDetails userDetails, String selectTypeOfPayment){
        User user = userService.findByUsername(userDetails.getUsername());
        Basket basket = basketService.getUsersBasket(user.getUserId());
        Card card = cardService.getCardByUserId(user.getUserId());

        List<BasketProduct> basketProducts = basket.getBasketProducts();
        validateBasketProducts(basketProducts);

        List<Product> products = new ArrayList<>();
        List<Long> quantities = new ArrayList<>();

        double total = processBasketProducts(basketProducts, products, quantities);

        Order order = createOrder(user, total, products);
        Payment payment = createPayment(order, selectTypeOfPayment, card, total);

        processDeleteBasket(basketProducts, products, quantities);

        updateSellerBalances(products, quantities);

        orderRepository.save(order);

        paymentService.save(payment);

        return createOrderResponseDto(order);

    }

    private double processBasketProducts(List<BasketProduct> basketProducts, List<Product> products, List<Long> quantities) {
        double total = 0.0;
        for (BasketProduct basketProduct : basketProducts) {
            validateProductAvailability(basketProduct.getProduct(), basketProduct.getQuantity());
            products.add(basketProduct.getProduct());
            quantities.add(basketProduct.getQuantity());

            double subTotal = calculateSubTotal(basketProduct);
            total += subTotal;
        }
        return total;
    }
    private double calculateSubTotal(BasketProduct basketProduct) {
        double subTotal = basketProduct.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(basketProduct.getQuantity()))
                .doubleValue();

        Sale sale = saleService.getSaleByProduct(basketProduct.getProduct().getProductId()).orElse(null);
        if (checkSaleIsAvailable(sale)) {
            subTotal *= (1.0 - sale.getPercentage());
        }
        return subTotal;
    }

    public List<OrderResponseDto> getAllOrders(UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());

        List<Order> orders = orderRepository.findOrdersByUserUserId(user.getUserId());

        validateOrdersNotEmpty(orders);

        return orders.stream()
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getOrderId())
                        .orderTime(order.getOrderTime())
                        .username(order.getUser().getUsername())
                        .totalAmount(order.getTotalAmount())
                        .products(createProductResponseDtoList(order.getProducts()))
                        .build())
                .collect(Collectors.toList());

    }
    private void validateOrdersNotEmpty(List<Order> orders) {
        if (orders.isEmpty()) {
            throw new ServiceException(
                    ErrorMessage.ORDER_IS_EMPTY.getMessage(),
                    ErrorMessage.ORDER_IS_EMPTY.getStatus()
            );
        }
    }
    private List<ProductResponseDto> createProductResponseDtoList(List<Product> products) {
        return products.stream()
                .map(this::createProductResponseDto)
                .collect(Collectors.toList());

    }
    private ProductResponseDto createProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .product_name(product.getProductName())
                .type_of_product(product.getTypeOfProduct().name())
                .seller_phone_number(product.getSeller().getPhoneNumber())
                .seller_username(product.getSeller().getUsername())
                .seller_name(product.getSeller().getSellerName())
                .description(product.getDescription())
                .image(product.getImage())
                .category(product.getCategory().getCategoryTitle())
                .price(product.getPrice())
                .build();
    }



    private void validateBasketProducts(List<BasketProduct> basketProducts) {
        if (basketProducts.isEmpty()) {
            throw new ServiceException(
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    ErrorMessage.LIST_IS_EMPTY.getStatus()
            );
        }
    }

    public boolean checkSaleIsAvailable(Sale sale){
        return sale != null;
    }

    public void processDeleteBasket(List<BasketProduct> basketProducts, List<Product> products, List<Long> quantities) {
        IntStream.range(0, products.size())
                .forEach(i -> {
                    Product product = products.get(i);
                    long updatedQuantityOrWeight = product.getQuantityOrWeight() - quantities.get(i);
                    product.setQuantityOrWeight(updatedQuantityOrWeight);
                });

        basketProducts
                .forEach(basketProductService::deleteBasketProductByProductId);
    }


    private void validateProductAvailability(Product product, long quantity) {
        if (product.getQuantityOrWeight() - quantity < 0) {
            throw new ServiceException(
                    String.format(ErrorMessage.PRODUCT_IS_NOT_AVAILABLE.getMessage(), product.getProductId()),
                    ErrorMessage.PRODUCT_IS_NOT_AVAILABLE.getStatus()
            );
        }
    }
    private void validatePayment(Card card, BigDecimal total) {
        if (card != null && card.getBalance().doubleValue() < total.doubleValue()) {
            throw new ServiceException(
                    String.format(ErrorMessage.NOT_ENOUGH_MONEY.getMessage(), total, card.getBalance()),
                    ErrorMessage.NOT_ENOUGH_MONEY.getStatus()
            );
        }
    }
    private Order createOrder(User user, double total, List<Product> products) {
        return Order.builder()
                .orderTime(time.dateNow())
                .user(user)
                .totalAmount(BigDecimal.valueOf(total))
                .products(products)
                .build();
    }
    private Payment createPayment(Order order, String selectTypeOfPayment, Card card, double total) {
        Payment payment = Payment.builder()
                .paymentAmount(BigDecimal.valueOf(total))
                .orders(order)
                .paymentTime(time.dateNow())
                .build();

        updateBalancesAndSavePayment(selectTypeOfPayment, card, payment);
        return payment;
    }
    private void updateBalancesAndSavePayment(String selectTypeOfPayment, Card card, Payment payment) {
        if (TypeOfPayment.DEBIT_CARD.name().equalsIgnoreCase(selectTypeOfPayment)) {
            validatePayment(card, payment.getPaymentAmount());
            updateBalancesForDebitCard(card, payment.getPaymentAmount());
            payment.setTypeOfPayment(TypeOfPayment.DEBIT_CARD);
        } else if(TypeOfPayment.CREDIT_CARD.name().equalsIgnoreCase(selectTypeOfPayment)){
            createAndSaveCredit(card, payment);
            payment.setTypeOfPayment(TypeOfPayment.CREDIT_CARD);
        }else
            throw new ServiceException(
                    String.format(ErrorMessage.PAYMENT_TYPE_IS_NOT_FOUND.getMessage(), selectTypeOfPayment),
                    ErrorMessage.PRODUCT_IS_NOT_FOUND.getStatus()
            );

        paymentService.save(payment);

        log.info("Successful payment by {} card", payment.getTypeOfPayment().name());
    }
    private void updateBalancesForDebitCard(Card card, BigDecimal total) {
        card.setBalance(card.getBalance().subtract(total));
    }

    private void createAndSaveCredit(Card card, Payment payment) {
        Credit credit = Credit.builder()
                .startTime(time.dateNow())
                .endTime(time.creditEndDate())
                .card(card)
                .monthlyPayment(payment.getPaymentAmount().divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP))
                .build();
        creditService.save(credit);
        log.info("Successfully paid by Credit card");
    }

    @Transactional
    public void updateSellerBalances(List<Product> products, List<Long> quantities) {
        IntStream.range(0, products.size())
                .forEach(i -> {
                    Product product = products.get(i);
                    Sale sale = saleService.getSaleByProduct(product.getProductId()).orElse(null);
                    Seller seller = sellerService.findByUsername(product.getSeller().getUsername());

                    double totalSeller = calculateTotalSeller(product, quantities.get(i), sale);
                    updateSellerBalance(seller, totalSeller);
                });
    }

    private double calculateTotalSeller(Product product, Long quantity, Sale sale) {
        double total = product.getPrice().multiply(BigDecimal.valueOf(quantity)).doubleValue();
        return checkSaleIsAvailable(sale) ? total * (1.0 - sale.getPercentage()) : total;
    }

    private void updateSellerBalance(Seller seller, double totalSeller) {
        seller.setBalance(seller.getBalance().add(BigDecimal.valueOf(totalSeller)));
    }

    private OrderResponseDto createOrderResponseDto(Order order) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        int i = 0;
        for (Product product : order.getProducts()) {
            ProductResponseDto productResponseDto = ProductResponseDto.builder()
                    .product_name(product.getProductName())
                    .type_of_product(product.getTypeOfProduct().name())
                    .seller_phone_number(product.getSeller().getPhoneNumber())
                    .seller_username(product.getSeller().getUsername())
                    .seller_name(product.getSeller().getSellerName())
                    .description(product.getDescription())
                    .image(product.getImage())
                    .category(product.getCategory().getCategoryTitle())
                    .price(product.getPrice())
                    .build();
            productResponseDtoList.add(productResponseDto);
            i++;
        }

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderTime(order.getOrderTime())
                .username(order.getUser().getUsername())
                .totalAmount(order.getTotalAmount())
                .products(productResponseDtoList)
                .build();
    }

}
