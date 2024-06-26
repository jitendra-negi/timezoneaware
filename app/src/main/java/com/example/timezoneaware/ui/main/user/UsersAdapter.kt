package com.example.timezoneaware.ui.main.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timezoneaware.data.model.User
import com.example.timezoneaware.databinding.UsersItemRawBinding
import com.example.timezoneaware.utils.HelperUtils

class UsersAdapter (
    var userList: List<User>,
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    var onItemClick: ((pos: Int, view: View) -> Unit)? = null

    inner class ViewHolder(val binding: UsersItemRawBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        override fun onClick(view: View?) {
            if (view != null) {
                onItemClick?.invoke(adapterPosition, view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UsersItemRawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(userList[position]){
                binding.tvUsername.text = "User Name: "+ this.userName
                binding.tvUserrole.text = "User Role: "+ HelperUtils.getUserRoles(this.userType!!)
            }
            binding.btnDelete.setOnClickListener(this)
            binding.btnEdit.setOnClickListener(this)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun update(refreshedUserList:List<User>){
        userList = refreshedUserList
        notifyDataSetChanged()
    }

}