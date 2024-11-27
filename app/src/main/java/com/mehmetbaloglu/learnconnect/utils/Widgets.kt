package com.mehmetbaloglu.learnconnect.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mehmetbaloglu.learnconnect.navigation.AppScreens

object Widgets {

    @Composable
    fun BottomNavBar(
        navController: NavController,
        currentRoute: String
    ) {
        NavigationBar(
            containerColor = AppColors.Primary, // BottomNavigation bar background color
            contentColor = AppColors.Tertiary, // BottomNavigation text and icon color
            modifier = Modifier.padding(0.dp).height(84.dp)
            ) {
            NavigationBarItem(
                icon = {
                    Icon(Icons.Filled.Home,
                        contentDescription = "Home",
                        modifier = Modifier.padding(0.dp).size(24.dp)
                    )
                       },
                label = { Text("Home",modifier = Modifier.padding(0.dp)) },
                selected = currentRoute == AppScreens.CoursesScreen.name,
                onClick = {
                    navController.navigate(AppScreens.CoursesScreen.name) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(0.dp) // Item'ları yatay olarak genişletmek
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "User", modifier = Modifier.padding(0.dp)) },
                label = { Text("User", modifier = Modifier.padding(0.dp)) },
                selected = currentRoute == AppScreens.UserDetailScreen.name,
                onClick = {
                    navController.navigate(AppScreens.UserDetailScreen.name) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier.padding(0.dp)
            )
        }
    }

}