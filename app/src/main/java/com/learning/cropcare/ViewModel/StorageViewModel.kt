package com.learning.cropcare.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.learning.agrovision.Model.RainfallOutputModel
import com.learning.cropcare.Fragment.PestDetection
import com.learning.cropcare.Model.PestDetectionInputModel
import com.learning.cropcare.Model.PestPredictionOutputModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class StorageViewModel : ViewModel() {
    private  val storage = Firebase.storage
    private  var storageRef = storage.reference
    private var downloadUri: MutableLiveData<Uri> = MutableLiveData()


    fun uploadImage(context: Context, fragment:PestDetection, fileUri : Uri) {
        viewModelScope.launch(Dispatchers.IO)
        {
            val ref = storageRef.child("UserImages/${fileUri.lastPathSegment}")
            val uploadTask = ref.putFile(fileUri)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
//
                Log.d("rk","download ${ref.downloadUrl}")
                ref.downloadUrl

            }.addOnCompleteListener { task ->
                fragment.cancelProgressbar()
                if (task.isSuccessful) {
                    downloadUri.value = task.result
                    Log.d("rk","final "+downloadUri.toString())
                    val uri: Uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/crop-care-b1ede.appspot.com/o/UserImages%2F1000115795?alt=media&token=1c585f57-336d-443f-bfe3-b2924570937e")

                    APIViewModel().pest(context, PestDetectionInputModel(uri),fragment)
                    val valuee=downloadUri.toString()
                    val map= HashMap<String,String>()
                    map["image"] = valuee
                } else {

                }
            }
        }
    }
    fun observe(): LiveData<Uri> = downloadUri
}