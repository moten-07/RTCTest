package com.dzgame.rtctest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.rongcloud.liveroom.api.RCLiveEngine
import cn.rongcloud.liveroom.api.callback.RCLiveCallback
import cn.rongcloud.liveroom.api.error.RCLiveError
import cn.rongcloud.rtc.api.RCRTCConfig
import cn.rongcloud.rtc.api.RCRTCEngine
import cn.rongcloud.rtc.api.callback.IRCRTCResultDataCallback
import cn.rongcloud.rtc.api.stream.RCRTCVideoStreamConfig
import cn.rongcloud.rtc.api.stream.RCRTCVideoView
import cn.rongcloud.rtc.base.RCRTCParamsType
import cn.rongcloud.rtc.base.RTCErrorCode
import com.dzgame.rtctest.databinding.ActivityMainBinding
import io.rong.common.RLog
import io.rong.imkit.RongIM
import io.rong.imlib.RongIMClient
import io.rong.imlib.RongIMClient.ConnectCallback

class MainActivity : AppCompatActivity() {
	companion object {
		private const val TAG = "MainActivity"
	}

	private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		requestPermissions(
			arrayOf(
				android.Manifest.permission.CAMERA,
				android.Manifest.permission.CAMERA,
			), 666
		)

		// 未连接->初始化
		if (RongIMClient.getInstance().currentConnectionStatus != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
			initIM()
		} else {
			joinRoom()
		}
	}

	private fun initIM() {
		val token = "jlTtQha0nXbJqgbJXTcwYMGbE+tOIifA2sp6lEEKADY=@ulqk.sg.rongnav.com;ulqk.sg.rongcfg.com"
		RongIM.connect(token, object : ConnectCallback() {
			override fun onSuccess(t: String) {
				RLog.d(TAG, t)
				joinRoom()
			}

			override fun onError(e: RongIMClient.ConnectionErrorCode?) {
				RLog.e(TAG, e.toString(), Throwable(e.toString()))
			}

			override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) = Unit

		})
	}

	private fun joinRoom() {
		val rtcConfig = RCRTCConfig.Builder.create()
			// 启用硬编码
			.enableHardwareDecoder(true)
			// 启用硬解码
			.enableHardwareEncoder(true)
			.build()
		RCRTCEngine.getInstance().init(applicationContext, rtcConfig)

		val videoConfig = RCRTCVideoStreamConfig.Builder.create()
			// 分辨率
			.setVideoResolution(RCRTCParamsType.RCRTCVideoResolution.RESOLUTION_480_640)
			// 帧率
			.setVideoFps(RCRTCParamsType.RCRTCVideoFps.Fps_15)
			// 最小码率
			.setMinRate(200)
			// 最大码率
			.setMaxRate(900)
			.build()

		RCRTCEngine.getInstance().defaultVideoStream.videoConfig = videoConfig

		val videoView = RCRTCVideoView(applicationContext)
		RCRTCEngine.getInstance().defaultVideoStream.videoView = videoView

		binding.fl.addView(videoView)
		RCRTCEngine.getInstance().defaultVideoStream.startCamera(object :IRCRTCResultDataCallback<Boolean>(){
			override fun onFailed(errorCode: RTCErrorCode?) {
				Log.e(TAG, "onFailed: ", Throwable(errorCode.toString()))
			}

			override fun onSuccess(data: Boolean?) {
				Log.d(TAG, "onSuccess: ")
			}
		})

		binding.btn.setOnClickListener {
			Log.d(TAG, "joinRoom: ")
			RCLiveEngine.getInstance().switchCamera(null)
		}
	}
}