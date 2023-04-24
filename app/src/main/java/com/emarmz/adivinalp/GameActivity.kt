package com.emarmz.adivinalp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.view.get
import com.emarmz.adivinalp.databinding.ActivityGameBinding
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var palabra:String
    private lateinit var letraAux:String
    private lateinit var palabraAux:String
    private var auxRandomIndex:Int = 0
    private lateinit var chars:CharArray
    private var intentos = 10
    private lateinit var casillaSel:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectPalabra()
        startGame()
        showIntentos()

    }
    private fun selectPalabra() {
        val palabrasFacil:List<String> = listOf(
            "abeto", "balas", "bueno", "campo", "clavo", "datos", "dados", "enojo", "error", "feria", "fallo", "grito", "gafas",
            "hielo", "ideas", "islas", "julio", "japon", "marca", "monte", "nadar", "nubes", "ondas", "opera", "perro", "pilas",
            "reloj", "remar", "selva", "subir", "tenis", "tabla", "velas", "volar", "yemas", "zorro", "zurdo"
        )
        val palabrasIntermedio:List<String> = listOf(
            "arrojar", "ajustes", "botella", "ballena", "caldera", "cerrojo", "dolares", "domingo", "ecuador", "español", "flechas", "formula",
            "galones", "gigante", "huertos", "hogares", "irlanda", "jamaica", "kioscos", "liquido", "marquez", "militar", "nigeria", "numeros",
            "oxigeno", "polonia", "plantas", "radical", "rumania", "silabas", "sedante", "templos", "trafico", "unisono", "vinagre", "voltios"
        )
        val palabrasDificil:List<String> = listOf(
            "argentina", "aleatorio", "bolsillos", "canciones", "colectivo", "documento", "dibujador", "eslovenia", "esdrujula", "ferrovial",
            "finlandia", "garabatos", "glaciares", "holograma", "huracanes", "indonesia", "jabalinas", "kilogramo", "ladrillos", "libelulas",
            "moleculas", "matricula", "naturales", "proteinas", "potencias", "refugiado", "recuerdos", "submarino", "sonambulo", "terricola",
            "trompetas", "universal", "vehiculos", "vulcanico", "xilofonos"
        )
        val cantFacil = palabrasFacil.size-1
        val cantInter = palabrasIntermedio.size-1
        val cantDificil = palabrasDificil.size-1

        if (intent.extras?.getString("es") == "Facil") {
            val randomAux = Random.nextInt(0,cantFacil)
            palabra = palabrasFacil[randomAux].uppercase()
            palabraAux = palabra
            reemplazarChars(2)
        } else if (intent.extras?.getString("es") == "Intermedio") {
            val randomAux = Random.nextInt(0,cantInter)
            palabra = palabrasIntermedio[randomAux].uppercase()
            palabraAux = palabra
            reemplazarChars(4)
        } else if (intent.extras?.getString("es") == "Dificil") {
            val randomAux = Random.nextInt(0,cantDificil)
            palabra = palabrasDificil[randomAux].uppercase()
            palabraAux = palabra
            reemplazarChars(6)
        }
        binding.palabra.text = palabraAux
    }
    private fun reemplazarChars(i: Int) {
        var contAux = 0
        chars = palabra.toCharArray()
        while (contAux<i) {
            val char = '_'
            auxRandomIndex = Random.nextInt(0,chars.size)
            while (chars[auxRandomIndex] != char) {
                chars[auxRandomIndex] = char
                contAux++
            }
            palabraAux = String(chars)
            chars = palabraAux.toCharArray()
        }
    }
    fun onClickABC(view: View) {
        seleccLetra(view as TextView)
        casillaSel = view
    }
    private fun seleccLetra(id: TextView) {
        binding.letraSelecc.text = id.text
        letraAux = id.text.toString()
    }
    private fun startGame() {
        binding.enter.setOnClickListener {
            buscarLetra(letraAux[0])
            casillaSel.setTextColor(Color.RED)
            casillaSel.isClickable = false
            checkWin(chars)
            checkLose()
            showIntentos()
        }
    }
    private fun buscarLetra(buscar: Char) {
        var contAux2 = 0
        var contAux3 = 0
        val searchChar = palabra.toCharArray()
        while (contAux2 < searchChar.size) {
            if (buscar == searchChar[contAux2]) {
                chars[contAux2] = searchChar[contAux2]
                palabraAux = String(chars)
                chars = palabraAux.toCharArray()
            } else {
                contAux3++
            }
            contAux2++
        }
        if (contAux3 == searchChar.size) {
            intentos--
        }
        binding.palabra.text = palabraAux
    }
    private fun checkWin (char: CharArray) {
        var isWinner = false
        var contAux3 = 0
        while (contAux3 < char.size) {
            if (char[contAux3] == '_') {
                break
            } else {
                contAux3++
            }
            if (contAux3 == char.size) {
                isWinner = true
            }
        }
        if (isWinner) {
            weHaveWin()
        }
    }
    private fun weHaveWin () {
        val dialog2 = Dialog(this)
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog2.setCancelable(false)
        dialog2.setContentView(R.layout.dialog_winner)
        dialog2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnPlayAgain = dialog2.findViewById(R.id.playAgain) as Button
        val btnExit = dialog2.findViewById(R.id.exit) as Button
        btnPlayAgain.setOnClickListener {
            resetGame()
            dialog2.dismiss()
        }
        btnExit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            dialog2.dismiss()
        }
        dialog2.show()
        setKonfetti()
    }
    private fun setKonfetti () {
        val viewKonfetti = findViewById<KonfettiView>(R.id.viewKonfetti)
        viewKonfetti.build()
            .addColors(Color.RED, Color.YELLOW, Color.BLUE)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(20))
            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 2000L)
    }
    private fun checkLose() {
        if (intentos == 0) {
            val dialog3 = Dialog(this)
            dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog3.setCancelable(false)
            dialog3.setContentView(R.layout.dialog_loser)
            val btnInicio = dialog3.findViewById(R.id.inicio) as Button
            val btnReintentar = dialog3.findViewById(R.id.reintentar) as Button
            btnReintentar.setOnClickListener {
                resetGame()
                dialog3.dismiss()
            }
            btnInicio.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                dialog3.dismiss()
            }
            dialog3.show()
        }
    }
    private fun resetGame() {
        setCasillasColor()
        selectPalabra()
        intentos = 10
        binding.letraSelecc.text = ""
        startGame()
        showIntentos()
    }
    private fun showIntentos() {
        binding.numIntentos.text = intentos.toString()
    }
    private fun setCasillasColor() {
        binding.imgA.setTextColor(Color.YELLOW)
        binding.imgB.setTextColor(Color.YELLOW)
        binding.imgC.setTextColor(Color.YELLOW)
        binding.imgD.setTextColor(Color.YELLOW)
        binding.imgE.setTextColor(Color.YELLOW)
        binding.imgF.setTextColor(Color.YELLOW)
        binding.imgG.setTextColor(Color.YELLOW)
        binding.imgH.setTextColor(Color.YELLOW)
        binding.imgI.setTextColor(Color.YELLOW)
        binding.imgJ.setTextColor(Color.YELLOW)
        binding.imgK.setTextColor(Color.YELLOW)
        binding.imgL.setTextColor(Color.YELLOW)
        binding.imgM.setTextColor(Color.YELLOW)
        binding.imgN.setTextColor(Color.YELLOW)
        binding.imgEnie.setTextColor(Color.YELLOW)
        binding.imgO.setTextColor(Color.YELLOW)
        binding.imgP.setTextColor(Color.YELLOW)
        binding.imgQ.setTextColor(Color.YELLOW)
        binding.imgR.setTextColor(Color.YELLOW)
        binding.imgS.setTextColor(Color.YELLOW)
        binding.imgT.setTextColor(Color.YELLOW)
        binding.imgU.setTextColor(Color.YELLOW)
        binding.imgV.setTextColor(Color.YELLOW)
        binding.imgW.setTextColor(Color.YELLOW)
        binding.imgX.setTextColor(Color.YELLOW)
        binding.imgY.setTextColor(Color.YELLOW)
        binding.imgZ.setTextColor(Color.YELLOW)
        binding.imgA.isClickable = true
        binding.imgB.isClickable = true
        binding.imgC.isClickable = true
        binding.imgD.isClickable = true
        binding.imgE.isClickable = true
        binding.imgF.isClickable = true
        binding.imgG.isClickable = true
        binding.imgH.isClickable = true
        binding.imgI.isClickable = true
        binding.imgJ.isClickable = true
        binding.imgK.isClickable = true
        binding.imgL.isClickable = true
        binding.imgM.isClickable = true
        binding.imgN.isClickable = true
        binding.imgEnie.isClickable = true
        binding.imgO.isClickable = true
        binding.imgP.isClickable = true
        binding.imgQ.isClickable = true
        binding.imgR.isClickable = true
        binding.imgS.isClickable = true
        binding.imgT.isClickable = true
        binding.imgU.isClickable = true
        binding.imgV.isClickable = true
        binding.imgW.isClickable = true
        binding.imgX.isClickable = true
        binding.imgY.isClickable = true
        binding.imgZ.isClickable = true
    }
}