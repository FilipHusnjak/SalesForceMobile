package hr.atoscvc.salesforcemobile

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast


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
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
        }
        val alert = builder.create()
        alert.show()
    }
}
