package com.example.listadofichajes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {

            val intent= Intent(this,PantallaPrincipal::class.java)
             startActivity(intent)
        }
        tvBackRegister.setOnClickListener {
            val intent=Intent(this,Registro::class.java)
            startActivity(intent)

        }


    }

    private fun performLogin() {
        val usuario = etUsuario.text.toString()
        val password = etPassword.text.toString()


        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa con datos", Toast.LENGTH_SHORT).show()
            return
        }


        val intent = Intent(this, PantallaPrincipal::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}