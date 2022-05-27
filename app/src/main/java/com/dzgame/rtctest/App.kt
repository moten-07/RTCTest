package com.dzgame.rtctest

import android.app.Application
import io.rong.imkit.RongIM

class App : Application() {
	override fun onCreate() {
		super.onCreate()
		RongIM.init(this, "8luwapkv8wwul")
	}
}