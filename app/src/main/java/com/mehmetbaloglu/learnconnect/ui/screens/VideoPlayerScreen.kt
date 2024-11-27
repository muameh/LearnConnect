package com.mehmetbaloglu.learnconnect.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.mehmetbaloglu.learnconnect.data.models.VideoSavings
import com.mehmetbaloglu.learnconnect.ui.viewmodels.VideoPlayerViewModel

@Composable
fun VideoPlayerScreen(
    navController: NavController,
    videoUrl: String,
    videoPlayerViewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val userMail = FirebaseAuth.getInstance().currentUser?.email
    val lifecycleOwner = LocalLifecycleOwner.current

    // Son kaydedilen pozisyonu saklamak için state
    var savedPosition by remember { mutableStateOf(0L) }
    var isPlayerReady by remember { mutableStateOf(false) } // Player hazır olduğunda seek yapılacak

    // Kaydedilen pozisyonu almak için ViewModel çağrısı
    LaunchedEffect(videoUrl) {
        val savedVideo = videoPlayerViewModel.getSavedVideoPosition(videoUrl)
        savedPosition = savedVideo?.video_last_saved_time ?: 0L
        Log.d("VideoPlayer", "Saved Position: $savedPosition")
    }

    // ExoPlayer oluşturulması
    val context = LocalContext.current
    val exoPlayer = remember(context, videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true

            // Player hazır olduğunda kaydedilen pozisyona git
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY && !isPlayerReady) {
                        if (savedPosition > 0) {
                            seekTo(savedPosition) // Pozisyona git
                            Log.d("VideoPlayer", "Seeking to saved position: $savedPosition")
                        }
                        isPlayerReady = true // Player'ın pozisyona gittiği işaretlenir
                    }
                }
            })
        }
    }

    // Lifecycle-aware observer: Pozisyon kaydetme işlemi
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                val currentPosition = exoPlayer.currentPosition
                val video = VideoSavings(videoUrl, currentPosition, userMail ?: "")
                videoPlayerViewModel.saveVideoPosition(video)
                Log.d("VideoPlayer", "Position saved: $currentPosition")
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release() // ExoPlayer'ı serbest bırak
        }
    }

    // Video oynatma ekranı
    Surface(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
