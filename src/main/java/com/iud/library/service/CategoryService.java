package com.iud.library.service;

import com.iud.library.common.exception.NotFoundException;
import com.iud.library.dto.CategoryDTO;
import com.iud.library.entity.Category;
import com.iud.library.gateway.CategoryGateway;
import com.iud.library.repository.BookRepository;
import com.iud.library.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService implements CategoryGateway {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category category = convertDTOToCategory(categoryDTO);
        categoryRepository.save(category);
        return convertCategoryToDTO(category);
    }

    @Override
    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertCategoryToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findCategoryById(Integer categoryId) {
        Category category = getCategory(categoryId);
        return convertCategoryToDTO(category);
    }

    private Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("category", "id", categoryId));
    }

    @Override
    public CategoryDTO updateCategory(Integer categoryId, CategoryDTO categoryDTO) {
        Category foundCategory = getCategory(categoryId);
        foundCategory.setCategoryName(categoryDTO.getCategoryName());
        categoryRepository.save(foundCategory);
        return convertCategoryToDTO(foundCategory);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = getCategory(categoryId);
        categoryRepository.delete(category);
    }

    private CategoryDTO convertCategoryToDTO(Category category){return modelMapper.map(category, CategoryDTO.class);}

    private Category convertDTOToCategory(CategoryDTO categoryDTO){return modelMapper.map(categoryDTO, Category.class);}
}
