package hr.atoscvc.salesforcemobile

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class CompanyEditorActivity : AppCompatActivity() {

    private lateinit var userSession: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_editor)

        userSession = SessionManager(this)

        if (intent.getBooleanExtra(getString(R.string.isEditorForNewItemExtra), false)) {
            this.title = getString(R.string.newCompany)
        } else {
            this.title = getString(R.string.editCompany)
        }

    }

    override fun onResume() {
        super.onResume()
        userSession.checkLogin()

        window.setBackgroundDrawable(BitmapDrawable(
                applicationContext.resources,
                (application as MyApp).getInstance(applicationContext.resources)
        ))
    }

    override fun onUserInteraction() {
        if (userSession.isLoggedIn()) {
            (application as MyApp).onUserInteracted()
        }
    }
}
