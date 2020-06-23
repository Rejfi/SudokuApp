package com.example.sudokuapp.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.sudokuapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_menu_sudoku.*


class MenuSudokuActivity : AppCompatActivity() {

    private lateinit var buttons: List<Button>
    private val listOfLevels = listOf(Pair(0, "easy"), Pair(1,"medium"),Pair(2, "hard"), Pair(3, "random"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_sudoku)
        val wifi = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        buttons = listOf(easy_bt, medium_bt, hard_bt, random_bt)
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val intent = Intent(applicationContext, PlaySudokuActivity::class.java).apply {
                    putExtra("lvl", listOfLevels[index].second)
                }
                if(isConnected())
                    startActivity(intent)
                else
                    Snackbar.make(gameMenu, "Ehkem... Could you turn on internet?", Snackbar.LENGTH_LONG).show()
            }

        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }


}