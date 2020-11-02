/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brainup.readbyapp.com.brainup.readbyapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brainup.readbyapp.R
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.model.model.VideoRequestModel
import com.brainup.readbyapp.com.brainup.readbyapp.quiz.viewmodel.QuizResultViewModel
import com.brainup.readbyapp.com.brainup.readbyapp.utils.PlayerActivity.Companion.TOPIC_ID
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.new_activity_player.*
import java.util.*

/**
 * A fullscreen activity to play audio or video streams.
 */
class NewPlayerActivity : BaseActivity() {
     private var  isVideoStatus :String ="p"
    private lateinit var player: SimpleExoPlayer
    private lateinit var url: String
    private lateinit var topic_id: String
    private var totalTime: Long = 300000 // 5 minutes
    private var playedTime: Long = 0
    private lateinit var viewModel: QuizResultViewModel
    var REQUEST_CODE = 1234

      lateinit var  videoId :String
    var myCountDownTimer: MyCountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_player)
         viewModel = ViewModelProvider(this).get(QuizResultViewModel::class.java)
         url = intent.getStringExtra(KEY_URL)!!
         topic_id = intent.getStringExtra(TOPIC_ID)!!
        // url = "https://player.vimeo.com/video/464700822"
        var   tr = url.replace("https://vimeo.com/","")
        var  tsr  = StringTokenizer(tr,"/")
        videoId =   tsr.nextToken()
       // setupToolbar(url)
        initProgressbar(this)
        hideSystemUis()
    }

    private fun hideSystemUis() {
        vimeoPlayer!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


    private fun setupToolbar(url: String) {


        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(shareIntent, "pdfDoc"))




     /*   toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }*/
    }



    private fun setupView() {
        lifecycle.addObserver(vimeoPlayer)
        vimeoPlayer.initialize( videoId.toInt())
        //vimeoPlayer.initialize( 59777392)
        vimeoPlayer.seekTo(123452f)
        vimeoPlayer.addStateListener(object : VimeoPlayerStateListener {
            override fun onPlaying(duration: Float) {
                isVideoStatus ="p"
            }

            override fun onPaused(seconds: Float) {
                isVideoStatus ="p"
            }

            override fun onEnded(duration: Float) {
                isVideoStatus ="c"
            }
        })
        vimeoPlayer.setFullscreenClickListener {
            var requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_AUTO
            startActivityForResult(VimeoPlayerActivity.createIntent(this, requestOrientation, vimeoPlayer), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            var playAt = data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
            vimeoPlayer.seekTo(playAt)

            var playerState = PlayerState.valueOf(data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE)
                .toString())
            when (playerState) {
                PlayerState.PLAYING -> vimeoPlayer.play()
                PlayerState.PAUSED -> vimeoPlayer.pause()
            }
        }
    }

    public override fun onResume() {
        super.onResume()
            setupView()
    }


    override fun onPause() {
        super.onPause()
        var   model   =    VideoRequestModel(
            isVideoStatus,topic_id.toInt()
        )
        submitStatus(model)
    }

    public override fun onStop() {
        super.onStop()
        if (myCountDownTimer != null) {
            myCountDownTimer?.cancel()
        }

    }


    private fun submitStatus(lists: VideoRequestModel) {
        viewModel.updateVideoFlag(lists)
            ?.observe(QuizActivityNew@ this, Observer {
                if (it.isSuccessful) {

                }else{
                    Toast.makeText(this,"some thing went wrong.", Toast.LENGTH_SHORT).show()
                }
            })
    }



    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        vimeoPlayer!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }


    inner class MyCountDownTimer(
        millisInFuture: Long,
        countDownInterval: Long
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(timeRemaining: Long) {
            totalTime = timeRemaining
        }

        override fun onFinish() {
            player.playWhenReady = false
            playedTime = totalTime
            myCountDownTimer?.cancel()
            // showAlert()
        }
    }


    companion object {
        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        const val KEY_URL = "url"
        const val KEY_SCREEN = "keyScreen"
        const val KEY_TITLE = "keyTitle"
    }
}