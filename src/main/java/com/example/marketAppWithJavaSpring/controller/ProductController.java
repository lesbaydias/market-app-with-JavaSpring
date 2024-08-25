package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.ProductResponseDto;
import com.example.marketAppWithJavaSpring.model.Sale;
import com.example.marketAppWithJavaSpring.service.ProductService;
import com.example.marketAppWithJavaSpring.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final SaleService saleService;
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllClass();
    }
    @GetMapping("/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDto> getProductsByCategory(@RequestParam String category) {
        return productService.getByCategory(category);
    }


    @GetMapping("/showThreeTheMostPopularProduct")
    @ResponseStatus(HttpStatus.OK)
    public List<Object[]> showThreeTheMostPopularProduct(){
        return productService.getThreeTheMostPopularProduct();
    }


    @GetMapping("/show-all-sales-products")
    @ResponseStatus(HttpStatus.OK)
    public List<Sale> showAllSalesProducts(){
        return saleService.getAllSalesProducts();
    }


    @GetMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductsByProductId(@RequestParam Long id) {
        return productService.getProductByIdResponse(id);
    }


}


