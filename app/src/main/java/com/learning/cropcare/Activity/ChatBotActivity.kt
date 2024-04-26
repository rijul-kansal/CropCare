package com.learning.cropcare.Activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.applozic.mobicomkit.api.account.register.RegistrationResponse
import com.learning.cropcare.R
import io.kommunicate.KmChatBuilder
import io.kommunicate.KmConversationBuilder
import io.kommunicate.Kommunicate
import io.kommunicate.callbacks.KMLoginHandler
import io.kommunicate.callbacks.KmCallback
import io.kommunicate.users.KMUser


class ChatBotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        try {
            Kommunicate.init(applicationContext, "219178a5a842a9f33a15be86a9d3daae9")
            val user =  KMUser()
            user.userId = "rkk"
            user.displayName = "rijul"; // Pass the display name of the user
            user.setImageLink("https://firebasestorage.googleapis.com/v0/b/crop-care-9161c.appspot.com/o/UserImages%2F1000119345?alt=media&token=fd4ec75a-7f89-45d5-9a14-3419e2e35b69"); // Pass the image URL for the user's display image
            Kommunicate.login(this, user, object : KMLoginHandler {
                override fun onSuccess(
                    registrationResponse: RegistrationResponse?,
                    context: Context?
                ) {
                    // You can perform operations such as opening the conversation, creating a new conversation or update user details on success
                    KmConversationBuilder(this@ChatBotActivity)
                        .setKmUser(user)
                        .launchConversation(object : KmCallback {
                            override fun onSuccess(message: Any) {
                                Log.d("rk", "Success : $message")
                            }

                            override fun onFailure(error: Any) {
                                Log.d("rk", "Failure : $error")
                            }
                        })
                }

                override fun onFailure(
                    registrationResponse: RegistrationResponse,
                    exception: java.lang.Exception
                ) {
                    // You can perform actions such as repeating the login call or throw an error message on failure
                }
            })
        }catch (e:Exception)
        {
            Log.d("rk",e.message.toString())
        }
    }
}