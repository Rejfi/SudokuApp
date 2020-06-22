package com.example.sudokuapp.network

import com.example.sudokuapp.data.SudokuBoard
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface SudokuApi {

    @GET("board")
    fun getSudokuBoard(@Query("difficulty") diff:String): Deferred<Response<SudokuBoard>>

}

