package jf.janice.ainewsdaily.feature.sources.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jf.janice.ainewsdaily.core.di.NewsRetrofit
import jf.janice.ainewsdaily.feature.sources.data.network.SourceApi
import jf.janice.ainewsdaily.feature.sources.data.repository.SourceRepositoryImpl
import jf.janice.ainewsdaily.feature.sources.presentation.SourcesRepository
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
abstract class SourcesModule {

    @Binds
    @ViewModelScoped
    abstract fun bindSourcesRepository(
        sourceRepositoryImpl: SourceRepositoryImpl
    ): SourcesRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideSourcesApi(@NewsRetrofit retrofit: Retrofit) : SourceApi {
            return retrofit.create(SourceApi::class.java)
        }
    }
}