package com.mobileapp.battleship

import android.graphics.Insets.add
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.apply

class GameViewModel: ViewModel() {
    // Initialize a 10x10 grid of empty cells for both players
    private var player1Board = Array(10) { Array(10) { CellState.EMPTY} }
    private var player2Board = Array(10) { Array(10) { CellState.EMPTY} }

    private val _currentPlayer = MutableLiveData<Player>(Player.PLAYER1)
    val currentPlayer: LiveData<Player>
        get() =  _currentPlayer

    fun switchToPlayer1() {
        _currentPlayer.value = Player.PLAYER1
    }

    fun switchToPlayer2() {
        _currentPlayer.value = Player.PLAYER2
    }

    // keep track of ships left to place
    private var battleshipPlaced = false
    private var cruiserPlaced = false
    private var submarinePlaced = false
    private var destroyerPlaced = false

    // Keep track if we are placing the first tile for the ship
    // -1 means "not selected yet" -> -1 is an invalid coordinate
    var isSelectingStart = true
    var startRow = -1
    var startCol = -1

    fun getStartTile(): Pair<Int, Int> {
        return Pair(startRow, startCol)
    }

    fun buildShipCells(endRow: Int, endCol: Int): List<Pair<Int, Int>> {
        val shipCells = mutableListOf<Pair<Int, Int>>()

        // Build the ship horizontally
        if (startRow == endRow) {
            if (endCol < startCol) {
                // Build ship toward the left
                for (col in endCol..startCol) {
                    shipCells.add(Pair(startRow, col))
                }
            } else {
                // Build ship toward the right
                for (col in startCol..endCol) {
                    shipCells.add(Pair(startRow, col))
                }
            }
        } else if (startCol == endCol) {  // Build the ship vertically
            if (endRow < startRow) {
                // Build ship toward the top
                for (row in endRow..startRow) {
                    shipCells.add(Pair(row, startCol))
                }
            } else {
                // Build the ship toward the bottom
                for (row in startRow..endRow) {
                    shipCells.add(Pair(row, startCol))
                }
            }
        }

        return shipCells
    }

    fun placeShip(cells: List<Pair<Int, Int>>) {

    }

    // Keep track of ships Player 1 needs to place
    val p1ShipsToPlace = ArrayDeque<Ship>().apply {
        add(Ship.CARRIER)
        add(Ship.BATTLESHIP)
        add(Ship.CRUISER)
        add(Ship.SUBMARINE)
        add(Ship.DESTROYER)
    }

    // Keep track of ships Player 2 needs to place
    val p2ShipsToPlace = ArrayDeque<Ship>().apply {
        add(Ship.CARRIER)
        add(Ship.BATTLESHIP)
        add(Ship.CRUISER)
        add(Ship.SUBMARINE)
        add(Ship.DESTROYER)
    }

    fun currentShip(): Ship {
        return if (currentPlayer.value == Player.PLAYER1)
            p1ShipsToPlace.first()
        else
            p2ShipsToPlace.first()
    }


    // Checks if an attack will hit a ship
    fun isHit(x: Int, y: Int): Boolean {
        when (currentPlayer.value) {
            Player.PLAYER1 -> {
                if (player1Board[x][y] == CellState.SHIP)
                    return true
            }
            Player.PLAYER2 -> {
                if (player2Board[x][y] == CellState.SHIP)
                    return true
            }
            null -> Log.d("ERROR", "currentPlayer was null")
        }
        return false
    }

    // Turns cell state into HIT, call this function after checking with isHit
    fun registerHit(x: Int, y:Int) {
        when (currentPlayer.value) {
            Player.PLAYER1 -> {
                player1Board[x][y] = CellState.HIT
            }
            Player.PLAYER2 -> {
                player1Board[x][y] = CellState.HIT
            }
            null -> Log.d("ERROR", "currentPlayer was null")
        }
    }

    // Checks if either grid has any ships left
    // inefficiently searches entire grid, may rewrite
    fun isWin(): Boolean {
        for (x in 0 until 10) {
            for (y in 0 until 10) {
                if (player1Board[x][y] == CellState.SHIP || player2Board[x][y] == CellState.SHIP)
                    return true
            }
        }
        return false
    }

    // Determines who won the game, call this function after isWin
    // inefficiently checks player 1 if they have any ships left, may rewrite
    fun whoWon(): Player {
        for (x in 0 until 10) {
            for (y in 0 until 10) {
                if (player1Board[x][y] == CellState.SHIP)
                    return Player.PLAYER2
            }
        }
        return Player.PLAYER1
    }

    // might remove and place into UI logic instead
    fun isGridHidden(): Boolean {
        // TODO
        return false
    }

    /*
    add result screen takes arg string
     */

    /*
    Make stats for player1 and player2 shared preference
     */


    /*
    Add current player to top of screen for attack and ship placement phase - live data
     */













}