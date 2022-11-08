package com.fikri.submissiongithubuserbfaa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.databinding.UserListItemBinding
import com.fikri.submissiongithubuserbfaa.model.UserTail

class ListUserAdapter(private val listUserTail: ArrayList<UserTail>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = listUserTail[position].username
            tvUserGithubLink.text = listUserTail[position].github_link
        }
        Glide.with(holder.itemView.context)
            .load(listUserTail[position].avatar)
            .error(R.drawable.default_user_image)
            .circleCrop()
            .into(holder.binding.ivAvatarThumbnail)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onClickedItem(listUserTail[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUserTail.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: UserTail)
    }
}