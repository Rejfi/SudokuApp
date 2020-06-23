package com.example.sudokuapp.ui

import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.sudokuapp.R
import com.example.sudokuapp.data.Cell
import com.example.sudokuapp.ui.custom.SudokuBoardView
import com.example.sudokuapp.viewmodels.SudokuGameViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_play_sudoku.*

class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel: SudokuGameViewModel
    private lateinit var numberButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_sudoku)

        sudokuBoardView.registerListener(this)
        viewModel = ViewModelProvider(this).get(SudokuGameViewModel::class.java)
        intent.getStringExtra("lvl")?.let {
            if(!viewModel.isGameStarted) {
                viewModel.fetchSudokuBoard(it)
                viewModel.isGameStarted = true
            }
        }

        viewModel.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it)})
        viewModel.cellsLiveData.observe(this, Observer { updateCells(it) })

        numberButtons = listOf(oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton,
                sevenButton, eightButton, nineButton)

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.handleInput(index + 1) }
        }

        deleteButton.setOnClickListener { viewModel.delete() }
        checkButton.setOnClickListener {
            if (viewModel.checkSudoku()) {
                winText.text = getString(R.string.win_text)
                winText.visibility = View.VISIBLE
            } else {
                Snackbar.make(gameLayout, "Are you sure? Check again", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.updateSelectedCell(row, col)
    }


}
