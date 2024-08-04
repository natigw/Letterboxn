package com.example.letterboxn.domain.model

data class ListWithAuthor(
    val listItems: List<ListItem>,
    val author: List<ListPersonItem>
)