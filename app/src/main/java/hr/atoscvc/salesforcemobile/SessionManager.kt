package hr.atoscvc.salesforcemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

//FILIP - ako se u Manifestu u MainMenuActivity stavi noHistory=true i stisne Back u Change Password...
// ...aplikacija se ugasi, a na restartu user ostane ulogiran
class SessionManager(private val context: Context) {

    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private lateinit var savedData: String
    private var savedBool: Boolean = false

    init {
        pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
        editor = pref.edit()
        editor.apply()
    }

    companion object {
        const val PREF_NAME: String = "LoginData"
        const val IS_LOGIN: String = "isLoggedIn"
        const val KEY_USERNAME: String = "username"
        const val KEY_SAVE: String = "save"
    }

    fun createLoginSession(username: String, save: Boolean) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, username)
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