package com.example.movies.di

import android.app.Application
import android.content.Context
import com.example.movies.network.TheMovieDbService
import com.example.movies.network.TheMovieDbServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ModuleMovie {

    @Singleton
    @Provides
    fun provideTheMovieDbService(theMovieDbServiceImpl: TheMovieDbServiceImpl): TheMovieDbService {
        return theMovieDbServiceImpl
    }
}