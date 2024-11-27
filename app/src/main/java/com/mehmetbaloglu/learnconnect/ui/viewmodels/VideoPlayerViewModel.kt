package com.mehmetbaloglu.learnconnect.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmetbaloglu.learnconnect.data.models.VideoSavings
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VideoPlayerViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {


    // Video pozisyonunu kaydet
    fun saveVideoPosition(video : VideoSavings) {
        try {
            viewModelScope.launch {
                // Pozisyon kaydederken null kontrolü yapın
                if (video.video_last_saved_time > 0) {
                    repository.addVideo(video)
                }
            }
        } catch (e: Exception) {
            // Hata loglama
            e.printStackTrace()
            throw Exception("Video pozisyonu kaydedilirken hata oluştu: ${e.message}")
        }


    }

    // Kaydedilen video pozisyonunu alma
    suspend fun getSavedVideoPosition(videoUrl: String): VideoSavings? {
        return try {
            repository.getSavedVideoPosition(videoUrl)
        } catch (e: Exception) {
            // Hata loglama
            throw Exception("Kaydedilen pozisyon alınırken hata oluştu: ${e.message}")
        }
    }
}