package com.lk.service.impl;

import com.lk.mapper.CategoryMapper;
import com.lk.pojo.Category;
import com.lk.pojo.custom.CategoryCustom;
import com.lk.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> getCategoryList() {
        try {
            return categoryMapper.selectCategoryList();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<CategoryCustom> getCategoryCustom() {
        try {
            return categoryMapper.selectCategoryCustom();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Category getCategory(int cid) {
        return categoryMapper.selectCategory(cid);
    }

    @Override
    public void insertCategory(Category category) {
        categoryMapper.insertCategory(category);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCateGory(int cid) {
        categoryMapper.deleteCategory(cid);
    }
}
