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
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.brainup.readbyapp.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import timber.log.Timber

/**
 * A fullscreen activity to play audio or video streams.
 */
class PlayerActivity : AppCompatActivity(), Player.EventListener {
    private var playerView: PlayerView? = null
    private lateinit var player: SimpleExoPlayer
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var url: String
    private var totalTime: Long = 300000 // 5 minutes
    private var playedTime: Long = 0
    var myCountDownTimer: MyCountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playerView = findViewById(R.id.video_view)
       // url = intent.getStringExtra(KEY_URL)
        url = "https://player.vimeo.com/video/464700822"

    }

    public override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
        if (myCountDownTimer != null) {
            myCountDownTimer?.cancel()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            DefaultTrackSelector(),
            DefaultLoadControl()
        )
        player.addListener(this)
        playerView!!.player = player
        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)

        player.playWhenReady = playWhenReady
        player.seekTo(currentWindow, playbackPosition)
        player.prepare(mediaSource, false, false)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.release()
            //player = null
        }
    }

    /*private MediaSource buildMediaSource(Uri uri) {
      DataSource.Factory dataSourceFactory =
              new DefaultDataSourceFactory(this, "exoplayer-codelab");
      return new ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
              .createMediaSource(uri);
    }*/
    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = "exoplayer-test"
        /*if (uri.lastPathSegment!!.contains("mp3") || uri.lastPathSegment!!.contains("mp4")) {
            return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else if (uri.lastPathSegment!!.contains("m3u8")) {
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
        } else {
            val dashChunkSourceFactory = Factory(
                DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER)
            )
            val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
            return DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                .createMediaSource(uri)
        }*/return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
            .createMediaSource(uri)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

/*    private fun showAlert() {
        Timber.tag("showAlert()").d("calling....")
        AlertDialog.Builder(this)
            .setTitle("Subscription")
            .setMessage("Do you want to subscribe this video?") // Specifying a listener allows you to take an action before dismissing the dialog.
// The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton("Now",
                OnClickListener { dialog, which ->
                    // Continue with delete operation
                    dialog.dismiss()
                    player.playWhenReady = false
                    val intent = Intent(this, SubscriptionActivity::class.java)
                    startActivity(intent)
                    finish()
                    //myCountDownTimer.start()
                }) // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton("Later", OnClickListener { dialog, which ->
                // Continue with delete operation
                dialog.dismiss()
                player.playWhenReady = true

                //myCountDownTimer.start()
            })
            .setCancelable(false)
            .setIcon(R.drawable.ic_subscriptions_black_24dp)
            .show()
    }*/

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

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        Timber.tag("Player").d("onPlaybackParametersChanged ")
    }

    override fun onSeekProcessed() {
        Timber.tag("Player").d("onSeekProcessed ")
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
        Timber.tag("Player").d("onTracksChanged ")
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Timber.tag("Player").d("onPlayerError ")
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        Timber.tag("Player").d("onLoadingChanged ")
    }

    override fun onPositionDiscontinuity(reason: Int) {
        Timber.tag("Player").d("onPositionDiscontinuity ")
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        Timber.tag("Player").d("onRepeatModeChanged ")
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        Timber.tag("Player").d("onShuffleModeEnabledChanged ")
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
        Timber.tag("Player").d("onTimelineChanged ")
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> Timber.tag("Player").d("Player.STATE_BUFFERING ")
            Player.STATE_ENDED -> Timber.tag("Player").d("Player.STATE_ENDED")
            Player.STATE_IDLE -> {
                if (playedTime != totalTime) {
                    if (myCountDownTimer != null)
                        myCountDownTimer!!.start()
                }

                Timber.tag("Player").d(" Player.STATE_IDLE ")
            }
            Player.STATE_READY -> {
                if (playWhenReady) {
                    Timber.tag("Player").d(" Playing ")
                } else {
                    Timber.tag(
                        "Player"
                    ).d(" Paused")

                }
                Timber.tag("Video duration").d(player.duration.toString())
                Timber.tag("Player pos").d(player.currentPosition.toString())
            }

            else -> Timber.tag("Player").d(" Player.STATE_IDLE ")
        }
    }

    companion object {
        private const val AUTO_HIDE = true
        private const val AUTO_HIDE_DELAY_MILLIS = 3000
        const val KEY_URL = "url"
        const val KEY_SCREEN = "keyScreen"
        const val KEY_TITLE = "keyTitle"
        const val TOPIC_ID = "topic_id"
        const val USER_SUBS = "user_subs"
        const val TOPIC_STATUS_ID = "topic_status_id"
        const val TEST_STATUS= "test_status"
        const val VIDEO_SEEN_STATUS= "seen_status"

    }
}