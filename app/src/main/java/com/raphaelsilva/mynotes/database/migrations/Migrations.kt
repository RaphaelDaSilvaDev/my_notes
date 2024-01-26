package com.raphaelsilva.mynotes.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Note` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `image` TEXT, `color` INTEGER, PRIMARY KEY(`id`))")
    }
}

val MIGRATION_2_3 = object : Migration(2,3){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Note ADD synchronize INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_3_4 = object : Migration(3,4){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE Note ADD disabled INTEGER NOT NULL DEFAULT 0")
    }
}