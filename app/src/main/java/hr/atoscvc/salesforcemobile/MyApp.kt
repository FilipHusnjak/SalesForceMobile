package hr.atoscvc.salesforcemobile

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import java.util.*

class MyApp : Application() {

    private var instance: Bitmap? = null
    private lateinit var listener: LogoutListener
    private lateinit var timer: Timer

    fun startUserSession() {
        cancelTimer()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                listener.onSessionTimeout()
            }

        }, 30000)    //TODO Use a normal value
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

    fun getInstance(context: Context): Bitmap {

        // if (instance == null || instance!!.isRecycled) {
        if (instance == null) {
            instance = BitmapManager.decodeSampledBitmapFromResource(
                    context.resources,
                    R.drawable.background_test,
                    context.resources.displayMetrics.widthPixels,
                    context.resources.displayMetrics.heightPixels
            )
        }

        return instance as Bitmap

    }

    /*fun recycleInstanceAndSetToNull() {
        instance?.recycle()
        instance = null
    }*/

}

