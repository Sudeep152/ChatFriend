package com.sudeep.chatfriend.CreatProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.FirebaseFirestoreKtxRegistrar
import com.google.firebase.storage.FirebaseStorage
import com.sudeep.chatfriend.DataModels.CreateUser
import com.sudeep.chatfriend.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_creatprofile.*
import java.io.File

class CreatprofileActivity : AppCompatActivity() {

    val name:EditText by lazy {
        findViewById(R.id.nameET)
    }
    val bio:EditText by lazy {
        findViewById(R.id.bioET)
    }
    val nextBtn: Button by lazy {
        findViewById(R.id.Nbtn)

    }
    val profileImage:CircleImageView by lazy {
        findViewById(R.id.profile_image)
    }
    val addPF:FloatingActionButton by lazy {
        findViewById(R.id.addImg)
    }

    private var imgaeUri:Uri? =null
   val firebaseDB: FirebaseFirestore= FirebaseFirestore.getInstance()
    val storageRF=FirebaseStorage.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creatprofile)

        //profile Picture
        addPF.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }


        nextBtn.setOnClickListener {
           if(TextUtils.isEmpty(name.text.toString()) || TextUtils.isEmpty(bio.text.toString())){
               Toast.makeText(this,"Please fill all fields and Make Sure Upload profile Image",Toast.LENGTH_SHORT).show()

           }

           else{
               createData()
           }

        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data

            Glide
                .with(this)
                .load(fileUri)
                .centerCrop()
                .placeholder(R.drawable.person)
                .into(profileImage);
//            profileImage.setImageURI(fileUri)


            imgaeUri=fileUri
            //You can get File object from intent
            val file: File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    private fun createData() {

        val name=name.text.toString()
        val bio =bio.text.toString()
        val currentUserId=FirebaseAuth.getInstance().currentUser?.uid.toString()



      var storageForImage= storageRF.reference.child("ProfileImages").child(
            FirebaseAuth.getInstance().currentUser?.phoneNumber.toString() +"_"+System.currentTimeMillis()+".jpg")

        val uploadTask =storageForImage.putFile(imgaeUri!!)
        val urlTask= uploadTask?.continueWithTask{ task->
            if (!task.isSuccessful){
                Toast.makeText(this,"UploadTask",Toast.LENGTH_LONG).show()
            }
            storageForImage.downloadUrl
        }.addOnCompleteListener { task->
            if(task.isSuccessful){
               val download=task.result.toString()
                val userDataClass=CreateUser(name,currentUserId,bio,download)
                firebaseDB.collection("Users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).set(userDataClass)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Successfully",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,"sometiong"+task.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }.addOnFailureListener{
            val userDataClass=CreateUser(name,currentUserId,bio,"")
            firebaseDB.collection("Users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).set(userDataClass)
        }






    }

}