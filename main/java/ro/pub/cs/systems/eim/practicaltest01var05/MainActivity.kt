package ro.pub.cs.systems.eim.practicaltest01var05

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private val buttonTexts = mutableListOf<String>()
    private var buttonPressCount = 0
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var receiver: PracticalTest01Var05Receiver

    companion object {
        const val THRESHOLD = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        val buttons = listOf(
            findViewById<Button>(R.id.buttonTopLeft),
            findViewById<Button>(R.id.buttonTopRight),
            findViewById<Button>(R.id.buttonBottomLeft),
            findViewById<Button>(R.id.buttonBottomRight),
            findViewById<Button>(R.id.buttonCenter)
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                buttonTexts.add(button.text.toString())
                textView.text = buttonTexts.joinToString(", ")
                buttonPressCount++
            }
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show()
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
            textView.text = ""
            buttonTexts.clear()
            buttonPressCount = 0
        }

        findViewById<Button>(R.id.buttonTransfer).setOnClickListener {
            val template = textView.text.toString()
            val intent = Intent(this, PracticalTest01Var05SecondaryActivity::class.java)
            intent.putExtra("template", template)
            activityResultLauncher.launch(intent)

            if (buttonTexts.size > THRESHOLD) {
                val serviceIntent = Intent(this, PracticalTest01Var05Service::class.java)
                serviceIntent.putExtra("template", template)
                startService(serviceIntent)
            }
        }

        if (savedInstanceState != null) {
            buttonPressCount = savedInstanceState.getInt("buttonPressCount")
            Toast.makeText(this, "Button Press Count: $buttonPressCount", Toast.LENGTH_SHORT).show()
        }

        receiver = PracticalTest01Var05Receiver()
        val filter = IntentFilter("ro.pub.cs.systems.eim.practicaltest01var05.broadcast")
        registerReceiver(receiver, filter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("buttonPressCount", buttonPressCount)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        buttonPressCount = savedInstanceState.getInt("buttonPressCount")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        stopService(Intent(this, PracticalTest01Var05Service::class.java))
    }
}