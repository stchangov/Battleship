package com.mobileapp.battleship

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.io.File
import kotlin.Int

class GameOverViewModel(private val fileName: String, private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val args = GameOverFragmentArgs.fromSavedStateHandle(savedStateHandle)

    fun getWinner(): Int {
        return if (args.winner == Player.PLAYER1) 1 else 2
    }

    fun writeToFile(context: Context) {
        context.openFileOutput(fileName, Context.MODE_APPEND).use { outputStream ->
            outputStream.write(("P${getWinner()} Won | P1 hits: ${args.hitsMadeByP1} | P1 misses: ${args.missMadeByP1} " +
                    "| P2 hits: ${args.hitsMadeByP2} | P2 misses: ${args.missMadeByP2}\n").toByteArray()
            )
        }
    }

    fun readFromFile(context: Context): String {
        val file = File(context.filesDir, fileName)
        if(!file.exists()) {
            return ""
        }

        return context.openFileInput(fileName).bufferedReader().use {
            it.readText()
        }
    }
}