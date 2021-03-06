package com.sudeep.chatfriend.otpLogin

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide.init
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.sudeep.chatfriend.CreatProfile.CreatprofileActivity
import com.sudeep.chatfriend.CreatProfile.OldProfileActivity
import com.sudeep.chatfriend.R
import kotlinx.android.synthetic.main.activity_mobile.*
import kotlinx.android.synthetic.main.activity_otp_check.*
import java.util.concurrent.TimeUnit


class otpCheckActivity : AppCompatActivity(){

    lateinit var  auth:FirebaseAuth
    var storedVerificationId:String=""
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val resendOtp: Button by lazy {
        findViewById(R.id.resendBtn)
    }
    val mobileNo :TextView by lazy {
        findViewById(R.id.verifyTv)
    }
    val nextBtn : Button by lazy {
        findViewById(R.id.old_NBtn)
    }
    val counterTv:TextView by lazy {
        findViewById(R.id.counterTv)
    }

    lateinit var counter:CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_check)




        var mob=intent.getStringExtra("PHONENO")
        mobileNo.text= "$mob"


           intit()
        startVerify()



    }
    private fun startVerify() {
        var mob=intent.getStringExtra("PHONENO")
        startPhoneNumberVerfication(mob)
        starttime(60000)

    }

    fun starttime(time:Long){
        resendOtp.isEnabled=false
        counterTv.isEnabled=true
        counter=object:CountDownTimer(time,1000){
            override fun onTick(timeLeft: Long) {
              counterTv.text= "Seconds Remaining : " + timeLeft / 1000
            }

            override fun onFinish() {
                resendOtp.isEnabled=true

            }

        }

    }



    fun intit() {

        var process=ProgressDialog(this)
        process.setMessage("Loading...")
        process.show()
        process.setCancelable(false)
        nextBtn.setOnClickListener {
            if(firstPinView.text.toString().isEmpty()){
                Toast.makeText(this,"Please Enter Otp",Toast.LENGTH_SHORT).show()
            }else {

                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, firstPinView.text.toString())
                signInWithAuth(credential)
            }
        }
        var mob = intent.getStringExtra("PHONENO")
        auth = FirebaseAuth.getInstance()
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                process.dismiss()
                storedVerificationId = p0
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val smsCode = credential.smsCode
                process.dismiss()
                firstPinView.setText(smsCode)
                signInWithAuth(credential)

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                process.dismiss()
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid phone number", Snackbar.LENGTH_SHORT).show()
                    finish()
                } else if (p0 is FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "You have try too many times", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        startPhoneNumberVerfication(mob)
    }

    private fun startPhoneNumberVerfication(mob: String?) {
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(mob.toString())       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    private fun signInWithAuth(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        var sharedPreferences :SharedPreferences =getSharedPreferences("code", MODE_PRIVATE)
                        var editor:SharedPreferences.Editor=sharedPreferences.edit()

                        editor.putString("FLAG","TRUE")
                        editor.apply()
                        if(task.result?.additionalUserInfo?.isNewUser==true) {
                            Toast.makeText(this, "You are new User ", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,CreatprofileActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this,"Old User",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,OldProfileActivity::class.java))
                            finish()
                        }
//
                    } else {
                        Toast.makeText(this,"Please enter valid otp",Toast.LENGTH_SHORT).show()
                    }
                }
    }



}

