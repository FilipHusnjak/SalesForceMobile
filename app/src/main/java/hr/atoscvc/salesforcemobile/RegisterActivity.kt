package hr.atoscvc.salesforcemobile

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import hr.atoscvc.salesforcemobile.BackgroundWorker.AsyncResponse
import hr.atoscvc.salesforcemobile.CheckPasswordConstraints.checkPasswordConstraints
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.ref.WeakReference

//TODO Buttoni su fakat ruzni
//TODO Ikonice koje mijenjaju boju na fokus change
//TODO Activity za change password - old password + new + confirm
//TODO make scrollable - ocajno kad je landscape mode
//TODO Do you want to exit - yes -> i dalje trosi 180 MB RAM-a dok se ne ubije rucno
//TODO kako ispravno dodati sliku (background ostaje kad se ubije login/register activity)

//TODO Python line counter

class RegisterActivity : AppCompatActivity(), AsyncResponse {

    private lateinit var userSession: SessionManager

    lateinit var username: String
    private lateinit var passwordHashed: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var email: String

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userSession = SessionManager(this)

        registerProgress.visibility = View.INVISIBLE
        btnRegister.visibility = View.VISIBLE

        etUsername.addTextChangedListener(PasswordTextWatcher(etUsername, etPassword))
        etPassword.addTextChangedListener(PasswordTextWatcher(etUsername, etPassword))

        etFirstName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etFirstName.setText(etFirstName.text.toString().trim())
            }
        }
        etLastName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etLastName.setText(etLastName.text.toString().trim())
            }
        }
        etUsername.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etUsername.setText(etUsername.text.toString().trim())
            }
        }
        etEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etEmail.setText(etEmail.text.toString().trim())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerProgress.visibility = View.INVISIBLE
        btnRegister.visibility = View.VISIBLE
    }

    fun onRegister(@Suppress("UNUSED_PARAMETER") view: View) {
        var thereAreNoErrors = true

        val password = etPassword.text.toString()       // Do NOT trim the password
        firstName = etFirstName.text.toString().trim()
        lastName = etLastName.text.toString().trim()
        username = etUsername.text.toString().trim()
        email = etEmail.text.toString().trim()

        val passwordStatus = checkPasswordConstraints(username, password)

        if (!passwordStatus.success) {
            etPassword.error = passwordStatus.message
            thereAreNoErrors = false
        }
        if (username.isBlank()) {
            etUsername.error = getString(R.string.usernameEmptyMessage)
            thereAreNoErrors = false
        }
        if (firstName.isBlank()) {
            etFirstName.error = getString(R.string.firstNameEmptyMessage)
            thereAreNoErrors = false
        }
        if (lastName.isBlank()) {
            etLastName.error = getString(R.string.lastNameEmptyMessage)
            thereAreNoErrors = false
        }
        if (email.isBlank()) {
            etEmail.error = getString(R.string.emailEmptyMessage)
            thereAreNoErrors = false
        }
        if (password != etConfirm.text.toString()) {
            etConfirm.error = getString(R.string.wrongConfirmPassword)
            thereAreNoErrors = false
        }

        if (thereAreNoErrors) {
            val type = "Register"
            passwordHashed = HashSHA3.getHashedValue(password)
            btnRegister.visibility = View.INVISIBLE
            val backgroundWorker = BackgroundWorker(
                    WeakReference(this),
                    getString(R.string.registrationStatus),
                    this,
                    WeakReference(registerProgress)
            )
            backgroundWorker.execute(type, firstName, lastName, username, passwordHashed)
        }
    }

    override fun processFinish(output: String) {
        btnRegister.visibility = View.VISIBLE
        if (output.contains("successful")) {
            userSession.createLoginSession(username, passwordHashed, false)

            (application as MyApp).startUserSession()

            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
        }
    }

}