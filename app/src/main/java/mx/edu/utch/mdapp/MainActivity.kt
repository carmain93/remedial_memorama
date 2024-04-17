
package mx.edu.utch.mdapp

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.edu.utch.mdapp.databinding.ActivityMainBinding
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private var clicked: Boolean = true
    private var turno: Boolean = true
    private var gameFinished: Boolean = false
    private var finalquest= true

    private var first_card: ImageView? = null
    private var first_image: Int? = null

    private var cont: Int =0
    private var score1: Int = 0
    private var score2: Int = 0
    private lateinit var fab: FloatingActionButton



    private var deck = ArrayList<Int>(
        listOf(
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow,
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow
        )
    )

    private var images: ArrayList<ImageView>? = null
    private var binding: ActivityMainBinding? = null
    private var scoreViewPlayer1: TextView? = null
    private var scoreViewPlayer2: TextView? = null
    private  var cturn: TextView?=null
    private var cerror: TextView?=null
    private var err: Int=0
    private var t:Int=0
    private  var help1:Boolean=true
    private  var help2:Boolean=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        images = ArrayList<ImageView>(
            listOf(

                binding!!.gameZone.im11,
                binding!!.gameZone.im12,
                binding!!.gameZone.im13,
                binding!!.gameZone.im21,
                binding!!.gameZone.im22,
                binding!!.gameZone.im23,
                binding!!.gameZone.im31,
                binding!!.gameZone.im32,
                binding!!.gameZone.im33,
                binding!!.gameZone.im41,
                binding!!.gameZone.im42,
                binding!!.gameZone.im43
            )
        )

        scoreViewPlayer1 = binding!!.scoreZone.mainActivityTvPlayer1
        scoreViewPlayer2 = binding!!.scoreZone.mainActivityTvPlayer2
        cerror =binding!!.gameZone.errorCounter
        cturn=binding!!.gameZone.turnCounter

        fab = findViewById(R.id.fabPrincipal)
        //inicio del boton de
        fab.setOnClickListener {tiemsg()}
        Collections.shuffle(deck)
        startOn()
        clickOn()

        setSupportActionBar(binding!!.mainBottomAppBar)
    }
    private fun tiemsg(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("¡juego teminado!")
        alertDialogBuilder.setMessage("jufador uno : ${score1}, jugador dos :$score2")
        alertDialogBuilder.setPositiveButton("Reiniciar"){
                _, _ ->
            resetGame()
        }
        alertDialogBuilder.setNegativeButton("Salir") { dialog, which ->
            finish()
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }

    private fun startOn() {
        if (turno) {
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.GREEN)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.TRANSPARENT)
        } else {
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.TRANSPARENT)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.GREEN)
        }
        binding!!.scoreZone.mainActivityTvPlayer1.setTypeface(null, Typeface.BOLD_ITALIC)
        binding!!.scoreZone.mainActivityTvPlayer2.setTypeface(null, Typeface.BOLD_ITALIC)
    }

    private fun clickOn() {
        for (i in images!!.indices) {
            images!![i]!!.setOnClickListener {
                images!![i]!!.setImageResource(deck[i])
                saveClick(images!![i]!!, deck[i])
            }
        }
        Thread.sleep(3000)
    }
    ///
    private fun saveClick(img: ImageView, card: Int) {
        if (clicked) {
            first_card = img
            first_image = card
            first_card!!.isEnabled = false
            clicked = !clicked
        } else {
            xtivate(false)
            var handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (card == first_image) {
                    first_card!!.isVisible = false
                    img.isVisible = false
                    if (turno) {
                        score1++
                        cont++
                        if (cont ==5 && finalquest==true){
                            reme()
                        }
                        if (cont ==6 && score1==score2){showWinnerDialog("ubo un empate que bien")}
                        else if (cont==6){showWinnerDialog("el jugador uno es el ganador")}
                    } else {
                        score2++
                        cont++
                        if (cont ==5 && finalquest==true){
                            reme()
                        }
                        if (cont ==6 && score1==score2){showWinnerDialog("ubo un empate que bien")}
                        else if (cont==6){showWinnerDialog("el jugador dos es el ganador")}
                    }
                    startOn()
                    xtivate(true)
                } else {
                    err++
                    first_card!!.setImageResource(R.drawable.reverso)
                    img.setImageResource(R.drawable.reverso)
                    first_card!!.isEnabled = true
                    turno = !turno
                    startOn()
                    xtivate(true)
                }
                t++
                updateScores()
                checkGameFinished()
            }, 1000)//se altero de 2000 para poder checar mas rapido
            clicked = !clicked
        }

    }
    //funcion que intenta englobar lo pedido en el remedial
    private fun reme(){
        var msj:String=""
        if (score1>score2){
            msj="El jugador uno ya casi gana"
        }else{
            msj="El jugador dos ya casi gana"
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(msj)
        alertDialogBuilder.setMessage("¿que desea hacer?")
        alertDialogBuilder.setPositiveButton("terminar el juego"){
                _, _ ->
            if (msj=="El jugador uno ya casi gana"){
                if (turno){score1++}else{score2++}
                msj="uno"

            }else{
                if (turno){score1++}else{score2++}
                msj="dos"

            }
            updateScores()

            cont++
            revealAndHideCards()
            if (cont ==6 && score1==score2){showWinnerDialog("ubo un empate que bien")}
            else if (cont==6){showWinnerDialog("el jugador ${msj} es el ganador")}
        }
        alertDialogBuilder.setNegativeButton("segir") { dialog, which ->
            /*
            if (msj=="El jugador uno ya casi gana"){
                score1++
                msj="uno"
                updateScores()
            }else{score2++
                msj="dos"
                updateScores()
            }


            cont++
            revealAndHideCards()
            if (cont ==6 && score1==score2){showWinnerDialog("ubo un empate que bien")}
            else if (cont==6){showWinnerDialog("el jugador ${msj} es el ganador")}
            * */
            finalquest=false
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }




    ///
    private fun xtivate(b: Boolean) {
        for (i in images!!.indices) {
            images!![i]!!.isEnabled = b
        }
    }

    private fun updateScores() {
        scoreViewPlayer1?.text = "Jugador 1: $score1"
        scoreViewPlayer2?.text = "Jugador 2: $score2"
        cerror?.text = "Errores:$err "
        cturn?.text = "Turnos:$t "
    }

    private fun checkGameFinished() {
        val allMatched = deck.none { it != 0 }
        if (allMatched && !gameFinished) {
            gameFinished = true
            showGameResultDialog()
        }
    }

    private fun showGameResultDialog() {
        val winner = if (score1 > score2) "Jugador 1" else if (score1 < score2) "Jugador 2" else "Empate"
        val message = if (winner == "Empate") "¡Es un empate!" else "¡$winner gana!"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Fin del juego!")
            .setMessage(message)
            .setPositiveButton("Reiniciar") { _, _ ->
                resetGame()
            }
            .setNegativeButton("Salir") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun clickVisibleCards() {
        for (i in images!!.indices) {
            if (images!![i]!!.isVisible) {
                images!![i]!!.performClick()
            }
        }
        //Thread.sleep(4000)

    }
    private fun resetGame() {
        for (i in (0..<images!!.size)){
            images!![i]!!.isVisible = true
            images!![i]!!.setImageResource(R.drawable.reverso)
        }
        clickOn()
        xtivate(true)
        clicked = true
        turno = true
        cont = 0
        score1 = 0
        score2 = 0
        err=0
        t=0
        help1=true
        help2=true
        finalquest=true
        updateScores()
        startOn()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_1 -> {



                resetGame()

                true
            }
            R.id.option_2 -> {
                // Mostrar el diálogo de juego terminado
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("¡juego terminado!")
                alertDialogBuilder.setMessage("Jugador uno: $score1, Jugador dos: $score2")
                alertDialogBuilder.setNegativeButton("Salir") { dialog, which ->
                    finish()
                }
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.show()
                true
            }
            R.id.option_3 -> {
                // Salir del juego
                finish()
                true
            }
            R.id.option_4->{
                if (turno){
                    if (help1){
                        revealAndHideCards()
                        help1=false
                    }else{
                        negar()
                    }
                }else{
                    if (help2){
                        revealAndHideCards()
                        help2=false
                    }else{
                        negar()
                    }
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun revealAndHideCards() {

        for (i in images!!.indices) {
            images!![i]!!.setImageResource(deck[i])
        }
        Handler(Looper.getMainLooper()).postDelayed({

            for (i in images!!.indices) {
                images!![i]!!.setImageResource(R.drawable.reverso)
            }
        }, 2000)
    }
private fun negar(){
    val alertDialogBuilder = AlertDialog.Builder(this)
    alertDialogBuilder.setTitle("Usted ya a consumido su ayuda ya no tiene mas")
    alertDialogBuilder.setMessage("ya a consumido su ayuda en este juego no puede tene otra")

    alertDialogBuilder.setNegativeButton("Salir") { dialog, which ->

    }
    alertDialogBuilder.setCancelable(false)
    alertDialogBuilder.show()
}

    //funcion para el final
    private fun showWinnerDialog(winner: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("¡$winner ganaste!")
        alertDialogBuilder.setMessage("¿Qué quieres hacer a continuación?")
        alertDialogBuilder.setPositiveButton("Reiniciar") { dialog, which ->
            resetGame()
        }
        alertDialogBuilder.setNegativeButton("Salir") { dialog, which ->
            finish()
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)

        return true
    }


}