package com.learning.cropcare.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.learning.cropcare.R


class ViewPagerAdapter(
    var context: Context,
    var images: ArrayList<Int>,
    var headings: ArrayList<Int>,
    var descriptions: ArrayList<Int>
) : PagerAdapter() {

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view: View = layoutInflater.inflate(R.layout.slider_layout, container, false)
        val slideTitleImage = view.findViewById<ImageView>(R.id.animation_view)
        val slideHeading = view.findViewById<TextView>(R.id.texttitle)
        val slideDescription = view.findViewById<TextView>(R.id.textdeccription)
        Glide
            .with(context)
            .load(images[position])
            .centerCrop()
            .placeholder(R.drawable.crop_prediction1)
            .into(slideTitleImage);
        // Set heading and description
        slideHeading.setText(headings[position])
        slideDescription.setText(descriptions[position])

        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}