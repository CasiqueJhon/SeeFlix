package com.example.movies.di

import dagger.Component
import dagger.hilt.DefineComponent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@DefineComponent(parent = SingletonComponent::class)
@Component(
    modules = [
        ModuleMovieRepository::class,
        ModuleMovie::class
    ]
)
@Singleton
interface ApplicationComponent
