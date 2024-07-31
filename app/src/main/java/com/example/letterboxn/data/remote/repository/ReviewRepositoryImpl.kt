package com.example.letterboxn.data.remote.repository

//class ReviewRepositoryImpl @Inject constructor(
//    private val api: MyApi
//) : ReviewRepository {
//
//    override suspend fun getReviews(): List<ReviewItem> {
//
//        val response = api.get()
//        return response.results.map {
//            ListItem(
//                listTitle = it.name,
//                authorName = "Author",
//                authorImage = it.posterPath,
//                likeCount = 100,
//                commentCount = 21,
//                movieItems = listOf(MovieItem(it.posterPath, it.originalName, it.overview, it.id))
//            )
//        }
//    }
//
//}