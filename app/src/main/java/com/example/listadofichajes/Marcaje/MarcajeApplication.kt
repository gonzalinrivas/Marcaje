package com.example.listadofichajes.Marcaje

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MarcajeApplication : Application() {
    companion object {
        lateinit var database: MarcajesDatabase
    }

    override fun onCreate() {
        super.onCreate()

//--------CAMBIAR CADA VEZ QUE SE ACTUALICE LA ENTITY(PASAR DE LA VERSION ACTUAL A LA NUEVA)--------
        val MIGRATION_2_3 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE MarcajesEntity ADD COLUMN photoUrl TEXT NOT NULL DEFAULT''")
            }
        }

        database = Room.databaseBuilder(
            this, MarcajesDatabase::class.java,
            "MarcajesDatabase"
        )
            .addMigrations(MIGRATION_2_3)
            .build()
    }
}