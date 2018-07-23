package hr.atoscvc.salesforcemobile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.ref.WeakReference

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        val type = "Register"
        if (etPassword.text.toString() == etConfirm.text.toString()) {
            val backgroundWorker = BackgroundWorker(WeakReference(this), getString(R.string.registrationStatus))
            backgroundWorker.execute(type, etFirstName.text.toString(), etLastName.text.toString(), etUsername.text.toString(), etPassword.text.toString())
        } else {
            Toast.makeText(this, getString(R.string.wrongConfirmPassword), Toast.LENGTH_SHORT).show()
        }

    }

}

