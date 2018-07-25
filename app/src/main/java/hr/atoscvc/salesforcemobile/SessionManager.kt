package hr.atoscvc.salesforcemobile

import android.accounts.AccountManager.KEY_PASSWORD
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences


class SessionManager(private var context: Context) {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    lateinit var savedData: String
    var savedBool: Boolean = false
    private var PRIVATE_MODE: Int = 0

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()    //TODO LUKA FER tutorial
    }

    companion object {
        val PREF_NAME: String = "LoginData"
        val IS_LOGIN: String = "isLoggedIn"
        val KEY_USERNAME: String = "username"
        val KEY_PASSWORD: String = "password"
        val KEY_SAVE: String = "save"
    }

    fun createLoginSession(username: String, passwordHash: String, save: Boolean) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, passwordHash)
        editor.putBoolean(KEY_SAVE, save)
        editor.commit()
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

    }

    fun getUserDetails(): HashMap<String, String> {
        val user: HashMap<String, String> = HashMap()
        user[KEY_USERNAME] = pref.getString(KEY_USERNAME, "")
        user[KEY_PASSWORD] = pref.getString(KEY_PASSWORD, "")
        return user
    }

    fun logoutUserData() {
        savedData = ""
        savedBool = pref.getBoolean(KEY_SAVE, false)
        if (savedBool) {
            savedData = pref.getString(KEY_USERNAME, "")
        }

        editor.clear()
        editor.commit()

        editor.putBoolean(KEY_SAVE, savedBool)
        editor.putString(KEY_USERNAME, savedData)
        editor.commit()

    }

    fun logoutUserView() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun exitApp() {
        val intent = Intent(context.applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("EXIT", true)
        context.startActivity(intent)
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

}