package com.mobileapp.battleship

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

class GameOverViewModelFactory(private val fileName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(GameOverViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            return GameOverViewModel(fileName, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}