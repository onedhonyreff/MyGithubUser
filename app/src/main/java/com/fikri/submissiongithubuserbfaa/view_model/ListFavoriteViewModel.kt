package com.fikri.submissiongithubuserbfaa.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fikri.submissiongithubuserbfaa.room.database.Favorite
import com.fikri.submissiongithubuserbfaa.room.repository.FavoriteRepository

class ListFavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }

    fun getAllFavorites(): LiveData<List<Favorite>> =
        mFavoriteRepository.getAllFavorites()
}