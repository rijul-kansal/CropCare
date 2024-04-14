package com.learning.cropcare.Activity

import android.os.Bundle
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
import com.learning.cropcare.R

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO Language
        // TODO GPS
        // TODO UI Update
        // TODO MODEL Deployment
        // TODO PDF of Hisotory
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
//            R.id.nav_about -> supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, AboutFragment()).commit()
//            R.id.nav_logout -> Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}