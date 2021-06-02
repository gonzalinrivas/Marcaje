package com.example.listadofichajes.Marcaje

interface OnClickListener {
   fun onClick(marcajesId: Long)
   fun onFavoriteMarcaje(marcajesEntity: MarcajesEntity)
   fun onDeleteMarcaje(marcajesEntity: MarcajesEntity)
}