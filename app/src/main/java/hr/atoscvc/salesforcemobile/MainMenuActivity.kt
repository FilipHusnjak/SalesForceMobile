package hr.atoscvc.salesforcemobile

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast


class MainMenuActivity : AppCompatActivity() {

    private lateinit var userSession: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        userSession = SessionManager(this)
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        userSession.checkLogin()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Do you want to Exit?")
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
        }
        val alert = builder.create()
        alert.show()
    }

    fun onLogout(@Suppress("UNUSED_PARAMETER") view: View) {
        userSession.logoutUser()
    }
}
