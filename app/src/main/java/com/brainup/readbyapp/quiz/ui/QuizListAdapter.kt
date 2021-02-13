package com.brainup.readbyapp.com.brainup.readbyapp.quiz.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.auth.login.UserSelectedTopics
import com.brainup.readbyapp.com.brainup.readbyapp.common.CustomViewHolder
import com.brainup.readbyapp.databinding.QuizListItemBinding
import com.brainup.readbyapp.quiz.model.model.RBMULTIPLEOPTION
import com.brainup.readbyapp.quiz.model.model.RBQUESTIONS


class QuizListAdapter(
    private var list: List<RBQUESTIONS>?,
    private  var  activity :  QuizActivityNew ?
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
        itemBinding.tvQuestion.setText(""+values + ": "+ topic?.QUESTIONDESC)
        if(topic!!.RB_MULTIPLE_OPTION.size==0){
            itemBinding.first.visibility = View.GONE
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }else if(topic!!.RB_MULTIPLE_OPTION.size==1){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText(topic!!.RB_MULTIPLE_OPTION[0].POSSIBLE_OPTION)
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE

        }else if(topic!!.RB_MULTIPLE_OPTION.size==2){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText(topic!!.RB_MULTIPLE_OPTION[0].POSSIBLE_OPTION)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText(topic!!.RB_MULTIPLE_OPTION[1].POSSIBLE_OPTION)
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }else if(topic!!.RB_MULTIPLE_OPTION.size==3){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText(topic!!.RB_MULTIPLE_OPTION[0].POSSIBLE_OPTION)
            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText(topic!!.RB_MULTIPLE_OPTION[1].POSSIBLE_OPTION)
            itemBinding.third.visibility = View.VISIBLE
            itemBinding.third.setText(topic!!.RB_MULTIPLE_OPTION[2].POSSIBLE_OPTION)
            itemBinding.four.visibility = View.GONE

        }else if(topic!!.RB_MULTIPLE_OPTION.size==4){
            itemBinding.first.visibility = View.VISIBLE
            itemBinding.first.setText(topic!!.RB_MULTIPLE_OPTION[0].POSSIBLE_OPTION)

            itemBinding.second.visibility = View.VISIBLE
            itemBinding.second.setText(topic!!.RB_MULTIPLE_OPTION[1].POSSIBLE_OPTION)

            itemBinding.third.visibility = View.VISIBLE
            itemBinding.third.setText(topic!!.RB_MULTIPLE_OPTION[2].POSSIBLE_OPTION)
            itemBinding.four.visibility = View.VISIBLE
            itemBinding.four.setText(topic.RB_MULTIPLE_OPTION[3].POSSIBLE_OPTION)
        }else{
            itemBinding.first.visibility = View.GONE
            itemBinding.second.visibility = View.GONE
            itemBinding.third.visibility = View.GONE
            itemBinding.four.visibility = View.GONE
        }


        if(topic.RB_MULTIPLE_OPTION !=null  &&  topic!!.RB_MULTIPLE_OPTION.size>0){
            if( topic!!.RB_MULTIPLE_OPTION.size==1){
                if(topic!!.RB_MULTIPLE_OPTION[0].SET_SELECTION){
                    itemBinding.first.isChecked = true
                }else{
                    itemBinding.first.isChecked = false
                }

            }



            if( topic!!.RB_MULTIPLE_OPTION.size==2){
                if(topic!!.RB_MULTIPLE_OPTION[1].SET_SELECTION){
                    itemBinding.second.isChecked = true
                }else{
                    itemBinding.second.isChecked = false
                }
            }






            if( topic!!.RB_MULTIPLE_OPTION.size==3){
                if(topic!!.RB_MULTIPLE_OPTION[2].SET_SELECTION){
                    itemBinding.third.isChecked = true
                }else{
                    itemBinding.third.isChecked = false
                }

            }



            if( topic!!.RB_MULTIPLE_OPTION.size==4){
                if(topic!!.RB_MULTIPLE_OPTION[3].SET_SELECTION){
                    itemBinding.four.isChecked = true
                }else{
                    itemBinding.four.isChecked = false
                }
            }



        }


        itemBinding.first.setOnClickListener {
            itemBinding.first.isChecked = true
            if(itemBinding.first.isChecked){
                getSetSelctionChange(topic!!.RB_MULTIPLE_OPTION,topic!!.RB_MULTIPLE_OPTION[0].OPTION_ID)

            }

        }

        itemBinding.second.setOnClickListener {
            itemBinding.second.isChecked = true
            if(itemBinding.second.isChecked){
                getSetSelctionChange(topic!!.RB_MULTIPLE_OPTION,topic!!.RB_MULTIPLE_OPTION[1].OPTION_ID)
            }
        }

        itemBinding.third.setOnClickListener {
            itemBinding.third.isChecked = true
            if(itemBinding.third.isChecked){
                getSetSelctionChange(topic.RB_MULTIPLE_OPTION,topic.RB_MULTIPLE_OPTION[2].OPTION_ID)
            }
        }

        itemBinding.four.setOnClickListener {
            itemBinding.four.isChecked = true
            if(itemBinding.four.isChecked){
                getSetSelctionChange(topic!!.RB_MULTIPLE_OPTION,topic!!.RB_MULTIPLE_OPTION[3].OPTION_ID)
            }
        }
        activity?.setPositionOfQus(position)
        itemBinding.executePendingBindings()
    }

    private fun getSetSelctionChange(rbMultipleOption: List<RBMULTIPLEOPTION>, optionId: Int) {
        for (item in rbMultipleOption) {
             if(item.OPTION_ID == optionId){
                 item.SET_SELECTION = true
             }else{
                 item.SET_SELECTION = false
             }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size;

    }

   fun  getAllListWithSelected () : List<RBQUESTIONS>?{
       return list
   }

}