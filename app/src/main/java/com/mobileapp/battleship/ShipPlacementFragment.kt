package com.mobileapp.battleship

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobileapp.battleship.databinding.FragmentShipPlacementBinding
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton


/**
 * Ship placement screen.
 */
class ShipPlacementFragment : Fragment() {
    private var _binding: FragmentShipPlacementBinding? = null
    private val binding get() = _binding!!

    private lateinit var tileButtons: Array<Array<ImageView?>>
    private val emptyTileColor: Int = R.color.empty_tile

    private val gameViewModel: GameViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShipPlacementBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnPassDevice.setOnClickListener {
            handlePassDevice()
        }

        // Set up Player 1's board
        val player1Board = binding.gridGameBoard
        setupBoard(player1Board)


        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupBoard(gameBoard: GridLayout) {
        val size = 10
        // initialize the tiles
        tileButtons = Array(10) { arrayOfNulls<ImageView>(10) }

        for (row in 0 until size) {
            for (col in 0 until size) {
                // Create the tile and apply its initial appearance and behavior
                val tile = ImageView(requireContext()).apply {
                    setImageResource(R.drawable.circle)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE

                    val tileSize = (37.3 * resources.displayMetrics.density).toInt()
                    val margin = (2 * resources.displayMetrics.density).toInt()

                    layoutParams = GridLayout.LayoutParams().apply {
                        width = tileSize
                        height = tileSize
                        rowSpec = GridLayout.spec(row)
                        columnSpec = GridLayout.spec(col)
                        setMargins(margin, margin, margin, margin)
                    }

                    // Attach listener
                    setOnClickListener {
                        onTileClicked(row, col)
                    }
                }

                // Store this tile
                tileButtons[row][col] = tile

                // Add the tile to the GridLayout so it becomes visible
                gameBoard.addView(tile)
            }
        }
    }

    private fun resetBoardUI() {
        for (row in 0 until 10) {
            for (col in 0 until 10) {
                val tile = tileButtons[row][col]
                if (gameViewModel.isShipTile(row, col)) {
                    tile?.isEnabled = false
                } else {
                    tile?.apply {
                        setImageResource(R.drawable.circle)
                        setColorFilter(emptyTileColor)
                        isEnabled = true
                        alpha = 1.0f
                    }
                }
            }
        }
    }


    private fun onTileClicked(row: Int, col: Int) {
        // Handle selecting the start tile
        if (gameViewModel.isSelectingStart) {
            // No need to validate the start tile. It is always valid
            gameViewModel.startRow = row
            gameViewModel.startCol = col
            gameViewModel.isSelectingStart = false

            highlightStartTile(row, col)

            disableInvalidEndTiles(row, col)

            return
        }

        // End tile logic
        val shipCells = gameViewModel.buildShipCells(row, col)
        gameViewModel.placeShip(shipCells)

        // Show the full ship
        highlightFullShip(shipCells)

        gameViewModel.isSelectingStart = true
        resetBoardUI()



    }

    private fun highlightStartTile(row: Int, col: Int) {
        val tile = tileButtons[row][col]
        val shipColorRes = gameViewModel.currentShip().colorRes

        // Turn the resource ID into a real color int
        val shipColor = ContextCompat.getColor(requireContext(), shipColorRes)

        tile?.setImageResource(R.drawable.ship_tile)
        tile?.setColorFilter(shipColor)
    }

    private fun highlightFullShip(shipCells: List<Pair<Int, Int>>) {
        val shipColorRes = gameViewModel.currentShip().colorRes
        val shipColor = ContextCompat.getColor(requireContext(), shipColorRes)

        for (cell in shipCells) {
            val (row, col) = cell
            tileButtons[row][col]?.apply {
                setImageResource(R.drawable.ship_tile)
                setColorFilter(shipColor)
                alpha = 1f
            }
        }
    }

    private fun disableInvalidEndTiles(startRow: Int, startCol: Int) {
        val size = gameViewModel.currentShip().size
        val remainingLength = size - 1

        // Store the valid end points - these we will keep enabled
        val validEnds = mutableListOf<Pair<Int, Int>>()

        // Right
        if (startCol + remainingLength <= 9) {
            // `to` creates a Pair(row, col)
            validEnds.add(startRow to (startCol + remainingLength))
        }

        // Left
        if (startCol - remainingLength >= 0) {
            validEnds.add(startRow to (startCol - remainingLength))
        }

        // Up
        if (startRow - remainingLength >= 0) {
            validEnds.add((startRow - remainingLength) to startCol)

        }

        // Bottom
        if (startRow + remainingLength <= 9) {
            validEnds.add((startRow + remainingLength) to startCol)
        }

        // Disable all tiles
        for (row in 0 until 10) {
            for (col in 0 until 10) {
                // Disable the tile and make it faded
                tileButtons[row][col]?.apply {
                    isEnabled = false
                    alpha = 0.3f
                }
            }
        }

        // Enable start tile again
        tileButtons[startRow][startCol]?.apply {
            isEnabled = true
            alpha = 1.0f
        }

        // Now enable all valid end tiles
        validEnds.forEach { (row, col) ->
            tileButtons[row][col]?.apply {
                isEnabled = true
                alpha = 1.0f
            }
        }
    }

    private fun handlePassDevice() {
        if (gameViewModel.currentPlayer.value == Player.PLAYER1 &&
            gameViewModel.p1ShipsToPlace.isEmpty()) {
            gameViewModel.switchToPlayer2()
        } else if (gameViewModel.currentPlayer.value == Player.PLAYER2 &&
            gameViewModel.p2ShipsToPlace.isEmpty()) {
            findNavController().navigate(R.id.action_shipPlacement_to_gameplay)
        }
    }

}
