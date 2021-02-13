package com.brainup.readbyapp.com.brainup.readbyapp.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RandomQuizResponse
import com.brainup.readbyapp.databinding.QuizResultListItemsBinding


class RandomQuizResultDisplayAdapter(
        private var list: List<RandomQuizResponse>?,
        private  var  activity :  RandomQuizResultActivity?
) :RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = QuizResultListItemsBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val topic = list?.get(position)
        val itemBinding = holder.binding as QuizResultListItemsBinding
        var values =   position.plus(1)
        itemBinding.tvQuestion.setText(""+values + ": "+ topic?.QUSTN_DSC)
        if(topic!!.OPTN_1 ==null && topic!!.OPTN_2 ==null
                && topic!!.OPTN_3 ==null && topic!!.OPTN_4 ==null){
            itemBinding.first.visibility = View.GONE
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }
        if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("")){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText("1. "+topic!!.OPTN_1)
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE

        }
        if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("")
                && topic!!.OPTN_2 != null && !topic!!.OPTN_2.contentEquals("")){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText("1. "+topic!!.OPTN_1)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText("2. "+topic!!.OPTN_2)
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }
        if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("")
                && topic!!.OPTN_2 != null && !topic!!.OPTN_2.contentEquals("")
                && topic!!.OPTN_3 != null && !topic!!.OPTN_3.contentEquals("")){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText("1. "+topic!!.OPTN_1)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText("2. "+topic!!.OPTN_2)
            itemBinding.third.visibility = View.VISIBLE
            itemBinding.third.setText("3. "+topic!!.OPTN_3)
            itemBinding.four.visibility = View.GONE

        }
        if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("")
                && topic!!.OPTN_2 != null && !topic!!.OPTN_2.contentEquals("")
                && topic!!.OPTN_3 != null && !topic!!.OPTN_3.contentEquals("")
                && topic!!.OPTN_4 != null && !topic!!.OPTN_4.contentEquals("")){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText("1. "+topic!!.OPTN_1)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText("2. "+topic!!.OPTN_2)
            itemBinding.third.visibility = View.VISIBLE
            itemBinding.third.setText("3. "+topic!!.OPTN_3)
            itemBinding.four.visibility = View.VISIBLE
            itemBinding.four.setText("4. "+topic.OPTN_4)
        }else{
            itemBinding.first.visibility = View.GONE
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }

        if(topic.SET_SELECTION_1){
            itemBinding.userSelectedAnswer.text = topic.OPTN_1
            itemBinding.correctAnswerValue.text = topic.CRRCT_OPTN
        }else if(topic.SET_SELECTION_2){
            itemBinding.userSelectedAnswer.text = topic.OPTN_2
            itemBinding.correctAnswerValue.text = topic.CRRCT_OPTN
        }else if(topic.SET_SELECTION_3){
            itemBinding.userSelectedAnswer.text = topic.OPTN_3
            itemBinding.correctAnswerValue.text = topic.CRRCT_OPTN
        }else if(topic.SET_SELECTION_4){
            itemBinding.userSelectedAnswer.text = topic.OPTN_4
            itemBinding.correctAnswerValue.text = topic.CRRCT_OPTN
        }else if(topic.NOT_ATTEMPTED){
            itemBinding.userSelectedAnswer.text = "Not Attempted"
            itemBinding.correctAnswerValue.text = topic.CRRCT_OPTN
        }

        activity?.setPositionOfQus(position)
        itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list!!.size;

    }
}