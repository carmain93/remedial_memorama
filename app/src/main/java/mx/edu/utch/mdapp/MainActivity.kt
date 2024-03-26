package mx.edu.utch.mdapp


import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import mx.edu.utch.mdapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)

        binding.fabPrincipal.setOnClickListener{
            Toast.makeText(this,"Gone", Toast.LENGTH_LONG).show()
        }


        setSupportActionBar(binding.mainBottomAppBar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_1 -> {
                newGame()
                true
            }
            R.id.option_2 -> {
                showHelp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main_activity , menu)
        return true
    }
    private fun newGame() {
        Log.d("Click","opcion 1")
    }

    private fun showHelp() {
        Log.d("Click","Opcion 2")
    }


}