package com.fikri.submissiongithubuserbfaa.api

import com.fikri.submissiongithubuserbfaa.model.SearchUserResponse
import com.fikri.submissiongithubuserbfaa.model.User
import com.fikri.submissiongithubuserbfaa.model.UserTailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getAllUsers(): Call<List<UserTailResponse>>

    @GET("search/users")
    fun getSearchUser(
        @Query("q") username: String,
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String,
    ): Call<User>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String,
    ): Call<List<UserTailResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String,
    ): Call<List<UserTailResponse>>
}