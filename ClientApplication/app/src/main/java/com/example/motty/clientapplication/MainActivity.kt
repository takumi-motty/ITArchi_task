package com.example.motty.clientapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motty.IMyAidlInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var myRemoteService: IMyAidlInterface? = null

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            myRemoteService = IMyAidlInterface.Stub.asInterface(service)
            Toast.makeText(applicationContext, "Remote Service Connected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            myRemoteService = null
            Toast.makeText(applicationContext, "Remote Service Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        judge_button.setOnClickListener {
            val val1 = val1_text.text.toString()
            val val2 = val2_text.text.toString()
            val uri = "https://www.univcoop.or.jp/activity/wa-master/life/index.html"
            try {
                val time = myRemoteService?.getResult(val1.toInt(), val2.toInt())
                if(time == 1){
                    judgeText.setText("いいね！毎日継続しよう！")
                }else{
                    Toast.makeText(this, "それで修了できると思ってるの？", Toast.LENGTH_LONG).show()
                        val webintent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(webintent)
                    }
            } catch (e: RemoteException) {
                e.printStackTrace()
                Log.d("errorlog", e.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (myRemoteService == null) {
            val it = Intent("myservice")
            it.setPackage("com.example.motty.remoteservice")
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}
