package com.mobileapp.battleship

import android.graphics.BlendMode
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Insets.add
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.apply

class GameViewModel: ViewModel() {

    private var totalHealthP1: Int
    private var totalHealthP2: Int

    init {
        // looks in the enum class and calculates sum of tiles of all ships
        val totalHealth = calculateTotalHealth()

        totalHealthP1 = totalHealth
        totalHealthP2 = totalHealth
    }

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

    fun resetStartSelection() {
        isSelectingStart = true
        startRow = -1
        startCol = -1
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

    fun isShipTile(row: Int, col: Int): Boolean {
        return if (currentPlayer.value == Player.PLAYER1)
            player1Board[row][col] == CellState.SHIP
        else
            player2Board[row][col] == CellState.SHIP
    }

    fun shipCellsOverlap(shipCells: List<Pair<Int, Int>>): Boolean {
        for (cell in shipCells) {
            val (row, col) = cell
            if (isShipTile(row, col)) {
                return true
            }
        }
        return false
    }

    fun placeShip(cells: List<Pair<Int, Int>>) {
        for (cell in cells) {
            // Destructure the pair
            val (row, col) = cell
            if (currentPlayer.value == Player.PLAYER1) {
                player1Board[row][col] = CellState.SHIP
            } else {
                player2Board[row][col] = CellState.SHIP
            }
        }
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

    fun popShip() {
        Log.d("DEBUG_POP", "Popping: " + currentShip())
        if (currentPlayer.value == Player.PLAYER1)
            p1ShipsToPlace.removeFirst()
        else
            p2ShipsToPlace.removeFirst()
    }

    // Turns cell state into HIT, call this function after checking with isHit
    fun registerHit(x: Int, y:Int) {
        val currentBoard :  Array<Array<CellState>> = when (currentPlayer.value) {
            Player.PLAYER1 -> {
                player2Board
            }

            Player.PLAYER2 -> {
                player1Board
            }
        }

        when (currentBoard[x][y]) {
            CellState.SHIP -> {
                currentBoard[x][y] = CellState.HIT
            }
            CellState.EMPTY -> {
                currentBoard[x][y] = CellState.MISS
            }
            else -> {
                Log.d("DEBUG", "This tile is not supposed to be clickable")
            }
        }
    }

    // Checks if either players run out of health
    fun isGameComplete(): Boolean {
        return (totalHealthP1 <= 0 || totalHealthP2 <= 0)
    }

    // Determines who won the game, call this function after isGameComplete
    fun whoWon(): Player {
        return if (totalHealthP1 <= 0) Player.PLAYER1 else Player.PLAYER2
    }

    // might remove and place into UI logic instead
    fun isGridHidden(): Boolean {
        // TODO
        return false
    }

    private fun calculateTotalHealth(): Int {
        var totalHealth = 0
        for (ship in Ship.entries) {
            totalHealth += ship.size
        }
        return totalHealth
    }

    fun loadGameBoard(tileButtons: Array<Array<ImageView?>>) {
        val currentBoard :  Array<Array<CellState>> = when (currentPlayer.value) {
            Player.PLAYER1 -> {
                player2Board
            }

            Player.PLAYER2 -> {
                player1Board
            }
        }

        tileButtons.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, tile ->
                when (currentBoard[rowIndex][colIndex]) {
                    CellState.EMPTY -> {
                        tile?.apply {
                            isEnabled = true
                            alpha = 1.0f
                        }
                    }
                    CellState.SHIP -> {
                        tile?.apply {
                            isEnabled = true
                            alpha = 1.0f
                            setColorFilter(Color.GREEN)
                        }
                    }
                    CellState.HIT -> {
                        tile?.apply {
                            isEnabled = false
                            alpha = 1.0f
                            setImageResource(R.drawable.death_skull)
                            setColorFilter(Color.RED)
                        }
                    }
                    CellState.MISS -> {
                        tile?.apply {
                            isEnabled = false
                            alpha = 0.3f
                            setColorFilter(Color.YELLOW)
                        }
                    }
                }
            }
        }
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