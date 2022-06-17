package com.surajrathod.bcaprogram.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.surajrathod.bcaprogram.model.ProgramEntity


@Database(entities = [ProgramEntity::class], version = 1)
abstract class ProgramDatabase : RoomDatabase() {





    abstract fun programDao() : ProgramDao

    companion object{

        @Volatile
        private var INSTANCE : ProgramDatabase? = null

        fun getDatabase(context: Context) : ProgramDatabase{

            if(INSTANCE==null)
            {
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ProgramDatabase::class.java,
                        "Program_db"
                    ).build()
                }
            }

            return INSTANCE!!
        }
    }

}