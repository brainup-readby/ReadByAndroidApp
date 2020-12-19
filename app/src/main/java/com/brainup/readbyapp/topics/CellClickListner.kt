package com.brainup.readbyapp.com.brainup.readbyapp.topics

import com.brainup.readbyapp.auth.login.UserSelectedTopics

interface CellClickListener {
    fun onCellClickListener(topic: UserSelectedTopics?, topicPrevious: UserSelectedTopics?)

    fun onCellClickListenerPDF(topic: UserSelectedTopics?)

    fun onCellClickListenerCard(topic: UserSelectedTopics?)
}