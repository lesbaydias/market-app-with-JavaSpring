package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.model.BasketProduct;
import com.example.marketAppWithJavaSpring.repository.BasketProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketProductService {
    private final BasketProductRepository basketProductRepository;
    public BasketProduct save(BasketProduct basketProduct){
        return  basketProductRepository.save(basketProduct);
    }
    @Transactional
    public void deleteBasketProductByProductId(Long basketProductId){
        basketProductRepository.deleteBasketProductByProductProductId(basketProductId);
    }

    @Transactional
    public void deleteBasketProductByProductId(BasketProduct basketProduct) {
        basketProductRepository.deleteById(basketProduct.getBasketProductId());
    }
}
