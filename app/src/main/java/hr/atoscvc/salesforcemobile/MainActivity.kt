package hr.atoscvc.salesforcemobile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import hr.atoscvc.salesforcemobile.BackgroundWorker.AsyncResponse
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), AsyncResponse {
    //TODO 3 kriva logina blokiraju usera - boolean u bazi
    //TODO Forgot my password - novi random na mail - reset on first login - boolean u bazi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginProgress.visibility = View.INVISIBLE
        btnLogin.visibility = View.VISIBLE
        btnRegister.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        loginProgress.visibility = View.INVISIBLE
        btnLogin.visibility = View.VISIBLE
        btnRegister.visibility = View.VISIBLE
    }

    fun onLogin(@Suppress("UNUSED_PARAMETER") view: View) {
        btnLogin.visibility = View.INVISIBLE
        btnRegister.visibility = View.INVISIBLE
        val username: String = etUsername.text.toString()
        val password: String = etPassword.text.toString()
        val operation = "Login"
        val backgroundWorker = BackgroundWorker(WeakReference(this), getString(R.string.loginStatus), this, loginProgress)
        backgroundWorker.execute(operation, username, password)
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivityForResult(intent, 0)
    }

    override fun processFinish(output: String) {
        if (output.contains("Welcome")) {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            btnLogin.visibility = View.VISIBLE
            btnRegister.visibility = View.VISIBLE
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            finish()
        }
    }

}
