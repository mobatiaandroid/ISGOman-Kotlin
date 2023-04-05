package com.example.isgoman_kotlin.appcontroller

import android.text.TextUtils
import androidx.multidex.MultiDexApplication
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.isgoman_kotlin.activity.home.adapter.HomeViewPagerAdapter
import com.example.isgoman_kotlin.activity.home.model.BannerList
import com.example.isgoman_kotlin.activity.home.model.HomeTabModel
import com.viewpagerindicator.CirclePageIndicator


class AppController : MultiDexApplication() {
    private var mRequestQueue: RequestQueue? = null

    // public static ArrayList<StudentModels> studentArrayList=new ArrayList<>();
    override fun onCreate() {
        super.onCreate()
        AppController.Companion.mInstance = this
        AppController.Companion.mTabArrangeModelArrayList = ArrayList<HomeTabModel>()
        AppController.Companion.mTabTitleModelArrayList = ArrayList<HomeTabModel>()
    }

    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext())
            }
            return mRequestQueue
        }

    fun <T> addToRequestQueue(req: Request<T>, tag: String?) {
        // set the default tag if tag is empty
        req.setTag(if (TextUtils.isEmpty(tag)) AppController.Companion.TAG else tag)
        requestQueue!!.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.setTag(AppController.Companion.TAG)
        requestQueue!!.add(req)
    }

    fun cancelPendingRequests(tag: Any?) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        val TAG = AppController::class.java
            .simpleName
        var mapId = ""
        private var mInstance: AppController? = null
        var mTabArrangeModelArrayList: ArrayList<HomeTabModel>? = null
        var mTabTitleModelArrayList: ArrayList<HomeTabModel>? = null
        var viewPager: ViewPager? = null
        var homeViewPagerAdapter: HomeViewPagerAdapter? = null
        var mImageArrayList: ArrayList<String>? = null
        var mIndicator: CirclePageIndicator? = null

        @get:Synchronized
        val instance: AppController
            get() = AppController.Companion.mInstance!!
    }
}