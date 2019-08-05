package milu.kiriu2010.myapplication.a0x.a01

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.myapplication.R

class A0XActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a0x)

        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, A01Fragment.newInstance(), "xyz")
                .commit()
        }
    }
}
