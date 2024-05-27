package com.danielpasser.drivinglicensetestisl.utils

import android.content.Context
import com.danielpasser.drivinglicensetestisl.models.Question
import com.danielpasser.drivinglicensetestisl.models.QuestionList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object   Utils {
    fun readJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    fun parseJsonToQuestion(jsonString: String): QuestionList {
        val gson = Gson()
        return gson.fromJson(jsonString, object : TypeToken<QuestionList>() {}.type)
    }
}