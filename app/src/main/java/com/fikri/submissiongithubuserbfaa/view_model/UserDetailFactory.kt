package com.fikri.submissiongithubuserbfaa.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class UserDetailFactory(private val application: Application, private val username: String?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserDetailViewModel(application, username) as T
    }
}