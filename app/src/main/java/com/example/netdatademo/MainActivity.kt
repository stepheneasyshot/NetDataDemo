package com.example.netdatademo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.netdatademo.ui.pages.ArticlePage
import com.example.netdatademo.ui.pages.GithubRepoPage
import com.example.netdatademo.ui.pages.MainPage
import com.example.netdatademo.ui.pages.MyServerPage
import com.example.netdatademo.ui.pages.PicturePage
import com.example.netdatademo.ui.pages.PicturePageKtor
import com.example.netdatademo.ui.pages.VideoPage
import com.example.netdatademo.ui.theme.NetDataDemoTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetDataDemoTheme {
                val mainStateHolder: MainStateHolder by viewModel()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        MainContentView(innerPadding, mainStateHolder, this)
                    })
            }
        }
    }
}

@Composable
fun MainContentView(
    paddingValues: PaddingValues,
    mainStateHolder: MainStateHolder,
    lifecycleOwner: LifecycleOwner
) {
    Log.d("MainContentView", "paddingValues: $paddingValues")
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.MainPage
        ) {
            composable<Screen.MainPage> {
                MainPage { screen ->
                    when (screen) {
                        is Screen.ArticlePage -> {
                            navController.navigate(Screen.ArticlePage)
                        }

                        is Screen.PicturePage -> {
                            navController.navigate(Screen.PicturePage)
                        }

                        is Screen.PicturePageKtor -> {
                            navController.navigate(Screen.PicturePageKtor)
                        }

                        is Screen.VideoPage -> {
                            navController.navigate(Screen.VideoPage)
                        }

                        is Screen.GithubReposPage -> {
                            navController.navigate(Screen.GithubReposPage)
                        }

                        is Screen.MyServerPage -> {
                            navController.navigate(Screen.MyServerPage)
                        }

                        else -> {
                            navController.navigate(Screen.MainPage)
                        }
                    }
                }
            }
            composable<Screen.ArticlePage> { backSackEntry ->
                val article = backSackEntry.toRoute<Screen.ArticlePage>()
                ArticlePage(mainStateHolder, article) {
                    navController.popBackStack()
                }
            }
            composable<Screen.PicturePage> { backSackEntry ->
                val picture = backSackEntry.toRoute<Screen.PicturePage>()
                PicturePage(mainStateHolder, picture) {
                    navController.popBackStack()
                }
            }
            composable<Screen.PicturePageKtor> { backSackEntry ->
                val picture = backSackEntry.toRoute<Screen.PicturePageKtor>()
                PicturePageKtor(mainStateHolder, picture) {
                    navController.popBackStack()
                }
            }
            composable<Screen.VideoPage> { backSackEntry ->
                val videoPage = backSackEntry.toRoute<Screen.VideoPage>()
                VideoPage(mainStateHolder, videoPage) {
                    navController.popBackStack()
                }
            }
            composable<Screen.GithubReposPage> {
                GithubRepoPage(mainStateHolder) {
                    navController.popBackStack()
                }
            }
            composable<Screen.MyServerPage> {
                MyServerPage(mainStateHolder, lifecycleOwner = lifecycleOwner) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object MainPage : Screen("mainPage")

    @Serializable
    data object ArticlePage : Screen("articlePage")

    @Serializable
    data object PicturePage : Screen("picturePage")

    @Serializable
    data object PicturePageKtor : Screen("picturePageKtor")

    @Serializable
    data object VideoPage : Screen("videoPage")

    @Serializable
    data object GithubReposPage : Screen("githubReposPage")

    @Serializable
    data object MyServerPage : Screen("myserverpage")
}
