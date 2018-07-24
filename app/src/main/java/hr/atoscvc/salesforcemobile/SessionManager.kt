package hr.atoscvc.salesforcemobile

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent


class SessionManager(private var context: Context) {

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var PRIVATE_MODE: Int = 0

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {
        val PREF_NAME: String = "LoginData"
        val IS_LOGIN: String = "isLoggedIn"
        val KEY_USERNAME: String = "username"
        val KEY_PASSWORD: String = "password"

    }

    fun createLoginSession(username: String, password: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.commit()
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            var intent: Intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

    }

    fun getUserDetails(): HashMap<String, String> {
        var user: HashMap<String, String> = HashMap<String, String>()
        user[KEY_USERNAME] = pref.getString(KEY_USERNAME, null)
        user[KEY_PASSWORD] = pref.getString(KEY_PASSWORD, null)
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()

        val intent: Intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

}