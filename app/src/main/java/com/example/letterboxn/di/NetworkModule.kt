package com.example.letterboxn.di

import com.example.letterboxn.data.remote.api.TmdbApi
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


//    @Binds
//    abstract fun bindMovieRepository(impl: MovieRepositoryImpl) : MovieRepository

//    @Singleton
//    @Provides
//    fun provideMovieRepository(api : TmdbApi) : MovieRepository {
//        return MovieRepositoryImpl(api)
//    }

}