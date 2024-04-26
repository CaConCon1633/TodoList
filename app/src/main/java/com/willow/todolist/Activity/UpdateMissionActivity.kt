package com.willow.todolist.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.willow.todolist.Datas.Database
import com.willow.todolist.Models.Mission
import com.willow.todolist.databinding.ActivityUpdateMissionBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class UpdateMissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateMissionBinding
    private lateinit var db: Database
    private var missionId: Int = -1
    var myCalendar: Calendar = Calendar.getInstance()
    var fmtTime: DateFormat = DateFormat.getTimeInstance()
    var fmtDate: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
    var dateFormat: SimpleDateFormat? = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateMissionBinding.inflate(layoutInflater)

        setContentView(binding.root)

        db = Database(this)
        missionId = intent.getIntExtra("mission_id", -1)
        if (missionId == -1) {
            finish()
            return
        }

        val mission = db.getById(missionId)
        binding.txtMission.setText(mission.content)
        binding.txtDate.text = mission.date
        binding.txtTime.text = mission.time

        binding.btnSave.setOnClickListener {
            val newContent = binding.txtMission.text.toString()
            val newDate = binding.txtDate.text.toString()
            val newTime = binding.txtTime.text.toString()

            val updateMission = Mission(missionId, newContent, newTime, newDate, mission.status)
            db.updateMission(updateMission)
            finish()
            Toast.makeText(this, " Changes successful!", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDelete.setOnClickListener {
            db.deleteMission(missionId)
            updateUI(this, MainActivity::class.java)
            finish()
        }

        var t = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            binding.txtTime.text = fmtTime.format(myCalendar.time)
        }
        var d = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.txtDate.text = dateFormat?.format(myCalendar.time)
        }
        binding.time.setOnClickListener {
            TimePickerDialog(
                this, t,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE), true
            ).show()
        }
        binding.date.setOnClickListener {
            DatePickerDialog(
                this, d,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

    }

    fun updateUI(context: Context, targetActivity: Class<*>) {
        val intent = Intent(context, targetActivity)
        intent.flags =
            Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        context.startActivity(intent)
    }
}