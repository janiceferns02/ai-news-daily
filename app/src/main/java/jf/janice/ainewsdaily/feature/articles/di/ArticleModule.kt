package jf.janice.ainewsdaily.feature.articles.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jf.janice.ainewsdaily.core.di.NewsRetrofit
import jf.janice.ainewsdaily.feature.articles.data.network.ArticleApi
import jf.janice.ainewsdaily.feature.articles.data.repository.ArticleRepositoryImpl
import jf.janice.ainewsdaily.feature.articles.presentation.ArticleRepository
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
abstract class ArticleModule {

    @Binds
    @ViewModelScoped
    abstract fun bindArticleRepository(
        articleRepositoryImpl: ArticleRepositoryImpl
    ): ArticleRepository

    companion object {
        @Provides
        @ViewModelScoped
        fun provideArticleApi(@NewsRetrofit retrofit: Retrofit): ArticleApi {
            return retrofit.create(ArticleApi::class.java)
        }
    }
}