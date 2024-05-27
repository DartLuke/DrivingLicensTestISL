package com.danielpasser.drivinglicensetestisl.repository

import android.app.Application
import com.danielpasser.drivinglicensetestisl.models.DriveLicenceCategoryEnum
import com.danielpasser.drivinglicensetestisl.models.Question
import com.danielpasser.drivinglicensetestisl.utils.Utils
import javax.inject.Inject

class OfflineRepository @Inject constructor(private val context: Application) {

    fun getQuestionsAll(): List<Question> {
        val json = Utils.readJsonFromAssets(context = context, fileName = "driving_exam.json")
        return Utils.parseJsonToQuestion(json).questions
    }

    fun getQuestionsByCategory(categories: Set<DriveLicenceCategoryEnum>): List<Question> {
        return getQuestionsAll().filter { it.categories.intersect(categories).isNotEmpty() }
    }
}