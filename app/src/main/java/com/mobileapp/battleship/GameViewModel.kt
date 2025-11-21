package com.mobileapp.battleship

import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    // Initialize a 10x10 grid of empty cells for both players
    private var player1Board = Array(10) { Array(10) { CellState.EMPTY} }
    private var player2Board = Array(10) { Array(10) { CellState.EMPTY} }

    private var currentPlayer: Player = Player.PLAYER1 // live data
    private var currentShipPlaced: Ship = Ship.DESTROYER

    // keep track of ships left to place
    private var battleshipPlaced = false
    private var cruiserPlaced = false
    private var submarinePlaced = false
    private var destroyerPlaced = false


    fun isHit(x: Int, y: Int): Boolean {
        // TODO
        return false
    }

    fun tryPlaceShip(x: Int, y: Int) {
        // TODO
    }

    fun isWin(): Boolean {
        // TODO
        return false
    }

    fun whoWon(): Player {
        // TODO
        return Player.PLAYER1
    }

    fun isGridHidden(): Boolean {
        // TODO
        return false
    }

    fun whichPlayerTurn(): Player {
        // TODO
        return Player.PLAYER1
    }
    /*
    add result screen takes arg string
     */




    /*
    live data - tiles
     */


    /*
    Make stats for player1 and player2 shared preference
     */


    /*
    Add current player to top of screen for attack and ship placement phase - live data
     */













}