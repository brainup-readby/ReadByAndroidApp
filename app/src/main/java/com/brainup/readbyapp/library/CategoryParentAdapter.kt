package com.brainup.readbyapp.com.brainup.readbyapp.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.MainActivity
import com.brainup.readbyapp.auth.login.UserSelectedSubject
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.databinding.ItemCategoryParentBinding

class CategoryParentAdapter : ListAdapter<UserSelectedSubject, CustomViewHolder>(Companion) {
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object : DiffUtil.ItemCallback<UserSelectedSubject>() {
        override fun areItemsTheSame(
            oldItem: UserSelectedSubject,
            newItem: UserSelectedSubject
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: UserSelectedSubject,
            newItem: UserSelectedSubject
        ): Boolean {
            return oldItem.SUBJECT_ID == newItem.SUBJECT_ID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryParentBinding.inflate(inflater, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentBookCategory = getItem(position)
        val itemBinding = holder.binding as ItemCategoryParentBinding

        itemBinding.bookCategory = currentBookCategory
       // MainActivity.subject_id = currentBookCategory.SUBJECT_ID.toString()
        itemBinding.nestedRecyclerView.setRecycledViewPool(viewPool)
        itemBinding.executePendingBindings()
    }
}