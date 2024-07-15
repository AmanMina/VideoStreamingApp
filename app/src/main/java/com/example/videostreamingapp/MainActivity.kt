package com.example.videostreamingapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videostreamingapp.ui.theme.VideoStreamingAppTheme

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoStreamingAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "search") {
                    composable("search") {
                        SearchScreen(navController = navController)
                    }
                    composable("search_results/{query}") { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        VideoListScreen(navController = navController, query = query)
                    }
                    composable("video_player/{query}/{videoID}") { backStackEntry ->
                        val vquery = backStackEntry.arguments?.getString("query") ?: ""
                        val videoID = backStackEntry.arguments?.getString("videoID") ?: ""
                        VideoScreen(navController = navController, videoQuery = vquery, videoID = videoID)
                    }
                }
            }
        }
    }
}
