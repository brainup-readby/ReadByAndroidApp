package com.brainup.readbyapp.com.brainup.readbyapp.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.databinding.ItemChapterBinding
import com.brainup.readbyapp.databinding.ItemLibTopicBinding
import com.brainup.readbyapp.library.LibTopicListActivity
import com.squareup.picasso.Picasso

class LibTopicListAdapter(
    private var list: List<UserSelectedTopics>?,
    private var handler: LibTopicListActivity.TopicHandler
) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLibTopicBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val topic = list?.get(position)
        val itemBinding = holder.binding as ItemLibTopicBinding
        if (topic?.icon_path != null) {
            Picasso.get().load(topic?.icon_path).into(itemBinding.ivTopic)
        } else {
            Picasso.get().load(R.drawable.maths).into(itemBinding.ivTopic)
        }
        // itemBinding.handle = handler
        itemBinding.topic = topic
        itemBinding.handler = handler
        itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

}