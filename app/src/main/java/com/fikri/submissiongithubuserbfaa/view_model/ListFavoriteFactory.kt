package com.fikri.submissiongithubuserbfaa.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ListFavoriteFactory(private val application: Application) : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListFavoriteViewModel(application) as T
    }
}