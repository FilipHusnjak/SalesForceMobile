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

        if (intent.getBooleanExtra(getString(R.string.EXTRA_IS_EDITOR_FOR_NEW_ITEM), false)) {
            this.title = getString(R.string.newContact)
        } else {
            this.title = getString(R.string.editContact)
        }

        val adapterTitle = ArrayAdapter.createFromResource(
                this,
                R.array.contactTitle_array,
                R.layout.simple_spinner_dropdown_item
        )
        spContactTitle.adapter = adapterTitle

        val adapterStatus = ArrayAdapter.createFromResource(
                this,
                R.array.contactStatus_array,
                R.layout.simple_spinner_dropdown_item
        )
        spContactStatus.adapter = adapterStatus

        etContactFirstName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etContactFirstName.setText(etContactFirstName.text.toString().trim())
                etContactFirstName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile, 0, 0, 0)
            } else {
                etContactFirstName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile_accent, 0, 0, 0)
            }
        }
        etContactLastName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etContactLastName.setText(etContactLastName.text.toString().trim())
                etContactLastName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile, 0, 0, 0)
            } else {
                etContactLastName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_smile_accent, 0, 0, 0)
            }
        }
        //FIXME etCompanyNameContact bi trebao imati svoj button i onda search ili create new (nije editable)
        etContactCompanyName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etContactCompanyName.setText(etContactCompanyName.text.toString().trim())
                etContactCompanyName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_company, 0, 0, 0)
            } else {
                etContactCompanyName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_company_accent, 0, 0, 0)
            }
        }
        etContactPhone.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etContactPhone.setText(etContactPhone.text.toString().trim())
                etContactPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_phone, 0, 0, 0)
            } else {
                etContactPhone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_phone_accent, 0, 0, 0)
            }
        }
        etContactEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                etContactEmail.setText(etContactEmail.text.toString().trim())
                etContactEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email_outline, 0, 0, 0)
            } else {
                etContactEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_email_outline_accent, 0, 0, 0)
            }
        }

        spContactTitle.setSelection(intent.getIntExtra(getString(R.string.EXTRA_CONTACT_TITLE_SPINNER_INDEX), 0))
        spContactStatus.setSelection(intent.getIntExtra(getString(R.string.EXTRA_CONTACT_STATUS_SPINNER_INDEX), 0))
        etContactFirstName.setText(intent.getStringExtra(getString(R.string.EXTRA_CONTACT_FIRST_NAME)))
        etContactLastName.setText(intent.getStringExtra(getString(R.string.EXTRA_CONTACT_LAST_NAME)))
        //FIXME Ovo ne bi trebao biti string (ovisi o tome sto ce se dogoditi s tim poljem)
        etContactCompanyName.setText(intent.getStringExtra(getString(R.string.EXTRA_CONTACT_COMPANY)))
        etContactPhone.setText(intent.getStringExtra(getString(R.string.EXTRA_CONTACT_PHONE)))
        etContactEmail.setText(intent.getStringExtra(getString(R.string.EXTRA_CONTACT_EMAIL)))
        etContactDetails.setText(intent.getStringExtra(getString(R.string.EXTRA_CONTACT_DETAILS)))

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
