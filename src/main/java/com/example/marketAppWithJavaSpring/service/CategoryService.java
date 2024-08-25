package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Category;
import com.example.marketAppWithJavaSpring.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<Category> getAllClass(){
        return categoryRepository.findAll();
    }

    public Category getByCategoryTitle(String categoryName){
        return categoryRepository.getByCategoryTitle(categoryName).orElseThrow(()->
                new ServiceException(
                        String.format(ErrorMessage.CATEGORY_IS_NOT_FOUND.getMessage(), categoryName),
                        ErrorMessage.CATEGORY_IS_NOT_FOUND.getStatus()
                )
        );
    }
}
