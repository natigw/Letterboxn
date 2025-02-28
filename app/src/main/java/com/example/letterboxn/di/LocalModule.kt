package com.example.letterboxn.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.letterboxn.data.local.database.review.ReviewDao
import com.example.letterboxn.data.local.database.review.ReviewDatabase
import com.example.letterboxn.data.local.database.user.UserDao
import com.example.letterboxn.data.local.database.user.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {

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
    fun provideSharedPreferenceLogon(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("userloggedin", Context.MODE_PRIVATE)
    }

    @Named("UserProfileImage")
    @Provides
    fun provideSharedPreferenceUserProfile(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("userProfileImage", Context.MODE_PRIVATE)
    }

    @Named("UserBackPosterImage")
    @Provides
    fun provideSharedPreferenceUserBackPoster(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("userBackPosterImage", Context.MODE_PRIVATE)
    }
    @Named("UserBackPosterIsDefault")
    @Provides
    fun provideSharedPreferenceBackPosterIsDefault(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("userBackPosterIsDefault", Context.MODE_PRIVATE)
    }

    @Named("UserStatusInApp")
    @Provides
    fun provideSharedPreferenceStatus(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("userStatus", Context.MODE_PRIVATE)
    }

}