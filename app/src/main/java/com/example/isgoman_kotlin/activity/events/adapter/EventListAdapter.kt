package com.example.isgoman_kotlin.activity.events.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.isgoman_kotlin.R
import com.example.isgoman_kotlin.activity.events.model.EventModels
import com.example.isgoman_kotlin.manager.AppPreferenceManager
import com.example.isgoman_kotlin.manager.AppUtilityMethods

class EventListAdapter(
    private val mContext: Context,
    mAboutusModelArrayList: ArrayList<EventModels>
) :
    RecyclerView.Adapter<EventListAdapter.MyViewHolder>() {
    private val mAboutusModelArrayList: ArrayList<EventModels>

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var start_date: TextView
        var start_time: TextView
        var event: TextView
        var end_date: TextView
        var end_time: TextView
        var layoutList: LinearLayout? = null
        var lineLinear: LinearLayout? = null
        var eventBg: ImageView

        init {
            start_date = view.findViewById<View>(R.id.start_date) as TextView
            start_time = view.findViewById<View>(R.id.start_time) as TextView
            end_date = view.findViewById<View>(R.id.end_date) as TextView
            end_time = view.findViewById<View>(R.id.end_time) as TextView
            event = view.findViewById<View>(R.id.event) as TextView
            eventBg = view.findViewById<View>(R.id.eventBg) as ImageView
        }
    }

    init {
        this.mAboutusModelArrayList = mAboutusModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_adapter_event_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
        if (AppPreferenceManager().getSchoolSelection(mContext).equals("ISG")) {
            holder.eventBg.setBackgroundResource(R.drawable.evnticon)
        } else {
            holder.eventBg.setBackgroundResource(R.drawable.evnticonisg)
        }
        holder.end_date.setText(AppUtilityMethods.separateDate(mAboutusModelArrayList[position].end_date))
        holder.end_time.setText(
            AppUtilityMethods.separateTime(mAboutusModelArrayList[position].end_date)
                .replace(".", "")
        )
        holder.start_date.setText(AppUtilityMethods.separateDate(mAboutusModelArrayList[position].start_date))
        holder.start_time.setText(
            AppUtilityMethods.separateTime(mAboutusModelArrayList[position].start_date)
                .replace(".", "")
        )
        holder.event.setText(mAboutusModelArrayList[position].name)
    }

    override fun getItemCount(): Int {
        return mAboutusModelArrayList.size
    }
}