package com.fikri.submissiongithubuserbfaa.model

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)

data class ItemsItem(

	@field:SerializedName("avatar_url")
	val avatarUrl: String? = null,

	@field:SerializedName("html_url")
	val htmlUrl: String? = null,

	@field:SerializedName("login")
	val login: String? = null
)
