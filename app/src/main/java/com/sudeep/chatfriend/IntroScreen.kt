package com.sudeep.chatfriend

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.sudeep.chatfriend.otpLogin.MobileActivity
import kotlinx.android.synthetic.main.activity_intro_screen.*

class IntroScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_screen)

        val animation =AnimationUtils.loadAnimation(applicationContext,R.anim.btnclick)
        nextBtn.setOnClickListener {

            var sharedPreferences:SharedPreferences=getSharedPreferences("code", MODE_PRIVATE)
            var code=sharedPreferences.getString("FLAG","FALSE")

            if(code.equals("TRUE")){
                nextBtn.startAnimation(animation)
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                nextBtn.startAnimation(animation)
                startActivity(Intent(this, MobileActivity::class.java))

            }



        }
    }
}