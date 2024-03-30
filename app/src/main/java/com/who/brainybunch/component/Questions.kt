package com.who.brainybunch.component

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.who.brainybunch.model.Quiz
import com.who.brainybunch.screens.QuestionsViewModel
import com.who.brainybunch.utils.fontFamily

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

//    val questionIndex = remember {
//
//    }
    if(viewModel.data.value.isLoading == true) {
        CircularProgressIndicator()
    } else {
        if(questions != null) {
            QuestionDisplay(question = questions[0])
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: Quiz,
//    questionIndex: MutableState<Int>,
//    viewModel: QuestionsViewModel,
    onAnswerSelected: (Int) -> Unit = {}
) {
    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val borderColor = remember { Animatable(Color(0xFF000000)) }


    LaunchedEffect(answerState.value) {
        val targetColor = if (correctAnswerState.value == true) Color(0xFF00FF00) else Color(0xFFFF0000)
        borderColor.animateTo(targetColor,
//            animationSpec = tween(
//                durationMillis = 1000,
//                easing = LinearEasing
//            )
            animationSpec = keyframes {
                durationMillis = 1000
                Color(0xFF000000) at 0 using FastOutSlowInEasing
                targetColor at 1 using FastOutSlowInEasing
            }
        )
    }

    val upadteAnswerState: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer

        }
    }



    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement =  Arrangement.Start
            ) {
                QuestionTracker()
            }
            DashedDivider(thickness = 1.dp, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp))

            Text(text = "Q: ${question.question}",
                fontFamily = fontFamily,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(16.dp))

            question.choices.forEachIndexed { index, choiceText ->
                val isSelected = answerState.value == index
                val borderColor = if (isSelected) borderColor.value else Color(0xFF000000)
                Row(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clickable {
                        upadteAnswerState(index)
                    }) {
                        RadioButton(selected = (
                                               answerState.value == index
                                ), onClick = {
                                    upadteAnswerState(index)
                        }, modifier = Modifier.padding(start = 8.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                    if(correctAnswerState.value ==  true) Color(0xFF00FF00) //&& index == answerState.value
                                    else if(correctAnswerState.value == false) Color(0xFFFF0000)
                                    else Color(0xFFFFFFFF)
                            )
                        )
                        Text(
                            text = choiceText,
                            fontFamily = fontFamily,
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            modifier = Modifier.padding(start = 8.dp, top = 14.dp)
                        )
                }
            }

        }




    }
}

@Composable
fun QuestionTracker(
    currNumber: Int = 10,
    outOf: Int = 20
) {
    Text(
        fontFamily = fontFamily,
        text = "Question $currNumber/",
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 27.sp,
            fontWeight = FontWeight.ExtraBold
        )
    )
    Text(
        modifier = Modifier.padding(top = 11.dp),
        fontFamily = fontFamily,
        text = outOf.toString(),
        style = androidx.compose.ui.text.TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    )
}

@Composable
fun DashedDivider(
    thickness: Dp,
    color: Color = Color(0xFF000000),
    phase: Float = 10f,
    intervals: FloatArray = floatArrayOf(10f, 10f),
    modifier: Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        val dividerHeight = thickness.toPx()
        // Draw a dashed line
        drawLine(
            start = Offset(1f, 0f),
            end = Offset(size.width, 0f),
            color = color,
            strokeWidth = dividerHeight,
            pathEffect = PathEffect.dashPathEffect(
                intervals = intervals,
                phase = phase
            )
        )

    }
}