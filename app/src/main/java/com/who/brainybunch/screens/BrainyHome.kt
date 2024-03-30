package com.who.brainybunch.screens

import androidx.compose.runtime.Composable
import com.who.brainybunch.component.Questions

@Composable
fun BrainyHome(viewModel: QuestionsViewModel) {
    Questions(viewModel = viewModel)
}