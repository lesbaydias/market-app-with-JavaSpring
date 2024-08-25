package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.dto.ProductDto;
import com.example.marketAppWithJavaSpring.dto.ProductResponseDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.enums.TypeOfProduct;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Category;
import com.example.marketAppWithJavaSpring.model.Product;
import com.example.marketAppWithJavaSpring.model.Seller;
import com.example.marketAppWithJavaSpring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final SellerService sellerService;
    public List<ProductResponseDto> getAllClass() {
        return productRepository.findAll()
                .stream()
                .map(this::mapProductToDto)
                .collect(Collectors.toList());
    }

    public List<Object[]> getThreeTheMostPopularProduct(){
        List<Object[]> list = productRepository.findByAllProductWithRating();
        if(list.isEmpty())
            throw new ServiceException(
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    ErrorMessage.LIST_IS_EMPTY.getStatus()
            );

        return list;
    }
    public Product getProductsByProductId(Long id){
        return productRepository.findProductsByProductId(id).orElseThrow(() ->
                new ServiceException(
                        String.format(ErrorMessage.PRODUCT_IS_NOT_FOUND.getMessage(), id),
                        ErrorMessage.PRODUCT_IS_NOT_FOUND.getStatus()
                )
        );
    }

    @Transactional
    public void deleteProductByProductId(Long productId){
        productRepository.deleteProductByProductId(productId);
    }
    public ProductResponseDto getProductByIdResponse(Long id){
        Product product = getProductsByProductId(id);
        return mapProductToDto(product);
    }

    public Optional<Product> getProductsByProductNameAndSellerId(String productName, Long sellerId){
        return productRepository.findProductsByProductNameAndSellerSellerId(productName, sellerId);
    }

    public List<ProductResponseDto> getByCategory(String categoryTitle){
        List<Product> products = productRepository.findByCategory(categoryTitle);

        if (products.isEmpty())
            throw new ServiceException(
                    String.format(ErrorMessage.CATEGORY_IS_NOT_FOUND.getMessage(), categoryTitle),
                    ErrorMessage.CATEGORY_IS_NOT_FOUND.getStatus()
            );

        return products.stream()
                .map(this::mapProductToDto)
                .collect(Collectors.toList());
    }

    public ProductDto save(ProductDto productDto, MultipartFile image, UserDetails userDetails) throws IOException {
        log.info("Received product {} in added items by seller {}", productDto.getProductName(), userDetails.getUsername());

        Category category = getCategoryFromDto(productDto);
        Seller seller = getSellerFromDto(userDetails.getUsername());

        validateProductUniqueness(productDto.getProductName(), seller.getSellerId());

        String image_name = imageService.upload(image);

        Product newProduct = buildProductFromDto(productDto, category, seller, image_name);

        productRepository.save(newProduct);
        log.info("Successfully add product = {} by seller = {}", newProduct.getProductName(), seller.getSellerName());
        return productDto;
    }

    public List<ProductResponseDto> getSellerWithAllProducts(UserDetails userDetails){
        Seller seller = getSellerFromDto(userDetails.getUsername());

        return productRepository.findSellerWithAllProducts(seller.getSellerId())
                .stream()
                .map(this::mapProductToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProductFromDatabase(UserDetails userDetails, Long productId){
        log.info("Received data to delete product with id {}", productId);
        Seller seller = getSellerFromDto(userDetails.getUsername());
        Product product = getProductsByProductId(productId);

        validateSellerOwnsProduct(seller, product);

        productRepository.deleteProductByProductId(productId);
        log.info("Successfully deleted product with id {}", productId);
    }
    public void addQuantity(Long productId, Long quantity, UserDetails userDetails){
        log.info("Received data to add {} quantity to product with id {}", quantity, productId);
        Seller seller = getSellerFromDto(userDetails.getUsername());
        Product product = getProductsByProductId(productId);

        validateSellerOwnsProduct(seller, product);

        product.setQuantityOrWeight(product.getQuantityOrWeight() + quantity);
        productRepository.save(product);
        log.info("Successfully added {} quantity to product with id {}", quantity, productId);
    }




    private void validateSellerOwnsProduct(Seller seller, Product product) {
        if (!seller.getSellerId().equals(product.getSeller().getSellerId())) {
            throw new ServiceException(
                    String.format(ErrorMessage.PRODUCT_IS_NOT_FOUND.getMessage(), product.getProductId()),
                    ErrorMessage.PRODUCT_IS_NOT_FOUND.getStatus()
            );
        }
    }
    private ProductResponseDto mapProductToDto(Product product) {
        return ProductResponseDto.builder()
                .product_name(product.getProductName())
                .description(product.getDescription())
                .type_of_product(product.getTypeOfProduct().name())
                .category(product.getCategory().getCategoryTitle())
                .image(product.getImage())
                .price(product.getPrice())
                .seller_name(product.getSeller().getSellerName())
                .seller_username(product.getSeller().getUsername())
                .seller_phone_number(product.getSeller().getPhoneNumber())
                .build();
    }
    private Category getCategoryFromDto(ProductDto productDto) {
        return categoryService.getByCategoryTitle(productDto.getCategory().toUpperCase());
    }

    private Seller getSellerFromDto(String username) {
        return sellerService.findByUsername(username);
    }

    private TypeOfProduct weightOrQuantity(String weightOrQuantity){
        return weightOrQuantity.equalsIgnoreCase(TypeOfProduct.Weight.name()) ? TypeOfProduct.Weight : TypeOfProduct.Quantity;
    }
    private Product buildProductFromDto(ProductDto productDto, Category category, Seller seller, String imageName) {
        TypeOfProduct type = weightOrQuantity(productDto.getSelectTypeOfProduct());
        return Product.builder()
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .category(category)
                .image(imageName)
                .price(productDto.getPrice())
                .seller(seller)
                .typeOfProduct(type)
                .quantityOrWeight(productDto.getQuantityOrWeight())
                .build();
    }
    private void validateProductUniqueness(String productName, Long sellerId) {
        getProductsByProductNameAndSellerId(productName, sellerId).ifPresent(product -> {
            throw new ServiceException(
                    String.format(ErrorMessage.PRODUCT_NAME_IS_ALREADY_EXIST.getMessage(), productName),
                    ErrorMessage.PRODUCT_NAME_IS_ALREADY_EXIST.getStatus()
            );
        });
    }

}