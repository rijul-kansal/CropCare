package com.learning.cropcare.Activity

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.applozic.mobicomkit.api.account.register.RegistrationResponse
import com.google.android.material.navigation.NavigationView
import com.learning.cropcare.Fragment.CropPrediction
import com.learning.cropcare.Fragment.CropYieldPrediction
import com.learning.cropcare.Fragment.FertilizerRecommendation
import com.learning.cropcare.Fragment.History
import com.learning.cropcare.Fragment.PestDetection
import com.learning.cropcare.Fragment.Profile
import com.learning.cropcare.R
import com.learning.cropcare.Utils.BaseActivity
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.ViewModel.AuthenticationViewModel
import io.kommunicate.KmConversationBuilder
import io.kommunicate.Kommunicate
import io.kommunicate.callbacks.KMLoginHandler
import io.kommunicate.callbacks.KmCallback
import io.kommunicate.users.KMUser


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO MODEL Deployment
        Kommunicate.init(applicationContext, "219178a5a842a9f33a15be86a9d3daae9")
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toolbar.setTitleTextColor(resources.getColor(R.color.black))
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.black)
        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CropPrediction()).commit()
            navigationView.setCheckedItem(R.id.crop_prediction)
        }
    }
//
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.crop_prediction -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CropPrediction()).commit()
                supportActionBar?.setTitle("Crop Prediction")
            }
            R.id.crop_yield -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CropYieldPrediction()).commit()
                supportActionBar?.setTitle("Crop Yield Prediction")
            }
            R.id.Fertilizer -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FertilizerRecommendation()).commit()
                supportActionBar?.setTitle("Fertilizer Recommendation")
            }
            R.id.pest_prediction -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PestDetection()).commit()
                supportActionBar?.setTitle("Pest Detection")
            }
            R.id.profile->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, Profile()).commit()
                supportActionBar?.setTitle("Profile")
            }
            R.id.logout->{
                AuthenticationViewModel().SignOut()
                startActivity(Intent(this,IntroActivity::class.java))
                finish()
            }
            R.id.language->{
                var intent =Intent(this,LanguageActivity::class.java)
                intent.putExtra(Constants.START_LANGUAGE_CHOSEN_OR_NOT,"no")
                startActivity(intent)
            }
            R.id.history->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, History()).commit()
                supportActionBar?.setTitle("History")
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (isOnlyOneActivityInStack()) {
            if (doubleBackToExitPressedOnce) {
                showBackBtnDialog()
                return
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CropPrediction()).commit()
            supportActionBar?.setTitle("Crop Prediction")
            this.doubleBackToExitPressedOnce = true
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)

        } else {

            Log.d("rk", "More than one activity in the stack")
            super.onBackPressed()
        }
    }

    private fun isOnlyOneActivityInStack(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.appTasks
        for (task in tasks) {
            val taskInfo = task.taskInfo
            if (taskInfo.numActivities == 1) {
                return true
            }
        }
        return false
    }

    fun showBackBtnDialog()
    {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Want to exit the App")
            .setPositiveButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setNegativeButton("Exit") { dialog, which ->
                finish()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.side_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.chatBot) {
          chatBotStart()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun chatBotStart()
    {
        showProgressBar(this)
        val user =  KMUser()
        user.userId = "1234"
        user.displayName = "1234"; // Pass the display name of the user
        user.setImageLink("https://firebasestorage.googleapis.com/v0/b/crop-care-9161c.appspot.com/o/UserImages%2F1000119345?alt=media&token=fd4ec75a-7f89-45d5-9a14-3419e2e35b69"); // Pass the image URL for the user's display image
        Kommunicate.login(this, user, object : KMLoginHandler {
            override fun onSuccess(
                registrationResponse: RegistrationResponse?,
                context: Context?
            ) {
                // You can perform operations such as opening the conversation, creating a new conversation or update user details on success
                KmConversationBuilder(this@MainActivity)
                    .setKmUser(user)
                    .launchConversation(object : KmCallback {
                        override fun onSuccess(message: Any) {
                            cancelProgressBar()
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
    }
}