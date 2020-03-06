package com.nandra.myschool

import android.app.Application
import android.util.Log
import com.ale.rainbowsdk.RainbowSdk
import com.nandra.myschool.utils.Utility

class MySchoolApp : Application() {

    private val appID = "8221b4c03a1611eab3730b8300261bf3"
    private val appSecret = "cfUci19c0LrybD3mLqCYVTptY8YVoa0JMbEAsmLbw7iIdY3puLXVa2fWkTJR1WnM"

    override fun onCreate() {
        super.onCreate()
        RainbowSdk.instance().initialize(this, appID, appSecret)
    }
}