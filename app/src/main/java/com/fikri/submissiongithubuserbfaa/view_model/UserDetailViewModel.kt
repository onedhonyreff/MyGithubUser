package com.fikri.submissiongithubuserbfaa.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fikri.submissiongithubuserbfaa.api.ApiConfig
import com.fikri.submissiongithubuserbfaa.model.User
import com.fikri.submissiongithubuserbfaa.room.database.Favorite
import com.fikri.submissiongithubuserbfaa.room.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application, username: String?) : ViewModel() {

    companion object {
        const val FAIL_WITH_ERROR_CODE_MESSAGE = "Failed to load data. Kode:"
        const val ERROR_NULL_USERNAME = "User undefined"
        const val FAILURE_MESSAGE = "Connection to server failed"
    }

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailed = MutableLiveData<Boolean>()
    val isFailed: LiveData<Boolean> = _isFailed

    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> = _isFavorited

    var favorite = Favorite(0, null, null, null)
    var errorMessage: String? = null

    init {
        retrieveUserData(username)
    }

    fun retrieveUserData(username: String?) {
        if (username != null) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getUserDetail(username)
            client.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _userData.value = response.body()
                        favorite.username = response.body()?.login
                        favorite.github_link = response.body()?.htmlUrl
                        favorite.avatar = response.body()?.avatarUrl
                        errorMessage = null
                        _isFailed.value = false
                    } else {
                        errorMessage = FAIL_WITH_ERROR_CODE_MESSAGE + " " + response.code()
                        _isFailed.value = true
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _isLoading.value = false
                    errorMessage = FAILURE_MESSAGE
                    _isFailed.value = true
                }
            })
        } else {
            errorMessage = ERROR_NULL_USERNAME
            _isFailed.value = true
        }
    }

    fun setIsFavorited(isFavorited: Boolean) {
        _isFavorited.value = isFavorited
    }

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
        _isFavorited.value = true
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
        _isFavorited.value = false
    }

    fun getSpecificFavorite(username: String): LiveData<List<Favorite>> =
        mFavoriteRepository.getSpecificFavorite(username)
}