package com.example.letterboxn.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.letterboxn.data.local.dao.ReviewDao
import com.example.letterboxn.data.local.dao.ReviewDatabase
import com.example.letterboxn.data.local.dao.UserDao
import com.example.letterboxn.data.local.dao.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext appContext: Context): UserDatabase {
        return Room.databaseBuilder(
            appContext,
            UserDatabase::class.java,
            "user_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.getUserDao()
    }

    @Singleton
    @Provides
    fun provideReviewDatabase(@ApplicationContext appContext: Context): ReviewDatabase {
        return Room.databaseBuilder(
            appContext,
            ReviewDatabase::class.java,
            "review_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideReviewDao(reviewDatabase: ReviewDatabase): ReviewDao {
        return reviewDatabase.getReviewDao()
    }


    @Named("UserLoggedIn")
    @Provides
    fun provideSharedPreferenceLogon(@ApplicationContext context: android.content.Context) : SharedPreferences {
        return context.getSharedPreferences("userloggedin", android.content.Context.MODE_PRIVATE)
    }

    @Named("UserProfileImage")
    @Provides
    fun provideSharedPreferenceUserProfile(@ApplicationContext context: android.content.Context) : SharedPreferences {
        return context.getSharedPreferences("userProfileImage", android.content.Context.MODE_PRIVATE)
    }

    @Named("UserBackPosterImage")
    @Provides
    fun provideSharedPreferenceUserBackPoster(@ApplicationContext context: android.content.Context) : SharedPreferences {
        return context.getSharedPreferences("userBackPosterImage", android.content.Context.MODE_PRIVATE)
    }
    @Named("UserBackPosterIsDefault")
    @Provides
    fun provideSharedPreferenceBackPosterIsDefault(@ApplicationContext context: android.content.Context) : SharedPreferences {
        return context.getSharedPreferences("userBackPosterIsDefault", android.content.Context.MODE_PRIVATE)
    }

    @Named("UserStatusinApp")
    @Provides
    fun provideSharedPreferenceStatus(@ApplicationContext context: android.content.Context) : SharedPreferences {
        return context.getSharedPreferences("userStatus", android.content.Context.MODE_PRIVATE)
    }

}