package jf.janice.ainewsdaily.core.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jf.janice.ainewsdaily.feature.ai.data.network.OpenAiApi
import jf.janice.ainewsdaily.feature.articles.data.network.ArticleApi
import jf.janice.ainewsdaily.feature.sources.data.network.SourceApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GptRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://newsapi.org/"
    private const val OPEN_AI_BASE_URL = "https://api.openai.com/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .callTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    @NewsRetrofit
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    @GptRetrofit
    fun provideGPTRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OPEN_AI_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideArticleApi(@NewsRetrofit retrofit: Retrofit): ArticleApi {
        return retrofit.create(ArticleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSourceApi(@NewsRetrofit retrofit: Retrofit): SourceApi {
        return retrofit.create(SourceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenAiApi(@GptRetrofit retrofit: Retrofit): OpenAiApi {
        return retrofit.create(OpenAiApi::class.java)
    }
}