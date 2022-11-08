package com.fikri.submissiongithubuserbfaa.room.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.fikri.submissiongithubuserbfaa.room.database.Favorite
import com.fikri.submissiongithubuserbfaa.room.database.FavoriteDao
import com.fikri.submissiongithubuserbfaa.room.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoritesDao.getAllFavorites()

    fun getSpecificFavorite(username: String): LiveData<List<Favorite>> = mFavoritesDao.getSpecificFavorite(username)

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoritesDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { mFavoritesDao.delete(favorite) }
    }
}