package jf.janice.ainewsdaily.feature.ai.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jf.janice.ainewsdaily.core.di.GptRetrofit
import jf.janice.ainewsdaily.feature.ai.data.network.OpenAiApi
import jf.janice.ainewsdaily.feature.ai.data.repository.AiRepositoryImpl
import jf.janice.ainewsdaily.feature.ai.presentation.model.AiRepository
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
abstract class AiModule {

    @Binds
    @ViewModelScoped
    abstract fun bindAiRepository(
        aiRepositoryImpl: AiRepositoryImpl
    ): AiRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideOpenAiApi(@GptRetrofit retrofit: Retrofit): OpenAiApi {
            return retrofit.create(OpenAiApi::class.java)
        }
    }
}