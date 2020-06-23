package com.example.sudokuapp.network

import com.example.sudokuapp.data.SudokuBoard
import retrofit2.Response

object SudokuRepository {
    private val sudokuApi = SudokuClient.instance

   suspend fun getSudokuBoard(diff: String): Response<SudokuBoard>? {
       return try {
           sudokuApi.getSudokuBoardAsync(diff).await()
       }catch (t: Throwable){
           null
       }
    }

}