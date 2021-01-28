package com.sudeep.chatfriend.otpLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.sudeep.chatfriend.R
import kotlinx.android.synthetic.main.activity_mobile.*

class MobileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile)

        mobileET.addTextChangedListener {
            nextBtn.isEnabled = !(it.isNullOrEmpty() || (it.length <10))
        }

        nextBtn.setOnClickListener {
            if (mobileET.text.length>10){
                Toast.makeText(this,"Please enter valid number",Toast.LENGTH_LONG).show()
            }else{
              
                val country=cpp.selectedCountryCodeWithPlus.toString()
                val mobileNumebr=country + mobileET.text.toString()
                Toast.makeText(this,"Mobile $mobileNumebr",Toast.LENGTH_LONG).show()

                val intent= Intent(this, otpCheckActivity::class.java)
                intent.putExtra("PHONENO",mobileNumebr)
                startActivity(intent)
            }


        }
    }


}