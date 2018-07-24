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
    //TODO Forgot my password - novi random na mail - reset on first login - boolean u bazi

    private lateinit var userSession: SessionManager
    lateinit var username: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userSession = SessionManager(this)

        (application as MyApp).registerSessionListener(this)

        loginProgress.visibility = INVISIBLE
        btnLogin.visibility = VISIBLE
        btnRegister.visibility = VISIBLE
    }

    override fun onResume() {
        super.onResume()

        loginProgress.visibility = INVISIBLE
        btnLogin.visibility = VISIBLE
        btnRegister.visibility = VISIBLE

        if (userSession.isLoggedIn()) {
            (application as MyApp).startUserSession()
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun onLogin(@Suppress("UNUSED_PARAMETER") view: View) {

        btnLogin.visibility = INVISIBLE
        btnRegister.visibility = INVISIBLE
        username = etUsername.text.toString()
        password = etPassword.text.toString()
        val operation = "Login"
        val backgroundWorker = BackgroundWorker(WeakReference(this), getString(R.string.loginStatus), this, WeakReference(loginProgress))
        backgroundWorker.execute(operation, username, password)
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivityForResult(intent, 0)
    }

    override fun processFinish(output: String) {
        if (output.contains("Welcome")) {
            userSession.createLoginSession(username, password)

            (application as MyApp).startUserSession()

            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            btnLogin.visibility = VISIBLE
            btnRegister.visibility = VISIBLE
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            finish()
        }
    }

    override fun onSessionTimeout() {
        userSession.logoutUser()
    }

}
