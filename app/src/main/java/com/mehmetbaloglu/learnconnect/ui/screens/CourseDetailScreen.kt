package com.mehmetbaloglu.learnconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.CourseDetails
import com.mehmetbaloglu.learnconnect.data.models.CourseDetails.Hit
import com.mehmetbaloglu.learnconnect.data.models.DataOrException
import com.mehmetbaloglu.learnconnect.navigation.AppScreens
import com.mehmetbaloglu.learnconnect.ui.viewmodels.CourseDetailViewModel

@Composable
fun CourseDetailsScreen(
    navController: NavController,
    courseName: String,
    courseDetailViewModel: CourseDetailViewModel = hiltViewModel()
) {

    val courseDetailsData = produceState<DataOrException<CourseDetails, Boolean, Exception>>(
        initialValue = DataOrException(loading = true),
        producer = {
            value = courseDetailViewModel.getCourseDetails(courseName)
        }).value

    if (courseDetailsData.loading == true) {

        Box(
            modifier = Modifier.fillMaxSize(), // Tüm ekranı kapla
            contentAlignment = Alignment.Center // İçeriği tam ortaya hizala
        ) {
            CircularProgressIndicator() // Ortada gösterilecek indikatör
        }


    } else if (courseDetailsData.data != null) {
        val hits = courseDetailsData.data!!.hits
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hits!!) {
                CourseVideosCard(courseVideos = it!!, navController = navController)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Error loading course details.", color = Color.Red)
        }
    }
}


@Composable
fun CourseVideosCard(
    courseVideos: Hit,
    navController: NavController
) {
    val tags: List<String>? = courseVideos.tags?.split(",")
    val duration: Int? = courseVideos.duration
    val likes: Int? = courseVideos.likes
    val views: Int? = courseVideos.views
    val comments: Int? = courseVideos.comments
    val downloads: Int? = courseVideos.downloads
    val videoUrl: String? = courseVideos.videos?.tiny?.url
    val thumbnailUrl: String? = courseVideos.videos?.tiny?.thumbnail

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            val encodedUrl = java.net.URLEncoder.encode(videoUrl, "UTF-8")
            navController.navigate(AppScreens.VideoPlayerScreen.name + "/$encodedUrl")
            //navController.navigate(AppScreens.VideoPlayerScreen.name + "/$videoUrl")
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
                Text(
                    text = tags?.joinToString(", ") ?: "No Tags",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Duration: ${duration ?: 0} seconds",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Likes: ${likes ?: 0}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Views: ${views ?: 0}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Comments: ${comments ?: 0}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
