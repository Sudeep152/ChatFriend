package com.sudeep.chatfriend.CreatProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.sudeep.chatfriend.DataModels.CreateUser
import com.sudeep.chatfriend.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_old_profile.*

class OldProfileActivity : AppCompatActivity() {

    val bio: TextView by lazy {
        findViewById(R.id.oldpfBio)
    }
    val nextBtn: Button by lazy {
        findViewById(R.id.old_NBtn)

    }
    val profileImage: CircleImageView by lazy {
        findViewById(R.id.old_profile_image)
    }
    val name:TextView by lazy {
        findViewById(R.id.oldpfName)
    }
   var firestore: FirebaseFirestore= FirebaseFirestore.getInstance()
  lateinit  var databaseReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_profile)

  var user=CreateUser()
        var databaseReferenc: Task<DocumentSnapshot> =firestore.collection("Users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).get()
            .addOnSuccessListener { document->
                if(document.exists()){
                   name.text=document.getString("name")
                   bio.text=document.getString("bio")
                    Glide
                        .with(this)
                        .load(document.getString("profile"))
                        .centerCrop()
                        .placeholder(R.drawable.person)
                        .into(profileImage);

                }else
                {
                    Toast.makeText(this,"Your Profile was not created",Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,CreatprofileActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }


        old_Edit.setOnClickListener {
            val intent=Intent(this,CreatprofileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}