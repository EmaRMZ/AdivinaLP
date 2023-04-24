package com.emarmz.adivinalp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.emarmz.adivinalp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var laPalabraEs =""
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            if (laPalabraEs != "Facil" && laPalabraEs != "Intermedio" && laPalabraEs != "Dificil") {
                Toast.makeText(applicationContext, "Selecciona una Dificultad", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("es", laPalabraEs)
                startActivity(intent)
            }
        }
        binding.btnDificultad.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_dificultad)
            val btnOk = dialog.findViewById(R.id.btn_dialog_ok) as Button
            val btnFacil = dialog.findViewById(R.id.btn_facil) as Button
            val btnInter = dialog.findViewById(R.id.btn_intermedio) as Button
            val btnDificil = dialog.findViewById(R.id.btn_dificil) as Button
            btnFacil.setOnClickListener {
                btnFacil.setBackgroundColor(Color.RED)
                btnInter.setBackgroundColor(Color.GRAY)
                btnDificil.setBackgroundColor(Color.GRAY)
                laPalabraEs = "Facil"
            }
            btnInter.setOnClickListener {
                btnFacil.setBackgroundColor(Color.GRAY)
                btnInter.setBackgroundColor(Color.RED)
                btnDificil.setBackgroundColor(Color.GRAY)
                laPalabraEs = "Intermedio"
            }
            btnDificil.setOnClickListener {
                btnFacil.setBackgroundColor(Color.GRAY)
                btnInter.setBackgroundColor(Color.GRAY)
                btnDificil.setBackgroundColor(Color.RED)
                laPalabraEs = "Dificil"
            }
            btnOk.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.btnInfo.setOnClickListener {
            val dialog1 = Dialog(this)
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog1.setCancelable(false)
            dialog1.setContentView(R.layout.info_dialog)
            dialog1.setCanceledOnTouchOutside(true)
            dialog1.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog1.show()
        }
    }
}