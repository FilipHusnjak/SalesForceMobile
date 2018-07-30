package hr.atoscvc.salesforcemobile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import hr.atoscvc.salesforcemobile.BackgroundWorker.AsyncResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.reset_password.view.*
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), AsyncResponse, LogoutListener {

    //LUKA - password reset trimming ne valja
    //LUKA - bolje dizajnirati password reset (forgot) prozor - onfocus boja
    //LUKA - Activity za change password - old password + new + confirm - ChangePasswordActivity.kt (Constraints!)
    //LUKA - make scrollable - ocajno kad je landscape mode
    //LUKA - Dodati Log na sve Create/Resume itd.
    //LUKA - Python line counter

    //FILIP - novi password (hard reset) nema nikakve provjere - implementirati u PHP-u
    //FILIP - forgot password loader indicator se trenutno vrti iza prozora pa se ne vidi (?)
    //FILIP - EventLog SQL tablica (EventID primary key + String) - "User $userID forgot his password", "User $userID set a new password", "User $userID changed his password", "User $userID logged in/out"

    private lateinit var userSession: SessionManager
    private lateinit var username: String
    private lateinit var passwordHash: String
    private lateinit var alertDialogBuilder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private lateinit var resetPasswordView: View
    private lateinit var operation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getBooleanExtra("EXIT", false)) {
            finish()
            System.exit(0)
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

        loginLayout.background = BitmapDrawable(
                applicationContext.resources,
                (application as MyApp).getInstance(applicationContext.resources)
        )

        if (userSession.isLoggedIn()) {
            (application as MyApp).startUserSession()
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /*override fun onPause() {
        super.onPause()
        (application as MyApp).recycleInstanceAndSetToNull()
    }*/

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

            operation = "Login"
            val backgroundWorker = BackgroundWorker(
                    WeakReference(applicationContext),
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

    @SuppressLint("InflateParams")
    fun onForgotPassword(@Suppress("UNUSED_PARAMETER") view: View) {
        resetPasswordView = layoutInflater.inflate(R.layout.reset_password, null)
        alertDialogBuilder = AlertDialog.Builder(this)
        alertDialog = alertDialogBuilder.create()
        alertDialog.setView(resetPasswordView)
        alertDialog.show()

        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun onSendEmail(@Suppress("UNUSED_PARAMETER") view: View) {
        var thereAreNoErrors = true

        val username = resetPasswordView.etUsernamePassReset.text.toString().trim()
        val email = resetPasswordView.etEmailPassReset.text.toString().trim()

        if (username.isBlank()) {
            resetPasswordView.etUsernamePassReset.error = getString(R.string.usernameEmptyMessage)
            thereAreNoErrors = false
        }

        if (email.isBlank()) {
            resetPasswordView.etEmailPassReset.error = getString(R.string.emailEmptyMessage)
            thereAreNoErrors = false
        }

        if (thereAreNoErrors) {
            resetPasswordView.btnSendPassReset.visibility = INVISIBLE
            operation = "PasswordReset"
            val backgroundWorker = BackgroundWorker(
                    WeakReference(applicationContext),
                    getString(R.string.loginStatus),
                    this,
                    WeakReference(loginProgress)
            )
            backgroundWorker.execute(operation, username, email)
        }
    }

    override fun processFinish(output: String) {
        when {
            output.contains("Welcome") -> {
                userSession.createLoginSession(username, passwordHash, cbSave.isChecked)

                (application as MyApp).startUserSession()

                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
            }
            output.contains("Success") -> {
                resetPasswordView.btnSendPassReset.visibility = INVISIBLE
                Toast.makeText(this, "Check your email", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
            else -> {
                if (operation == getString(R.string.typeLogin)) {
                    btnLogin.visibility = VISIBLE
                    tvRegister.visibility = VISIBLE
                } else {
                    resetPasswordView.btnSendPassReset.visibility = VISIBLE
                }

                Toast.makeText(this, output, Toast.LENGTH_SHORT).show()
            }
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
