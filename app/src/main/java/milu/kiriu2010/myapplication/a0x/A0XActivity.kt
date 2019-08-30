package milu.kiriu2010.myapplication.a0x

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import milu.kiriu2010.myapplication.R
import milu.kiriu2010.myapplication.a0x.a01.A01Fragment
import milu.kiriu2010.myapplication.a0x.a02.A02Fragment
import milu.kiriu2010.myapplication.a0x.a03.A03Fragment
import milu.kiriu2010.myapplication.a0x.a04.A04Fragment

class A0XActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a0x)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("xyz") == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, A04Fragment.newInstance(), "xyz")
                .commit()
        }

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_a0x, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            // saveLayer
            R.id.a04 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a04") == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, A04Fragment.newInstance(), "a04")
                        .commit()
                }
                true
            }
            // PorterDuff
            R.id.a03 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a03") == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, A03Fragment.newInstance(), "a03")
                        .commit()
                }
                true
            }
            // PorterDuff
            R.id.a02 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a02") == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, A02Fragment.newInstance(), "a02")
                        .commit()
                }
                true
            }
            // PathEffect
            R.id.a01 -> {
                supportFragmentManager.popBackStack()
                if (supportFragmentManager.findFragmentByTag("a01") == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, A01Fragment.newInstance(), "a01")
                        .commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
