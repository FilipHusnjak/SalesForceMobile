package hr.atoscvc.salesforcemobile

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    private lateinit var userSession: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        userSession = SessionManager(this)
        tvUsername.text = userSession.getUserDetails()[SessionManager.KEY_USERNAME]
    }

    override fun onResume() {
        super.onResume()
        userSession.checkLogin()

//        mainMenuLayout.background = BitmapDrawable(
//                applicationContext.resources,
//                (application as MyApp).getInstance(applicationContext.resources)
//        )

        window.setBackgroundDrawable(BitmapDrawable(
                applicationContext.resources,
                (application as MyApp).getInstance(applicationContext.resources)
        ))
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Do you want to Exit?")
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        builder.setPositiveButton("Yes") { _, _ ->
            (application as MyApp).cancelTimer()
            userSession.logoutUserData()
            userSession.exitApp()
            finish()
        }
        val alert = builder.create()
        alert.show()
    }

    fun onLogout(@Suppress("UNUSED_PARAMETER") view: View) {
        (application as MyApp).cancelTimer()
        userSession.logoutUserData()
        userSession.logoutUserView()
    }

    override fun onUserInteraction() {
        if (userSession.isLoggedIn()) {
            (application as MyApp).onUserInteracted()
        }
    }

    fun onAddContact(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, ContactEditorActivity::class.java).apply {
            putExtra(getString(R.string.isEditorForNewItem), true)
        }
        startActivity(intent)
    }

    fun onViewContacts(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, ContactListActivity::class.java)
        startActivity(intent)
    }

    fun onAddCompany(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, CompanyEditorActivity::class.java).apply {
            putExtra(getString(R.string.isEditorForNewItem), true)
        }
        startActivity(intent)
    }

    fun onViewCompanies(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, CompanyListActivity::class.java)
        startActivity(intent)
    }


    //FIXME Method should be moved elsewhere
    //FIXME In Manifest: android:parentActivityName=".MainMenuActivity" should be changed accordingly
    fun onChangePassword(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

}
