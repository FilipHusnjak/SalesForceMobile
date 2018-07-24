package hr.atoscvc.salesforcemobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.R.string.cancel
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Do you want to Exit?")
        builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            finish()
        })
        val alert = builder.create()
        alert.show()

    }
}
