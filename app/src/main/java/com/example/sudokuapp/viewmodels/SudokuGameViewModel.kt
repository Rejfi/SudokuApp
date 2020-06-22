package com.example.sudokuapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudokuapp.data.Board
import com.example.sudokuapp.data.Cell
import com.example.sudokuapp.data.SudokuBoard
import com.example.sudokuapp.network.SudokuRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

class SudokuGameViewModel: ViewModel() {

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    var isGameStarted: Boolean = false

    private var selectedRow = -1
    private var selectedCol = -1

    private  var board: Board? = null

    init {
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
    }

     fun fetchSudokuBoard(diff: String){
        CoroutineScope(viewModelScope.coroutineContext).launch {
            val sudoku: Response<SudokuBoard>? = SudokuRepository.getSudokuBoard(diff)
            if(sudoku != null && sudoku.isSuccessful){
                val sudokuFilledBoard = convertResponse(sudoku.body())
                if(!sudokuFilledBoard.isNullOrEmpty()){
                    cellsLiveData.value = convertResponse(sudoku.body())
                    board = Board(9, cellsLiveData.value!!)
                    Log.d("FETCH_DEBUG", sudoku.body().toString())
                }
            }else board = Board(9, emptyList())
        }

    }

    private fun convertResponse(sudokuBoard: SudokuBoard?): List<Cell> {
        if(sudokuBoard != null){
            val array = ArrayList<Cell>()
            for(i in 0 until 9){
                for (j in 0 until 9){
                    val cell = Cell(i, j, sudokuBoard.board[i][j], sudokuBoard.board[i][j] != 0)
                    Log.d("CELL_VAL_DEBUG", "[${cell.row},${cell.col}]:: ${cell.value}")
                    array.add(cell)
                }
            }
            return array.toList()
        }
        return emptyList()
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = board!!.getCell(selectedRow, selectedCol)
        if (cell.isStartingCell) return

        cell.value = number

        cellsLiveData.postValue(board!!.cells)
    }
    fun updateSelectedCell(row: Int, col: Int) {
        val cell = board!!.getCell(row, col)
        if (!cell.isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))
        }
    }
    fun delete() {
        val cell = board!!.getCell(selectedRow, selectedCol)
        cell.value = 0
        cellsLiveData.postValue(board!!.cells)
    }

    fun checkSudoku(): Boolean {
        val array= Array(9, {IntArray(9)})
        val cell = cellsLiveData.value
            cell!!.forEach {
                Log.d("CHECK_DEBUG", "[${it.row}, ${it.col}]::${it.value}")
                array[it.row][it.col] = it.value
            }
       /*
        for (x in 0 until 9){
            for(y in 0 until 9){
                Log.d("CHECK_DEBUG", "[$x,$y] = ${array[x][y]}")
            }
        }

        */
        Log.d("CHECK_DEBUG", checkRowsColumnsSectors(array).toString())

        return checkRowsColumnsSectors(array)
        }

    private fun checkRowsColumnsSectors(array: Array<IntArray>): Boolean {
        val arrayOfSums = IntArray(27) { i -> i*0}
        for (x in 0 until 9){
            for(y in 0 until 9){
                val value = array[x][y]
                if(value < 1 || value > 9) return false
                arrayOfSums[x] += value
                arrayOfSums[y+9] += value
                arrayOfSums[(x/3 + (y/3) * 3) + 18] += value
            }
        }
        for (i in 0 until 27){
            if(arrayOfSums[i] != 45) return false
        }
        return true
    }

    }

