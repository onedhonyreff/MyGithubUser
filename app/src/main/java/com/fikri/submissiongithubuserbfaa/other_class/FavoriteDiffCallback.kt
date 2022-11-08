package com.fikri.submissiongithubuserbfaa.other_class

import androidx.recyclerview.widget.DiffUtil
import com.fikri.submissiongithubuserbfaa.room.database.Favorite

class FavoriteDiffCallback(
    private val mOldFavoriteList: List<Favorite>,
    private val mNewFavoriteList: List<Favorite>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavoriteList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavoriteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteList[oldItemPosition].id == mNewFavoriteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavoriteList[oldItemPosition]
        val newEmployee = mNewFavoriteList[newItemPosition]
        return oldEmployee.username == newEmployee.username
                && oldEmployee.github_link == newEmployee.github_link
                && oldEmployee.avatar == newEmployee.avatar
    }
}