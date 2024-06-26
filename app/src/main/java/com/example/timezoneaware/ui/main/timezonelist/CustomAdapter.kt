package com.example.timezoneaware.ui.main.timezonelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timezoneaware.data.model.TimeZoneInfo
import com.example.timezoneaware.databinding.TimezoneItemRawBinding
import com.example.timezoneaware.prefs
import com.example.timezoneaware.utils.DateUtils

class CustomAdapter(
    var cityList: List<TimeZoneInfo>,
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    var onItemClick: ((pos: Int, view: View) -> Unit)? = null

    inner class ViewHolder(val binding: TimezoneItemRawBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        override fun onClick(view: View?) {
            if (view != null) {
                onItemClick?.invoke(adapterPosition, view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TimezoneItemRawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(cityList[position]){
                binding.tvCityname.text = "City Name: "+ this.cityName
                binding.tvTimezone.text = "City Timezone: "+ this.timeZoneCityName
                binding.tvTimeGmt!!.text= "GMT Time: "+ DateUtils.getTimezoneGMT(this.timeZoneCityName)
                getUserRoles(binding)
        }
            binding.btnDelete.setOnClickListener(this)
            binding.btnEdit.setOnClickListener(this)

        }
    }

    fun getUserRoles(binding: TimezoneItemRawBinding) {
        val roleId = prefs!!.userTypePref
        when (roleId)
        {
            0->{
                binding.btnDelete.visibility=View.VISIBLE
                binding.btnEdit.visibility=View.VISIBLE
            }

            1->{
                binding.btnDelete.visibility=View.INVISIBLE
                binding.btnEdit.visibility=View.INVISIBLE
            }
            2->{
                binding.btnDelete.visibility=View.VISIBLE
                binding.btnEdit.visibility=View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun update(modelList:List<TimeZoneInfo>){
        cityList = modelList
        notifyDataSetChanged()
    }

}
