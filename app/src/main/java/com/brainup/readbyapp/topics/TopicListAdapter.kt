package com.brainup.readbyapp.com.brainup.readbyapp.topics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.databinding.TopicTimeLineBinding
import com.squareup.picasso.Picasso

class TopicListAdapter(
    private var list: List<UserSelectedTopics>?,
    private var handler: TopicListActivity.TopicHandler
) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TopicTimeLineBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val topic = list?.get(position)
        val itemBinding = holder.binding as TopicTimeLineBinding
        if (topic?.icon_path != null) {
            Picasso.get().load(topic?.icon_path).into(itemBinding.ivTopic)
        } else {
            Picasso.get().load(R.drawable.maths).into(itemBinding.ivTopic)
        }
       /* if (position == 1 || position == 2) {
            // unselected
            setColors(false, itemBinding)
        } else {
            setColors(true, itemBinding)

        }*/

        if(topic!!.isEnable){
            setColors(true, itemBinding)
        }else{
            setColors(false, itemBinding)
        }

        itemBinding.topic = topic
        itemBinding.handler = handler
        itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    private fun setColors(isSelected: Boolean, itemBinding: TopicTimeLineBinding) {
        val topicTitle: Int = if (isSelected) {
            R.color.white_color
        } else {
            R.color.color_topic_title
        }
        val topicAction: Int = if (isSelected) {
            R.color.white_color
        } else {
            R.color.color_topic_action
        }
        val iconColor = if (isSelected) {
            R.color.white_color
        } else {
            R.color.icon_unselected
        }
        val dividerColor: Int = if (isSelected) {
            R.color.white_color
        } else {
            R.color.color_divider
        }
        val bgColor: Int = if (isSelected) {
            R.color.color_card_selected
        } else {
            R.color.dot_unselected
        }
        val cardColor: Int = if (isSelected) {
            R.color.color_card_selected
        } else {
            R.color.white_color
        }


        itemBinding.ivCircle.setColorFilter(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                bgColor
            )
        )
        itemBinding.cardContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                cardColor
            )
        )
        itemBinding.tvTopicName.setTextColor(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                topicTitle
            )
        )
        itemBinding.divider.setBackgroundColor(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                dividerColor
            )
        )
        itemBinding.ivPlay.setColorFilter(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                iconColor
            )
        )
        itemBinding.ivDownload.setColorFilter(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                iconColor
            )
        )
        itemBinding.tvPlay.setTextColor(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                topicAction
            )
        )
        itemBinding.tvDownload.setTextColor(
            ContextCompat.getColor(
                itemBinding.ivCircle.context,
                topicAction
            )
        )
    }

}