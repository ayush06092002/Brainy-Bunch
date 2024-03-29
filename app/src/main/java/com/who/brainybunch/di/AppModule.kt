package com.who.brainybunch.di

import com.who.brainybunch.network.QuestionApi
import com.who.brainybunch.repository.QuestionRepository
import com.who.brainybunch.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesQuestionRepository(questionApi: QuestionApi) = QuestionRepository(questionApi)
    @Singleton
    @Provides
    fun providesQuestionApi(): QuestionApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionApi::class.java)
    }
}