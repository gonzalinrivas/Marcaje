package com.example.listadofichajes.Marcaje

interface MainAux {
    fun hideFab(isVisible:Boolean=false)
    fun addMarcaje(marcajesEntity: MarcajesEntity)
    fun updateMarcaje(marcajesEntity: MarcajesEntity)
}