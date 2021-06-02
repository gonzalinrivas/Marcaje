package com.example.listadofichajes.Marcaje

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MarcajesEntity::class),version=3)//INCREMENTAR CADA VEZ Q SE MODIFIQUE LA ENTITY!!!!!!!!
abstract class MarcajesDatabase:RoomDatabase() {

    abstract fun marcajeDao(): MarcajeDao
}