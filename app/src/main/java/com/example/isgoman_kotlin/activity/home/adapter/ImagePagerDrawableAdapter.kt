package com.example.isgoman_kotlin.activity.home.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class ImagePagerDrawableAdapter(mImagesArrayListUrlBg: ArrayList<String>, context: Context) :
    PagerAdapter() {
    var mContext: Context
    var mImagesArrayListUrlBg: ArrayList<String>
    private var mInflaters: LayoutInflater? = null
    var imageView: ImageView? = null
    override fun getCount(): Int {
        return mImagesArrayListUrlBg.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var pageview: View? = null
        mInflaters = LayoutInflater.from(mContext)
        pageview = mInflaters!!.inflate(R.layout.layout_image_pager_adapter, null)
        imageView = pageview!!.findViewById<View>(R.id.adImg) as ImageView

//        imageView.setBackgroundResource(mImagesArrayListBg.get(position));
        if (mImagesArrayListUrlBg[position] != "") {
            //  loadImage(mContext, mImagesArrayListUrlBg.get(position).replaceAll(" ", "%20"), imageView);   //OLD COMMENTED DATA NTN
//            imageView.setAdjustViewBounds(true);
            Picasso.with(mContext).load(AppUtilityMethods.replace(mImagesArrayListUrlBg[position]))
                .fit().into(imageView, object : Callback {
                    override fun onSuccess() {
                        System.out.println(
                            "Image Succes:" + AppUtilityMethods.replace(
                                mImagesArrayListUrlBg[position]
                            )
                        )

                        // imageView.setAdjustViewBounds(true);
                    }

                    override fun onError() {
//            Glide.with(mContext).load(AppUtils.replace(mImagesArrayListUrlBg.get(position).toString())).centerCrop().into(imageView);
                        //  imageView.setAdjustViewBounds(true);
                    }
                })
        }
        (container as ViewPager).addView(pageview, 0)
        return pageview
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    private var mTarget: Target? = null

    init {
        this.mImagesArrayListUrlBg = ArrayList()
        mContext = context
        this.mImagesArrayListUrlBg = mImagesArrayListUrlBg
    }

    fun loadImage(context: Context?, url: String?, img: ImageView) {
        mTarget = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                //Do something
                img.setImageBitmap(bitmap)
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {}
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        }
        Picasso.with(context)
            .load(url).placeholder(R.drawable.homebgschool)
            .into(mTarget)
    }
}