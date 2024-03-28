package com.who.brainybunch.network

import com.who.brainybunch.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("questions.json")
    suspend fun getQuestions(): Question
}