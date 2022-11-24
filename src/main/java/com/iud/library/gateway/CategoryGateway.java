package com.iud.library.gateway;

import com.iud.library.dto.CategoryDTO;

import java.util.List;

public interface CategoryGateway {

    CategoryDTO saveCategory(CategoryDTO categoryDTO);
    List<CategoryDTO> findAllCategories();
    CategoryDTO findCategoryById(Integer categoryId);
    CategoryDTO updateCategory(Integer categoryId, CategoryDTO categoryDTO);
    void deleteCategory(Integer categoryId);
}
