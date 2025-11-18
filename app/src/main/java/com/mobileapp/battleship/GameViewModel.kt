package com.mobileapp.battleship

import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private lateinit var player1Board: Array<Array<CellState>>
    private lateinit var player2Board: Array<Array<CellState>>
    private var currentPlayer: currentPlayers = currentPlayers.player1 // live data
    private var currentShipPlaced: ships = ships.destroyer

    // keep track of ships left to place
    private var battleshipPlaced = false
    private var cruiserPlaced = false
    private var submarinePlaced = false
    private var destroyerPlaced = false


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