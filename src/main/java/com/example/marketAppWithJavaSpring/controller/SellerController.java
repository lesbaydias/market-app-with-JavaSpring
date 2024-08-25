package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.ProductDto;
import com.example.marketAppWithJavaSpring.dto.ProductResponseDto;
import com.example.marketAppWithJavaSpring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final ProductService productService;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails profile(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute ProductDto productDto,
            @ModelAttribute MultipartFile image) throws IOException
    {
        return productService.save(productDto, image, userDetails);
    }

    @GetMapping("/see-all-own-products")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> seeAllOwnProducts(@AuthenticationPrincipal UserDetails userDetails) {
        return productService.getSellerWithAllProducts(userDetails);
    }
    @DeleteMapping("/delete-product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteProductFreomDatabase(@AuthenticationPrincipal UserDetails userDetails,@RequestParam Long productId) {
        productService.deleteProductFromDatabase(userDetails, productId);
        return ResponseEntity.ok("Successfully delete product with id " + productId);
    }
    @PostMapping("/add-quantity/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addQuantity(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long productId, @RequestParam Long quantity) {
        productService.addQuantity(productId, quantity, userDetails);
        return ResponseEntity.ok("Successfully added "+quantity+" quantity/weight to product with id " + productId);
    }

}
