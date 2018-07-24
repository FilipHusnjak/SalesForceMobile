package hr.atoscvc.salesforcemobile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import hr.atoscvc.salesforcemobile.BackgroundWorker.AsyncResponse
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.ref.WeakReference


class RegisterActivity : AppCompatActivity(), AsyncResponse {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerProgress.visibility = View.INVISIBLE
        btnRegister.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        registerProgress.visibility = View.INVISIBLE
        btnRegister.visibility = View.VISIBLE
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        val type = "Register"
        if (etPassword.text.toString() == etConfirm.text.toString()) {
            btnRegister.visibility = View.INVISIBLE
            val backgroundWorker = BackgroundWorker(WeakReference(this), getString(R.string.registrationStatus), this, registerProgress)
            backgroundWorker.execute(
                    type,
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    etUsername.text.toString(),
                    etPassword.text.toString()
            )

        } else {
            Toast.makeText(this, getString(R.string.wrongConfirmPassword), Toast.LENGTH_SHORT).show()
        }
    }

    override fun processFinish(output: String) {
        btnRegister.visibility = View.VISIBLE
        if (output.contains("successful")) {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
        }
    }
}