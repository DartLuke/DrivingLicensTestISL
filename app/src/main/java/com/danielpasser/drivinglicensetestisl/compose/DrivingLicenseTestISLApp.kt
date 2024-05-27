package com.danielpasser.drivinglicensetestisl.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danielpasser.drivinglicensetestisl.compose.questionlist.QuestionsListScreen

@Composable
fun DrivingLicenseTestISLApp() {
    val navController = rememberNavController()
    CategoriesNavController(navController = navController)
}

@Composable
fun CategoriesNavController(navController: NavHostController) {
    NavHost(navController = navController, startDestination = ScreenNav.QuestionsList.route) {
        composable(route = ScreenNav.QuestionsList.route) {
            QuestionsListScreen()
        }
    }
}