package com.brainup.readbyapp.com.brainup.readbyapp.auth.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.com.brainup.readbyapp.auth.ChooseCompetitiveActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.model.Course
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.databinding.ItemExamBinding

class ExamListAdapter(
    private var list: ArrayList<Course>,
    private val handler: ChooseCompetitiveActivity.ClickHandler
) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemExamBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val course = list.get(position)
        val itemBinding = holder.binding as ItemExamBinding
        itemBinding.course = course
        itemBinding.handle = handler
        itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list.size
    }


}