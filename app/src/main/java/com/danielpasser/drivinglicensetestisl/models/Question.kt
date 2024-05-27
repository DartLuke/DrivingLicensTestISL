package com.danielpasser.drivinglicensetestisl.models

data class Question(
    val id: Int,
    val question: String,
    val questionAnswers: List<String>,
    val correctAnswerId: Int,
    val categories: Set<DriveLicenceCategoryEnum>,
    val imageUrl: String? = null
)
