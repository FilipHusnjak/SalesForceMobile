package hr.atoscvc.salesforcemobile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    //TODO 3 kriva logina blokiraju usera - boolean u bazi
    //TODO Forgot my password - novi random na mail - reset on first login - boolean u bazi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onLogin(@Suppress("UNUSED_PARAMETER") view: View) {
        val username: String = etUsername.text.toString()
        val password: String = etPassword.text.toString()
        val operation = "Login"
        val backgroundWorker = BackgroundWorker(WeakReference(this), getString(R.string.loginStatus))
        backgroundWorker.execute(operation, username, password)
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

}
