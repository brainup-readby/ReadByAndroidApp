package com.brainup.readbyapp.com.brainup.readbyapp.chapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.chapter.ChapterListActivity
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.databinding.ItemChapterBinding
import com.squareup.picasso.Picasso

class ChapterListAdapter(
    private var list: List<UserSelectedChapters>?,
    private var handler: ChapterListActivity.ChapterClickHandler
) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChapterBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val chapters = list?.get(position)
        val itemBinding = holder.binding as ItemChapterBinding
        if (chapters?.icon_path != null) {
            Picasso.get().load(chapters?.icon_path).into(itemBinding.ivChapter)
        } else {
            Picasso.get().load(R.drawable.maths).into(itemBinding.ivChapter)
        }
        // itemBinding.handle = handler
        itemBinding.chapter = chapters
        itemBinding.handler=handler
        itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }


}