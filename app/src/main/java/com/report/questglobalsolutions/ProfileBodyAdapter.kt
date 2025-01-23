package com.report.questglobalsolutions

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileBodyAdapter(private val items: User) : RecyclerView.Adapter<ProfileBodyAdapter.ProfileBodyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileBodyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_body, parent, false)
        return ProfileBodyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileBodyViewHolder, position: Int) {
        val item = items
        holder.bind(item)
    }

    override fun getItemCount(): Int = 1

    class ProfileBodyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val username: TextView = itemView.findViewById(R.id.username)
        private val password: TextView = itemView.findViewById(R.id.password)
        private val logout: TextView = itemView.findViewById(R.id.btnLogout)

        fun bind(item: User) {
            val context = itemView.context
            val sharedToken = Token(context)
            name.text = item.username
            username.text = item.email
            password.text = item.password

            logout.setOnClickListener {
                sharedToken.del()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                //if (context is Profile) context.finish()
            }
        }
    }
}
