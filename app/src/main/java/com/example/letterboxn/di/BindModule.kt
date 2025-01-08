package com.example.letterboxn.di

import com.example.letterboxn.data.remote.repository.ExploreRepositoryImpl
import com.example.letterboxn.data.remote.repository.MovieRepositoryImpl
import com.example.letterboxn.data.remote.repository.ReviewRepositoryImpl
import com.example.letterboxn.data.remote.repository.SearchRepositoryImpl
import com.example.letterboxn.domain.repository.ExploreRepository
import com.example.letterboxn.domain.repository.MovieRepository
import com.example.letterboxn.domain.repository.ReviewRepository
import com.example.letterboxn.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class BindModule {

    @Binds
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl) : MovieRepository

    @Binds
    abstract fun bindExploreRepository(impl: ExploreRepositoryImpl) : ExploreRepository

    @Binds
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl) : SearchRepository

    @Binds
    abstract fun bindMReviewRepository(impl: ReviewRepositoryImpl) : ReviewRepository

}