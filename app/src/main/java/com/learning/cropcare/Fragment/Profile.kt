package com.learning.cropcare.Fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.learning.cropcare.R
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.FireStoreDataBaseViewModel
import com.learning.cropcare.ViewModel.StorageViewModel
import com.learning.cropcare.databinding.FragmentProfileBinding


class Profile : Fragment() {
    lateinit var binding:FragmentProfileBinding
    lateinit var viewModel: FireStoreDataBaseViewModel
    lateinit var viewModel1: StorageViewModel
    lateinit var name:String
    lateinit var email:String
    lateinit var mobileNumber:String
    lateinit var userProfileUrl:String
    var fileUri: Uri?=null
    var dialog:Dialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showProgressBar(requireActivity())
        viewModel= ViewModelProvider(this)[FireStoreDataBaseViewModel::class.java]
        viewModel1= ViewModelProvider(this)[StorageViewModel::class.java]
        viewModel.getUserProfileData(requireContext(),this)
        viewModel.observerGetUserProfileData().observe(viewLifecycleOwner, Observer {
            cancelProgressBar()
            Log.d("rk",it.toString())
            if(it["name"]!=null) {
                name =it["name"].toString()
                binding.etName.setText(name)
            }
            else{
                name=""
            }
            if(it["email"]!=null) {
                email = it["email"].toString()
                binding.etEmail.setText(email)
            }
            else{
                email=""
            }
            if(it["mobilenumber"]!=null) {
                mobileNumber = it["mobilenumber"].toString()
                binding.etMobileNo.setText(mobileNumber)
            }
            else{
                mobileNumber=""
            }
            if(it["image"]!=null) {
                userProfileUrl = it["image"].toString()
                Glide
                    .with(this)
                    .load(userProfileUrl)
                    .centerCrop()
                    .placeholder(R.drawable.img)
                    .into(binding.profileImage)
            }
            else{
                userProfileUrl=""
            }
        })
        binding.profileImage.setOnClickListener {
            if(checkPermission())
            {
                imageChooser()
            }
            else
            {
                requestPermission()
            }
        }

        viewModel1.observe().observe(viewLifecycleOwner, Observer {
            task->
            var s=task.toString()
            var map= HashMap<String,String>();
            map["image"]=s
            viewModel.updateUserPersonalDataIntoDB(requireContext(),this,map)
        })
        binding.updateBtn.setOnClickListener {
            showProgressBar(requireActivity())
            val newName=binding.etName.text.toString()
            val newEmail=binding.etEmail.text.toString()
            val newMobileNumber=binding.etMobileNo.text.toString()
            Log.d("rk","$newName $newEmail $newMobileNumber")
            Log.d("rk","$name $email $mobileNumber")
            if(fileUri!=null)
            {
                viewModel1.uploadImage(requireContext(),this,fileUri!!)

            }
            if(newName!=name)
            {
                var map= HashMap<String,String>();
                map["name"]=newName
                viewModel.updateUserPersonalDataIntoDB(requireContext(),this,map)
            }
            if(newEmail!=email)
            {
                var map= HashMap<String,String>();
                map["email"]=newEmail
                viewModel.updateUserPersonalDataIntoDB(requireContext(),this,map)
            }
            if(newMobileNumber!=mobileNumber)
            {
                Log.d("rk","111")
                var map= HashMap<String,String>();
                map["mobilenumber"]=newMobileNumber
                viewModel.updateUserPersonalDataIntoDB(requireContext(),this,map)
            }
        }
        return binding.root
    }

    fun errorFn(message:String)
    {
        cancelProgressBar()
        Toast(requireActivity(),message)
    }
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED)
    }
    // else req permission
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity() ,permissions(), 1)

    }
    val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    val storagePermissions33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
    )
    fun permissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }

    fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Select Picture"), Constants.SELECT_PICTURE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE) {
                val selectedImageUri = data?.data
                fileUri=data?.data
                Log.d("rk",selectedImageUri.toString())
                if (selectedImageUri !=null) {
                    // update the preview image in the layout
                    binding.profileImage.setImageURI(selectedImageUri)
                }
            }
        }
    }

    fun Toast(constext: Context, message:String)
    {
        android.widget.Toast.makeText(constext,message, android.widget.Toast.LENGTH_LONG).show()
    }

    fun showProgressBar(context: Context) {
        dialog = Dialog(context)
        dialog!!.setContentView(R.layout.progress_bar)
        dialog!!.show()
    }

    fun cancelProgressBar()
    {
        if(dialog!=null)
        {
            dialog!!.dismiss()
            dialog=null
        }
    }
}