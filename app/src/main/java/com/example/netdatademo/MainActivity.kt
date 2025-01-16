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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.netdatademo.ui.pages.ArticlePage
import com.example.netdatademo.ui.pages.ElsePage
import com.example.netdatademo.ui.pages.MainPage
import com.example.netdatademo.ui.pages.PicturePage
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

                Scaffold(modifier = Modifier.fillMaxSize(),
                    content = { MainContentView(it, mainStateHolder) })
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
        NavHost(navController = navController, startDestination = MainPageObject("initial page")) {
            composable<MainPageObject> {
                MainPage { clazz ->
                    when (clazz) {
                        ArticleData::class.java -> {
                            navController.navigate(ArticleData())
                        }

                        ElseData::class.java -> {
                            navController.navigate(ElseData())
                        }

                        PictureData::class.java -> {
                            navController.navigate(PictureData())
                        }
                    }
                }
            }
            composable<ArticleData> { backSackEntry ->
                val article = backSackEntry.toRoute<ArticleData>()
                ArticlePage(mainStateHolder, article) {
                    navController.popBackStack()
                }
            }
            composable<PictureData> { backSackEntry ->
                val picture = backSackEntry.toRoute<PictureData>()
                PicturePage(mainStateHolder, picture) {
                    navController.popBackStack()
                }
            }
            composable<ElseData> { backSackEntry ->
                val elseData = backSackEntry.toRoute<ElseData>()
                ElsePage(mainStateHolder, elseData) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Serializable
data class MainPageObject(val data: String? = null)

@Serializable
data class ArticleData(val data: String? = null)

@Serializable
data class PictureData(val data: String? = null)

@Serializable
data class ElseData(val data: String? = null)

