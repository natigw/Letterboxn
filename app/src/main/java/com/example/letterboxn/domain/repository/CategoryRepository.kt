package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.CategoryItem

interface CategoryRepository {
    suspend fun getCategories() : List<CategoryItem>
}