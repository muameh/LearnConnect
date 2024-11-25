package com.mehmetbaloglu.learnconnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.mehmetbaloglu.learnconnect.ui.screens.CourseDetailsScreen
import com.mehmetbaloglu.learnconnect.ui.screens.CoursesScreen
import com.mehmetbaloglu.learnconnect.ui.screens.LogInScreen
import com.mehmetbaloglu.learnconnect.ui.screens.SignUpScreen
import com.mehmetbaloglu.learnconnect.ui.screens.VideoPlayerScreen

@Composable
fun AppNavigation(){
    val navController: NavHostController = rememberNavController()

    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) AppScreens.CoursesScreen.name else AppScreens.LogInScreen.name
    
    NavHost(navController = navController, startDestination = startDestination) {

        composable(route=AppScreens.LogInScreen.name){
            LogInScreen(navController = navController)
        }

        composable(route=AppScreens.SignUpScreen.name){
            SignUpScreen(navController = navController)
        }

        composable(route=AppScreens.CoursesScreen.name){
            CoursesScreen(navController = navController)
        }

        composable(
            route=AppScreens.CourseDetailScreen.name+ "/{courseName}",
            arguments = listOf(navArgument("courseName", builder = { NavType.StringType }))
            ){ backStackEntry ->
            val courseName = backStackEntry.arguments?.getString("courseName")
            if (courseName != null) {
                CourseDetailsScreen(navController = navController, courseName = courseName)
            }
        }

        composable(
            route=AppScreens.VideoPlayerScreen.name+ "/{videoUrl}",
            arguments = listOf(navArgument("videoUrl", builder = { NavType.StringType }))
        ){ backStackEntry ->
            val videoUrl = backStackEntry.arguments?.getString("videoUrl")
            if (videoUrl != null) {
                VideoPlayerScreen(navController = navController, videoUrl = videoUrl)
            }
        }


        
    }





}