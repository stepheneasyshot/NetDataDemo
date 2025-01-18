package com.example.netdatademo

import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import com.example.netdatademo.helper.DataStoreHelper
import com.example.netdatademo.retrofit.CollectedArticle
import com.example.netdatademo.retrofit.PicAdress
import com.example.netdatademo.retrofit.RetroService
import com.example.netdatademo.uistate.ArticleState
import com.example.netdatademo.uistate.CollectedArticleState
import com.example.netdatademo.uistate.PitureState
import com.example.netdatademo.uistate.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainStateHolder(private val retroService: RetroService) : ViewModel() {

    companion object {
        const val TAG = "MainStateHolder"
        const val TOKEN_KEY = "token"
        const val USER_NAME_KEY = "user_name"
    }

    private val articleState = MutableStateFlow(ArticleState())
    val articleListStateFlow = articleState.asStateFlow()

    private val collectedArticleState = MutableStateFlow(CollectedArticleState())
    val collectedArticleListStateFlow = collectedArticleState.asStateFlow()

    private val userState = MutableStateFlow(UserState())
    val userStateFlow = userState.asStateFlow()

    private val pitureState = MutableStateFlow(PitureState(PicAdress()))
    val pitureListStateFlow = pitureState.asStateFlow()

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
}