package com.who.brainybunch.component

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val questions = viewModel.data.value.data?.toMutableList()
    val displayedQuestions = remember {
        mutableSetOf<Int>()
    }
    val shuffledQuestions = questions?.filterIndexed { index, _ ->
        !displayedQuestions.contains(index) // Filter out already displayed questions
    }?.shuffled()
    val questionIndex = remember {
        mutableIntStateOf(0)
    }
    if (viewModel.data.value.isLoading == true) {
        CircularProgressIndicator()
    } else {
        val question = try {
            shuffledQuestions?.get(questionIndex.intValue)
        } catch (e: IndexOutOfBoundsException) {
            null
        }
        if (questions != null) {
            QuestionDisplay(
                context,
                question = question!!,
                viewModel = viewModel
            ) {
                displayedQuestions.add(questions.indexOf(question))
                if (displayedQuestions.size == questions.size) {
                    Toast.makeText(context, "All questions answered", Toast.LENGTH_SHORT).show()
                }
                questionIndex.intValue += 1
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    context: Context,
    question: Quiz,
    viewModel: QuestionsViewModel,
    onAnswerSelected: (Int) -> Unit
) {
    val selectedOnce = remember {
        mutableStateOf(false)
    }

    val currQuesIndex = remember {
        mutableIntStateOf(0)
    }
    val choicesState = remember(question) {
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val correctAnswers = remember {
        mutableIntStateOf(0)
    }

    val wrongAnswers = remember {
        mutableIntStateOf(0)
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

    val updateAnswerState: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer

        }
    }



    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        Column {
            if(currQuesIndex.intValue >= 3)
                ShowProgress(score = correctAnswers.intValue,
                    size = viewModel.data.value.data!!.size
                    )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement =  Arrangement.Start
            ) {
                QuestionTracker(currNumber = currQuesIndex.intValue + 1, outOf = viewModel.data.value.data!!.size)
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
                val borderColorr = if (isSelected) borderColor.value else Color(0xFF000000)
                Row(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = borderColorr,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clip(RoundedCornerShape(topStartPercent = 50,
                        topEndPercent = 50,
                        bottomEndPercent = 50,
                        bottomStartPercent = 50))
                    .clickable {
                        if (selectedOnce.value) {
                            Toast
                                .makeText(
                                    context,
                                    "You have already selected an answer",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            return@clickable
                        } else if (!selectedOnce.value) {
                            selectedOnce.value = true
                            updateAnswerState(index)
                        }
                    }) {
                        RadioButton(selected = (
                                               answerState.value == index
                                ), onClick = {
                                    if(selectedOnce.value){
                                        Toast.makeText(context, "You have already selected an answer", Toast.LENGTH_SHORT).show()
                                        return@RadioButton
                                    }else if(!selectedOnce.value){
                                        selectedOnce.value = true
                                        updateAnswerState(index)
                                    }

                        }, modifier = Modifier.padding(start = 8.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                when (correctAnswerState.value) {
                                    true -> Color(0xFF00FF00) //&& index == answerState.value
                                    false -> Color(0xFFFF0000)
                                    else -> Color(0xFFFFFFFF)
                                }
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

            Button(onClick = {
                if(answerState.value == null) {
                    Toast.makeText(context, "Please select an answer", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                currQuesIndex.intValue += 1
                onAnswerSelected(answerState.value!!)
                             if(correctAnswerState.value == true) {
                    correctAnswers.intValue += 1
                } else {
                    wrongAnswers.intValue += 1
                }
                selectedOnce.value = false
                             },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Next",
                    fontFamily = fontFamily,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

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
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold
        )
    )
    Text(
        modifier = Modifier.padding(top = 22.dp),
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

@Preview
@Composable
fun ShowProgress(score: Int = 2, size: Int = 58) {

    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075),
        Color(0xFFBE6BE5)))


    val progressFactor by remember {
        mutableFloatStateOf(score*0.17f)
    }
    Row(modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(width = 4.dp,
            brush = Brush.linearGradient(colors = listOf(Color(0xFFE0E0E0), Color(0xFFE0E0E0) )),
            shape = RoundedCornerShape(34.dp))
        .clip(RoundedCornerShape(topStartPercent = 50,
            topEndPercent = 50,
            bottomEndPercent = 50,
            bottomStartPercent = 50))
        .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                containerColor = Color.Transparent,

            )) {
            Text(text = (score).toString(),
                modifier = Modifier
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 8.dp)
                ,
                color = Color.White,
                textAlign = TextAlign.Start)

//
        }


    }
}

