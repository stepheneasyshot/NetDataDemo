package com.example.netdatademo

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netdatademo.helper.DataStoreHelper
import com.example.netdatademo.ktor.FileDownloadManager
import com.example.netdatademo.ktor.FileDownloadManager.DownloadProgress
import com.example.netdatademo.ktor.KtorClient
import com.example.netdatademo.retrofit.PicAdress
import com.example.netdatademo.retrofit.PicAdressItem
import com.example.netdatademo.retrofit.RetroService
import com.example.netdatademo.uistate.ArticleState
import com.example.netdatademo.uistate.CollectedArticleState
import com.example.netdatademo.uistate.GithubReposState
import com.example.netdatademo.uistate.PitureState
import com.example.netdatademo.uistate.UserState
import com.example.netdatademo.utils.SpeechUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

class MainStateHolder(
    private val retroService: RetroService,
    private val ktorClient: KtorClient,
    private val fileDownloadManager: FileDownloadManager
) : ViewModel() {

    companion object {
        const val TAG = "MainStateHolder"
        const val TOKEN_KEY = "token"
        const val USER_NAME_KEY = "user_name"
    }

    private val articleState = MutableStateFlow(ArticleState())
    val articleListStateFlow = articleState.asStateFlow()

    private val githubReposSate = MutableStateFlow(GithubReposState())
    val githubReposListStateFlow = githubReposSate.asStateFlow()

    private val collectedArticleState = MutableStateFlow(CollectedArticleState())
    val collectedArticleListStateFlow = collectedArticleState.asStateFlow()

    private val userState = MutableStateFlow(UserState())
    val userStateFlow = userState.asStateFlow()

    private val pitureState = MutableStateFlow(PitureState(PicAdress()))
    val pitureListStateFlow = pitureState.asStateFlow()

    private val myServerResponse = MutableStateFlow("")
    val myServerResponseStateFlow = myServerResponse.asStateFlow()

    init {
        Log.d(TAG, "============>init<=============")
        SpeechUtils.init()
    }

    fun getMyServerResponse() {
        Log.d(TAG, "getMyServerResponse: ")
        CoroutineScope(Dispatchers.IO).launch {
            val response = ktorClient.getMyServerResponse()
            myServerResponse.update {
                response
            }
        }
    }

    fun getMainPageArticleList(pageIndex: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            articleState.update {
                it.copy(
                    articleList = retroService.getArticleList(pageIndex),
                    isLoading = false
                )
            }
            articleState.value = articleState.value.toUiState()
        }
    }

    fun loginWanAndroid(userName: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            // 先判断本地是否已经登录过
            if (DataStoreHelper.getData(stringPreferencesKey(TOKEN_KEY), "").isNotEmpty()) {
                val savedUserName = DataStoreHelper.getData(stringPreferencesKey(USER_NAME_KEY), "")
                Log.d(TAG, "loginWanAndroid: 本地已经登录过，直接获取用户名 $savedUserName")
                // 本地已经登录过，直接获取用户名
                userState.update {
                    it.copy(
                        userName = savedUserName,
                        isLoading = false
                    )
                }
            } else {
                // 本地没有登录过，调用登录接口
                Log.d(TAG, "loginWanAndroid: 本地没有登录过，调用登录接口, 用户名: $userName")
                val tokenUserPair = retroService.loginWanAndroid(userName, password)
                // 获取登陆结果，失败则为空
                Log.d(TAG, "loginWanAndroid: ${tokenUserPair?.first}, ${tokenUserPair?.second}")
                tokenUserPair?.let {
                    // 登录成功，保存token和用户名
                    DataStoreHelper.saveData(stringPreferencesKey(TOKEN_KEY), tokenUserPair.first)
                    DataStoreHelper.saveData(
                        stringPreferencesKey(USER_NAME_KEY),
                        tokenUserPair.second
                    )
                    // 更新状态
                    userState.update {
                        it.copy(
                            userName = tokenUserPair.second,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    /**
     * 退出登录
     */
    fun logoutWanAndroid() {
        CoroutineScope(Dispatchers.IO).launch {
            // 先判断本地是否已经登录过
            if (DataStoreHelper.getData(stringPreferencesKey(TOKEN_KEY), "").isNotEmpty()) {
                // 本地已经登录过，调用退出登录接口
                val tokenUserPair = retroService.logoutWanAndroid()
                Log.d(TAG, "logoutWanAndroid: ${tokenUserPair?.first}, ${tokenUserPair?.second}")
                tokenUserPair?.let {
                    // 退出登录成功，清除token和用户名
                    DataStoreHelper.saveData(stringPreferencesKey(TOKEN_KEY), "")
                    DataStoreHelper.saveData(stringPreferencesKey(USER_NAME_KEY), "")
                    // 更新状态
                    userState.update {
                        it.copy(
                            userName = "",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    /**
     * 收藏文章列表
     */
    fun getCollectedArticleList() {
        CoroutineScope(Dispatchers.IO).launch {
            collectedArticleState.update {
                val collectedArticleList = retroService.getCollectArticleList()
                Log.d(TAG, "getCollectedArticleList-> list: $collectedArticleList")
                it.copy(
                    articleList = collectedArticleList,
                    isLoading = false
                )
            }
            collectedArticleState.value = collectedArticleState.value.toUiState()
        }
    }

    /**
     * 获取猫猫图片
     */
    fun getCatPic() {
        CoroutineScope(Dispatchers.IO).launch {
            pitureState.update {
                it.copy(
                    picAdress = retroService.getCatPic(),
                    isLoading = false
                )
            }
        }
    }


    fun getVideoUrl() = "https://vod.api.video/vod/viXCshSBWtwMb3pcFUc8mmd/mp4/source.mp4"

//    ===============================================  ktor  ====================================================

    /**
     * 获取猫猫图片 Ktor写法
     */
    fun getCatPicByKtor() {
        CoroutineScope(Dispatchers.IO).launch {
            val catKtorItem = ktorClient.getCatPicture()
            val transferList = PicAdress()
            catKtorItem.let {
                it.forEach { item ->
                    transferList.add(
                        PicAdressItem(
                            url = item.url,
                            width = item.width,
                            height = item.height,
                            id = item.id
                        )
                    )
                }
            }
            pitureState.update {
                it.copy(
                    picAdress = transferList,
                    isLoading = false
                )
            }
        }
    }

    fun getGithubRepos(userName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val repos = ktorClient.getGithubRepos(userName)
            Log.d(TAG, "getMyGithubRepos: $repos")
            githubReposSate.update {
                it.copy(
                    repos = repos,
                    isLoading = false
                )
            }
            githubReposSate.value = githubReposSate.value.toUiState()
        }
    }

    val downloadProgressState = MutableStateFlow(DownloadProgress(0, 0))
    fun downloadDebugManager() {
        Log.d(TAG, "downloadDebugManager: 开始下载")
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "downloadDebugManager: 下载异常", exception)
        }

        viewModelScope.launch(exceptionHandler) {
            val url = "https://github.com/Genymobile/scrcpy/releases/download/v3.3.3/scrcpy-macos-aarch64-v3.3.3.tar.gz"
            val fileName = url.substringAfterLast("/")

            // 1. 定义文件的元数据
            val contentValues = ContentValues().apply {
                // 设置文件名
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)

                // 设置 MIME 类型 (例如：APK)
                // MediaStore.Downloads.MIME_TYPE: 如果是通用文件，可以使用 'application/octet-stream'
                put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")

                // 允许文件在 Downloads 目录下可见 (Android 10+)
                // IS_PENDING 设为 1 表示文件正在写入，其他应用暂时看不到或访问不到
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = appContext.contentResolver
            val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

            val uri: Uri = resolver.insert(contentUri, contentValues)
                ?: throw IOException("Failed to create new MediaStore entry")
            fileDownloadManager.downloadFileWithProgress(
                url,
                uri,
                resolver,
            ).collect { progress ->
                Log.d(TAG, "downloadDebugManager: ${progress.progress}")
                downloadProgressState.value = progress
            }
            // 5. 下载完成，清除 IS_PENDING 标记
            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }
    }

    fun speak(text: String, locale: Locale) {
        SpeechUtils.speak(text, locale)
    }

    fun stopSpeech() {
        SpeechUtils.stop()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "=============>onCleared<==============")
        ktorClient.release()
        SpeechUtils.shutdown()
    }
}