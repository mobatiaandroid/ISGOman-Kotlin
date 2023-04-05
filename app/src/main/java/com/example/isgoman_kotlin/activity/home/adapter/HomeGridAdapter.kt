package com.example.isgoman_kotlin.activity.home.adapter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.home.model.HomeTabModel
import com.example.isgoman_kotlin.appcontroller.AppController
import com.example.isgoman_kotlin.constants.TabIDConstants
import com.example.isgoman_kotlin.constants.TabIDConstants.*
import com.example.isgoman_kotlin.manager.AppPreferenceManager


class HomeGridAdapter : BaseAdapter, TabIDConstants {
   lateinit var mContext: Context
    var mTabModelArrayList: ArrayList<HomeTabModel>? = null
    lateinit var mImageList: IntArray
    var mTxtView: TextView? = null
    var mImgView: ImageView? = null
    var row_counts = 1
    var mPosition = 0

    constructor(
        mContext: Context,
        mTabModelArrayList: ArrayList<HomeTabModel>?,
        mImageList: IntArray
    ) {
        this.mContext = mContext
        this.mTabModelArrayList = mTabModelArrayList
        this.mImageList = mImageList
    }

    constructor(context: Context, row_count: Int, mPosition: Int) {
        mContext = context
        row_counts = row_count
        this.mPosition = mPosition
    }

    override fun getCount(): Int {
        return row_counts
    }

    override fun getItem(position: Int): Any {
        return mTabModelArrayList!![position].tab_title!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // if (convertView == null) {
        val inflate = LayoutInflater.from(mContext)
        HomeGridAdapter.Companion.mView = inflate.inflate(R.layout.adapter_home_gridview, null)
        /*} else {
            mView = convertView;
        }
*/
        mTxtView = mView!!.findViewById<View>(R.id.dep_name) as TextView?
        mImgView = mView!!.findViewById<View>(R.id.imgView) as ImageView?
        //       AppController.textBadge = (TextView) mView.findViewById(R.id.notifyIconBadge);
        notifyIconBadge = mView!!.findViewById<View>(R.id.notifyIconBadge) as TextView?
        try {
            /*System.out.println(
                "Size of array---" + AppController.mTabArrangeModelArrayList!!.size()
                    .toString() + "row count---" + row_counts + mPosition
            )*/
            Log.e("Success","Sucess")
            if (row_counts == 6 && mPosition == 0) {
                Log.e("InsideSuccess","Sucess")
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName());
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())-1).getTabName());
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName());
                val TAB_ID: Int =
                    AppController.mTabArrangeModelArrayList!!.get(position).tab_id!!.toInt()
                SetIconsAndTitle(TAB_ID)
            } else if (row_counts == 12 && mPosition == 1) {
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName()+6);
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())+5).getTabName());
                val TAB_ID: Int =
                    AppController.mTabArrangeModelArrayList!!.get(position + 6).tab_id!!.toInt()
                //            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position+6).getTabName());
                println("TAB_ID--$TAB_ID")
                SetIconsAndTitle(TAB_ID)
            }
            /*            else if (row_counts == 5 && mPosition == 2) {
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName()+18);
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())+17).getTabName());
                int TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(position + 18).getTab_id()));
                System.out.println("TAB_ID--" + TAB_ID);

//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position+18).getTabName());
//System.out.println("Tab id---"+TAB_ID);
                SetIconsAndTitle(TAB_ID);
            } else if (row_counts == 12 && mPosition == 2) {
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position).getTabName()+18);
//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(Integer.valueOf(AppController.mTabArrangeModelArrayList.get(position).getTabId())+17).getTabName());
                int TAB_ID = (Integer.parseInt(AppController.mTabArrangeModelArrayList.get(position + 18).getTab_id()));
                System.out.println("TAB_ID--" + TAB_ID);

//            mTxtView.setText(AppController.mTabTitleModelArrayList.get(position+18).getTabName());
//System.out.println("Tab id---"+TAB_ID);
                SetIconsAndTitle(TAB_ID);
            }*/
        } catch (e: Exception) {
          //  Log.e("Numberformat: ", e.message!!)
        }
        return HomeGridAdapter.Companion.mView!!
    }

    fun setNotifyIconVisibility(visible: Int) {
        if (visible == 0) {
//            AppController.textBadge.setVisibility(View.INVISIBLE);
            HomeGridAdapter.Companion.notifyIconBadge!!.setVisibility(View.INVISIBLE)
        } else {
//            AppController.textBadge.setVisibility(View.VISIBLE);
            HomeGridAdapter.Companion.notifyIconBadge!!.setVisibility(View.VISIBLE)
        }
    }

    fun SetIconsAndTitle(tab_id: Int) {
        when (tab_id) {
            NEWS_LETTER_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.news)
                mTxtView!!.setText(NEWS_LETTER)
                setNotifyIconVisibility(0)
            }
            ABOUT_US_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.aboutus)
                mTxtView!!.setText(ABOUT_US)
                setNotifyIconVisibility(0)
            }
            CONTACT_US_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.location)
                mTxtView!!.setText(CONTACT_US)
                setNotifyIconVisibility(0)
            }
            COURSES_TAB_ID -> {
                //                mImgView.setBackgroundResource(R.drawable.courses);
                mImgView!!.setBackgroundResource(R.drawable.videos)
                mTxtView!!.setText(COURSES)
                setNotifyIconVisibility(0)
            }
            STAFF_DIRECTORY_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.staffdirectory_icon)
                mTxtView!!.setText(STAFF_DIRECTORY)
                setNotifyIconVisibility(0)
            }
            SCHEDULES_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.schedule)
                mTxtView!!.setText(SCHEDULES)
            }
            NEWS_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.news_new)
                mTxtView!!.setText(NEWS)
                setNotifyIconVisibility(0)
            }
            CLUB_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.club)
                mTxtView!!.setText(CLUB)
                setNotifyIconVisibility(0)
            }
            TIMETABLE_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.timetable)
                mTxtView!!.setText(TIMETABLE)
                setNotifyIconVisibility(0)
            }
            CALENDAR_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.calender)
                mTxtView!!.setText(CALENDAR)
                setNotifyIconVisibility(0)
            }
            EVENTS_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.event)
                mTxtView!!.setText(EVENTS)
                setNotifyIconVisibility(0)
            }
            LEAVES_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.leaves)
                mTxtView!!.setText(LEAVES)
                setNotifyIconVisibility(0)
            }
            LOSTANDFOUND_TAB_ID -> {
                //                mImgView.setBackgroundResource(R.drawable.lostandfound);
                mImgView!!.setBackgroundResource(R.drawable.studentleaders)
                mTxtView!!.setText(LOSTANDFOUND)
                setNotifyIconVisibility(0)
            }
            STUDENT_AWARDS_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.student_awards)
                mTxtView!!.setText(STUDENT_AWARDS)
                setNotifyIconVisibility(0)
            }
            GALLERY_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.photos)
                mTxtView!!.setText(GALLERY)
                setNotifyIconVisibility(0)
            }
            QUOTESSTORIES_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.quotesstories)
                mTxtView!!.setText(QUOTESSTORIES)
                setNotifyIconVisibility(0)
            }
            NOTIFICATION_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.notification)
                mTxtView!!.setText(NOTIFICATION)
                if (AppPreferenceManager().getBadgecount(mContext).equals("0")) {
                    setNotifyIconVisibility(0)
                } else {
//                    AppController.textBadge.setText(AppPreferenceManager.getBadgecount(mContext));
                    HomeGridAdapter.Companion.notifyIconBadge!!.setText(
                        AppPreferenceManager().getBadgecount(
                            mContext
                        )
                    )
                    setNotifyIconVisibility(1)
                }
                LocalBroadcastManager.getInstance(mContext)
                    .registerReceiver(mMessageReceiver, IntentFilter("badgenotify"))
            }
            SETTINGS_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.settings)
                mTxtView!!.setText(SETTINGS)
                setNotifyIconVisibility(0)
            }
            USERPROFILE_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.userprofile)
                mTxtView!!.setText(USERPROFILE)
                setNotifyIconVisibility(0)
            }
            STUDENTROFILE_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.kidsprofile)
                mTxtView!!.setText(STUDENTROFILE)
                setNotifyIconVisibility(0)
            }
            STUDYANDCURRICULAMTAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.study_plan_and_curriculam)
                mTxtView!!.setText(STUDYANDCURRICULAM)
                setNotifyIconVisibility(0)
            }
            SPECIAL_MESSAGE_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.special_messages)
                mTxtView!!.setText(SPECIAL_MESSAGE)
                setNotifyIconVisibility(0)
            }
            CIRCULAR_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.circulars)
                mTxtView!!.setText(CIRCULAR_MESSAGE)
                setNotifyIconVisibility(0)
            }
            HOMEWORK_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mTxtView!!.setText(HOME_WORK)
                setNotifyIconVisibility(0)
            }
            WORKSHEET_TAB_ID -> {
                mImgView!!.setBackgroundResource(R.drawable.worksheet)
                mTxtView!!.setText(WORK_SHEET)
                setNotifyIconVisibility(0)
            }
            INV_TAB_ID_1 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_1)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_2 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_2)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_3 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_3)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_4 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_4)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_5 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_5)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_6 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                //                mImgView.setVisibility(View.INVISIBLE);
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_6)
            }
            INV_TAB_ID_7 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_7)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_8 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_8)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_9 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_9)
                mTxtView!!.visibility = View.INVISIBLE
            }
            INV_TAB_ID_10 -> {
                mImgView!!.setBackgroundResource(R.drawable.homework)
                mImgView!!.visibility = View.INVISIBLE
                mImgView!!.isEnabled = false
                mTxtView!!.setText(INV_TAB_SHEET_10)
                mTxtView!!.visibility = View.INVISIBLE
            }
        }
    }

    var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val identifierString = intent.action
            if (identifierString == "badgenotify") {
                System.out.println("Badge Count::" + AppPreferenceManager().getBadgecount(mContext))
                //                notifyIconBadge = (TextView) mView.findViewById(R.id.notifyIconBadge);
//                notifyIconBadge.setVisibility(View.VISIBLE);
                if (!AppPreferenceManager().getBadgecount(mContext).equals("0")) {
                    notifyIconBadge!!.setText(
                        AppPreferenceManager().getBadgecount(mContext)
                    )
                    HomeGridAdapter.Companion.notifyIconBadge!!.setVisibility(View.VISIBLE)
                } else {
                    HomeGridAdapter.Companion.notifyIconBadge!!.setText(
                        AppPreferenceManager().getBadgecount(mContext)
                    )
                    //                    setNotifyIconVisibility(1);
                    HomeGridAdapter.Companion.notifyIconBadge!!.setVisibility(View.INVISIBLE)
                }
                //                SetIconsAndTitle(NOTIFICATION_TAB_ID);

//                notifyIconBadge.setText(AppPreferenceManager.getBadgecount(mContext));
//                setNotifyIconVisibility(1);
            }
        }
    }

    companion object {
        var mView: View? = null
        var notifyIconBadge: TextView? = null
    }
}