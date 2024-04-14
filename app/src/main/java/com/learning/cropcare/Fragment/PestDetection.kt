package com.learning.cropcare.Fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learning.cropcare.Activity.LanguageActivity
import com.learning.cropcare.Model.PestDetectionInputModel
import com.learning.cropcare.R
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.APIViewModel
import com.learning.cropcare.ViewModel.StorageViewModel
import com.learning.cropcare.databinding.FragmentPestDetectionBinding
import java.io.File
import java.util.UUID


class PestDetection : Fragment() {

    var dialog: Dialog?=null
    val CAMERA_PERMISSION_CODE = 1
    lateinit var binding: FragmentPestDetectionBinding
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var imageUri: Uri
    lateinit var viewModel: StorageViewModel
    lateinit var viewModel1: APIViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentPestDetectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            viewModel= ViewModelProvider(this)[StorageViewModel::class.java]
            viewModel1 = ViewModelProvider(this)[APIViewModel::class.java]
            imageUri = createUri();
            registerPictureLauncher()
            binding.SignIInBtn.setOnClickListener {
                showOptionsDialog()
            }
//            viewModel1.observe_pest().observe(requireActivity(), Observer {res->
//                cancelProgressbar()
//                if(res.isSuccessful)
//                {
//                    binding.value.text= "The Predected pest is   ${res.body()!!.pest.toString()}"
//                }
//                else
//                {
//                }
//            })
//            viewModel.observe().observe(requireActivity(), Observer {res->
//                if(res!=null)
//                {
//                    Log.d("rk","final final ${res.toString()}")
//                    viewModel1.pest(requireContext(),PestDetectionInputModel(res!!),this)
//                }
//            })
        }catch (e:Exception)
        {
            Log.d("rk",e.message.toString())
        }

        return binding.root
    }
    private fun createUri(): Uri {
        val imageFile = File(requireContext().filesDir, "${UUID.randomUUID()}camera_photo.jpg")
        return FileProvider.getUriForFile(
            requireContext(), "com.learning.cropcare.Activity.fileProvider", imageFile
        )
    }

    private fun registerPictureLauncher() {
        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success: Boolean ->
            try {
                if (success) {
                    binding.enjoy.setImageURI(null)
                    binding.enjoy.setImageURI(imageUri)
                    showProgressbar()
                    viewModel.uploadImage(requireContext(),this,imageUri)
//                    Handler().postDelayed({
//                        viewModel1.pest(requireContext(), PestDetectionInputModel(imageUri),this)
//                    },3000)

                    Log.d("rk", imageUri.toString())
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted, launch the camera
            takePictureLauncher.launch(imageUri)
        } else {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch the camera
                takePictureLauncher.launch(imageUri)
            } else {
                // Permission denied
                android.widget.Toast.makeText(
                    requireContext(),
                    "Camera permission denied, please allow permission to take photos",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showOptionsDialog() {
        val options = arrayOf("Camera", "Gallery")
        val icons = arrayOf(
            R.drawable.baseline_camera_24,
            R.drawable.baseline_add_a_photo_24
        )

        val adapter = OptionsAdapter(requireContext(), options, icons)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose an Option")
            .setAdapter(adapter) { dialog, which ->
                // Handle option selection
                when (which) {
                    0 -> {
                        // Camera option clicked
                        openCamera()
                    }

                    1 -> {
                        // Gallery option clicked
                        openGallery()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun openCamera() {
        checkCameraPermissionAndOpenCamera()
        android.widget.Toast.makeText(requireContext(), "Opening Camera", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        if(checkPermission())
        {
            imageChooser()
        }
        else
        {
            requestPermission()
        }
        android.widget.Toast.makeText(requireContext(), "Opening Gallery", android.widget.Toast.LENGTH_SHORT).show()
    }

    private class OptionsAdapter(
        private val context: Context,
        private val options: Array<String>,
        private val icons: Array<Int>
    ) : ArrayAdapter<String>(context, R.layout.list_item_option, options) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.list_item_option, parent, false)

            val iconImageView: ImageView = view.findViewById(R.id.icon)
            val optionTextView: TextView = view.findViewById(R.id.text)

            iconImageView.setImageResource(icons[position])
            optionTextView.text = options[position]
            return view
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED)
    }

    // else req permission
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), permissions(), 1)

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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_PICTURE) {
                val selectedImageUri = data?.data
                imageUri = data?.data!!
                showProgressbar()
                try {
                    viewModel.uploadImage(requireContext(),this,imageUri)
                     Log.d("rk","final url ${imageUri}")
//                    Handler().postDelayed({
//                        viewModel1.pest(requireContext(), PestDetectionInputModel(imageUri),this)
//                    },3000)
                }catch (e:Exception)
                {
                    Log.d("rk",e.message.toString())
                }
                Log.d("rk", selectedImageUri.toString())
                if (selectedImageUri != null) {
                    // update the preview image in the layout
                    binding.enjoy.setImageURI(selectedImageUri)
                }
            }
        }
    }
    fun Toast( message:String)
    {
        android.widget.Toast.makeText(requireContext(),message, android.widget.Toast.LENGTH_LONG).show()
    }

    fun showProgressbar()
    {
        dialog= Dialog(requireContext())
        dialog!!.setContentView(R.layout.progress_bar)
        dialog!!.show()

    }
    fun cancelProgressbar()
    {
        if(dialog!=null)
        {
            dialog!!.cancel()
            dialog=null
        }
    }
    fun errorFn(message:String)
    {
        cancelProgressbar()
        Toast(message)
    }
}