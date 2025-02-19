package com.example.fobutry

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class AddSchedule(private val userId: Long, private val onScheduleAdded: () -> Unit) : BottomSheetDialogFragment() {

    private lateinit var additionalScheduleContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weekdaySpinner: Spinner = view.findViewById(R.id.spinnerWeekday_sched)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.weekdays_array_schedule,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weekdaySpinner.adapter = adapter

        val timeEditText: EditText = view.findViewById(R.id.editTime_sched)
        val timeEditText2: EditText = view.findViewById(R.id.editTime_sched2)
        val subjectEditText: EditText = view.findViewById(R.id.editSubject_sched)
        val saveButton: Button = view.findViewById(R.id.btnSaveSchedule)
        val addScheduleTextView: TextView = view.findViewById(R.id.add_schedule)

        additionalScheduleContainer = view.findViewById(R.id.additional_schedule_container)

        // ✅ Set up time picker for both time fields
        timeEditText.setOnClickListener { showTimePicker(timeEditText) }
        timeEditText2.setOnClickListener { showTimePicker(timeEditText2) }

        // ✅ Dynamically add schedules
        addScheduleTextView.setOnClickListener {
            val inflater = LayoutInflater.from(requireContext())
            val additionalScheduleView = inflater.inflate(R.layout.additional_schedule, additionalScheduleContainer, false)

            val additionalTimeEditText: EditText = additionalScheduleView.findViewById(R.id.additional_editTime_sched)
            val additionalTimeEditText2: EditText = additionalScheduleView.findViewById(R.id.additional_editTime_sched2)
            val additionalSubjectEditText: EditText = additionalScheduleView.findViewById(R.id.additional_editSubject_sched)

            additionalTimeEditText.setOnClickListener { showTimePicker(additionalTimeEditText) }
            additionalTimeEditText2.setOnClickListener { showTimePicker(additionalTimeEditText2) }

            additionalScheduleContainer.addView(additionalScheduleView)
        }

        saveButton.setOnClickListener {
            val weekday = weekdaySpinner.selectedItem.toString()
            val time1 = timeEditText.text.toString()
            val time2 = timeEditText2.text.toString()
            val subject = subjectEditText.text.toString()

            val dbHelper = DatabaseHelper(requireContext())

            val schedules = mutableListOf<Triple<String, String, String>>() // List of (subject, time1, time2)

            // ✅ Collect the main schedule entry
            if (time1.isNotEmpty() && time2.isNotEmpty() && subject.isNotEmpty()) {
                schedules.add(Triple(subject, time1, time2))
            }

            // ✅ Loop through dynamically added schedules and collect them
            for (i in 0 until additionalScheduleContainer.childCount) {
                val view = additionalScheduleContainer.getChildAt(i)
                val additionalTime1 = view.findViewById<EditText>(R.id.additional_editTime_sched)?.text.toString()
                val additionalTime2 = view.findViewById<EditText>(R.id.additional_editTime_sched2)?.text.toString()
                val additionalSubject = view.findViewById<EditText>(R.id.additional_editSubject_sched)?.text.toString()

                if (additionalTime1.isNotEmpty() && additionalTime2.isNotEmpty() && additionalSubject.isNotEmpty()) {
                    schedules.add(Triple(additionalSubject, additionalTime1, additionalTime2))
                }
            }

            // ✅ Sort AM schedules first, PM schedules last
            schedules.sortBy { it.second.contains("PM") }

            var success = true
            for ((subj, t1, t2) in schedules) {
                success = success && dbHelper.addClassSchedule(subj, weekday, t1, t2, userId)
            }

            dbHelper.close()

            if (success) {
                Toast.makeText(requireContext(), "Schedule saved successfully!", Toast.LENGTH_SHORT).show()
                onScheduleAdded() // Refresh RecyclerView
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Failed to save schedule.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ Fix time picker function to always update editText correctly
    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val isPM = selectedHour >= 12
                val amPm = if (isPM) "PM" else "AM"
                val hourFormatted = if (selectedHour % 12 == 0) 12 else selectedHour % 12
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hourFormatted, selectedMinute, amPm)

                // ✅ Set the time in the correct field
                editText.setText(formattedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }
}
