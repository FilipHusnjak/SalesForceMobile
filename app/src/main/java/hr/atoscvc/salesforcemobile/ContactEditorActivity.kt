package hr.atoscvc.salesforcemobile

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_contact_editor.*

class ContactEditorActivity : AppCompatActivity() {

    //LUKA - Add ScrollView
    //TODO Ovako se dobiva string iz indeksa u bazi:  resources.getStringArray(R.array.contactTitle_array)[i]
    //TODO Ovako se dobiva indeks selektiranog u spinneru:  spTitleContact.selectedItemPosition.toString()

    private lateinit var userSession: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_editor)

        userSession = SessionManager(this)

        if (intent.getBooleanExtra(getString(R.string.isEditorForNewItemExtra), false)) {
            this.title = getString(R.string.newContact)
        } else {
            this.title = getString(R.string.editContact)
        }

        val adapterTitle = ArrayAdapter.createFromResource(
                this,
                R.array.contactTitle_array,
                R.layout.support_simple_spinner_dropdown_item
        )
        adapterTitle.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spTitleContact.adapter = adapterTitle

        val adapterStatus = ArrayAdapter.createFromResource(
                this,
                R.array.contactStatus_array,
                R.layout.support_simple_spinner_dropdown_item
        )
        adapterStatus.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spStatusContact.adapter = adapterStatus

        etFirstNameContact.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etFirstNameContact.setText(etFirstNameContact.text.toString().trim())
                etFirstNameContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile, 0, 0, 0)
            } else {
                etFirstNameContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile_accent, 0, 0, 0)
            }
        }
        etLastNameContact.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etLastNameContact.setText(etLastNameContact.text.toString().trim())
                etLastNameContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile, 0, 0, 0)
            } else {
                etLastNameContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile_accent, 0, 0, 0)
            }
        }
        //FIXME etCompanyNameContact bi trebao imati svoj button i onda search ili create new (nije editable)
        etCompanyNameContact.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etCompanyNameContact.setText(etCompanyNameContact.text.toString().trim())
                etCompanyNameContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_company, 0, 0, 0)
            } else {
                etCompanyNameContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_company_accent, 0, 0, 0)
            }
        }
        etPhoneContact.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etPhoneContact.setText(etPhoneContact.text.toString().trim())
                etPhoneContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_phone, 0, 0, 0)
            } else {
                etPhoneContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_phone_accent, 0, 0, 0)
            }
        }
        etEmailContact.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etEmailContact.setText(etEmailContact.text.toString().trim())
                etEmailContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email_outline, 0, 0, 0)
            } else {
                etEmailContact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email_outline_accent, 0, 0, 0)
            }
        }

        spTitleContact.setSelection(intent.getIntExtra(getString(R.string.contactTitleSpinnerIndexExtra), 0))
        spStatusContact.setSelection(intent.getIntExtra(getString(R.string.contactStatusSpinnerIndexExtra), 0))
        etFirstNameContact.setText(intent.getStringExtra(getString(R.string.contactFirstNameExtra)))
        etLastNameContact.setText(intent.getStringExtra(getString(R.string.contactLastNameExtra)))
        //FIXME Ovo ne bi trebao biti string (ovisi o tome sto ce se dogoditi s tim poljem)
        etCompanyNameContact.setText(intent.getStringExtra(getString(R.string.contactCompanyExtra)))
        etPhoneContact.setText(intent.getStringExtra(getString(R.string.contactPhoneExtra)))
        etEmailContact.setText(intent.getStringExtra(getString(R.string.contactEmailExtra)))
        etDetailsContact.setText(intent.getStringExtra(getString(R.string.contactDetailsExtra)))

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
