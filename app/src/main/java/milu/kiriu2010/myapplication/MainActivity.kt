package milu.kiriu2010.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import milu.kiriu2010.myapplication.a0x.A0XActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // A0xページへ遷移
        btnA0X.transformationMethod = null
        btnA0X.setOnClickListener {
            val intent = Intent(this, A0XActivity::class.java)
            startActivity(intent)
        }
    }
}
