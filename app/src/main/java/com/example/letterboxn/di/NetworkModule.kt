package com.example.letterboxn.di

import androidx.paging.PagingSource
import com.example.letterboxn.data.local.dao.ReviewDao
import com.example.letterboxn.presentation.adapters.MoviesPagingSource
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.data.remote.repository.ExploreRepositoryImpl
import com.example.letterboxn.data.remote.repository.ListRepositoryImpl
import com.example.letterboxn.data.remote.repository.MovieRepositoryImpl
import com.example.letterboxn.data.remote.repository.ReviewRepositoryImpl
import com.example.letterboxn.data.remote.repository.SearchRepositoryImpl
import com.example.letterboxn.domain.model.MovieItem
import com.example.letterboxn.domain.repository.ExploreRepository
import com.example.letterboxn.domain.repository.ListRepository
import com.example.letterboxn.domain.repository.MovieRepository
import com.example.letterboxn.domain.repository.ReviewRepository
import com.example.letterboxn.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitClient() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieApi(client : Retrofit) : TmdbApi {
        return client.create(TmdbApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(api : TmdbApi) : MovieRepository {
        return MovieRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun providePopularListsRepository(api : TmdbApi) : ListRepository {
        return ListRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideExploreMovieRepository(api : TmdbApi) : ExploreRepository {
        return ExploreRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(api : TmdbApi) : SearchRepository {
        return SearchRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun providePagingSource(api: TmdbApi) : PagingSource<Int, MovieItem> {
        return MoviesPagingSource(api)
    }

    @Singleton
    @Provides
    fun provideReviewRepository(reviewDao: ReviewDao) : ReviewRepository {
        return ReviewRepositoryImpl(reviewDao)
    }

}