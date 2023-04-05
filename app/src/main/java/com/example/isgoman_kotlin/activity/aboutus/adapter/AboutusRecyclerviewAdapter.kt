package com.example.isgoman_kotlin.activity.aboutus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.aboutus.model.AboutUsModel
import com.example.isgoman_kotlin.manager.AppPreferenceManager

class AboutusRecyclerviewAdapter(
    private val mContext: Context,
    timeTableList: ArrayList<AboutUsModel>
) :
    RecyclerView.Adapter<AboutusRecyclerviewAdapter.MyViewHolder>() {
    private val mTimeTableList: ArrayList<AboutUsModel>
    var dept: String? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView
        var listTxtClass: TextView? = null
        var imgIcon: ImageView
        var v: View
        var listBackGround: RelativeLayout

        init {
            mTitleTxt = view.findViewById<View>(R.id.listTxtTitle) as TextView
            imgIcon = view.findViewById<View>(R.id.imagicon) as ImageView
            v = view.findViewById(R.id.view)
            v.visibility = View.GONE
            listBackGround = view.findViewById<View>(R.id.listBackGround) as RelativeLayout
        }
    }

    init {
        mTimeTableList = timeTableList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_adapter_settings, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        System.out.println("Schooll---" + AppPreferenceManager().getSchoolSelection(mContext))
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            holder.mTitleTxt.setTextColor(mContext.resources.getColor(R.color.login_button_bg))
            holder.v.setBackgroundColor(mContext.resources.getColor(R.color.login_button_bg))
            holder.listBackGround.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.listbg_isg))
        } else if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG-INT")) {
            holder.mTitleTxt.setTextColor(mContext.resources.getColor(R.color.isg_int_blue))
            holder.v.setBackgroundColor(mContext.resources.getColor(R.color.isg_int_blue))
            holder.listBackGround.setBackgroundDrawable(mContext.resources.getDrawable(R.drawable.listbg_isg_int))
        }
        holder.mTitleTxt.setText(mTimeTableList[position].title)
    }

    override fun getItemCount(): Int {
        return mTimeTableList.size
    }
}