package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.ListItem

interface ListRepository {
    suspend fun getLists() : List<ListItem>
}