package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.dto.BasketProductResponseDto;
import com.example.marketAppWithJavaSpring.dto.BasketResponseDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Basket;
import com.example.marketAppWithJavaSpring.model.BasketProduct;
import com.example.marketAppWithJavaSpring.model.Product;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketService {
    private final BasketRepository basketRepository;
    private final UserService userService;
    private final ProductService productService;
    private final BasketProductService basketProductService;

    private static final Long DEFAULT_QUANTITY = 1L;

    public Basket getUsersBasket(Long userId){
        Basket basket = basketRepository.findUsersBasket(userId);
        if(basket == null)
            throw new ServiceException(
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    ErrorMessage.LIST_IS_EMPTY.getStatus()
            );
        return basket;
    }

    public BasketResponseDto getBasket(UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        Basket basket = basketRepository.findUsersBasket(user.getUserId());

        if(basket == null || basket.getBasketProducts().isEmpty())
            throw new ServiceException(
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    ErrorMessage.LIST_IS_EMPTY.getStatus()
            );
        List<BasketProduct> list = basket.getBasketProducts();
        checkIfListIsEmpty(list);

        List<BasketProductResponseDto> basketProductResponseDtoList = list.stream()
                .map(bp -> BasketProductResponseDto.builder()
                        .basketProductId(bp.getBasketProductId())
                        .productName(bp.getProduct().getProductName())
                        .description(bp.getProduct().getDescription())
                        .price(bp.getProduct().getPrice())
                        .sellerUsername(bp.getProduct().getSeller().getUsername())
                        .sellerPhoneNumber(bp.getProduct().getSeller().getPhoneNumber())
                        .quantity(bp.getQuantity())
                        .build())
                .collect(Collectors.toList());


        return BasketResponseDto.builder()
                .basketId(basket.getBasketId())
                .username(user.getUsername())
                .basketProducts(basketProductResponseDtoList)
                .build();
    }

    public BasketResponseDto addToBasketProduct(UserDetails userDetails, Long id){
        log.info("An item with id={} was received and added to basket.", id);
        User user = userService.findByUsername(userDetails.getUsername());

        Product product = productService.getProductsByProductId(id);

        Basket basket = getUsersBasket(user.getUserId());

        List<BasketProduct> list = basket.getBasketProducts();

        boolean check = list.stream()
                .filter(value -> value.getProduct().equals(product))
                .peek(value -> checkProductAvailability(product, value))
                .findFirst()
                .isPresent();

        if(!check)
            addNewProductToBasket(product, basket, list);

        basketRepository.save(basket);
        log.info("An item with id={} was added to basket.", id);

        List<BasketProductResponseDto> basketProductResponseDtoList = list.stream()
                .map(bp -> BasketProductResponseDto.builder()
                        .basketProductId(bp.getBasketProductId())
                        .productName(bp.getProduct().getProductName())
                        .description(bp.getProduct().getDescription())
                        .price(bp.getProduct().getPrice())
                        .sellerUsername(bp.getProduct().getSeller().getUsername())
                        .sellerPhoneNumber(bp.getProduct().getSeller().getPhoneNumber())
                        .quantity(bp.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return BasketResponseDto.builder()
                .username(user.getUsername())
                .basketId(basket.getBasketId())
                .basketProducts(basketProductResponseDtoList)
                .build();
    }
    private void checkIfListIsEmpty(List<BasketProduct> list) {
        if (list.isEmpty())
            throw new ServiceException(
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    ErrorMessage.LIST_IS_EMPTY.getStatus()
            );
    }
    private void addNewProductToBasket(Product product, Basket basket, List<BasketProduct> list) {
        if (product.getQuantityOrWeight() < 1) {
            throw new ServiceException(
                    String.format(ErrorMessage.PRODUCT_IS_NOT_AVAILABLE.getMessage(), product.getProductName()),
                    ErrorMessage.PRODUCT_IS_NOT_AVAILABLE.getStatus()
            );
        }

        BasketProduct basketProduct = BasketProduct.builder()
                .product(product)
                .quantity(DEFAULT_QUANTITY)
                .id(basket.getBasketId())
                .build();

        basketProductService.save(basketProduct);
        list.add(basketProduct);
    }

    private void checkProductAvailability(Product product, BasketProduct value) {
        if (product.getQuantityOrWeight() - value.getQuantity() < 1)
            throw new ServiceException(
                    String.format(ErrorMessage.PRODUCT_IS_NOT_AVAILABLE.getMessage(), product.getProductName()),
                    ErrorMessage.PRODUCT_IS_NOT_AVAILABLE.getStatus()
            );
        value.setQuantity(value.getQuantity() + 1);
    }


    @Transactional
    public void deleteProductFromBasket(UserDetails userDetails){
        log.info("Successfully arrived username with id={}", userDetails.getUsername());

        User user = userService.findByUsername(userDetails.getUsername());
        Basket basket = getUsersBasket(user.getUserId());

        List<BasketProduct> list = basket.getBasketProducts();
        checkIfListIsEmpty(list);

        List<Long> ids = list.stream()
                .map(bp -> bp.getProduct().getProductId())
                .toList();

        if (!ids.isEmpty())
            for (Long productId : ids)
                basketProductService.deleteBasketProductByProductId(productId);

        else
            throw new ServiceException(
                    ErrorMessage.UNAVAILABLE_OPERATION.getMessage(),
                    ErrorMessage.UNAVAILABLE_OPERATION.getStatus()
            );
        log.info("Successfully deleted basket");
    }

    @Transactional
    public void deleteProductQuantityFromBasket(UserDetails userDetails, Long productId){
        User user = userService.findByUsername(userDetails.getUsername());
        Product product = productService.getProductsByProductId(productId);
        Basket basket = getUsersBasket(user.getUserId());

        List<BasketProduct> list = basket.getBasketProducts();
        checkIfListIsEmpty(list);

        if(!checkIfProductExist(product, list))
            throw new ServiceException(
                    String.format(ErrorMessage.PRODUCT_IS_NOT_FOUND.getMessage(), product.getProductName()),
                    ErrorMessage.PRODUCT_IS_NOT_FOUND.getStatus()
            );

        list.stream()
                .filter(bp -> isBasketProductForProduct(bp, product))
                .findFirst()
                .ifPresent(this::updateOrDeleteBasketProduct);

        log.info("Successfully deleted basket quantity");
    }

    private boolean checkIfProductExist(Product product, List<BasketProduct> list){
        return list.stream().anyMatch(bp -> bp.getProduct().equals(product));
    }

    private void updateOrDeleteBasketProduct(BasketProduct basketProduct) {
        if (basketProduct.getQuantity() > 1)
            basketProduct.setQuantity(basketProduct.getQuantity() - 1);
        else
            deleteBasketProductIfQuantityEqualsOne(basketProduct);
    }
    public void deleteBasketProductIfQuantityEqualsOne(BasketProduct basketProduct) {
        if (basketProduct.getQuantity() == 1L)
            basketProductService.deleteBasketProductByProductId(basketProduct.getProduct().getProductId());

    }
    private boolean isBasketProductForProduct(BasketProduct basketProduct, Product product) {
        return basketProduct.getProduct().equals(product);
    }
    public Basket createBasket(User user){
        log.info("Received user with username {} in create basket method ", user.getUsername());
        return Basket.builder()
                .basketProducts(new CopyOnWriteArrayList<>())
                .user(user)
                .build();
    }

    public Basket save(Basket basket){
        return basketRepository.save(basket);
    }
}
