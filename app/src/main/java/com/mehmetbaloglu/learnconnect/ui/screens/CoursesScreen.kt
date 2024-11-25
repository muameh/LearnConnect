package com.mehmetbaloglu.learnconnect.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mehmetbaloglu.learnconnect.navigation.AppScreens
import com.mehmetbaloglu.learnconnect.ui.viewmodels.CoursesViewModel

@Composable
fun CoursesScreen(
    navController: NavController,
    coursesViewModel: CoursesViewModel = hiltViewModel()
) {
    val courseCategories = coursesViewModel.courseCategories.collectAsState()
    val currentUser = FirebaseAuth.getInstance().currentUser


    Scaffold { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Hello ${currentUser?.email}  --> ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(
                        modifier = Modifier.padding(0.dp).width(120.dp).height(35.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(text = "Your Page")
                    }
                }

                Text(
                    text = "Courses",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineMedium)
                LazyColumn {
                    items(courseCategories.value) {
                        CategoryCard(
                            courseName = it,
                            onClick = {navController.navigate(AppScreens.CourseDetailScreen.name + "/$it")}
                            )
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryCard(courseName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
            Text(
                text = courseName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
    }
}