package com.example.isgoman_kotlin.activity.events

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.events.adapter.EventListAdapter
import com.example.isgoman_kotlin.activity.events.model.EventModels
import com.example.isgoman_kotlin.activity.events.model.EventResponseModel
import com.example.isgoman_kotlin.activity.login.HomeActivity
import com.example.isgoman_kotlin.activity.login.LoginActivity
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods
import com.example.isgoman_kotlin.manager.HeaderManager
import com.example.isgoman_kotlin.recyclerviewmanager.RecyclerItemListener
import com.mobatia.nasmanila.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsActivity:AppCompatActivity() {
    var relativeHeader: RelativeLayout? = null
    var headermanager: HeaderManager? = null
     lateinit  var back: ImageView
    var eventsList: RecyclerView? = null
    var eventModelsArrayList: ArrayList<EventModels> = ArrayList<EventModels>()
    var eventLayout: LinearLayout? = null
    lateinit var mContext:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        mContext=this
        initFn()
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            callEventListAPI()
        } else {
            AppUtilityMethods.showDialogAlertDismiss(
                mContext as Activity,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
    }

    private fun callEventListAPI() {
        var boardId = ""
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            boardId = "1"
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
            boardId = "2"
        } else {
            boardId = "1"
        }

        val call: Call<EventResponseModel> = ApiClient.getApiService().events(
            11,
            AppPreferenceManager().getAccessToken(mContext).toString(),boardId,
            AppPreferenceManager().getStudentId(mContext).toString(),
            AppPreferenceManager().getUserId(mContext).toString()
        )
        call.enqueue(object : Callback<EventResponseModel> {
            override fun onResponse(
                call: Call<EventResponseModel>,
                response: Response<EventResponseModel>
            ) {
                eventModelsArrayList=response.body()!!.response.about
                val mAboutusRecyclerviewAdapter = EventListAdapter(mContext, eventModelsArrayList)
                eventsList!!.adapter = mAboutusRecyclerviewAdapter

            }

            override fun onFailure(call: Call<EventResponseModel?>, t: Throwable) {
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
        eventModelsArrayList= ArrayList()
        relativeHeader = findViewById<View>(R.id.relativeHeader) as RelativeLayout
        eventsList = findViewById<View>(R.id.mSporsEventListView) as RecyclerView
        eventLayout = findViewById<View>(R.id.eventLayout) as LinearLayout
        eventsList!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        eventsList!!.layoutManager = llm
        val spacing = 10 // 50px

        /*ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        eventsList.addItemDecoration(itemDecoration);*/
        /*ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        eventsList.addItemDecoration(itemDecoration);*/headermanager =
            HeaderManager(this, getString(R.string.events))
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            headermanager!!.getHeader(relativeHeader, 1)
            eventLayout!!.setBackgroundResource(R.color.events_bg)
        } else {
            headermanager!!.getHeader(relativeHeader, 3)
            eventLayout!!.setBackgroundResource(R.color.calendar_blue_selector)
        }
        back = headermanager!!.leftButton
        headermanager!!.setButtonLeftSelector(
            R.drawable.backbtn,
            R.drawable.backbtn
        )

        back.setOnClickListener(View.OnClickListener {
           stopDisconnectTimer()
            finish()
        })

        eventsList!!.addOnItemTouchListener(
            RecyclerItemListener(
                applicationContext, eventsList!!,
                object : RecyclerItemListener.RecyclerTouchListener {
                    override fun onClickItem(v: View?, position: Int) {
                        stopDisconnectTimer()
                        val intent =
                            Intent(mContext, EventDetailsActivity::class.java)
                        intent.putExtra("position", position)
                        intent.putExtra("array", eventModelsArrayList)
                        startActivity(intent)
                    }

                    override fun onLongClickItem(v: View?, position: Int) {
                        println("On Long Click Item interface")
                    }
                })
        )
    }
    override fun onRestart() {
        super.onRestart()
        if (AppPreferenceManager().getUserId(mContext).equals("")) {
            val mIntent = Intent(this, LoginActivity::class.java)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mIntent)
            finish()
        } else {
            eventModelsArrayList.clear()
            if (AppUtilityMethods.isNetworkConnected(mContext)) {
                callEventListAPI()
            } else {
                AppUtilityMethods.showDialogAlertDismiss(
                    mContext as Activity,
                    "Network Error",
                    getString(R.string.no_internet),
                    R.drawable.nonetworkicon,
                    R.drawable.roundred
                )
            }
        }
    }


    /* public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(EventListActivity.this, false);
            AppPreferenceManager.setUserId(EventListActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(EventListActivity.this, false);
            AppPreferenceManager.setSchoolSelection(EventListActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(EventListActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };*/
    fun resetDisconnectTimer() {
        HomeActivity().disconnectHandler.removeCallbacks(HomeActivity().disconnectCallback)
        HomeActivity().disconnectHandler.postDelayed(
            HomeActivity().disconnectCallback,
            HomeActivity().DISCONNECT_TIMEOUT
        )
    }

    fun stopDisconnectTimer() {
        HomeActivity().disconnectHandler.removeCallbacks(HomeActivity().disconnectCallback)
    }

    override fun onUserInteraction() {
        resetDisconnectTimer()
    }

    override fun onResume() {
        super.onResume()
        resetDisconnectTimer()
    }

    override fun onStop() {
        super.onStop()
//        stopDisconnectTimer();
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stopDisconnectTimer()
    }
}