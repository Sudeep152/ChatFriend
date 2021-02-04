package com.sudeep.chatfriend

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sudeep.chatfriend.otpLogin.MobileActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sharedPreferences :SharedPreferences =getSharedPreferences("code", MODE_PRIVATE)
        var editor: SharedPreferences.Editor= sharedPreferences.edit()

        signOut.setOnClickListener{
            editor.putString("FLAG","FALSE")
            editor.apply()
           onBackPressed()


        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity();
    }
}