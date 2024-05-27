package com.danielpasser.drivinglicensetestisl.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.danielpasser.drivinglicensetestisl.models.DriveLicenceCategoryEnum
import com.danielpasser.drivinglicensetestisl.models.Question
import com.danielpasser.drivinglicensetestisl.repository.OfflineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionsListSViewModel @Inject constructor(
    private val repository: OfflineRepository,
) :
    ViewModel() {
    val questions: SnapshotStateList<Question> = mutableStateListOf()
    private val isExpand: SnapshotStateMap<Int, Boolean> = mutableStateMapOf()
    private val selectedCategories: SnapshotStateMap<DriveLicenceCategoryEnum, DriveLicenceCategoryEnum> =
        mutableStateMapOf()
    private val selectedAnswer: SnapshotStateMap<Int, Int> =
        SnapshotStateMap() // key - questionId, value - selectedAnswerID

    init {
        onSelectAll()
        questions.addAll(repository.getQuestionsByCategory(selectedCategories.keys))
    }

    fun onQuestionClicked(id: Int) {
        if (isExpand.containsKey(id)) isExpand.remove(id)
        else isExpand[id] = true
    }

    fun isQuestionExpanded(id: Int): Boolean {
        return isExpand[id] ?: false
    }

    fun onAnswerClicked(questionId: Int, selectedAnswerId: Int) {
        selectedAnswer[questionId] = selectedAnswerId
    }

    fun getSelectedAnswerId(id: Int): Int {
        return selectedAnswer[id] ?: -1
    }

    fun onCategoryClicked(category: DriveLicenceCategoryEnum) {
        if (selectedCategories.contains(category)) selectedCategories.remove(category) else selectedCategories[
            category] = category
    }

    fun isCategorySelected(category: DriveLicenceCategoryEnum) =
        selectedCategories.contains(category)

    fun onSelectCategoryMenuCollapsed() {
        questions.clear()
        questions.addAll(repository.getQuestionsByCategory(selectedCategories.keys))
    }

    fun onSelectAll() {
        selectedCategories.putAll(DriveLicenceCategoryEnum.values().associateWith { it })
    }

    fun onDeselectAll() {
        selectedCategories.clear()
    }
}