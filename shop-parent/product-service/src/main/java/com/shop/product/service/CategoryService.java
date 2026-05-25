package com.shop.product.service;

import com.shop.product.entity.Category;
import com.shop.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        log.debug("getAllCategories request");
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByParentId(Long parentId) {
        log.debug("getCategoriesByParentId request: parentId={}", parentId);
        return categoryRepository.findByParentId(parentId);
    }
}
