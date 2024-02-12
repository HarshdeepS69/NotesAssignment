package com.harsh.todoassignment

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var title : String ?= null,
    var description :String ?= null,
    var priority : Int ?= null,
    var date : String ?= null,
    var time : String ?= null

)
{
    companion object{
        // Define format patterns for date and time
        private const val DATE_FORMAT_PATTERN = "dd/MM/yyyy"
        private const val TIME_FORMAT_PATTERN = "HH:mm"
    }

    // Function to format date and time strings
    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN)
        return dateFormat.format(date)
    }

    private fun formatTime(date: Date): String {
        val timeFormat = SimpleDateFormat(TIME_FORMAT_PATTERN)
        return timeFormat.format(date)
    }

    // Function to set the date and time fields
    fun setDateAndTime(date: Date) {
        this.date = formatDate(date)
        this.time = formatTime(date)
    }
    }

