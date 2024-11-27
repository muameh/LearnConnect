package com.mehmetbaloglu.learnconnect.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.CourseDetails
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.Hit
import com.mehmetbaloglu.learnconnect.data.models.DataOrException
import com.mehmetbaloglu.learnconnect.navigation.AppScreens
import com.mehmetbaloglu.learnconnect.ui.viewmodels.CourseDetailViewModel
import com.mehmetbaloglu.learnconnect.utils.Widgets

@Composable
fun CourseDetailsScreen(
    navController: NavController,
    courseName: String,
    courseDetailViewModel: CourseDetailViewModel = hiltViewModel()
) {
    val userMail = FirebaseAuth.getInstance().currentUser?.email.toString()
    val context = LocalContext.current
    var showRegisterDialog by remember { mutableStateOf(false) } // AlertDialog kontrolü
    val courseCategories = courseDetailViewModel.actionState.collectAsState()

    var isRegistered by remember { mutableStateOf(false) }

    // Kayıtlı olup olmadığını kontrol et
    LaunchedEffect(courseName) {
        courseDetailViewModel.isCourseRegisteredInFirestore(userMail, courseName) { registered ->
            isRegistered = registered
            Log.d("isRegistered",isRegistered.toString())
        }
    }

    val courseDetailsData = produceState<DataOrException<CourseDetails, Boolean, Exception>>(
        initialValue = DataOrException(loading = true),
        producer = {
            value = courseDetailViewModel.getCourseDetails(courseName)
        }).value

    Scaffold(
        bottomBar = {
            // BottomNavigation Bar
            Widgets.BottomNavBar(
                navController = navController,
                currentRoute = AppScreens.CourseDetailScreen.name)
        }
    ) { innerPadding ->
        if (courseDetailsData.loading == true) {
            Box(
                modifier = Modifier.fillMaxSize(), // Tüm ekranı kapla
                contentAlignment = Alignment.Center // İçeriği tam ortaya hizala
            ) {
                CircularProgressIndicator() // Ortada gösterilecek indikatör
            }
        } else if (courseDetailsData.data != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Text(
                    text = "' $courseName ' course videos",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            courseDetailViewModel.addCourseToFavoritesFirestore(userMail, courseName)
                        },
                        modifier = Modifier.padding(4.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Text(text = "Add to favorites")
                    }
                    Button(
                        onClick = {
                            showRegisterDialog = true // Dialog'u göster
                        },
                        modifier = Modifier.padding(4.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Text(text = "Add to registered courses")
                    }
                }

                val hits = courseDetailsData.data!!.hits
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(hits!!) { index, hit ->
                        CourseVideosCard(
                            courseVideos = hit!!,
                            navController = navController,
                            videoNumber = index + 1, // 1'den başlatmak için
                            isRegistered = isRegistered
                        )
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error loading course details.", color = Color.Red)
            }
        }

        // AlertDialog
        if (showRegisterDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showRegisterDialog = false },
                title = {
                    Text(text = "Register for Course")
                },
                text = {
                    Text(text = "Are you sure you want to register for this course? Once registered, videos will be unlocked.")
                },
                confirmButton = {
                    Button(onClick = {
                        showRegisterDialog = false
                        courseDetailViewModel.addCourseToRegisteredCoursesFirestore(userMail, courseName)
                        isRegistered = true // Kayıt durumu güncelleniyor
                        Toast.makeText(context, "You are now registered!", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showRegisterDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
        // Listen to the actionState changes and show a Toast when the state is updated
        LaunchedEffect(courseCategories.value) {
            val actionMessage = courseCategories.value
            actionMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

}


@Composable
fun CourseVideosCard(
    courseVideos: Hit,
    navController: NavController,
    videoNumber: Int,
    isRegistered:Boolean
) {
    val tags: List<String>? = courseVideos.tags?.split(",")
    val duration: Int? = courseVideos.duration
    val likes: Int? = courseVideos.likes
    val views: Int? = courseVideos.views
    val comments: Int? = courseVideos.comments
    val downloads: Int? = courseVideos.downloads
    val videoUrl: String? = courseVideos.videos?.tiny?.url
    val thumbnailUrl: String? = courseVideos.videos?.tiny?.thumbnail

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            if (isRegistered){
                val encodedUrl = java.net.URLEncoder.encode(videoUrl, "UTF-8")
                navController.navigate(AppScreens.VideoPlayerScreen.name + "/$encodedUrl")
            } else {
                // Kayıtlı değilse Toast göster
                Toast.makeText(context, "You need to register for this course to view the video.", Toast.LENGTH_SHORT).show()
            }


        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Thumbnail Image
            Image(
                painter = rememberAsyncImagePainter(thumbnailUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            // Video Details
            Column(modifier = Modifier.fillMaxWidth()) {
                // Video Number
                Text(
                    text = "Video $videoNumber",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = tags?.joinToString(", ") ?: "No Tags",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
//                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Duration: ${duration ?: 0} seconds",
                    style = MaterialTheme.typography.bodySmall,
                )
//                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Likes: ${likes ?: 0}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "Views: ${views ?: 0}",
                    style = MaterialTheme.typography.bodySmall
                )
//                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Comments: ${comments ?: 0}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
