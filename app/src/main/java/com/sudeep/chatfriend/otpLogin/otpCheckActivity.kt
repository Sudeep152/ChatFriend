package com.sudeep.chatfriend.otpLogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sudeep.chatfriend.MainActivity
import com.sudeep.chatfriend.R
import kotlinx.android.synthetic.main.activity_mobile.*
import kotlinx.android.synthetic.main.activity_otp_check.*
import java.util.concurrent.TimeUnit


class otpCheckActivity : AppCompatActivity(){

    lateinit var  auth:FirebaseAuth

   var verificationInProcess=false
    var storedVerificationId:String=""
    lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    val mobileNo :TextView by lazy {
        findViewById(R.id.mobileN)
    }
    val nextBtn : Button by lazy {
        findViewById(R.id.nextBtn)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_check)

        var mob=intent.getStringExtra("PHONENO")
        mobileNo.text= "$mob"


      intit()


    }
    fun intit(){

        nextBtn.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, firstPinView.text.toString())
            signInWithAuth(credential)
        }

        var mob=intent.getStringExtra("PHONENO")
        auth = FirebaseAuth.getInstance()

        callbacks=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                storedVerificationId=p0
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }

            override fun onVerificationCompleted(credential:  PhoneAuthCredential) {
                val smsCode=credential.smsCode
                firstPinView.setText(smsCode)
                signInWithAuth(credential)

            }

            override fun onVerificationFailed(p0: FirebaseException) {
               if (p0 is FirebaseAuthInvalidCredentialsException){
                   Snackbar.make(findViewById(android.R.id.content),"Invalid phone number",Snackbar.LENGTH_SHORT).show()
               }else if(p0 is FirebaseTooManyRequestsException ){
                   Snackbar.make(findViewById(android.R.id.content),"You have try to many",Snackbar.LENGTH_SHORT).show()
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
                        // Sign in success, update UI with the signed-in user's information

                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()

                    }
                }
    }

}

