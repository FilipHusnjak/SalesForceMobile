package hr.atoscvc.salesforcemobile

import android.app.Application
import java.util.*


class MyApp : Application() {

    private lateinit var listener: LogoutListener

    private lateinit var timer: Timer

    fun startUserSession() {
        cancelTimer()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                listener.onSessionTimeout()
            }

        }, 5000)
    }

    fun cancelTimer() {
        if (::timer.isInitialized) timer.cancel()
    }

    fun registerSessionListener(listener: LogoutListener) {
        this.listener = listener
    }

    fun onUserInteracted() {
        startUserSession()
    }


}

