package com.example.taskconnect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.taskconnect.model.User
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(private val applicationContext: Context,private var userList: List<User>):RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val auth = FirebaseAuth.getInstance()
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name:TextView = itemView.findViewById(R.id.user_name)
        val desc:TextView = itemView.findViewById(R.id.user_desc)
        val btnFollow:CardView = itemView.findViewById(R.id.btn_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_users,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = userList[position]
        holder.name.text = currentData.userName
        holder.desc.text = currentData.userDesc
        if (auth.currentUser?.displayName == currentData.userName){
            holder.btnFollow.isVisible = false
        }
    }
    fun onSearch(list: List<User>){
        userList = list
        notifyDataSetChanged()
    }
}