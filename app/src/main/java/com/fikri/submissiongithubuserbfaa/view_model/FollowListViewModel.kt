package com.fikri.submissiongithubuserbfaa.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fikri.submissiongithubuserbfaa.api.ApiConfig
import com.fikri.submissiongithubuserbfaa.model.UserTail
import com.fikri.submissiongithubuserbfaa.model.UserTailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowListViewModel(username: String?, index: Int) : ViewModel() {

    private val _userTailList = MutableLiveData<ArrayList<UserTail>>()
    val userTailList: LiveData<ArrayList<UserTail>> = _userTailList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFailed = MutableLiveData<Boolean>()
    val isFailed: LiveData<Boolean> = _isFailed

    init {
        retrieveFollowData(username, index)
    }

    fun retrieveFollowData(username: String?, index: Int) {
        if (username != null) {
            _isLoading.value = true
            _isFailed.value = false
            var client: Call<List<UserTailResponse>>? = null

            when (index) {
                1 -> client = ApiConfig.getApiService().getUserFollowers(username)
                2 -> client = ApiConfig.getApiService().getUserFollowing(username)
            }
            client?.enqueue(object : Callback<List<UserTailResponse>> {
                override fun onResponse(
                    call: Call<List<UserTailResponse>>,
                    response: Response<List<UserTailResponse>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val tempUserTailList = ArrayList<UserTail>()
                            for (userTail in responseBody) {
                                tempUserTailList.add(
                                    UserTail(
                                        userTail.login,
                                        userTail.avatarUrl,
                                        userTail.htmlUrl
                                    )
                                )
                            }
                            _userTailList.value = tempUserTailList
                        }
                    } else {
                        _isFailed.value = true
                    }
                }

                override fun onFailure(call: Call<List<UserTailResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _isFailed.value = true
                }
            })
        }
    }
}