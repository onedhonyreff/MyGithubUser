package com.fikri.submissiongithubuserbfaa.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fikri.submissiongithubuserbfaa.fragment.FollowListFragment

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    tabCount: Int,
    usernameParam: String?,
    nameParam: String
) :
    FragmentStateAdapter(activity) {
    private val count = tabCount
    private val username = usernameParam
    private val name = nameParam
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return FollowListFragment.newInstance(position + 1, username, name)
    }
}