package com.example.videostreamingapp


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import retrofit2.HttpException
import coil.compose.rememberAsyncImagePainter


@Composable
fun VideoListScreen(navController: NavController, query: String) {
    val videos = remember { mutableStateOf<List<Video>>(emptyList()) }
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }

    val apiService = ApiService.create()

    LaunchedEffect(query) {
        isLoading.value = true
        try {
            val deferredResponse = apiService.searchVideos(apiKey = "44935829-0a408584d6504be290628c1dc", query = query)
            val response = deferredResponse
            videos.value = response.hits
        } catch (e: HttpException) {
            error.value = "Network error: ${e.message}"
        } catch (e: Exception) {
            error.value = "Error: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (isLoading.value) {
            // Show loading indicator
            CircularProgressIndicator(modifier = Modifier.background(MaterialTheme.colorScheme.background).align(Alignment.Center))
        } else {
            if (error.value != null) {
                // Show error message
                Text(text = error.value!!, modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
                    items(videos.value) { video ->
                        VideoListItem(video, onItemClick = {
                            navController.navigate("video_player/${query}/${video.id}")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun VideoListItem(video: Video, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick() }
    ) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceBright).padding(16.dp)) {

            Image(
                painter = rememberAsyncImagePainter(video.videos.large.thumbnail),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
            )

            Text(text = video.tags, style = MaterialTheme.typography.titleMedium)
            // Add other video details as needed
        }
    }
}
