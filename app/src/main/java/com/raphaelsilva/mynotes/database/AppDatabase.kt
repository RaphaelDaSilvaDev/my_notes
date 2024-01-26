package com.raphaelsilva.mynotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raphaelsilva.mynotes.database.dao.NoteDao
import com.raphaelsilva.mynotes.database.migrations.MIGRATION_1_2
import com.raphaelsilva.mynotes.database.migrations.MIGRATION_2_3
import com.raphaelsilva.mynotes.database.migrations.MIGRATION_3_4
import com.raphaelsilva.mynotes.model.Note

@Database(version = 4, entities = [Note::class], exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object{
        @Volatile
        private lateinit var db: AppDatabase

        fun instance(context: Context): AppDatabase{
            if(::db.isInitialized) return db
            return Room.databaseBuilder(context, AppDatabase::class.java, "notes.db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build().also {
                    db = it
                }
        }
    }
}