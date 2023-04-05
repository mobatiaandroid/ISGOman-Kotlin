package com.example.isgoman_kotlin.activity.circulars.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.circulars.model.CircularModel
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods

class CircularAdapter(private val mContext: Context, timeTableList: ArrayList<CircularModel>) :
    RecyclerView.Adapter<CircularAdapter.MyViewHolder>() {
    private val mTimeTableList: ArrayList<CircularModel>
    var dept: String? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView
        var listTxtDate: TextView
        var imgIcon: ImageView? = null
        var listBackGround: RelativeLayout

        init {
            mTitleTxt = view.findViewById<View>(R.id.listTxtTitle) as TextView
            listTxtDate = view.findViewById<View>(R.id.listTxtDate) as TextView
            listBackGround = view.findViewById<View>(R.id.listBackGround) as RelativeLayout
        }
    }

    init {
        mTimeTableList = timeTableList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_adapter_circular, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        System.out.println("Schooll---" + AppPreferenceManager().getSchoolSelection(mContext))
        Log.e("lst", mTimeTableList[position].id.toString())
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            holder.mTitleTxt.setTextColor(mContext.resources.getColor(R.color.login_button_bg))
            holder.listBackGround.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.listbg_isg))
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
            holder.mTitleTxt.setTextColor(mContext.resources.getColor(R.color.isg_int_blue))
            holder.listBackGround.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.listbg_isg_int))
        }
        holder.mTitleTxt.setText(mTimeTableList[position].name)
        holder.listTxtDate.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mTimeTableList[position].published))
        //        holder.listTxtDate.setText(AppUtilityMethods.separateDateTodDmMmMyYyY(mTimeTableList.get(position).getCircularDate())+" "+AppUtilityMethods.separateTime(mTimeTableList.get(position).getCircularDate()).replaceAll(".", ""));
    }

    override fun getItemCount(): Int {
        Log.e("size", mTimeTableList.size.toString())
        return mTimeTableList.size

    }
}