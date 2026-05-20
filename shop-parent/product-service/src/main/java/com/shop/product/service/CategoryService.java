package com.shop.product.service;

import com.shop.product.entity.Category;
import com.shop.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }
}
