package com.who.brainybunch.model

data class Quiz(
    val answer: String,
    val category: String,
    val choices: List<String>,
    val question: String
)