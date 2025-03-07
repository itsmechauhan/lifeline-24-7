package com.example.lifeline24_7


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

data class FireReport(
    val description: String,
    val location: String,
    val imageUrl: String?
)

class FireReportAdapter(private val reports: List<FireReport>) :
    RecyclerView.Adapter<FireReportAdapter.FireReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fire_report, parent, false)
        return FireReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: FireReportViewHolder, position: Int) {
        val report = reports[position]
        holder.description.text = report.description
        holder.location.text = report.location

        // Load image using Picasso (if image URL exists)
        if (report.imageUrl != null) {
            Picasso.get().load(report.imageUrl).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    class FireReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.reportDescription)
        val location: TextView = itemView.findViewById(R.id.reportLocation)
        val imageView: ImageView = itemView.findViewById(R.id.reportImage)
    }
}
