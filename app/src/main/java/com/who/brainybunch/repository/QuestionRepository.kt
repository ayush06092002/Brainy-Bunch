package com.who.brainybunch.repository

import android.util.Log
import com.who.brainybunch.data.DataOrException
import com.who.brainybunch.model.Quiz
import com.who.brainybunch.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val questionApi: QuestionApi) {
    private val dataOrException = DataOrException<ArrayList<Quiz>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<Quiz>, Boolean,
            Exception> {
        try {
            dataOrException.isLoading = true
            dataOrException.data = questionApi.getQuestions().quiz
            if(dataOrException.data.toString().isNotEmpty())
                dataOrException.isLoading = false
        }catch (e: Exception){
            dataOrException.exception = e
            Log.d("QuestionRepository", "getAllQuestions: ${e.message}")
        }

        return dataOrException
    }
}