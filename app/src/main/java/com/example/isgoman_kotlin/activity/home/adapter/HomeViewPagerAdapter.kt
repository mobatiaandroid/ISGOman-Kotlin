package com.example.isgoman_kotlin.activity.home.adapter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.aboutus.AboutusRecyclerActivity
import com.example.isgoman_kotlin.activity.circulars.CircularActivity
import com.example.isgoman_kotlin.activity.home.model.BannerList
import com.example.isgoman_kotlin.activity.home.model.HomeTabModel
import com.example.isgoman_kotlin.activity.login.HomeActivity
import com.example.isgoman_kotlin.appcontroller.AppController
import com.example.isgoman_kotlin.constants.JsonTagConstants
import com.example.isgoman_kotlin.constants.StausCodes
import com.example.isgoman_kotlin.constants.TabIDConstants
import com.example.isgoman_kotlin.constants.TabIDConstants.*
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import java.util.*
import kotlin.collections.ArrayList

class HomeViewPagerAdapter : PagerAdapter, TabIDConstants, JsonTagConstants,
    StausCodes {
    var mHomeTabModelsArrangement: ArrayList<HomeTabModel>? = null
    var mHomeTabModelsTitle: ArrayList<HomeTabModel>? = null
    var relativeHeader: RelativeLayout? = null
    var mContext: Context
    lateinit var mListImgArray: IntArray
    private var mInflaters: LayoutInflater? = null
    var mPagerCount = 0
    var viewPager: ViewPager? = null
    var row_count = 1
    var homeGridAdapter: HomeGridAdapter? = null
    var mGridView: GridView? = null
    var TAB_ID = 0
    var intent: Intent? = null
    var screensize = 0.0
    var llProfile: LinearLayout? = null
    var bannerImagePager: ViewPager? = null
    var homeBannerUrlImageArray: ArrayList<String>? = null
    var currentPage = 0
    var mActivity: Activity? = null
    var permissionsRequiredLocation = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE
    )
    private var locationPermissionStatus: SharedPreferences? = null
    private var locationToSettings = false
    var permissionsRequiredPhone = arrayOf(Manifest.permission.CALL_PHONE)

    constructor(
        mContext: Context,
        mHomeTabModelsArrangement: ArrayList<HomeTabModel>?,
        mHomeTabModelsTitle: ArrayList<HomeTabModel>?,
        mListImgArray: IntArray
    ) {
        this.mContext = mContext
        this.mHomeTabModelsArrangement = mHomeTabModelsArrangement
        this.mHomeTabModelsTitle = mHomeTabModelsTitle
        this.mListImgArray = mListImgArray
    }

    constructor(mContext: Context, pagerCount: Int, mViewPager: ViewPager?, screensize: Double) {
        this.mContext = mContext
        mPagerCount = pagerCount
        viewPager = mViewPager
        this.screensize = screensize
    }

    constructor(
        mContext: Context,
        mActivity: Activity?,
        pagerCount: Int,
        mViewPager: ViewPager?,
        screensize: Double,
        mImageArray: ArrayList<String>?
    ) {
        this.mContext = mContext
        this.mActivity = mActivity
        mPagerCount = pagerCount
        viewPager = mViewPager
        this.screensize = screensize
        homeBannerUrlImageArray = ArrayList()
        homeBannerUrlImageArray = mImageArray
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mPagerCount
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun getItemPosition(`object`: Any): Int {
        // TODO Auto-generated method stub
        return POSITION_NONE
    }

    override fun instantiateItem(container: View, position: Int): Any {
        var pageview: View? = null
        println("mPagerCount---$mPagerCount")
        mInflaters = LayoutInflater.from(mContext)
        if (position == 0) {
            pageview = mInflaters!!.inflate(R.layout.adapter_viewpager_home_first, null)
            bannerImagePager = pageview!!.findViewById<View>(R.id.bannerImagePager) as ViewPager
            llProfile = pageview.findViewById<View>(R.id.llProfile) as LinearLayout
            userName = pageview.findViewById<View>(R.id.userName) as TextView
            llProfile!!.background.alpha = 150
            userName!!.setText(
                AppPreferenceManager().getStudentName(mContext) + " (" + AppPreferenceManager().getSchoolSelection(
                    mContext
                ) + ")"
            )
            //            homeBannerUrlImageArray = new ArrayList<>();
//            homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/algubra/media/appimages/413607.jpg");
//            homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/algubra/media/appimages/2.jpg");
//            homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/algubra/media/appimages/1.jpg");
//            if(AppUtilityMethods.isNetworkConnected(mContext)) {
//                callBannerImageAPI();
//            }else{
//                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//            }
            llProfile!!.setOnClickListener {
                /*mContext.startActivity(
                    Intent(
                        mContext,
                       StudentProfileListActivity::class.java
                    )
                )*/
            }
            bannerImagePager!!.adapter =
                ImagePagerDrawableAdapter(homeBannerUrlImageArray!!, mContext)
            if (homeBannerUrlImageArray != null) {
                val handler = Handler()
                val Update = Runnable {
                    if (currentPage == homeBannerUrlImageArray!!.size) {
                        currentPage = 0
                        bannerImagePager!!.setCurrentItem(
                            currentPage,
                            false
                        )
                    } else {
                        bannerImagePager!!
                            .setCurrentItem(currentPage++, true)
                    }
                }
                val swipeTimer = Timer()
                swipeTimer.schedule(object : TimerTask() {
                    override fun run() {
                        handler.post(Update)
                    }
                }, 100, 6000)
            }
        } else {
            if (position == 1) {
                pageview = mInflaters!!.inflate(R.layout.adapter_home_view_pager, null)
            }
            if (position == 2) {
                pageview = mInflaters!!.inflate(R.layout.adapter_home_view_pager, null)
            }
        }
        mGridView = pageview!!.findViewById<View>(R.id.viewGrid) as GridView
        mGridView!!.numColumns = 3
        if (position == 0) {
//           mBannerImage.setVisibility(View.VISIBLE);
//            mBannerImage=(ImageView)pageview.findViewById(R.id.man);
            println("row_count 0 ---$row_count")
            mGridView!!.setPadding(0, 10, 0, 0)
            row_count = 6
            homeGridAdapter = HomeGridAdapter(mContext, row_count, position)
            //            mGridAdapter = new HomeGridViewAdapter(mContext, mTabTitleArrayList, row_count);
            mGridView!!.gravity = Gravity.CENTER
            mGridView!!.adapter = homeGridAdapter
            homeGridAdapter!!.notifyDataSetChanged()
        } else if (position == 1) {
            println("row_count 1 ---$row_count")
            if (screensize > 4 && screensize < 5) {
                mGridView!!.verticalSpacing = 27
            }
            mGridView!!.numColumns = 3
            mGridView!!.setPadding(0, 30, 0, 0)
            row_count = 12
            homeGridAdapter = HomeGridAdapter(mContext, row_count, position)
            mGridView!!.adapter = homeGridAdapter
            homeGridAdapter!!.notifyDataSetChanged()
        } else if (position == 2) {
            println("row_count 2 ---$row_count")
            if (screensize > 4 && screensize < 5) {
                mGridView!!.verticalSpacing = 27
            }
            mGridView!!.numColumns = 3
            mGridView!!.setPadding(0, 30, 0, 0)
            row_count = 12
            homeGridAdapter = HomeGridAdapter(mContext, row_count, position)
            //            mGridAdapter = new HomeGridViewAdapter(mContext, mTabTitleArrayList, row_count);
            mGridView!!.adapter = homeGridAdapter
            homeGridAdapter!!.notifyDataSetChanged()
        }
        mGridView!!.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                try {
                    if (position == 0) {
                        TAB_ID = AppController.mTabArrangeModelArrayList!!.get(i).tab_id!!.toInt()
                        IntentGridClick(TAB_ID)
                    } else if (position == 1) {
                        TAB_ID =
                            AppController.mTabArrangeModelArrayList!!.get(i + 6).tab_id!!.toInt()
                        IntentGridClick(TAB_ID)
                    } else if (position == 2) {
                        TAB_ID =
                            AppController.mTabArrangeModelArrayList!!.get(i + 18).tab_id!!.toInt()
                        IntentGridClick(TAB_ID)
                    }
                } catch (e: Exception) {
                    Log.e("Numberformat: ", e.message!!)
                }
            }
        (container as ViewPager).addView(pageview, 0)
        return pageview
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    fun IntentGridClick(tab_id: Int) {
        when (tab_id) {
            NEWS_LETTER_TAB_ID -> {
                /*intent = Intent(mContext, NewsLetterActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            ABOUT_US_TAB_ID -> {
                intent = Intent(mContext, AboutusRecyclerActivity::class.java)
                HomeActivity().stopDisconnectTimer()
                mContext.startActivity(intent)
            }
            CONTACT_US_TAB_ID -> {
                locationPermissionStatus =
                    mContext.getSharedPreferences("locationPermissionStatus", Context.MODE_PRIVATE)
                if (Build.VERSION.SDK_INT < 23) {
                    //Do not need to check the permission
                    /*intent = Intent(mContext, ContactUsActivity::class.java)
                    HomeActivity.stopDisconnectTimer()
                    mContext.startActivity(intent)*/
                } else {
                    if (ActivityCompat.checkSelfPermission(
                            mActivity!!,
                            permissionsRequiredLocation[0]
                        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                            mActivity!!,
                            permissionsRequiredLocation[1]
                        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                            mActivity!!,
                            permissionsRequiredLocation[2]
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                mActivity!!,
                                permissionsRequiredLocation[0]
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                mActivity!!,
                                permissionsRequiredLocation[1]
                            )
                            || ActivityCompat.shouldShowRequestPermissionRationale(
                                mActivity!!,
                                permissionsRequiredLocation[2]
                            )
                        ) {
                            //Show Information about why you need the permission
                            val builder = AlertDialog.Builder(
                                mActivity!!
                            )
                            builder.setTitle("Need Location and Phone Permission")
                            builder.setMessage("This module needs location and phone permissions.")
                            builder.setPositiveButton(
                                "Grant"
                            ) { dialog, which ->
                                dialog.cancel()
                                ActivityCompat.requestPermissions(
                                    mActivity!!,
                                    permissionsRequiredLocation,
                                    PERMISSION_CALLBACK_CONSTANT_LOCATION
                                )
                            }
                            builder.setNegativeButton(
                                "Cancel"
                            ) { dialog, which -> dialog.cancel() }
                            builder.show()
                        } else if (locationPermissionStatus!!.getBoolean(
                                permissionsRequiredLocation[0],
                                false
                            )
                        ) {
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            val builder = AlertDialog.Builder(
                                mActivity!!
                            )
                            builder.setTitle("Need Location and Phone Permission")
                            builder.setMessage("This module needs location and phone permissions.")
                            builder.setPositiveButton(
                                "Grant"
                            ) { dialog, which ->
                                dialog.cancel()
                                locationToSettings = true
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts(
                                    "package",
                                    mActivity!!.packageName,
                                    null
                                )
                                intent.data = uri
                                mActivity!!.startActivityForResult(
                                    intent,
                                    REQUEST_PERMISSION_LOCATION
                                )
                                Toast.makeText(
                                    mContext,
                                    "Go to settings and grant access to location and phone.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            builder.setNegativeButton(
                                "Cancel"
                            ) { dialog, which ->
                                dialog.cancel()
                                locationToSettings = false
                            }
                            builder.show()
                        } else if (locationPermissionStatus!!.getBoolean(
                                permissionsRequiredLocation[1],
                                false
                            )
                        ) {
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            val builder = AlertDialog.Builder(
                                mActivity!!
                            )
                            builder.setTitle("Need Location and Phone Permission")
                            builder.setMessage("This module needs location and phone permissions.")
                            builder.setPositiveButton(
                                "Grant"
                            ) { dialog, which ->
                                dialog.cancel()
                                locationToSettings = true
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts(
                                    "package",
                                    mActivity!!.packageName,
                                    null
                                )
                                intent.data = uri
                                mActivity!!.startActivityForResult(
                                    intent,
                                    REQUEST_PERMISSION_LOCATION
                                )
                                Toast.makeText(
                                    mContext,
                                    "Go to settings and grant access to location and phone.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            builder.setNegativeButton(
                                "Cancel"
                            ) { dialog, which ->
                                dialog.cancel()
                                locationToSettings = false
                            }
                            builder.show()
                        } else if (locationPermissionStatus!!.getBoolean(
                                permissionsRequiredLocation[2],
                                false
                            )
                        ) {
                            //Previously Permission Request was cancelled with 'Dont Ask Again',
                            // Redirect to Settings after showing Information about why you need the permission
                            val builder = AlertDialog.Builder(
                                mActivity!!
                            )
                            builder.setTitle("Need Location and Phone Permission")
                            builder.setMessage("This module needs location and phone permissions.")
                            builder.setPositiveButton(
                                "Grant"
                            ) { dialog, which ->
                                dialog.cancel()
                                locationToSettings = true
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts(
                                    "package",
                                    mActivity!!.packageName,
                                    null
                                )
                                intent.data = uri
                                mActivity!!.startActivityForResult(
                                    intent,
                                    REQUEST_PERMISSION_LOCATION
                                )
                                Toast.makeText(
                                    mContext,
                                    "Go to settings and grant access to location and phone.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            builder.setNegativeButton(
                                "Cancel"
                            ) { dialog, which ->
                                dialog.cancel()
                                locationToSettings = false
                            }
                            builder.show()
                        } else {

                            //just request the permission
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                            mActivity!!.requestPermissions(
                                permissionsRequiredLocation,
                                PERMISSION_CALLBACK_CONSTANT_LOCATION
                            )
                        }
                        val editor = locationPermissionStatus!!.edit()
                        editor.putBoolean(permissionsRequiredLocation[0], true)
                        editor.commit()
                    } else {
                       /* intent = Intent(mContext, ContactUsActivity::class.java)
                        HomeActivity.stopDisconnectTimer()
                        mContext.startActivity(intent)*/
                    }
                }
            }
            COURSES_TAB_ID -> {
                /*intent = Intent(mContext, VideosListActivity::class.java)
                //                intent = new Intent(mContext, CoursesListActivity.class);
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            STAFF_DIRECTORY_TAB_ID -> {
                /*intent = Intent(mContext, StaffDirectoryActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            SCHEDULES_TAB_ID -> {
                /*intent = Intent(mContext, SchedulesActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            CLUB_TAB_ID -> {
                /*intent = Intent(mContext, ClubListActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            TIMETABLE_TAB_ID -> {
                /*intent = Intent(mContext, TimeTableHomeActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            QUOTESSTORIES_TAB_ID -> {
                /*intent = Intent(mContext, QuotesandStoriesActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            EVENTS_TAB_ID -> {
               /* intent = Intent(mContext, EventListActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            LEAVES_TAB_ID -> {
                /*intent = Intent(mContext, LeavesActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            LOSTANDFOUND_TAB_ID -> {
                /*intent = Intent(mContext, StudentLeadersListActivity::class.java)
                //                intent = new Intent(mContext, LostandFoundListActivity.class);
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            STUDENT_AWARDS_TAB_ID -> {
                /*intent = Intent(mContext, StudentAwardsListActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            GALLERY_TAB_ID -> {
               /* intent = Intent(mContext, GalleryActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            USERPROFILE_TAB_ID -> {
                /*intent = Intent(mContext, UserProfileActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            STUDENTROFILE_TAB_ID -> {
               /* intent = Intent(mContext, StudentProfileListActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            CALENDAR_TAB_ID -> {
                /*intent = Intent(mContext, CalendarActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            NOTIFICATION_TAB_ID -> {
                /*intent = Intent(mContext, NotificationListActivity::class.java)
                HomeActivity.stopDisconnectTimer()

//                HomeGridAdapter.notifyIconBadge.setVisibility(View.INVISIBLE);
                mContext.startActivity(intent)*/
            }
            SETTINGS_TAB_ID -> {
                /*intent = Intent(mContext, SettingsActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            STUDYANDCURRICULAMTAB_ID -> {
               /* intent = Intent(mContext, CurriculamActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            NEWS_TAB_ID -> {
               /* intent = Intent(mContext, NewsActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                intent!!.putExtra("page_type", "news")
                intent!!.putExtra("tab_type", "News")
                mContext.startActivity(intent)*/
            }
            CIRCULAR_TAB_ID -> {
                intent = Intent(mContext, CircularActivity::class.java)
                HomeActivity().stopDisconnectTimer()
                mContext.startActivity(intent)
            }
            SPECIAL_MESSAGE_TAB_ID -> {
               /* intent = Intent(mContext, SpecialMessageActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            HOMEWORK_TAB_ID -> {
               /* intent = Intent(mContext, HomeWorkSubjectActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
            WORKSHEET_TAB_ID -> {
                /*intent = Intent(mContext, WorkSheetSubjectListActivity::class.java)
                HomeActivity.stopDisconnectTimer()
                mContext.startActivity(intent)*/
            }
        }
    }

    /*private fun callBannerImageAPI() {
        val volleyWrapper = VolleyAPIManager(URL_GET_HOMELIST_IMAGE)
        val name = arrayOf("access_token")
        val value = arrayOf<String>(AppPreferenceManager.getAccessToken(mContext))
        volleyWrapper.getResponsePOST(mContext, 11, name, value, object : ResponseListener() {
            fun responseSuccess(successResponse: String) {
                println("The response is$successResponse")
                try {
                    homeBannerUrlImageArray = ArrayList()
                    val obj = JSONObject(successResponse)
                    val response_code = obj.getString(JTAG_RESPONSECODE)
                    if (response_code.equals(RESPONSE_SUCCESS, ignoreCase = true)) {
                        val secobj = obj.getJSONObject(JTAG_RESPONSE)
                        val status_code = secobj.getString(JTAG_STATUSCODE)
                        if (status_code.equals(STATUSCODE_SUCCESS, ignoreCase = true)) {
                            val staffArray = secobj.getJSONArray("sliders")
                            if (staffArray.length() > 0) {
                                for (i in 0 until staffArray.length()) {
                                    val mImageObject = staffArray.optJSONObject(i)
                                    homeBannerUrlImageArray!!.add(mImageObject.optString("image"))
                                }
                                bannerImagePager!!.adapter =
                                    ImagePagerDrawableAdapter(homeBannerUrlImageArray, mContext)
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss(
                                    mContext as Activity,
                                    mContext.getString(R.string.alert_heading),
                                    mContext.getString(R.string.no_datafound),
                                    R.drawable.infoicon,
                                    R.drawable.roundblue
                                )
                            }
                        } else {
                            AppUtilityMethods.showDialogAlertDismiss(
                                mContext as Activity,
                                mContext.getString(R.string.error_heading),
                                mContext.getString(R.string.common_error),
                                R.drawable.infoicon,
                                R.drawable.roundblue
                            )
                        }
                    } else if (response_code.equals(
                            RESPONSE_INTERNALSERVER_ERROR,
                            ignoreCase = true
                        )
                    ) {
                        AppUtilityMethods.showDialogAlertDismiss(
                            mContext as Activity,
                            mContext.getString(R.string.error_heading),
                            mContext.getString(R.string.internal_server_error),
                            R.drawable.infoicon,
                            R.drawable.roundblue
                        )
                    } else if (response_code.equals(
                            RESPONSE_INVALID_ACCESSTOKEN,
                            ignoreCase = true
                        ) || response_code.equals(
                            RESPONSE_ACCESSTOKEN_MISSING,
                            ignoreCase = true
                        ) || response_code.equals(RESPONSE_ACCESSTOKEN_EXPIRED, ignoreCase = true)
                    ) {
                        AppUtilityMethods.getToken(mContext, object : GetTokenSuccess() {
                            fun tokenrenewed() {}
                        })
                        callBannerImageAPI()
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss(
                            mContext as Activity,
                            "Alert",
                            mContext.getString(R.string.common_error),
                            R.drawable.exclamationicon,
                            R.drawable.roundblue
                        )
                    }
                } catch (ex: Exception) {
                    println("The Exception in edit profile is$ex")
                }
            }

            fun responseFailure(failureResponse: String?) {
                *//*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                        , getResources().getString(R.string.ok));
                dialog.show();*//*
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    "Alert",
                    mContext.getString(R.string.common_error),
                    R.drawable.exclamationicon,
                    R.drawable.roundblue
                )
            }
        })
    }*/

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT_LOCATION) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }
            if (allgranted) {
                proceedAfterPermission()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                val builder = AlertDialog.Builder(
                    mActivity!!
                )
                builder.setTitle("Need Location and Phone Permission")
                builder.setMessage("This module needs location and phone permissions.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel()
                    locationToSettings = false
                    ActivityCompat.requestPermissions(
                        mActivity!!,
                        permissionsRequiredLocation,
                        PERMISSION_CALLBACK_CONSTANT_LOCATION
                    )
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    locationToSettings = false
                    dialog.cancel()
                }
                builder.show()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                val builder = AlertDialog.Builder(
                    mActivity!!
                )
                builder.setTitle("Need Location and Phone Permission")
                builder.setMessage("This module needs location and phone permissions.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel()
                    locationToSettings = false
                    ActivityCompat.requestPermissions(
                        mActivity!!,
                        permissionsRequiredLocation,
                        PERMISSION_CALLBACK_CONSTANT_LOCATION
                    )
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    locationToSettings = false
                    dialog.cancel()
                }
                builder.show()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity!!,
                    Manifest.permission.CALL_PHONE
                )
            ) {
                val builder = AlertDialog.Builder(
                    mActivity!!
                )
                builder.setTitle("Need Location and Phone Permission")
                builder.setMessage("This module needs location and phone permissions.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel()
                    locationToSettings = false
                    ActivityCompat.requestPermissions(
                        mActivity!!,
                        permissionsRequiredLocation,
                        PERMISSION_CALLBACK_CONSTANT_LOCATION
                    )
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    locationToSettings = false
                    dialog.cancel()
                }
                builder.show()
            } else {
//                Toast.makeText(mActivity,"Unable to get Permission",Toast.LENGTH_LONG).show();
                locationToSettings = true
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", mActivity!!.packageName, null)
                intent.data = uri
                mActivity!!.startActivityForResult(intent, PERMISSION_CALLBACK_CONSTANT_LOCATION)
                Toast.makeText(
                    mContext,
                    "Go to settings and grant access to location and phone.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (ActivityCompat.checkSelfPermission(
                    mActivity!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mActivity!!, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mActivity!!, Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Got Permission
                proceedAfterPermission()
            } /*else  if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                //DENIAL
                requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

            }*/
        }
    }

    fun proceedAfterPermission() {
       /* intent = Intent(mContext, ContactUsActivity::class.java)
        HomeActivity.stopDisconnectTimer()
        mContext.startActivity(intent)*/
    }

    companion object {
        var userName: TextView? = null
        private const val REQUEST_PERMISSION_LOCATION = 103
        private const val PERMISSION_CALLBACK_CONSTANT_LOCATION = 3
        private const val REQUEST_PERMISSION_PHONE = 104
    }
}