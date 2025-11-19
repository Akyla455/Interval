package com.example.intervalsapp.di

import com.example.intervalsapp.data.events.WorkoutEventBus
import com.example.intervalsapp.data.platform.ServiceStarterImpl
import com.example.intervalsapp.data.platform.SoundPlayerImpl
import com.example.intervalsapp.data.repository.IntervalTimerRepositoryImpl
import com.example.intervalsapp.data.repository.WorkoutStateRepositoryImpl
import com.example.intervalsapp.domain.events.WorkoutObserver
import com.example.intervalsapp.domain.events.WorkoutPublisher
import com.example.intervalsapp.domain.platform.ServiceStarter
import com.example.intervalsapp.domain.platform.SoundPlayer
import com.example.intervalsapp.domain.repository.IntervalTimerRepository
import com.example.intervalsapp.domain.repository.WorkoutStateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindRepo(
        impl: IntervalTimerRepositoryImpl
    ): IntervalTimerRepository

    @Binds
    abstract fun bindEventBus(
        impl: WorkoutEventBus
    ): WorkoutPublisher

    @Binds
    abstract fun bindObserver(
        impl: WorkoutEventBus
    ): WorkoutObserver

    @Binds
    abstract fun bindSound(
        impl: SoundPlayerImpl
    ): SoundPlayer

    @Binds
    abstract fun bindServiceStarter(
        impl: ServiceStarterImpl
    ): ServiceStarter

    @Binds
    abstract fun bindWorkoutStateRepository(
        impl: WorkoutStateRepositoryImpl
    ): WorkoutStateRepository
}