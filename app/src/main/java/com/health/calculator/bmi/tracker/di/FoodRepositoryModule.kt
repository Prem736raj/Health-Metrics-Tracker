package com.health.calculator.bmi.tracker.di

import android.app.Application
import com.health.calculator.bmi.tracker.HealthCalculatorApp
import com.health.calculator.bmi.tracker.data.repository.FoodLogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Both legacy consumers and injected consumers must observe the same food state. */
@Module
@InstallIn(SingletonComponent::class)
object FoodRepositoryModule {
    @Provides
    @Singleton
    fun provideFoodLogRepository(application: Application): FoodLogRepository =
        (application as HealthCalculatorApp).foodLogRepository
}
