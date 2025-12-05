package com.mobileapp.battleship

data class ShipStatus(val shipType: Ship, val cells: List<Pair<Int, Int>>, var health: Int = shipType.size)
