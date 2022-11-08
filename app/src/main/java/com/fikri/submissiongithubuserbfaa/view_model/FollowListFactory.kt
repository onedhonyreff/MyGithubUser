package com.fikri.submissiongithubuserbfaa.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class FollowListFactory(_username: String?, index: Int) : ViewModelProvider.Factory {
    private val username = _username
    private val i = index
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowListViewModel(username, i) as T
    }
}