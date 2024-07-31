package com.example.letterboxn.presentation.adapters

import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.SampleCategoryUnderSearchBinding
import com.example.letterboxn.domain.model.CategoryItem

class ExploreCategoriesAdapter(
    val onClick : (CategoryItem)->Unit
) : BaseAdapter<SampleCategoryUnderSearchBinding>(SampleCategoryUnderSearchBinding::inflate) {

    var categories = emptyList<CategoryItem>()

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindLight(binding: SampleCategoryUnderSearchBinding, position: Int) {
        val categoryitem = categories[position]
        binding.chipCategoryExplore.text = categoryitem.categoryName
        binding.chipCategoryExplore.setOnClickListener {
            onClick(categoryitem)
        }
    }

    fun updateAdapter(newcategories : List<CategoryItem>) {
        categories = newcategories
        notifyDataSetChanged()
    }
}