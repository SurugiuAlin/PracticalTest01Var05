package ro.pub.cs.systems.eim.practicaltest01var05

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PracticalTest01Var05SecondaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test01_var05_secondary)

        val textView = findViewById<TextView>(R.id.textView)
        val verifyButton = findViewById<Button>(R.id.verifyButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        val template = intent.getStringExtra("template")
        textView.text = template

        verifyButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}