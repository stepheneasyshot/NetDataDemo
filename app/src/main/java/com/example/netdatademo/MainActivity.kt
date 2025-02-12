package com.example.netdatademo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetDataDemoTheme {
                val mainStateHolder: MainStateHolder by viewModel()

//                Scaffold(modifier = Modifier.fillMaxSize(),
//                    content = { MainContentView(it, mainStateHolder) })
                Column {
                    Spacer(modifier = Modifier.height(40.dp))
                    ListItemAnimateDemo()
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
fun AnimatedVisibilityDemo() {
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {

        val scope = rememberCoroutineScope()

        var state by remember { mutableStateOf(false) }

        Text(
            text = "How are you?",
            color = Color.White,
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(1f)
                .height(if (state) 160.dp else 80.dp)
                .background(Color.Red)
                .clip(RoundedCornerShape(10))
                .clickable {
                    state = true
                    scope.launch {
                        delay(2000)
                        state = false
                    }
                }
                .padding(20.dp)
        )

        Text(
            text = "I am fine.",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(Color.Blue)
                .clip(RoundedCornerShape(10))
                .padding(20.dp)
        )
    }
}

@Composable
fun ListItemAnimateDemo() {
    val listState = remember { mutableStateListOf<ListItem>() }

    LaunchedEffect(Unit) {
        repeat(10) {
            listState.add(ListItem(it, "Item $it"))
            delay(1000)
        }
        delay(1000)
        listState.removeAt(5)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(listState, key = { it.id }) { item ->
            Text(
                text = item.title,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .animateItem()
                    .background(Color.Blue)
                    .clip(RoundedCornerShape(10))
            )
        }
    }
}

data class ListItem(
    val id: Int,
    val title: String,
)
