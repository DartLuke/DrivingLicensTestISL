package com.danielpasser.drivinglicensetestisl.compose

import androidx.navigation.NamedNavArgument

sealed class ScreenNav(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object QuestionsList : ScreenNav("questionsList")
}
