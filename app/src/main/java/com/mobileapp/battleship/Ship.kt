package com.mobileapp.battleship

enum class Ship(val size: Int, val colorRes: Int) {
    CARRIER(5, R.color.ship_color_carrier),
    BATTLESHIP(4, R.color.ship_color_battleship),
    CRUISER(3, R.color.ship_color_cruiser),
    SUBMARINE(3, R.color.ship_color_submarine),
    DESTROYER(2, R.color.ship_color_destroyer)
}