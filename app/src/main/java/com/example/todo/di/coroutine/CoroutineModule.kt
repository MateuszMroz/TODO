package com.example.todo.di.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(ViewModelComponent::class)
class CoroutineModule {

//    @JobCoroutinesIO
//    @Provides
//    fun provideJobCoroutineContextIO(
//        @IODispatcher ioDispatcher: CoroutineDispatcher
//    ): CoroutineContext = Job() + ioDispatcher


    @SupervisorJobCoroutinesIO
    @Provides
    fun provideSupervisorJobCoroutineContextIO(
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): CoroutineContext = SupervisorJob() + ioDispatcher

}