package com.example.motty.remoteservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.motty.IMyAidlInterface

class MyRemoteService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private val binder: IMyAidlInterface.Stub = object : IMyAidlInterface.Stub() {
        override fun getResult(val1: Int, val2: Int): Int {
            var flag = 2
            if (val2 - val1 >= 10) {
                flag = 1
            } else if (val2 - val1 < 10) {
                flag = 2
            }
            return flag
        }
    }
}