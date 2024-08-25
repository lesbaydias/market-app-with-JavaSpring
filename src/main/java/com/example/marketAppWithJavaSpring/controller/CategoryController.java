package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.model.Category;
import com.example.marketAppWithJavaSpring.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<String> categories() {
        return categoryService.getAllClass()
                .stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }
}
