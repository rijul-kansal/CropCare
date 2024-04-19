package com.learning.cropcare.Activity

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.learning.cropcare.Fragment.CropPrediction
import com.learning.cropcare.Fragment.CropYieldPrediction
import com.learning.cropcare.Fragment.FertilizerRecommendation
import com.learning.cropcare.Fragment.PestDetection
import com.learning.cropcare.Fragment.Profile
import com.learning.cropcare.R
import com.learning.cropcare.ViewModel.AuthenticationViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //  TODO BOT
        // TODO MODEL Deployment
        // TODO PDF of History
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
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (isOnlyOneActivityInStack()) {

            showBackBtnDialog()
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
}