package com.mehmetbaloglu.learnconnect.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mehmetbaloglu.learnconnect.navigation.AppScreens
import com.mehmetbaloglu.learnconnect.ui.viewmodels.UserDetailViewModel
import com.mehmetbaloglu.learnconnect.utils.AppColors
import com.mehmetbaloglu.learnconnect.utils.Widgets

@Composable
fun UserDetailScreen(
    navController: NavController,
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {

    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email

    // Collecting the state flow data
    val registeredCourses by userDetailViewModel.userRegisteredCourses.collectAsState()
    val favCourses by userDetailViewModel.userFavCourses.collectAsState()


    // Call the functions to fetch the data (this could be done based on lifecycle or user interaction)
    LaunchedEffect(true) {
        if (email != null) {
            userDetailViewModel.getUserRegisteredCourses(email)
        }
        if (email != null) {
            userDetailViewModel.getUserFavCourses(email)
        }
    }

    Scaffold(
        bottomBar = {
            Widgets.BottomNavBar(
                navController = navController,
                currentRoute = AppScreens.UserDetailScreen.name
            )
        }
    ) { innerPadding ->
        // UI layout
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly, // Öğeler arasına boşluk ekler
                verticalAlignment = Alignment.CenterVertically // Öğelerin dikeyde ortalanmasını sağlar
            ) {
                Text(
                    text = "welcome ${user?.displayName.toString()}!",
                    style = MaterialTheme.typography.titleSmall
                )
                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()

                        // Tüm back stack'i temizlemek ve 'LogInScreen'e gitmek için
                        navController.navigate(AppScreens.LogInScreen.name) {
                            // Back stack'teki tüm sayfalara geri dönmeyi engellemek için popUpTo kullanıyoruz
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Başlangıç noktasına kadar tüm sayfaları temizler
                            }
                            // 'LogInScreen' yeni bir başlangıç yapacak şekilde açılır
                            launchSingleTop = true
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(AppColors.Tertiary) ,
                    modifier = Modifier
                        .padding(1.dp)

                ) {
                    Text("Sign Out")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(2.dp))
            Text(
                text = "Registered Courses",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // If registeredCourses list is empty, show a message
            if (registeredCourses.isNullOrEmpty()) {
                Text(
                    text = "No Registered Courses",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 48.dp)
                )
            } else {
                // LazyRow for registered courses
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(registeredCourses.orEmpty()) { course ->
                        CourseCard(
                            onClick = { navController.navigate(AppScreens.CourseDetailScreen.name + "/$course") },
                            courseName = course,
                            isFavorite = false,
                            onRemove = {
                                if (email != null) {
                                    userDetailViewModel.removeCourseFromRegisteredCourses(
                                        email,
                                        course
                                    )
                                }
                            })
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Favorite Courses", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // If favCourses list is empty, show a message
            if (favCourses.isNullOrEmpty()) {
                Text(
                    text = "No Favorite Courses",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 48.dp)
                )
            } else {
                // LazyRow for favorite courses
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(favCourses.orEmpty()) { course ->
                        CourseCard(
                            courseName = course,
                            isFavorite = true,
                            onClick = { navController.navigate(AppScreens.CourseDetailScreen.name + "/$course") },
                            onRemove = {
                                // Remove from favorite courses
                                if (email != null) {
                                    userDetailViewModel.removeCourseFromFavorites(email, course)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


// Course card composable with delete icon
@Composable
fun CourseCard(
    courseName: String,
    isFavorite: Boolean,
    onRemove: () -> Unit,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clickable { onClick.invoke() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),


        ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = courseName,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Display the delete icon
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Course",
//                        tint = MaterialTheme.colors.error
                    )
                }
            }
        }
    }
}