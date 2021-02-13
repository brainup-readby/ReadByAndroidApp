package com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.com.brainup.readbyapp.dashboard.model.RandomQuizResponse
import com.brainup.readbyapp.databinding.QuizListItemBinding


class RandomQuizDisplayAdapter(
        private var list: List<RandomQuizResponse>?,
        private  var  activity :  RandomQuizDisplayActivity ?
) :
        RecyclerView.Adapter<CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = QuizListItemBinding.inflate(inflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val topic = list?.get(position)
        val itemBinding = holder.binding as QuizListItemBinding
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
            itemBinding.first.setText(topic!!.OPTN_1)
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE

        }
        if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("")
                && topic!!.OPTN_2 != null && !topic!!.OPTN_2.contentEquals("")){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText(topic!!.OPTN_1)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText(topic!!.OPTN_2)
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }
        if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("")
                && topic!!.OPTN_2 != null && !topic!!.OPTN_2.contentEquals("")
                && topic!!.OPTN_3 != null && !topic!!.OPTN_3.contentEquals("")){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText(topic!!.OPTN_1)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText(topic!!.OPTN_2)
            itemBinding.third.visibility = View.VISIBLE
            itemBinding.third.setText(topic!!.OPTN_3)
            itemBinding.four.visibility = View.GONE

        }
        if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("")
                && topic!!.OPTN_2 != null && !topic!!.OPTN_2.contentEquals("")
                && topic!!.OPTN_3 != null && !topic!!.OPTN_3.contentEquals("")
                && topic!!.OPTN_4 != null && !topic!!.OPTN_4.contentEquals("")){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText(topic!!.OPTN_1)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText(topic!!.OPTN_2)
            itemBinding.third.visibility = View.VISIBLE
            itemBinding.third.setText(topic!!.OPTN_3)
            itemBinding.four.visibility = View.VISIBLE
            itemBinding.four.setText(topic.OPTN_4)
        }else{
            itemBinding.first.visibility = View.GONE
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }
            if(topic!!.OPTN_1 != null && !topic!!.OPTN_1.contentEquals("") && topic.SET_SELECTION_1){
                itemBinding.first.isChecked = topic!!.SET_SELECTION_1
            }else{
                itemBinding.first.isChecked = false
            }
            if(topic!!.OPTN_2 != null && !topic!!.OPTN_2.contentEquals("") && topic.SET_SELECTION_2){
                itemBinding.second.isChecked = topic!!.SET_SELECTION_2
            }else{
                itemBinding.second.isChecked = false
            }
            if(topic!!.OPTN_3 != null && !topic!!.OPTN_3.contentEquals("") && topic.SET_SELECTION_3){
                itemBinding.third.isChecked = topic!!.SET_SELECTION_3
            }else{
                itemBinding.third.isChecked = false
            }
            if(topic!!.OPTN_4 != null && !topic!!.OPTN_4.contentEquals("") && topic!!.SET_SELECTION_4){
                itemBinding.four.isChecked = topic!!.SET_SELECTION_4
            }else{
                itemBinding.four.isChecked = false
            }

        itemBinding.first.setOnClickListener {
            itemBinding.first.isChecked = true
            if(itemBinding.first.isChecked){
                val selectedOption = 1
                getSetSelectionChange(topic!!.OPTN_1, topic!!.QUIZ_ID, topic, selectedOption)
            }

        }

        itemBinding.second.setOnClickListener {
            itemBinding.second.isChecked = true
            if(itemBinding.second.isChecked){
                val selectedOption = 2
                getSetSelectionChange(topic!!.OPTN_2, topic!!.QUIZ_ID, topic, selectedOption)
            }
        }

        itemBinding.third.setOnClickListener {
            itemBinding.third.isChecked = true
            if(itemBinding.third.isChecked){
                val selectedOption = 3
                getSetSelectionChange(topic.OPTN_3, topic!!.QUIZ_ID, topic, selectedOption)
            }
        }

        itemBinding.four.setOnClickListener {
            itemBinding.four.isChecked = true
            if(itemBinding.four.isChecked){
                val selectedOption = 4
                getSetSelectionChange(topic!!.OPTN_4, topic!!.QUIZ_ID, topic, selectedOption)
            }
        }
        activity?.setPositionOfQus(position)
        itemBinding.executePendingBindings()
    }

    private fun getSetSelectionChange(option: String, quizId: Int, topic: RandomQuizResponse, selectedOption: Int) {

        if(topic.QUIZ_ID == quizId){

            if(selectedOption == 1){
                topic.SET_SELECTION_1 = true
                topic.SET_SELECTION_2 = false
                topic.SET_SELECTION_3 = false
                topic.SET_SELECTION_4 = false
            }else{
                topic.SET_SELECTION_1 = false

            }
            if(selectedOption == 2){
                topic.SET_SELECTION_2 = true
                topic.SET_SELECTION_1 = false
                topic.SET_SELECTION_3 = false
                topic.SET_SELECTION_4 = false
            }else{
                topic.SET_SELECTION_2 = false
            }
            if(selectedOption == 3){
                topic.SET_SELECTION_3 = true
                topic.SET_SELECTION_1 = false
                topic.SET_SELECTION_2 = false
                topic.SET_SELECTION_4 = false
            }else{
                topic.SET_SELECTION_3 = false
            }
            if(selectedOption == 4){
                topic.SET_SELECTION_4 = true
                topic.SET_SELECTION_1 = false
                topic.SET_SELECTION_2 = false
                topic.SET_SELECTION_3 = false
            }else{
                topic.SET_SELECTION_4 = false
            }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size;

    }

    fun  getAllListWithSelected () : List<RandomQuizResponse>?{
        return list
    }

}