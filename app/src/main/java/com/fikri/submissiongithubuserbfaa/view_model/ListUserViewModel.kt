package com.fikri.submissiongithubuserbfaa.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fikri.submissiongithubuserbfaa.api.ApiConfig
import com.fikri.submissiongithubuserbfaa.model.SearchUserResponse
import com.fikri.submissiongithubuserbfaa.model.UserTail
import com.fikri.submissiongithubuserbfaa.model.UserTailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUserViewModel : ViewModel() {

    companion object {
        const val EMPTY_DATA_MESSAGE = "Sorry, Not found."
        const val FAIL_WITH_ERROR_CODE_MESSAGE = "Failed to load data. Kode:"
        const val FAILURE_MESSAGE = "Connection to server failed"
    }

    private val _userTailList = MutableLiveData<ArrayList<UserTail>>()
    val userTailList: LiveData<ArrayList<UserTail>> = _userTailList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNoData = MutableLiveData<Boolean>()
    val isNoData: LiveData<Boolean> = _isNoData

    var errorMessage: String? = null

    init {
        gettingUserList()
    }

    fun gettingUserList() {
        _isLoading.value = true
        errorMessage = null
        _isNoData.value = false
        _userTailList.value?.clear()
        val client = ApiConfig.getApiService().getAllUsers()
        client.enqueue(object : Callback<List<UserTailResponse>> {
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
                    errorMessage = FAIL_WITH_ERROR_CODE_MESSAGE + " " + response.code()
                    _isNoData.value = true
                }
            }

            override fun onFailure(call: Call<List<UserTailResponse>>, t: Throwable) {
                _isLoading.value = false
                errorMessage = FAILURE_MESSAGE
                _isNoData.value = true
            }

        })
    }

    fun searchUser(username: String) {
        _isLoading.value = true
        errorMessage = null
        _isNoData.value = false
        _userTailList.value?.clear()
        val client = ApiConfig.getApiService().getSearchUser(username)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userTailItem = response.body()?.items
                    if (userTailItem != null && userTailItem.isNotEmpty()) {
                        val tempUserTailList = ArrayList<UserTail>()
                        for (userTail in userTailItem) {
                            tempUserTailList.add(
                                UserTail(
                                    userTail?.login,
                                    userTail?.avatarUrl,
                                    userTail?.htmlUrl
                                )
                            )
                        }
                        _userTailList.value = tempUserTailList
                    } else {
                        errorMessage = EMPTY_DATA_MESSAGE
                        _isNoData.value = true
                    }
                } else {
                    errorMessage = FAIL_WITH_ERROR_CODE_MESSAGE + " " + response.code()
                    _isNoData.value = true
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                errorMessage = FAILURE_MESSAGE
                _isNoData.value = true
            }
        })
    }
}