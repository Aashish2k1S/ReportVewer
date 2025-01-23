package com.report.questglobalsolutions

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class HomeBodyAdapter(private val items: List<Data>) : RecyclerView.Adapter<HomeBodyAdapter.HomeBodyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBodyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_body, parent, false)
        return HomeBodyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeBodyViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class HomeBodyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val reportTitle: TextView = itemView.findViewById(R.id.reportTitle)
        private val docIcon: ImageView = itemView.findViewById(R.id.docIcon)


        fun bind(item: Data) {
            reportTitle.text = item.report

            if (item.downloadUrl.isEmpty()) {
                docIcon.setImageResource(R.drawable.e404)
            } else{
                when (item.report) {
                    "Outstanding Details" -> docIcon.setImageResource(R.drawable.outstanding)
                    "Outstanding Summary" -> docIcon.setImageResource(R.drawable.outstanding)

                    "PartyWise Outstanding Details" -> docIcon.setImageResource(R.drawable.outstanding)
                    "PartyWise Outstanding Summary" -> docIcon.setImageResource(R.drawable.outstanding)

                    "Order Pending" -> docIcon.setImageResource(R.drawable.orderpending)

                    "PartyWise Order Pending" -> docIcon.setImageResource(R.drawable.orderpending)

                    "Sales Report" -> docIcon.setImageResource(R.drawable.salesreport)

                    else -> docIcon.setImageResource(R.drawable.doc)
                }
            }

            itemView.setOnClickListener {
                val context = itemView.context

                if (item.downloadUrl.isEmpty()) {
                    Toast.makeText(context, "Please refresh or try again later..", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(item.downloadUrl)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}
