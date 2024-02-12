package com.harsh.todoassignment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao()
interface NoteDao {
    @Insert
    fun insertNote(noteEntitiy: NoteEntity)

    @Query("Select * from NoteEntity")
    fun getNoteEntities() : List<NoteEntity>

    @Query("Select * from NoteEntity where priority = :priority")
    fun getNoteEntitiesPriorityBased(priority : Int) : List<NoteEntity>

    @Delete
    fun deleteNoteEntities(noteEntity: NoteEntity)
    @Update
    fun updateNoteEntities(noteEntity: NoteEntity)

}