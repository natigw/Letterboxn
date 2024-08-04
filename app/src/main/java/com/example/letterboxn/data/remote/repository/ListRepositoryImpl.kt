package com.example.letterboxn.data.remote.repository

import com.example.letterboxn.domain.model.ListItem
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.domain.model.ListPersonItem
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.ListRepository
import javax.inject.Inject

class ListRepositoryImpl @Inject constructor(
    private val api : TmdbApi
) : ListRepository {

    override suspend fun getLists(): List<ListItem> {

        val response = api.getLists()
        return response.results.map {
            ListItem(
                listTitle = it.name,
                authorName = "Author",
                authorImage = it.posterPath,
                likeCount = 100,
                commentCount = 21,
                movieItems = listOf(MovieItem(
                    movieId =  it.id,
                    movieTitle = it.originalName,
                    moviePoster = it.posterPath,
                    movieDescription = it.overview))
            )
        }
    }

    override suspend fun getUser(): List<ListPersonItem> {
        val respnse = api.getPeople(3)
        return respnse.resultPeople.map {
            ListPersonItem(
                userName = it.name,
                userImage = it.profilePath
            )
        }
    }

}