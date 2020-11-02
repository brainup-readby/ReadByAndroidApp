package com.brainup.readbyapp.com.brainup.readbyapp.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.dashboard.Home.ChapterClickHandler
import com.brainup.readbyapp.databinding.ItemCategoryChildBinding

class CategoryChildAdapter : ListAdapter<UserSelectedChapters, CustomViewHolder>(Companion) {
    companion object : DiffUtil.ItemCallback<UserSelectedChapters>() {
        override fun areItemsTheSame(
            oldItem: UserSelectedChapters,
            newItem: UserSelectedChapters
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: UserSelectedChapters,
            newItem: UserSelectedChapters
        ): Boolean {
            return oldItem.CHAPTER_ID == newItem.CHAPTER_ID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryChildBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentBook = getItem(position)
        val itemBinding = holder.binding as ItemCategoryChildBinding
        itemBinding.book = currentBook
        itemBinding.handler = ChapterClickHandler(holder.itemView.context)
        itemBinding.executePendingBindings()

    }

}