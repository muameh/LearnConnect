package com.mehmetbaloglu.learnconnect.ui.screens

import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController


@Composable
fun VideoPlayerScreen(navController: NavController, videoUrl: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                val videoView = VideoView(context).apply {
                    // Medya kontrollerini ekler
                    val mediaController = MediaController(context)
                    mediaController.setAnchorView(this)
                    setMediaController(mediaController)

                    // Video URL'sini ayarlar ve oynatmaya başlar
                    setVideoPath(videoUrl)
                    start()
                }
                videoView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}


//val videoUrl = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("videoUrl"), "UTF-8")