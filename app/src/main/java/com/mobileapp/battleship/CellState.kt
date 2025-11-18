package com.mobileapp.battleship

enum class CellState {
    EMPTY,      // No ship, not hit yet
    SHIP,       // A ship is on this cell, not hit yet
    HIT,        // A ship was on this cell and it has been hit
    MISS        // An empty cell that has been fired upon
}