package hr.atoscvc.salesforcemobile

import android.app.Application
import android.graphics.Bitmap
import java.util.*

class MyApp : Application() {

    lateinit var bitmapBackground: Bitmap

    private lateinit var listener: LogoutListener
    private lateinit var timer: Timer

    fun startUserSession() {
        cancelTimer()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                listener.onSessionTimeout()
            }

        }, 30000)    //TODO Staviti normalnu vrijednost
    }

    fun cancelTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }

    fun registerSessionListener(listener: LogoutListener) {
        this.listener = listener
    }

    fun onUserInteracted() {
        startUserSession()
    }

}

