package com.example.fobutry

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleAdapter(
    private var scheduleList: MutableList<ScheduleModel>,
    private val dbHelper: DatabaseHelper,
    private val userId: Long
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    init {
        val weekdayOrder = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

        // Sort scheduleList to ensure Monday appears first and Sunday last
        scheduleList.sortBy { weekdayOrder.indexOf(it.weekday) }
    }

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weekdayText: TextView = view.findViewById(R.id.spinnerWeekday_dis)
        val deleteButton: Button = view.findViewById(R.id.delete_sched_btn)
        val scheduleContainer: LinearLayout = view.findViewById(R.id.schedule_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val scheduleModel = scheduleList[position]

        // Set weekday text
        holder.weekdayText.text = scheduleModel.weekday

        // Clear previous views to avoid duplication
        holder.scheduleContainer.removeAllViews()

        val inflater = LayoutInflater.from(holder.itemView.context)

        // Sort schedules inside each weekday by AM first, PM last
        val sortedSchedules = scheduleModel.schedules.sortedBy { it.time1.contains("PM") }

        // Loop through sorted schedules and display them correctly
        for (schedule in sortedSchedules) {
            val scheduleView = inflater.inflate(R.layout.additional_schedule_item, holder.scheduleContainer, false)

            val timeTextView = scheduleView.findViewById<TextView>(R.id.Time_sched_dis)
            val timeTextView2 = scheduleView.findViewById<TextView>(R.id.Time_sched_dis2)
            val subjectTextView = scheduleView.findViewById<TextView>(R.id.Subject_sched_dis)

            // Set time and subject
            timeTextView.text = schedule.time1
            timeTextView2.text = schedule.time2
            subjectTextView.text = schedule.subject

            holder.scheduleContainer.addView(scheduleView)
        }

        // Handle delete button
        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Schedule")
                .setMessage("Are you sure you want to delete this schedule?")
                .setPositiveButton("Yes") { _, _ ->
                    if (scheduleModel.schedules.isNotEmpty()) {
                        val success = dbHelper.deleteSchedule(
                            scheduleModel.schedules[0].subject,
                            scheduleModel.weekday,
                            scheduleModel.schedules[0].time1,
                            scheduleModel.schedules[0].time2,
                            userId
                        )
                        if (success) {
                            scheduleList.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, scheduleList.size)
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount() = scheduleList.size
}
