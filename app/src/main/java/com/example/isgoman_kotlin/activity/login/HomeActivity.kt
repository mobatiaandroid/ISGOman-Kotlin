package com.example.isgoman_kotlin.activity.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.home.adapter.HomeViewPagerAdapter
import com.example.isgoman_kotlin.activity.home.model.BannerList
import com.example.isgoman_kotlin.activity.home.model.HomeResponseModel
import com.example.isgoman_kotlin.activity.home.model.HomeTabModel
import com.example.isgoman_kotlin.appcontroller.AppController
import com.example.isgoman_kotlin.constants.JsonTagConstants
import com.example.isgoman_kotlin.constants.JsonTagConstants.JTAG_RESPONSECODE
import com.example.isgoman_kotlin.constants.JsonTagConstants.JTAG_STATUSCODE
import com.example.isgoman_kotlin.constants.StausCodes
import com.example.isgoman_kotlin.constants.StausCodes.*
import com.example.isgoman_kotlin.constants.URLConstants
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.mobatia.nasmanila.api.ApiClient
import com.viewpagerindicator.CirclePageIndicator
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*

class HomeActivity :AppCompatActivity(), JsonTagConstants, URLConstants, StausCodes {
    lateinit var mContext: Context
    var mActivity: Activity? = null
    var mContexts: Context? = null

    //    ViewPager viewPager;
    var pagerCount = 0

    //    HomeViewPagerAdapter homeViewPagerAdapter;
    var imageSettings: ImageView? = null

    //HomeViewPagerAdapter_New homeViewPagerAdapter;
    //ArrayList<HomeTabModel> homeTabModelsTabArrangemnt=new ArrayList<>();
    var homeTabModelsTabTitle: ArrayList<HomeTabModel> = ArrayList<HomeTabModel>()
    var selectschoolLinearLayout: LinearLayout? = null
    lateinit var topLinearLayout :LinearLayout
    var popupWindow: PopupWindow? = null
    var listSchool: ListView? = null
    var switchLanguagearrow: ImageView? = null
    var logo:android.widget.ImageView? = null
    var chooseSchoolRelative: RelativeLayout? = null
    var llHead: RelativeLayout? = null
    var mBannerListHome = ArrayList<BannerList>()
    var mBannerList = ArrayList<String>()



    //    CirclePageIndicator mIndicator;
    var txtSwitch: TextView? = null
    var selectedFromList: String? = null
    private val timer: Timer? = null
    var mWidthPixels = 0
    var mHeightPixels:Int = 0
    var confirmlogin = false
    var extras: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mContext=this
        initFn()
        callBannerImageAPI()
    }

    private fun callBannerImageAPI() {
        mBannerListHome= ArrayList()
        mBannerList= ArrayList()
        val call: Call<HomeResponseModel> = ApiClient.getApiService().homeslider(
            11,
            AppPreferenceManager().getAccessToken(mContext).toString())
        call.enqueue(object : Callback<HomeResponseModel> {
            override fun onResponse(
                call: Call<HomeResponseModel>,
                response: Response<HomeResponseModel>
            ) {
                if (response.body()!!.responsecode.equals("200")) {
                    val homeresponse: HomeResponseModel? = response.body()
                    if(response.body()!!.response.statuscode.equals("201"))
                    {
                        AppController.viewPager = findViewById<View>(R.id.viewPager) as ViewPager
                        AppController.mIndicator =
                            findViewById<View>(R.id.indicator) as CirclePageIndicator
                        AppController.mIndicator!!.pageColor = Color.parseColor("#b2b2b2")
                        AppController.mIndicator!!.fillColor = Color.parseColor("#71ACD6")
                        mBannerListHome=(response.body()!!.response.sliders)
                        for (i in 0..mBannerListHome.size-1) {
                            var imagee:String
                            imagee=(mBannerListHome.get(i).image!!)
                            //AppController.mImageArrayList!!.add(imagee)
                            Log.e("Image",imagee)
                            mBannerList.addAll(listOf(imagee))
                            //AppController.mImageArrayList!!.addAll(listOf(imagee))

                        }

                        Log.e("mBannerList", response.body()!!.response.sliders.get(1).id.toString())
                        AppController.homeViewPagerAdapter = HomeViewPagerAdapter(
                            mContext,
                            mActivity,
                            pagerCount,
                            AppController.viewPager,
                            getScreenSizeInInches(),
                            mBannerList
                        )
                        AppController.homeViewPagerAdapter!!.notifyDataSetChanged()
                        AppController.viewPager!!.adapter = AppController.homeViewPagerAdapter
                        AppController.mIndicator!!.setViewPager(AppController.viewPager)
                        AppController.viewPager!!.currentItem = 0
                        AppPreferenceManager().setHomePos(mContext, 0.toString())
                    }

                } else {

                    // CommonMethods.alert_validemail(mcontext,"Alert","Invalid Email")
                }

            }

            override fun onFailure(call: Call<HomeResponseModel?>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Fail to get the data..",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.e("succ", t.message.toString())
            }
        })
    }

    private fun initFn() {
        AppController.viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        selectschoolLinearLayout = findViewById<View>(R.id.selectschool) as LinearLayout
        topLinearLayout = findViewById<View>(R.id.toplayout) as LinearLayout
        listSchool = findViewById<View>(R.id.listSchool) as ListView
        switchLanguagearrow = findViewById<View>(R.id.switchLanguagearrow) as ImageView
        logo = findViewById<View>(R.id.logo) as ImageView
        chooseSchoolRelative = findViewById<View>(R.id.chooseSchoolRelative) as RelativeLayout
        txtSwitch = findViewById<View>(R.id.txtSwitch) as TextView


        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        System.out.println("Icon---" + AppPreferenceManager().getSchoolSelection(mContext))
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
//            logo.setImageResource(R.drawable.title_logo);
            // getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbargreennew));
            supportActionBar!!.title = ""
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME
            supportActionBar!!.setDisplayShowCustomEnabled(true)
            supportActionBar!!.setCustomView(R.layout.custom_action_bar)
            val view = supportActionBar!!.customView
            supportActionBar!!.elevation = 0f
            val toolbar = view.parent as Toolbar
            toolbar.setContentInsetsAbsolute(0, 0)
            imageSettings = view.findViewById<View>(R.id.imageSettings) as ImageView
            llHead = view.findViewById<View>(R.id.llHead) as RelativeLayout
            llHead!!.setBackgroundDrawable(resources.getDrawable(R.drawable.titalbargreennew))
            imageSettings!!.setOnClickListener {
               /* HomeActivity.stopDisconnectTimer()
                startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))*/
            }
            txtSwitch!!.text = "ISG"
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
//            logo.setImageResource(R.drawable.titlelogoblue);
            //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.titalbarbluenew));
            supportActionBar!!.setTitle("")
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME
            supportActionBar!!.setDisplayShowCustomEnabled(true)
            supportActionBar!!.setCustomView(R.layout.custom_action_bar)
            val view = supportActionBar!!.customView
            supportActionBar!!.elevation = 0f
            val toolbar = view.parent as Toolbar
            toolbar.setContentInsetsAbsolute(0, 0)
            imageSettings = view.findViewById<View>(R.id.imageSettings) as ImageView
            llHead = view.findViewById<View>(R.id.llHead) as RelativeLayout
            llHead!!.setBackgroundDrawable(resources.getDrawable(R.drawable.titalbarbluenew))
            imageSettings!!.setOnClickListener {
               /* HomeActivity.stopDisconnectTimer()
                startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))*/
            }
            txtSwitch!!.text = "ISG-INT"
        }
        setArrayList(loadJSONFromAsset()!!)
        //System.out.println("count::"+homeTabModelsTabArrangemnt.size());
        /*  if (homeTabModelsTabArrangemnt.size() <= 6) {
            pagerCount = 1;

        } else if (homeTabModelsTabArrangemnt.size() <= 12) {
            pagerCount = 2;

        } else*/
//
        //System.out.println("count::"+homeTabModelsTabArrangemnt.size());
        /*  if (homeTabModelsTabArrangemnt.size() <= 6) {
            pagerCount = 1;

        } else if (homeTabModelsTabArrangemnt.size() <= 12) {
            pagerCount = 2;

        } else*/
//
        pagerCount = 2 //was 3

        AppController.mIndicator = findViewById<View>(R.id.indicator) as CirclePageIndicator
        AppController.mIndicator!!.pageColor = Color.parseColor("#b2b2b2")
        AppController.mIndicator!!.fillColor = Color.parseColor("#71ACD6")
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
//            callBannerImageAPI();
           // getBadgeAPI()
        } else {
            AppUtilityMethods.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
//        homeViewPagerAdapter=new HomeViewPagerAdapter(mContext,pagerCount,viewPager,getScreenSizeInInches());
//        //homeViewPagerAdapter=new HomeViewPagerAdapter_New(mContext,pagerCount,viewPager);
//
//        viewPager.setAdapter(homeViewPagerAdapter);
//        mIndicator.setViewPager(viewPager);


        //        homeViewPagerAdapter=new HomeViewPagerAdapter(mContext,pagerCount,viewPager,getScreenSizeInInches());
//        //homeViewPagerAdapter=new HomeViewPagerAdapter_New(mContext,pagerCount,viewPager);
//
//        viewPager.setAdapter(homeViewPagerAdapter);
//        mIndicator.setViewPager(viewPager);
        AppController.viewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                AppPreferenceManager.setHomePos(mContext,String.valueOf(position));
            }

            override fun onPageSelected(position: Int) {
                // Check if this is the page you want.
                AppPreferenceManager().setHomePos(mContext, position.toString())
                if (AppPreferenceManager().getIsGuest(mContext)) {
                    if (position == 0) {
//                        selectschoolLinearLayout.setVisibility(View.VISIBLE);//09oct2017 commented
                        selectschoolLinearLayout!!.visibility = View.GONE
                    } else {
                        selectschoolLinearLayout!!.visibility = View.GONE
                    }
                } else {
                    selectschoolLinearLayout!!.visibility = View.GONE
                }
                //                if (AppController.viewPager!=null)
//                AppController.mIndicator.setCurrentItem(position);
            }
        })

        selectschoolLinearLayout!!.setOnClickListener { v ->
            val popUp: PopupWindow = popupWindowsort()!!
            val coordinate = chooseSchoolRelative!!.bottom
            popUp.showAtLocation(v, Gravity.RIGHT or Gravity.TOP, 0, coordinate)
            //showDialog((Activity) mContext, getString(R.string.alert_heading), "Do you want to switch to ISG", R.drawable.infoicon,  R.drawable.roundblue);
        }
        extras = intent.extras
        confirmlogin = if (extras != null) {
            extras!!.getBoolean("confirmlogin")
        } else {
            false
        }
        if (confirmlogin) {
            AppUtilityMethods.showDialogAlertDismiss(
                mContext as Activity,
                getString(R.string.alert_heading),
                getString(R.string.first_otp_alert),
                R.drawable.infoicon,
                R.drawable.roundblue
            )
        }
    }

    private fun setArrayList(json: String) {
        try {
            AppController.mTabTitleModelArrayList = ArrayList<HomeTabModel>()
            AppController.mTabArrangeModelArrayList = ArrayList<HomeTabModel>()
            val rootObject = JSONObject(json)
            val responseCode = rootObject.getString(JTAG_RESPONSECODE)
            if (responseCode == RESPONSE_SUCCESS) {
                val responseObject = rootObject.optJSONObject("response")
                val statusCode = responseObject.getString(JTAG_STATUSCODE)
                if (statusCode.equals("303", ignoreCase = true)) {
                    val data = responseObject.optJSONObject("data")
                    val tabArrangeArray = data.optJSONArray("tab_arrange")
                    for (i in 0 until tabArrangeArray.length()) {
                        val mTabArrangeModel = HomeTabModel()
                        mTabArrangeModel.tab_id=(tabArrangeArray.getString(i))

                        //homeTabModelsTabArrangemnt.add(mTabArrangeModel);
                        AppController.mTabArrangeModelArrayList!!.add(mTabArrangeModel)
                        //AppController.mTabArrangeModelArrayList.add(mTabArrangeModel);
                    }
                    //                                            PreferenceManager.setTabArrange(mContext, mTabArrangeModelArrayList);
                    val tabTitleArray = data.optJSONArray("tab_title")
                    for (i in 0 until tabTitleArray.length()) {
                        val mTabTitleModel = HomeTabModel()
                        mTabTitleModel.tab_title=(tabTitleArray.getString(i))
                        //System.out.println("item---2--" + tabTitleArray.getString(i));

                        // homeTabModelsTabTitle.add(mTabTitleModel);
                        AppController.mTabTitleModelArrayList!!.add(mTabTitleModel)
                    }
                } else if (statusCode.equals(RESPONSE_ACCESSTOKEN_EXPIRED, ignoreCase = true) ||
                    statusCode.equals(RESPONSE_ACCESSTOKEN_MISSING, ignoreCase = true)
                ) {
                } else {
                }
            } else {
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val `is`: InputStream
            //            if (AppPreferenceManager.getIsGuest(mContext)) {
            if (AppPreferenceManager().getUserId(mContext).equals("")) {
                `is` = mContext.assets.open("tabarrangemntsguest.json")
            } else {
                `is` = mContext.assets.open("tabarrangemnts.json")
                selectschoolLinearLayout!!.visibility = View.GONE
            }
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8")
            )
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        //System.out.println("JSON---"+json);
        return json
    }
    @SuppressLint("MissingInflatedId")
    private fun popupWindowsort(): PopupWindow? {

        // initialize a pop up window type
        popupWindow = PopupWindow(this)
        popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup));
        val popUpView = layoutInflater.inflate(
            R.layout.activity_main,
            null
        )
        //popupWindow.setContentView(popUpView);
        val customAdapter: CustomAdapter = CustomAdapter(mContext)
        // the drop down list is a list view
        //ListView listViewSort = new ListView(this);
        // set our adapter and pass our pop up window contents
        val listViewSort = popUpView.findViewById<View>(R.id.listSchool) as ListView
        listViewSort.adapter = customAdapter

        // some other visual settings for popup window
        popupWindow!!.isFocusable = true
        popupWindow!!.width = 400
        //popupWindow.set;
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // set the listview as popup content
        popupWindow!!.contentView = popUpView
        listViewSort.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                popupWindow!!.dismiss()
                if (position == 0) {
                    selectedFromList = "ISG"
                } else if (position == 1) {
                    selectedFromList = "ISG-INT"
                }
                AppPreferenceManager().setSchoolSelection(mContext, selectedFromList)
                showDialog(
                    mContext as Activity,
                    getString(R.string.alert_heading),
                    "Do you want to switch to $selectedFromList",
                    R.drawable.infoicon,
                    R.drawable.roundblue,
                    position
                )
            }
        return popupWindow
    }
    private fun getScreenSizeInInches(): Double {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        setRealDeviceSizeInPixels()
        val x = Math.pow((mWidthPixels / dm.xdpi).toDouble(), 2.0)
        val y = Math.pow((mHeightPixels / dm.ydpi).toDouble(), 2.0)
        val screenInches = Math.sqrt(x + y)
        Log.d("debug", "Screen inches : $screenInches")
        return screenInches
    }


    private fun setRealDeviceSizeInPixels() {
        val windowManager = windowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)


        // since SDK_INT = 1;
        mWidthPixels = displayMetrics.widthPixels
        mHeightPixels = displayMetrics.heightPixels

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                mWidthPixels =
                    Display::class.java.getMethod("getRawWidth").invoke(display) as Int
                mHeightPixels =
                    Display::class.java.getMethod("getRawHeight").invoke(display) as Int
            } catch (ignored: Exception) {
            }
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                val realSize = Point()
                Display::class.java.getMethod("getRealSize", Point::class.java)
                    .invoke(display, realSize)
                mWidthPixels = realSize.x
                mHeightPixels = realSize.y
            } catch (ignored: Exception) {
            }
        }
    }
    fun showDialog(
        activity: Activity?,
        msgHead: String?,
        msg: String?,
        ico: Int,
        bgIcon: Int,
        position: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_switch_dialog)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead
        val dialogButton = dialog.findViewById<View>(R.id.btn_signup) as Button
        val dialogCancel = dialog.findViewById<View>(R.id.btn_maybelater) as Button
        dialogButton.setOnClickListener {
            dialog.dismiss()
            if (position == 0) {
                logo!!.setImageResource(R.drawable.title_logo)
                txtSwitch!!.text = "ISG"
                AppPreferenceManager().setSchoolSelection(mContext, "ISG")
            } else if (position == 1) {
                logo!!.setImageResource(R.drawable.titlelogoblue)
                txtSwitch!!.text = "ISG-INT"
                AppPreferenceManager().setSchoolSelection(mContext, "ISG-INT")
            }
        }
        dialogCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    val DISCONNECT_TIMEOUT: Long = 300000 // 5 min = 5 * 60 * 1000 ms


    var disconnectHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {}
    }

    var disconnectCallback = Runnable { // Perform any required operation on disconnect
        AppPreferenceManager().setIsGuest(mContext, false)
        AppPreferenceManager().setUserId(mContext, "")
        AppPreferenceManager().setIsUserAlreadyLoggedIn(mContext, false)
        AppPreferenceManager().setSchoolSelection(mContext, "ISG")
        if (AppUtilityMethods.isAppInForeground(mContext)) {
            val mIntent = Intent(mContext, LoginActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
          mContext.startActivity(mIntent)
           mActivity!!.finish()
        }
    }

    fun resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback)
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT)
    }

    fun stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback)
    }







    class CustomAdapter(  // String [] result=new String[]{};
        var context: Context
    ) : BaseAdapter() {
        var result = arrayOf("ISG", "ISG-INT")
        var prgmImages = intArrayOf(R.drawable.logo2, R.drawable.logo1)
        private var inflater: LayoutInflater? = null

        init {
            // TODO Auto-generated constructor stub
            //result=prgmNameList;
            // imageId=prgmImages;
           inflater =
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getCount(): Int {
            // TODO Auto-generated method stub
            return result.size
        }

        override fun getItem(position: Int): Any {
            // TODO Auto-generated method stub
            return position
        }

        override fun getItemId(position: Int): Long {
            // TODO Auto-generated method stub
            return position.toLong()
        }

        inner class Holder {
            var tv: TextView? = null
            var img: ImageView? = null
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            // TODO Auto-generated method stub
            val holder: Holder = Holder()
            val rowView: View
            rowView = inflater!!.inflate(R.layout.custom_adapter_home_school_item, null)
            holder.tv = rowView.findViewById<View>(R.id.dep_name) as TextView
            holder.img = rowView.findViewById<View>(R.id.imgView) as ImageView
            holder.tv!!.text = result[position]
            holder.img!!.setImageResource(prgmImages[position])
            return rowView
        }
    }
}