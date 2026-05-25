package com.shop.product.service;

import com.shop.product.entity.Category;
import com.shop.product.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setParentId(0L);
        category.setSort(1);
        category.setCreateTime(LocalDateTime.now());

        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
        verify(categoryRepository).findAll();
    }

    @Test
    void testGetCategoriesByParentId() {
        Category category = new Category();
        category.setId(2L);
        category.setName("Smartphones");
        category.setParentId(1L);
        category.setSort(1);

        when(categoryRepository.findByParentId(1L)).thenReturn(List.of(category));

        List<Category> result = categoryService.getCategoriesByParentId(1L);

        assertEquals(1, result.size());
        assertEquals("Smartphones", result.get(0).getName());
        verify(categoryRepository).findByParentId(1L);
    }
}
