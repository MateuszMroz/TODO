package com.example.todo.di.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(FragmentComponent::class, ViewModelComponent::class)
class CoroutineModule {

    @SupervisorJobCoroutinesIO
    @Provides
    fun provideSupervisorJobCoroutineContextIO(
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): CoroutineContext = SupervisorJob() + ioDispatcher

}