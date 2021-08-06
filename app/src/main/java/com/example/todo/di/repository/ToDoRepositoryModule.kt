package com.example.todo.di.repository

import com.example.todo.data.repository.IToDoRepository
import com.example.todo.data.repository.ToDoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class)
abstract class ToDoRepositoryModule {

    @Binds
    abstract fun provideToDoRepository(toDoRepository: ToDoRepository): IToDoRepository
}