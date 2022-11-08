package com.fikri.submissiongithubuserbfaa.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.adapter.SectionsPagerAdapter
import com.fikri.submissiongithubuserbfaa.databinding.ActivityUserDetailBinding
import com.fikri.submissiongithubuserbfaa.model.User
import com.fikri.submissiongithubuserbfaa.model.UserTail
import com.fikri.submissiongithubuserbfaa.view_model.UserDetailFactory
import com.fikri.submissiongithubuserbfaa.view_model.UserDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FROM = "extra_from"
        const val BASE_PATH = "https://www.submission.fikri.com/user/"
        const val MESSAGE = "Berbagi tautan bermanfaat sesama teman dengan GitHub User."
    }

    private var userTail = UserTail(null, null, null)

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_user_actionbar_title)

        if (intent.data != null) {
            try {
                userTail.username = intent.data?.path?.split("/")?.get(2)
            } catch (e: Exception) {
                Log.d("ErrorLink", e.message.toString())
            }
        }
        if (intent.getParcelableExtra<UserTail>(EXTRA_USER) != null) {
            userTail = intent.getParcelableExtra<UserTail>(EXTRA_USER) as UserTail
        }

        viewModel =
            ViewModelProvider(this, UserDetailFactory(this.application, userTail.username)).get(
                UserDetailViewModel::class.java
            )

        viewModel.userData.observe(this, {
            setUserData(it)
        })

        viewModel.getSpecificFavorite(userTail.username ?: "").observe(this, { favorite ->
            if (favorite != null && favorite.isNotEmpty()) {
                viewModel.favorite.id = favorite[0].id
                viewModel.setIsFavorited(true)
            } else {
                viewModel.favorite.id = 0
                viewModel.setIsFavorited(false)
            }
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewModel.isFailed.observe(this, {
            setNoData(it, viewModel.errorMessage)
        })

        viewModel.isFavorited.observe(this, {
            setFavoriteFabState(it)
        })

        binding.btnShareUser.setOnClickListener(this)
        binding.btnRefreshUser.setOnClickListener(this)
        binding.fabFavorite.setOnClickListener(this)
    }

    private fun setUserData(user: User?) {
        binding.apply {
            tvFollowers.text = user?.followers.toString()
            tvFollowing.text = user?.following.toString()
            tvLinkGithubUser.text = user?.htmlUrl ?: getString(R.string.unknow)
            tvUsername.text =
                resources.getString(R.string.strudel, user?.login ?: getString(R.string.unknow))
            tvName.text = user?.name ?: getString(R.string.unknow)
            tvLocation.text = user?.location ?: getString(R.string.unknow)
            tvRepository.text = user?.publicRepos.toString()
            tvCompany.text = user?.company ?: getString(R.string.unknow)
            tvBlog.text = if (user?.blog.toString().isEmpty()) {
                getString(R.string.not_set)
            } else {
                user?.blog ?: getString(R.string.unknow)
            }
        }
        Glide.with(this)
            .load(user?.avatarUrl)
            .error(R.drawable.default_user_image)
            .circleCrop()
            .into(binding.ivProfilePhoto)

        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            resources.getStringArray(R.array.tab_name).size,
            userTail.username,
            user?.name ?: getString(R.string.unknow)
        )
        binding.vpUserFollow.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tlUserFollow, binding.vpUserFollow) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_name)[position]
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbUserDetail.visibility = View.VISIBLE
            binding.btnRefreshUser.visibility = View.GONE
            binding.tvErrorUserDetail.visibility = View.GONE
        } else {
            binding.pbUserDetail.visibility = View.INVISIBLE
        }
    }

    private fun setNoData(isNoData: Boolean, message: String?) {
        when (isNoData) {
            true -> {
                binding.clMainInfo.visibility = View.GONE
                binding.rlFollowContent.visibility = View.GONE
                binding.tvErrorUserDetail.visibility = View.VISIBLE
                if (message == UserDetailViewModel.FAILURE_MESSAGE) {
                    binding.btnRefreshUser.visibility = View.VISIBLE
                } else {
                    binding.btnRefreshUser.visibility = View.GONE
                }
            }
            false -> {
                binding.clMainInfo.visibility = View.VISIBLE
                binding.rlFollowContent.visibility = View.VISIBLE
                binding.tvErrorUserDetail.visibility = View.GONE
                binding.btnRefreshUser.visibility = View.GONE
            }
        }
        binding.tvErrorUserDetail.text = message
    }

    private fun setFavoriteFabState(isFavorited: Boolean) {
        binding.fabFavorite.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                applicationContext,
                if (isFavorited) R.color.active_favorite else R.color.unactive_favorite
            )
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_refresh_user -> viewModel.retrieveUserData(userTail.username)
            R.id.btn_share_user -> {
                val message = BASE_PATH + binding.tvUsername.text.toString().split("@")[1] +
                        "\n\"" + binding.tvName.text.toString().uppercase() + "\"\n" + MESSAGE
                val sendLink = Intent(Intent.ACTION_SEND)
                sendLink.type = "text/plain"
                sendLink.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(sendLink)
            }
            R.id.fab_favorite -> {
                if (viewModel.favorite.id <= 0) {
                    try {
                        viewModel.insert(viewModel.favorite)
                        Toast.makeText(
                            this,
                            getString(
                                R.string.you_give_someone_likes,
                                binding.tvName.text.toString()
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            getString(R.string.something_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    try {
                        viewModel.delete(viewModel.favorite)
                        Toast.makeText(
                            this,
                            getString(
                                R.string.you_dont_give_someone_likes,
                                binding.tvName.text.toString()
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            getString(R.string.something_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        menu?.findItem(R.id.menu_favorite)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                val moveToSettings = Intent(this@UserDetailActivity, SettingsActivity::class.java)
                startActivity(moveToSettings)
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val popTo =
            if (intent.getStringExtra(EXTRA_FROM) == ListFavoriteActivity::class.simpleName) {
                Intent(this, ListFavoriteActivity::class.java)
            } else {
                Intent(this, ListUserActivity::class.java)
            }
        popTo.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        popTo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(popTo)
    }
}