package hr.atoscvc.salesforcemobile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import hr.atoscvc.salesforcemobile.BackgroundWorker.AsyncResponse
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), AsyncResponse, LogoutListener {

    private lateinit var userSession: SessionManager
    lateinit var username: String
    private lateinit var passwordHash: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getBooleanExtra("EXIT", false)) {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
            finish()
        }

        userSession = SessionManager(this)

        (application as MyApp).registerSessionListener(this)

        etUsername.setText(userSession.getUserDetails()[SessionManager.KEY_USERNAME])

        if (userSession.getUserDetails()[SessionManager.KEY_USERNAME] != "") {
            cbSave.isChecked = true
        }

        etUsername.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val username = etUsername.text.toString().trim()
                etUsername.setText(username)
                etUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_username_outline, 0, 0, 0)

                if (username.isBlank()) {
                    etUsername.error = getString(R.string.usernameEmptyMessage)
                }
            } else {
                etUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_username_outline_accent, 0, 0, 0)
            }
        }

        etPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_password_outline, 0, 0, 0)
                if (etPassword.text.isBlank()) {
                    etPassword.error = getString(R.string.passwordEmptyMessage)
                }
            } else {
                etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_password_outline_accent, 0, 0, 0)
            }

        }

        loginProgress.visibility = INVISIBLE
        btnLogin.visibility = VISIBLE
        tvRegister.visibility = VISIBLE
    }

    override fun onResume() {
        super.onResume()

        loginProgress.visibility = INVISIBLE
        btnLogin.visibility = VISIBLE
        tvRegister.visibility = VISIBLE

        if (userSession.isLoggedIn()) {
            (application as MyApp).startUserSession()
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun onLogin(@Suppress("UNUSED_PARAMETER") view: View) {
        username = etUsername.text.toString().trim()
        val tempPass = etPassword.text.toString()       // Do NOT trim the password

        var thereAreNoErrors = true

        if (username.isBlank()) {
            etUsername.error = getString(R.string.usernameEmptyMessage)
            thereAreNoErrors = false
        }
        if (tempPass.isBlank()) {
            etPassword.error = getString(R.string.passwordEmptyMessage)
            thereAreNoErrors = false
        }

        if (thereAreNoErrors) {
            btnLogin.visibility = INVISIBLE
            tvRegister.visibility = INVISIBLE

            passwordHash = HashSHA3.getHashedValue(tempPass)

            val operation = "Login"
            val backgroundWorker = BackgroundWorker(
                    WeakReference(this),
                    getString(R.string.loginStatus),
                    this,
                    WeakReference(loginProgress)
            )
            backgroundWorker.execute(operation, username, passwordHash)
        }
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivityForResult(intent, 0)
    }

    fun onForgotPassword(@Suppress("UNUSED_PARAMETER") view: View) {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
        //TODO Za forgot my password napraviti kao alertbox s username i email
    }

    override fun processFinish(output: String) {
        if (output.contains("Welcome")) {
            userSession.createLoginSession(username, passwordHash, cbSave.isChecked)

            (application as MyApp).startUserSession()

            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            btnLogin.visibility = VISIBLE
            tvRegister.visibility = VISIBLE
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            finish()
        }
    }

    override fun onSessionTimeout() {
        userSession.logoutUserData()
        userSession.logoutUserView()
    }
}
