package com.example.sudokuapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.sudokuapp.R
import kotlinx.android.synthetic.main.activity_menu_sudoku.*

class MenuSudokuActivity : AppCompatActivity() {

    private lateinit var buttons: List<Button>
    private val listOfLevels = listOf(Pair(0, "easy"), Pair(1,"medium"),Pair(2, "hard"), Pair(3, "random"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_sudoku)

        buttons = listOf(easy_bt, medium_bt, hard_bt, random_bt)
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val intent = Intent(applicationContext, PlaySudokuActivity::class.java).apply {
                    putExtra("lvl", listOfLevels[index].second)
                }
                startActivity(intent)
            }

        }
    }




}