package com.example.fictional_octo_waddle

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fictional_octo_waddle.databinding.ActivityLogInBinding
import com.example.fictional_octo_waddle.databinding.LogInDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LogInActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogInBinding
    lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.login.setOnClickListener {
            logindialog()
        }
    }
    fun logindialog() {
        val binding = LogInDialogBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this)
            .setView(binding.root)
            .setPositiveButton("Log In") { dialog, which ->

            }
            .setNegativeButton("Cancel") { dialog, which ->

            }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val steamid = binding.text.editText!!.text.toString()
            if (steamid == "") {
                binding.text.error = "Error"
            } else {
                session = Session(this)
                session.set(steamid)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}