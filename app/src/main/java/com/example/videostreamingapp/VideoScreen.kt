package com.example.videostreamingapp



import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun VideoScreen(navController: NavController, videoQuery: String, videoID: String) {
    val videoDetails = remember { mutableStateOf<Video?>(null) }
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }

    val apiService = ApiService.create()

    LaunchedEffect(videoQuery) {
        isLoading.value = true
        try {
            val response = apiService.searchVideos(apiKey = "44935829-0a408584d6504be290628c1dc", query = videoQuery)
            videoDetails.value = response.hits.find { it.id.toString() == videoID}
        } catch (e: HttpException) {
            error.value = "Network error: ${e.message}"
        } catch (e: Exception) {
            error.value = "Error: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
        if (isLoading.value) {
            // Show loading indicator
            CircularProgressIndicator(modifier = Modifier.background(MaterialTheme.colorScheme.background).align(Alignment.Center))
        } else {
            if (error.value != null) {
                // Show error message
                Text(text = error.value!!, modifier = Modifier.align(Alignment.Center))
            } else {
                val video = videoDetails.value
                if (video != null) {
                    // Display video player and video details

                    VideoPlayer(video.videos.large.url) // Replace with actual video player implementation
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = video.user, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
                            Text(text = video.tags)
                            // Display other video details as needed
                        }
                    }
                } else {
                    // Show message if video details not found
                    Text(text = "Video not found", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}


@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true // Enable default controls
                }
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}