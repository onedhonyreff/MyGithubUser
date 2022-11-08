package com.fikri.submissiongithubuserbfaa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.databinding.UserListItemBinding
import com.fikri.submissiongithubuserbfaa.other_class.FavoriteDiffCallback
import com.fikri.submissiongithubuserbfaa.room.database.Favorite

class ListFavoriteAdapter :
    RecyclerView.Adapter<ListFavoriteAdapter.ListViewHolder>() {

    private val listFavorites = ArrayList<Favorite>()
    fun setListFavorites(listFavorites: List<Favorite>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorites, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(listFavorites)
        diffResult.dispatchUpdatesTo(this)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = listFavorites[position].username
            tvUserGithubLink.text = listFavorites[position].github_link
            btnDeleteUser.visibility = View.VISIBLE
            btnDeleteUser.setOnClickListener {
                onItemClickCallback.onClickedDeleteButton(listFavorites[holder.adapterPosition])
            }
        }
        Glide.with(holder.itemView.context)
            .load(listFavorites[position].avatar)
            .error(R.drawable.default_user_image)
            .circleCrop()
            .into(holder.binding.ivAvatarThumbnail)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onClickedItem(listFavorites[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFavorites.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onClickedItem(data: Favorite)
        fun onClickedDeleteButton(data: Favorite)
    }

}