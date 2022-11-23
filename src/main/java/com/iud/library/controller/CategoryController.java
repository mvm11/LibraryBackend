package com.iud.library.controller;


import com.iud.library.dto.CategoryDTO;
import com.iud.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/findAllCategories")
    public List<CategoryDTO> findAllCategorie(){

        return categoryService.findAllCategories();

    }

    @GetMapping("/findCategoryById/categoryId/{categoryId}")
    public ResponseEntity<CategoryDTO> findCategoryById(
            @PathVariable(value = "categoryId") Integer categoryId
    ){
        CategoryDTO categoryDTO = categoryService.findCategoryById(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveCategory")
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO){

        return new ResponseEntity<>(categoryService.saveCategory(categoryDTO), HttpStatus.CREATED);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateCategory/categoryId/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Integer categoryId, @RequestBody CategoryDTO categoryDTO){

        return new ResponseEntity<>(categoryService.updateCategory(categoryId, categoryDTO), HttpStatus.CREATED);
    }

    //Delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteCategory/categoryId/{categoryId}")
    ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("The publisher has been deleted successful",HttpStatus.NO_CONTENT);
    }
}
