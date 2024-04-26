package com.willow.todolist.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.willow.todolist.Datas.Database
import com.willow.todolist.Models.Mission
import com.willow.todolist.R
import com.willow.todolist.Activity.UpdateMissionActivity


class MissionAdapter(private var missions: List<Mission>, context: Context) :
    RecyclerView.Adapter<MissionAdapter.MissionViewHolder>() {

    private lateinit var db: Database

    class MissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = itemView.findViewById(R.id.txtContent)
        val time: TextView = itemView.findViewById(R.id.time)
        val status: CheckBox = itemView.findViewById(R.id.btn_check)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mission_item, parent, false)
        return MissionViewHolder(view)
    }

    override fun getItemCount(): Int = missions.size

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val mission = missions[position]
        holder.content.text = mission.content
        holder.time.text = mission.time
        db = Database(holder.itemView.context)

        holder.status.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                db.updateStatus(mission.id, 1)
            } else {
                db.updateStatus(mission.id, 0)
            }
        }
        if (mission.status == 1) {
            holder.status.setChecked(toBoolean(mission.status))
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateMissionActivity::class.java).apply {
                putExtra("mission_id", mission.id)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newMissions: List<Mission>) {
        missions = newMissions
        notifyDataSetChanged()
    }

}