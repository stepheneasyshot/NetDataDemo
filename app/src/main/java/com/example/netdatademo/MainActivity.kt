package com.example.netdatademo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.netdatademo.ui.pages.ArticlePage
import com.example.netdatademo.ui.pages.GithubRepoPage
import com.example.netdatademo.ui.pages.MainPage
import com.example.netdatademo.ui.pages.PicturePage
import com.example.netdatademo.ui.pages.PicturePageKtor
import com.example.netdatademo.ui.pages.VideoPage
import com.example.netdatademo.ui.theme.NetDataDemoTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetDataDemoTheme {
                val mainStateHolder: MainStateHolder by viewModel()

//                Scaffold(modifier = Modifier.fillMaxSize(),
//                    content = { MainContentView(it, mainStateHolder) })
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(200.dp))
                    TransitionAnimation()
                }
            }
        }
    }
}

@Composable
fun MainContentView(paddingValues: PaddingValues, mainStateHolder: MainStateHolder) {
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
}

@Composable
fun TransitionAnimation() {
    var boxState by remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(targetState = boxState, label = "box")

    val color by transition.animateColor(
        label = "color",
        targetValueByState = { state ->
            when (state) {
                BoxState.Collapsed -> Color.Red
                BoxState.Expanded -> Color.Green
            }
        }
    )

    val height by transition.animateDp(
        label = "height",
        targetValueByState = { state ->
            when (state) {
                BoxState.Collapsed -> 100.dp
                BoxState.Expanded -> 300.dp
            }
        }
    )

    Box(modifier = Modifier
        .fillMaxWidth(1f)
        .height(height)
        .background(color)
        .clickable {
            boxState = if (boxState == BoxState.Collapsed) {
                BoxState.Expanded
            } else {
                BoxState.Collapsed
            }
        })
}

enum class BoxState {
    Collapsed,
    Expanded
}

