package com.brainup.readbyapp.com.brainup.readbyapp.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.auth.login.UserSubscription
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.com.brainup.readbyapp.profile.SUbscriptionListActivity
import com.brainup.readbyapp.databinding.ItemChapterBinding
import com.brainup.readbyapp.databinding.SubsItemChapterBinding
import com.squareup.picasso.Picasso

class SubscriptionListAdapter(
    private var list: List<UserSubscription>?,
    private var handler: SUbscriptionListActivity.SubsClickHandler
) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SubsItemChapterBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val chapters = list?.get(position)
        val itemBinding = holder.binding as SubsItemChapterBinding
        itemBinding.chapter = chapters
        itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }


}