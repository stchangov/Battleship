package com.mobileapp.battleship

import android.content.Context
import androidx.lifecycle.ViewModel
import java.io.File

class StatsViewModel : ViewModel() {
    private val fileName = "stats.txt"

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