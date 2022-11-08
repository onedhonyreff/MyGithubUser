package com.fikri.submissiongithubuserbfaa.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTail(
    var username: String?,
    var avatar: String?,
    var github_link: String?,
) : Parcelable
