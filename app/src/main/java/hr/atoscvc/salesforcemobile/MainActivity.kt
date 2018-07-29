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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import hr.atoscvc.salesforcemobile.BackgroundWorker.AsyncResponse
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), AsyncResponse, LogoutListener {

    //TODO Luka background Glide
    //TODO Luka Reset trimming
    //TODO Posvijetliti background sliku

    private lateinit var userSession: SessionManager
    lateinit var username: String
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

        loginLayout.background = BitmapDrawable(resources, (application as MyApp).getInstance(this))

        if (userSession.isLoggedIn()) {
            (application as MyApp).startUserSession()
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            if (!(application as MyApp).getInstance(this).isRecycled) {
                (application as MyApp).getInstance(this).recycle()
            }
        } catch (e: Exception) {
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

            operation = "Login"
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

    @SuppressLint("InflateParams")
    fun onForgotPassword(@Suppress("UNUSED_PARAMETER") view: View) {
        resetPasswordView = layoutInflater.inflate(R.layout.reset_password, null)

        alertDialogBuilder = AlertDialog.Builder(this)
        alertDialog = alertDialogBuilder.create()
        alertDialog.setView(resetPasswordView)
        alertDialog.show()
    }

    fun onSendEmail(@Suppress("UNUSED_PARAMETER") view: View) {
        val etUsername: EditText = resetPasswordView.findViewById(R.id.etUsername)
        val etEmail: EditText = resetPasswordView.findViewById(R.id.etEmail)

        var thereAreNoErrors = true

        val username = etUsername.text.toString()
        val email = etEmail.text.toString()

        if (username.isBlank()) {
            etUsername.error = getString(R.string.usernameEmptyMessage)
            thereAreNoErrors = false
        }

        if (email.isBlank()) {
            etEmail.error = getString(R.string.emailEmptyMessage)
            thereAreNoErrors = false
        }

        if (thereAreNoErrors) {
            (resetPasswordView.findViewById(R.id.btnSend) as Button).visibility = INVISIBLE
            operation = "PasswordReset"
            val backgroundWorker = BackgroundWorker(
                    WeakReference(this),
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
                (resetPasswordView.findViewById(R.id.btnSend) as Button).visibility = INVISIBLE
                Toast.makeText(this, "Check your email", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
            else -> {
                if (operation == getString(R.string.typeLogin)) {
                    btnLogin.visibility = VISIBLE
                    tvRegister.visibility = VISIBLE
                } else {
                    (resetPasswordView.findViewById(R.id.btnSend) as Button).visibility = VISIBLE
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
