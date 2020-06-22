package com.example.sudokuapp.network

import com.example.sudokuapp.data.SudokuBoard
import retrofit2.Response
import java.io.IOException

object SudokuRepository {
    private val sudokuApi = SudokuClient.instance

    @Throws(IOException::class)
   suspend fun getSudokuBoard(diff: String): Response<SudokuBoard> {
        return sudokuApi.getSudokuBoard(diff).await()
    }

}