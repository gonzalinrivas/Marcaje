package com.example.listadofichajes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_registro.*
import java.util.*

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        btnRegistro.setOnClickListener {
            val intent=Intent(this,Login::class.java)
            startActivity(intent)
        }

        tvLogin.setOnClickListener {
            Log.d("Registro", "Intenta mostrar el Login Activity")

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        btnImg.setOnClickListener {
            Log.d("Registro", "Elige foto")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Registro", "Foto seleccionada")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            imageView.setImageBitmap(bitmap)
            btnImg.alpha = 0f

            /* val bitmapDrawable = BitmapDrawable(bitmap)
             btnImg.setBackgroundDrawable(bitmapDrawable)*/
        }

    }

    private fun performRegister() {
        val usuario = etUsuario.text.toString()
        val password = etPassword.text.toString()

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Introduce los campos requeridos", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("Registro", "Usuario es: " + usuario)
        Log.d("Registro", "Password: $password")


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if succesful
                Log.d("Registro", "Bien creado usuario con uid: ${it.result?.user?.uid}")


            }
            .addOnFailureListener {
                Log.d("Registro", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }


}