package com.example.fobutry

data class ScheduleModel(
    val weekday: String,
    val schedules: List<ScheduleDetail> // This must exist
)

data class ScheduleDetail(
    val subject: String,
    val time1: String,
    val time2: String

)


