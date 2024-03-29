package com.who.brainybunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.who.brainybunch.network.QuestionApi
import com.who.brainybunch.repository.QuestionRepository
import com.who.brainybunch.screens.QuestionsViewModel
import com.who.brainybunch.ui.theme.BrainyBunchTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrainyBunchTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                        val viewModel: QuestionsViewModel by viewModels()
                        BrainyHome(viewModel)
                }
            }
        }
    }
}

@Composable
fun BrainyHome(viewModel: QuestionsViewModel) {
    Questions(viewModel = viewModel)
}

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    Text(text = "Hello, ${questions?.size}")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BrainyBunchTheme {

    }
}