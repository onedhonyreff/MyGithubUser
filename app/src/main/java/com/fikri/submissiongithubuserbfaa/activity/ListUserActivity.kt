package com.fikri.submissiongithubuserbfaa.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.adapter.ListUserAdapter
import com.fikri.submissiongithubuserbfaa.databinding.ActivityListUserBinding
import com.fikri.submissiongithubuserbfaa.model.UserTail
import com.fikri.submissiongithubuserbfaa.view_model.ListUserViewModel

class ListUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityListUserBinding
    private val viewModel: ListUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.list_user_actionbar_title)

        binding.rvUserList.setHasFixedSize(true)
        binding.rvUserList.layoutManager = LinearLayoutManager(this)

        binding.fabSearchUser.setOnClickListener(this)
        binding.btnRefreshUserList.setOnClickListener(this)

        viewModel.userTailList.observe(this, {
            setUserList(it)
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewModel.isNoData.observe(this, {
            setNoData(it, viewModel.errorMessage)
        })
    }

    private fun setUserList(userTails: ArrayList<UserTail>) {
        val adapterUserTail = ListUserAdapter(userTails)
        binding.rvUserList.adapter = adapterUserTail

        adapterUserTail.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onClickedItem(data: UserTail) {
                val moveToUserDetail = Intent(this@ListUserActivity, UserDetailActivity::class.java)
                moveToUserDetail.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(moveToUserDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbUserList.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setNoData(isNoData: Boolean, message: String?) {
        when (isNoData) {
            true -> {
                binding.rvUserList.visibility = View.GONE
                binding.tvError.visibility = View.VISIBLE
                if (message == ListUserViewModel.FAILURE_MESSAGE) {
                    binding.btnRefreshUserList.visibility = View.VISIBLE
                } else {
                    binding.btnRefreshUserList.visibility = View.GONE
                }
            }
            false -> {
                binding.rvUserList.visibility = View.VISIBLE
                binding.tvError.visibility = View.GONE
                binding.btnRefreshUserList.visibility = View.GONE
            }
        }
        binding.tvError.text = message
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.fab_search_user || v?.id == R.id.btn_refresh_user_list) {
            if (binding.etSearchUser.text?.trim()?.isEmpty() == true) {
                viewModel.gettingUserList()
            } else {
                viewModel.searchUser(binding.etSearchUser.text?.trim().toString())
            }
            binding.etSearchUser.clearFocus()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                val moveToSettings = Intent(this@ListUserActivity, SettingsActivity::class.java)
                startActivity(moveToSettings)
                true
            }
            R.id.menu_favorite -> {
                val moveToFavorite = Intent(this@ListUserActivity, ListFavoriteActivity::class.java)
                startActivity(moveToFavorite)
                true
            }
            else -> true
        }
    }
}