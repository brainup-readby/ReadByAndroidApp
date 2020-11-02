package com.brainup.readbyapp.com.brainup.readbyapp.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.R
import com.brainup.readbyapp.auth.login.UserSelectedSubject
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.dashboard.Home
import com.brainup.readbyapp.databinding.ItemSubjectBinding
import com.squareup.picasso.Picasso

class SubjectListAdapter(
    private var list: List<UserSelectedSubject>?,
    private var handler: Home.SubjectClickHandler
) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSubjectBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val course = list?.get(position)
        val itemBinding = holder.binding as ItemSubjectBinding
        itemBinding.subject = course

        if (course?.icon_path != null && !course?.icon_path.equals("c://test")) {
            Picasso.get().load(course?.icon_path).into(itemBinding.ivSubject)
        } else {
            Picasso.get().load(R.drawable.maths).into(itemBinding.ivSubject)
        }
        itemBinding.handler = handler
        itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }


}