package hr.atoscvc.salesforcemobile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import hr.atoscvc.salesforcemobile.BackgroundWorker.AsyncResponse
import hr.atoscvc.salesforcemobile.RegisterActivity.CheckPasswordConstraints.checkPasswordConstraints
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.ref.WeakReference


class RegisterActivity : AppCompatActivity(), AsyncResponse {

    private lateinit var userSession: SessionManager

    lateinit var username: String
    lateinit var password: String

    data class PasswordErrors(val message: String, val success: Boolean)

    private class PasswordTextWatcher(val editTextUser: EditText, val editTextPass: EditText) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val editTextUserString = editTextUser.text.toString()
            val editTextPassString = editTextPass.text.toString()

            if (editTextPassString.isNotEmpty()) {
                val isPassGood = checkPasswordConstraints(editTextUserString, editTextPassString)
                if (!isPassGood.success) {
                    editTextPass.error = isPassGood.message
                } else {
                    editTextPass.error = null
                }
            } else {
                editTextPass.error = null
            }
        }
    }

    object CheckPasswordConstraints {
        fun checkPasswordConstraints(user: String, pass: String): PasswordErrors {
            if (pass.length < 8) {
                return PasswordErrors("Password must be at least 8 characters long", false)
            }
            if (pass.length > 30) {
                return PasswordErrors("Password cannot contain more than 30 characters", false)
            }
            if (user.isNotEmpty() && pass.contains(user, true)) {
                return PasswordErrors("Password must not contain the username", false)
            }
            if (user.contains(pass, true)) {
                return PasswordErrors("Username must not contain the password", false)
            }
            if (!pass.matches(".*[A-Z].*".toRegex())) {
                return PasswordErrors("Password must contain at least one uppercase letter", false)
            }
            if (!pass.matches(".*[a-z].*".toRegex())) {
                return PasswordErrors("Password must contain at least one lowercase letter", false)
            }
            if (!pass.matches(".*\\d.*".toRegex())) {
                return PasswordErrors("Password must contain at least one digit", false)
            }

            return PasswordErrors("Password is fine.", true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userSession = SessionManager(this)

        registerProgress.visibility = View.INVISIBLE
        btnRegister.visibility = View.VISIBLE

        etUsername.addTextChangedListener(PasswordTextWatcher(etUsername, etPassword))
        etPassword.addTextChangedListener(PasswordTextWatcher(etUsername, etPassword))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            etUsername.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_action_username, 0, 0, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        registerProgress.visibility = View.INVISIBLE
        btnRegister.visibility = View.VISIBLE
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        username = etUsername.text.toString()
        password = etPassword.text.toString()
        val passwordStatus = checkPasswordConstraints(username, password)

        if (!passwordStatus.success) {
            etPassword.error = passwordStatus.message
        } else {
            val type = "Register"
            if (password == etConfirm.text.toString()) {
                btnRegister.visibility = View.INVISIBLE
                val backgroundWorker = BackgroundWorker(WeakReference(this), getString(R.string.registrationStatus), this, registerProgress)
                backgroundWorker.execute(type, etFirstName.text.toString(), etLastName.text.toString(), username, password)
            } else {
                etConfirm.error = getString(R.string.wrongConfirmPassword)
            }
        }
    }

    override fun processFinish(output: String) {
        btnRegister.visibility = View.VISIBLE
        if (output.contains("successful")) {
            userSession.createLoginSession(username, password)

            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
        }
    }
}