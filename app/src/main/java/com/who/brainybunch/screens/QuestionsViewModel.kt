package com.who.brainybunch.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.who.brainybunch.data.DataOrException
import com.who.brainybunch.model.Quiz
import com.who.brainybunch.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
): ViewModel() {
    private val data: MutableState<DataOrException<ArrayList<Quiz>, Boolean, Exception>>
    = mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch {
            data.value.isLoading = true
            data.value = questionRepository.getAllQuestions()
            if(data.value.data.toString().isNotEmpty())
                data.value.isLoading = false
        }
    }
}