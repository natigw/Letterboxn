package com.example.letterboxn.domain.repository

import com.example.letterboxn.domain.model.ListItem
import com.example.letterboxn.domain.model.ListPersonItem

interface ListRepository {
    suspend fun getLists() : List<ListItem>
    suspend fun getUser() : List<ListPersonItem>
}