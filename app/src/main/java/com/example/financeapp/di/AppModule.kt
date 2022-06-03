package com.example.financeapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.financeapp.data.local.FinanceDao
import com.example.financeapp.data.local.FinanceDatabase
import com.example.financeapp.data.repository.FinanceRepositoryImpl
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app:Application,
        callback:FinanceDatabase.Callback
    ) = Room.databaseBuilder(app,FinanceDatabase::class.java,"finance_database")
        .addCallback(callback)
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideDao(db:FinanceDatabase)=db.financeDao()

    @Singleton
    @Provides
    fun provideRepositoryImpl(
        dao: FinanceDao
    )=FinanceRepositoryImpl(dao) as FinanceRepository

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope()= CoroutineScope(SupervisorJob())
}
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope