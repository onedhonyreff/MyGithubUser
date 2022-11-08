package com.fikri.submissiongithubuserbfaa.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.activity.UserDetailActivity
import com.fikri.submissiongithubuserbfaa.adapter.ListUserAdapter
import com.fikri.submissiongithubuserbfaa.databinding.FragmentFollowListBinding
import com.fikri.submissiongithubuserbfaa.model.UserTail
import com.fikri.submissiongithubuserbfaa.view_model.FollowListFactory
import com.fikri.submissiongithubuserbfaa.view_model.FollowListViewModel

class FollowListFragment : Fragment() {

    companion object {
        private const val ARG_SECTION = "arg_section"
        private const val ARG_USERNAME = "arg_usrename"
        private const val ARG_NAME = "arg_name"

        @JvmStatic
        fun newInstance(index: Int, username: String?, name: String) =
            FollowListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION, index)
                    putString(ARG_USERNAME, username)
                    putString(ARG_NAME, name)
                }
            }
    }

    private var binding: FragmentFollowListBinding? = null
    private lateinit var viewModel: FollowListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollowList?.setHasFixedSize(true)
        binding?.rvFollowList?.layoutManager = LinearLayoutManager(context)

        val username: String? = arguments?.getString(ARG_USERNAME)
        val index: Int? = arguments?.getInt(ARG_SECTION)

        viewModel = ViewModelProvider(this, FollowListFactory(username, index!!)).get(
            FollowListViewModel::class.java
        )

        viewModel.userTailList.observe(viewLifecycleOwner, {
            setFollowData(it)
        })

        viewModel.isFailed.observe(viewLifecycleOwner, {
            setErrorData(it)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        binding?.btnRefreshFollowData?.setOnClickListener {
            viewModel.retrieveFollowData(username, index)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.pbUserFollow?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setFollowData(userTails: ArrayList<UserTail>) {
        val adapterUserTail = ListUserAdapter(userTails)
        binding?.rvFollowList?.adapter = adapterUserTail

        adapterUserTail.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onClickedItem(data: UserTail) {
                val moveToUserDetail = Intent(context, UserDetailActivity::class.java)
                moveToUserDetail.putExtra(UserDetailActivity.EXTRA_USER, data)
                moveToUserDetail.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(moveToUserDetail)
                activity?.overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim)
            }
        })

        setNoFollow(userTails.isEmpty(), arguments?.getInt(ARG_SECTION))
    }

    private fun setErrorData(isError: Boolean) {
        if (isError) {
            binding?.flFollowData?.visibility = View.GONE
            binding?.llFailLoadFollow?.visibility = View.VISIBLE
        } else {
            binding?.flFollowData?.visibility = View.VISIBLE
            binding?.llFailLoadFollow?.visibility = View.GONE
        }
    }

    private fun setNoFollow(isNoData: Boolean, index: Int?) {
        val nameArg = arguments?.getString(ARG_NAME)
        val name = if (nameArg == getString(R.string.unknow)) getString(R.string.it) else nameArg

        if (isNoData) {
            binding?.rvFollowList?.visibility = View.GONE
            binding?.tvNoFollowersOrFollowing?.visibility = View.VISIBLE
        } else {
            binding?.rvFollowList?.visibility = View.VISIBLE
            binding?.tvNoFollowersOrFollowing?.visibility = View.GONE
        }

        when (index) {
            1 -> {
                binding?.tvNoFollowersOrFollowing?.text = getString(
                    R.string.has_no_followers,
                    name
                )
            }
            2 -> {
                binding?.tvNoFollowersOrFollowing?.text = getString(
                    R.string.doesnt_follow_anyone,
                    name
                )
            }
        }
    }
}