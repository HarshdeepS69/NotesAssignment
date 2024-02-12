package com.harsh.todoassignment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material3.DatePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.harsh.todoassignment.NoteDataBase.Companion.noteDatabase
import com.harsh.todoassignment.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var noteEntityList = arrayListOf<NoteEntity>()
    var baseAdapter: BaseAdapterClass = BaseAdapterClass(noteEntityList)
    lateinit var noteDataBase: NoteDataBase
    var calendar = Calendar.getInstance()
    var simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy, hh:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDataBase = NoteDataBase.getDataBaseInstance(this)
        getDataBaseValues()
        binding.lvListView.adapter = baseAdapter

        binding.rbAll.setChecked(true)


        binding.lvListView.setOnItemClickListener { adapterView, view, position, l ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle(noteEntityList[position].title.toString())
            builder.setPositiveButton("Delete") { _, _ ->
                noteDataBase.noteDao().deleteNoteEntities(noteEntityList[position])


                getDataBaseValues()
            }.setNeutralButton("Edit") { _, _ ->
                showEditDialog(position)
            }.show()


        }


        getDataBaseValues()

    }

    fun showEditDialog(position: Int) {
        val currentTime = Calendar.getInstance().time

        val noteDateTime = "${noteEntityList[position].date} ${noteEntityList[position].time}"
        val noteTime = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(noteDateTime)

        val timeDifference = abs(currentTime.time - noteTime.time)

        val minutesDifference = timeDifference / (60 * 1000)

        if (minutesDifference <= 15) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_note_editor)
            val edtTitleEdit = dialog.findViewById<EditText>(R.id.edtTitleEdit)
            val edtDescriptionEdit = dialog.findViewById<EditText>(R.id.edtDescriptionEdit)
            val btnUpdateEdit = dialog.findViewById<Button>(R.id.btn_Update_Edit)
            btnUpdateEdit.setOnClickListener {
                val editedTitle = edtTitleEdit.text.toString()
                val editedDescription = edtDescriptionEdit.text.toString()

                if (editedTitle.isEmpty()) {
                    edtTitleEdit.error = "Enter Title"
                } else if (editedDescription.isEmpty()) {
                    edtDescriptionEdit.error = "Enter Description"
                } else {
                    val updatedNote = NoteEntity(
                        id = noteEntityList[position].id,
                        title = editedTitle,
                        description = editedDescription,
                        priority = noteEntityList[position].priority,
                        date = noteEntityList[position].date,
                        time = noteEntityList[position].time
                    )
                    noteDataBase.noteDao().updateNoteEntities(updatedNote)
                    Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    getDataBaseValues()
                }
            }



            edtTitleEdit.setText(noteEntityList[position].title)
            edtDescriptionEdit.setText(noteEntityList[position].description)



            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.show()
        } else {
            Toast.makeText(this, "You can only edit notes created within the last 15 minutes.", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_Add -> callDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun callDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.custom_dialog)
        val edtTitle = dialog.findViewById<EditText>(R.id.edtTitle)
        val edtDescription = dialog.findViewById<EditText>(R.id.edtDescription)
        val add = dialog.findViewById<Button>(R.id.btnAdd)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.rg_priority)
        val edtDate = dialog.findViewById<EditText>(R.id.edtDate)
        val edtTime = dialog.findViewById<EditText>(R.id.edtTime)

        edtDate?.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    edtDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        edtTime?.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val selectedTime = "$hourOfDay:$minute"
                    edtTime.setText(selectedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePicker.show()
        }

        add?.setOnClickListener() {
            val selectedRadioButtonId = radioGroup?.checkedRadioButtonId
            val priorityRg = when (selectedRadioButtonId) {
                R.id.rbHighDialog -> 1
                R.id.rbMedDialog -> 2
                R.id.rbLowDialog -> 3
                else -> -1
            }

            if (edtTitle?.text.toString().isEmpty()) {
                edtTitle?.error = "Enter Text"
            } else if (edtDescription?.text.toString().isEmpty()) {
                edtDescription?.error = "Enter Text"
            } else if (priorityRg != -1) {
                val date = edtDate?.text.toString()
                val time = edtTime?.text.toString()

                val dateTime = SimpleDateFormat("dd/MM/yyyy HH:mm").parse("$date $time")

                val newNote = NoteEntity(
                    title = edtTitle?.text.toString(),
                    description = edtDescription?.text.toString(),
                    priority = priorityRg
                ).apply {
                    setDateAndTime(dateTime)
                }

                noteDataBase.noteDao().insertNote(newNote)
                getDataBaseValues()
            } else {
                Toast.makeText(this, "Select Priority", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        dialog.show()
        getDataBaseValues()
    }


    fun getDataBaseValues() {
        val selectedRadioButtonId = binding.rgPriorityMain.checkedRadioButtonId

        noteEntityList.clear()
        if (selectedRadioButtonId == R.id.rbAll) {
            noteEntityList.addAll(noteDatabase?.noteDao()?.getNoteEntities() as ArrayList<NoteEntity>)
        } else {
            var priority = when(selectedRadioButtonId){
                R.id.rbHigh-> 1
                R.id.rbMed-> 2
               else-> 3
            }
            noteEntityList.addAll(noteDatabase?.noteDao()?.getNoteEntitiesPriorityBased(priority) as ArrayList<NoteEntity>)

        }
        baseAdapter.notifyDataSetChanged()
    }




}
