package com.example.movies.di

import com.example.movies.db.Favorite
import com.example.movies.db.FavoriteDao
import com.example.movies.network.TheMovieDbServiceImpl
import com.example.movies.repository.FavoriteRepository
import com.example.movies.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ApplicationComponent::class)
object ModuleMovieRepository {
    @Provides
    fun provideMovieRepository(
        theMovieDbService: TheMovieDbServiceImpl
    ): MovieRepository {
        return MovieRepository(theMovieDbService)
    }

}