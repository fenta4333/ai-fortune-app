package com.aifortune.app.di

import com.aifortune.app.data.repository.FortuneRepositoryImpl
import com.aifortune.app.domain.repository.FortuneRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFortuneRepository(
        fortuneRepositoryImpl: FortuneRepositoryImpl
    ): FortuneRepository
}
