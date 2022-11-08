package com.fikri.submissiongithubuserbfaa.room.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM Favorite ORDER BY Id ASC")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorite WHERE username = :usernameParam")
    fun getSpecificFavorite(usernameParam: String): LiveData<List<Favorite>>

}