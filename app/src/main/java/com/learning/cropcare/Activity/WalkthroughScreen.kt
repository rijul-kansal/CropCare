package com.learning.cropcare.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.learning.cropcare.Adapter.ViewPagerAdapter
import com.learning.cropcare.R
import com.learning.cropcare.Utils.Constants
import com.learning.cropcare.databinding.ActivityWalkthroughScreenBinding

class WalkthroughScreen : AppCompatActivity() {
    lateinit var binding:ActivityWalkthroughScreenBinding
    var images = ArrayList<Int>()
    var headings = ArrayList<Int>()
    var descriptions = ArrayList<Int>()
    var languagechoosen:String?=null
    private lateinit var dots: ArrayList<TextView>

    lateinit var viewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityWalkthroughScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        try {
            val sharedPreference =  getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE)
            languagechoosen=sharedPreference.getString("language","english")
            Log.d("rk","languageChosen $languagechoosen")
            if(languagechoosen=="english")
            {
                populateData()
            }
            else
            {
                populateData2()
                binding.skipButton.text = resources.getString(R.string.skip_h)

                binding.frontbtn.text= resources.getString(R.string.Next_h)
            }
            dots = ArrayList()
            binding.frontbtn.setOnClickListener {
                if (getItem(0) < 3) {
                    binding.slideViewPager.setCurrentItem(getItem(1), true)
                } else {
                    val i = Intent(this, IntroActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                    finish()
                }
            }
            binding.skipButton.setOnClickListener {
                val i = Intent(this, IntroActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                finish()
            }
            viewPagerAdapter = ViewPagerAdapter(this,images,headings,descriptions)
            binding.slideViewPager.adapter = viewPagerAdapter
            setUpIndicator(0)
            binding.slideViewPager.addOnPageChangeListener(viewListener)
        } catch (e: Exception) {
            Log.d("rk", e.message.toString())
        }
    }
    private fun setUpIndicator(position: Int) {
        dots.clear()
        binding.indicatorLayout.removeAllViews()
        for (i in 0..3) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35F
            dots[i].setTextColor(
                resources.getColor(
                    R.color.grey,
                    applicationContext.theme
                )
            )
            binding.indicatorLayout.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[position].setTextColor(resources.getColor(R.color.main_color, applicationContext.theme))
        }
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            setUpIndicator(position)

            if (position > 0 && position<3) {
                if(languagechoosen == "hindi")
                    binding.frontbtn.text= resources.getString(R.string.Next_h)
                else
                    binding.frontbtn.text = resources.getString(R.string.Next_e)
            } else if(position==3) {
                if(languagechoosen == "hindi")
                    binding.frontbtn.text= resources.getString(R.string.Get_Started_h)
                else
                    binding.frontbtn.text = resources.getString(R.string.Get_Started_e)
            }
        }
        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getItem(i: Int): Int {
        return binding.slideViewPager.currentItem.plus(i) ?: 0
    }

    fun populateData() {
        images.add( R.drawable.crop_prediction1)
        images.add( R.drawable.crop_yield_prediction)
        images.add( R.drawable.fertilizer)
        images.add( R.drawable.img_4)

        headings.add(R.string.crop_prediction_eh)
        headings.add(R.string.crop_yield_prediction_eh)
        headings.add(R.string.fertilizer_prediction_eh)
        headings.add(R.string.crop_pest_pridiction_eh)

        descriptions.add(R.string.crop_prediction_ed)
        descriptions.add(R.string.crop_yield_prediction_ed)
        descriptions.add(R.string.fertilizer_prediction_ed)
        descriptions.add(R.string.crop_yield_prediction_ed)

    }
    fun populateData2() {
        images.add( R.drawable.crop_prediction1)
        images.add( R.drawable.crop_yield_prediction)
        images.add( R.drawable.fertilizer)
        images.add( R.drawable.img_4)

        headings.add(R.string.crop_prediction_hh)
        headings.add(R.string.crop_yield_prediction_hh)
        headings.add(R.string.fertilizer_prediction_hh)
        headings.add(R.string.crop_pest_prediction_hh)

        descriptions.add(R.string.crop_prediction_hd)
        descriptions.add(R.string.crop_yield_prediction_hd)
        descriptions.add(R.string.fertilizer_prediction_hd)
        descriptions.add(R.string.crop_yield_prediction_hd)

    }
}