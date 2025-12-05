package com.mobileapp.battleship

data class ShipStatus(var shipType: Ship, var health: Int = shipType.size)
