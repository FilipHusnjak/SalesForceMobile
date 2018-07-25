package hr.atoscvc.salesforcemobile

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_menu.*
import android.content.Intent




class MainMenuActivity : AppCompatActivity() {

    private lateinit var userSession: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        userSession = SessionManager(this)
        tvUsername.text = userSession.getUserDetails()[SessionManager.KEY_USERNAME]
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        (application as MyApp).isActivityInForeground = false
        userSession.checkLogin()
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("EXIT", true)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        (application as MyApp).isActivityInForeground = true
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Do you want to Exit?")
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        builder.setPositiveButton("Yes") { _, _ ->
            (application as MyApp).isActivityInForeground = true
            userSession.logoutUser()
        }
        val alert = builder.create()
        alert.show()
    }

    fun onLogout(@Suppress("UNUSED_PARAMETER") view: View) {
        userSession.logoutUser()
    }

    override fun onUserInteraction() {
        if (userSession.isLoggedIn()) {
            (application as MyApp).onUserInteracted()
        }
    }

}
