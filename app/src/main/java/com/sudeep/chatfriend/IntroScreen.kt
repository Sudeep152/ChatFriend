package com.sudeep.chatfriend

import android.content.Intent
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

            nextBtn.startAnimation(animation)
            startActivity(Intent(this, MobileActivity::class.java))

        }
    }
}