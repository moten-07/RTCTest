package com.dzgame.rtctest

import android.app.Application
import io.rong.imkit.RongIM

class App : Application() {
	override fun onCreate() {
		super.onCreate()
		RongIM.init(this, "sfci50a7sib4i")
	}
}