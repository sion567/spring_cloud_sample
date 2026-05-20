package com.shop.product.controller;

import com.shop.common.entity.Result;
import com.shop.product.entity.Category;
import com.shop.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }

    @GetMapping("/{parentId}")
    public Result<List<Category>> getCategoriesByParentId(@PathVariable Long parentId) {
        return Result.success(categoryService.getCategoriesByParentId(parentId));
    }
}
