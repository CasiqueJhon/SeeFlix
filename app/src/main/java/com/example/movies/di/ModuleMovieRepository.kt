package com.example.movies.di

import com.example.movies.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@Module
@InstallIn(ApplicationComponent::class)
object ModuleMovieRepository {
    @Provides
    fun provideMovieRepository(theMovieDbService: TheMovieDbServiceImpl) : MovieRepository {
        return MovieRepository(theMovieDbService)
    }
}