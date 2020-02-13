package com.gdc.firebasecrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_createdata.setOnClickListener {
            startActivity(Intent(this, AddDataActivity::class.java))
        }

        btn_viewdata.setOnClickListener {
            startActivity(Intent(this, DisplayDataActivity::class.java))
        }
    }
}
