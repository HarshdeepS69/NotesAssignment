package com.harsh.todoassignment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDataBase : RoomDatabase(){
    abstract fun noteDao() : NoteDao
    companion object{
        var noteDatabase : NoteDataBase ?= null

        @Synchronized
        fun getDataBaseInstance(context: Context) : NoteDataBase{
            if (noteDatabase == null){
                noteDatabase = Room.databaseBuilder(context,
                    NoteDataBase::class.java,context.resources.getString(R.string.app_name)
                ).allowMainThreadQueries()
                    .build()

            }
            return noteDatabase!!
        }
    }
}