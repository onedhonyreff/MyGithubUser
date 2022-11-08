package com.fikri.submissiongithubuserbfaa.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.adapter.ListFavoriteAdapter
import com.fikri.submissiongithubuserbfaa.databinding.ActivityListFavoriteBinding
import com.fikri.submissiongithubuserbfaa.model.UserTail
import com.fikri.submissiongithubuserbfaa.room.database.Favorite
import com.fikri.submissiongithubuserbfaa.view_model.ListFavoriteFactory
import com.fikri.submissiongithubuserbfaa.view_model.ListFavoriteViewModel

class ListFavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListFavoriteBinding
    private lateinit var viewModel: ListFavoriteViewModel
    private lateinit var adapterFavorite: ListFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.list_favorite_actionbar_title)

        viewModel =
            ViewModelProvider(this, ListFavoriteFactory(this.application)).get(
                ListFavoriteViewModel::class.java
            )

        viewModel.getAllFavorites().observe(this, { favoriteList ->
            if (favoriteList != null) {
                adapterFavorite.setListFavorites(favoriteList)
                setNoFavorite(favoriteList.isEmpty())
            }
        })

        adapterFavorite = ListFavoriteAdapter()

        binding.rvListFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvListFavorite.setHasFixedSize(true)
        binding.rvListFavorite.adapter = adapterFavorite

        adapterFavorite.setOnItemClickCallback(object : ListFavoriteAdapter.OnItemClickCallback {
            override fun onClickedItem(data: Favorite) {
                val moveToUserDetail =
                    Intent(this@ListFavoriteActivity, UserDetailActivity::class.java)
                val userTail = UserTail(data.username, data.avatar, data.github_link)
                moveToUserDetail.putExtra(UserDetailActivity.EXTRA_USER, userTail)
                moveToUserDetail.putExtra(
                    UserDetailActivity.EXTRA_FROM,
                    this@ListFavoriteActivity::class.simpleName
                )
                moveToUserDetail.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(moveToUserDetail)
                overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim)
            }

            override fun onClickedDeleteButton(data: Favorite) {
                viewModel.delete(data)
            }
        })
    }

    private fun setNoFavorite(isNoFavorite: Boolean) {
        if (isNoFavorite) {
            binding.apply {
                rvListFavorite.visibility = View.GONE
                tvNoFavorite.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                rvListFavorite.visibility = View.VISIBLE
                tvNoFavorite.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }
}