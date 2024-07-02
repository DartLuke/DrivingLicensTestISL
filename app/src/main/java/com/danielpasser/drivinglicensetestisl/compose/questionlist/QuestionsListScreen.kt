package com.danielpasser.drivinglicensetestisl.compose.questionlist

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.danielpasser.drivinglicensetestisl.R
import com.danielpasser.drivinglicensetestisl.models.Question
import com.danielpasser.drivinglicensetestisl.models.DriveLicenceCategoryEnum
import com.danielpasser.drivinglicensetestisl.viewmodel.QuestionsListSViewModel

@Composable
fun QuestionsListScreen(
    modifier: Modifier = Modifier,
    viewModel: QuestionsListSViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            QuestionsListTopAppBar(modifier = Modifier, viewModel = viewModel)
        }
    ) { contentPadding ->
        QuestionsList(
            Modifier.padding(
                top = contentPadding.calculateTopPadding(),
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_small),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ), viewModel = viewModel
        )
    }


}

@Composable
fun QuestionsList(modifier: Modifier, viewModel: QuestionsListSViewModel) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp))
    {
        items(items = viewModel.questions.toList()) { question ->
            Card(
                modifier = Modifier,
                question = question,
                selectedAnswerId = viewModel.getSelectedAnswerId(question.id),
                isQuestionExpanded = viewModel.isQuestionExpanded(question.id),
                onQuestionClicked = { viewModel.onQuestionClicked(question.id) },
                onAnswerClicked = { selectedAnswerId ->
                    viewModel.onAnswerClicked(
                        questionId = question.id,
                        selectedAnswerId = selectedAnswerId
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class)
@Composable
private fun Card(
    question: Question,
    onQuestionClicked: () -> Unit,
    onAnswerClicked: (Int) -> Unit,
    isQuestionExpanded: Boolean,
    selectedAnswerId: Int,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(text = question.question, modifier = modifier.clickable { onQuestionClicked() })
            if (isQuestionExpanded) {
                FlowRow {
                    question.questionAnswers.forEachIndexed { index, answer ->
                        val isSelected = selectedAnswerId == index
                        Row(modifier = modifier
                            .fillMaxWidth()
                            .background(
                                color = color(
                                    isSelected,
                                    selectedAnswerId,
                                    question.correctAnswerId
                                )
                            )
                            .clickable { onAnswerClicked(index) }) {
                            Text(text = answer)
                        }
                    }
                }
                if (question.imageUrl != null) {
                    GlideImage(
                        model = Uri.parse("file:///android_asset/${question.imageUrl}"),
                        contentDescription = "",
                        contentScale = ContentScale.None, )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsListTopAppBar(modifier: Modifier, viewModel: QuestionsListSViewModel) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Questions",
                    style = MaterialTheme.typography.displaySmall
                )
            }
        },
        modifier = modifier,
        actions = {
            var expanded by rememberSaveable {
                mutableStateOf(false)
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
                viewModel.onSelectCategoryMenuCollapsed()
            }) {
                Row() {
                    Checkbox(
                        checked = true,
                        onCheckedChange = {
                            viewModel.onSelectAll()
                        })
                    Spacer(modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = false,
                        onCheckedChange = {
                            viewModel.onDeselectAll()
                        })
                }
                DriveLicenceCategoryEnum.values().forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Checkbox(
                                    checked = viewModel.isCategorySelected(category),
                                    onCheckedChange = {
                                        viewModel.onSelectCategoryMenuCollapsed()
                                    })
                                Text(text = category.toString())
                            }
                        }, onClick = {
                            viewModel.onCategoryClicked(
                                category
                            )
                        })
                }
            }
        }
    )
}

fun color(selected: Boolean, selectedAnswerId: Int, correctAnswerId: Int): Color {
    return if (!selected) Color.White
    else if (selectedAnswerId == correctAnswerId) return Color.Green
    else return Color.Red

}
