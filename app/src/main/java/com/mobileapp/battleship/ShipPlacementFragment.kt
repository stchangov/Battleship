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
import android.widget.Toast
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
                    tile?.alpha = 1.0f
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

    private fun checkIfPlayerDonePlacing() {
        // Player 1 done placing
        if (gameViewModel.currentPlayer.value == Player.PLAYER1 &&
            gameViewModel.p1ShipsToPlace.isEmpty()
        ) {
            binding.btnPassDevice.visibility = View.VISIBLE
            binding.txtCurrentPlayerPlacing.text = "Player 1 done! Pass the device."
            disableBoard()
            return
        }

        // Player 2 done placing
        if (gameViewModel.currentPlayer.value == Player.PLAYER2 &&
            gameViewModel.p2ShipsToPlace.isEmpty()
        ) {
            binding.btnPassDevice.visibility = View.VISIBLE
            binding.txtCurrentPlayerPlacing.text = "Player 2 done! Start the battle!"
            disableBoard()
        }
    }


    private fun disableBoard() {
        for (row in 0 until 10) {
            for (col in 0 until 10) {
                tileButtons[row][col]?.apply {
                    isEnabled = false
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

        // END TILE LOGIC

        // Prevent selecting the same tile as the end tile
        if (row == gameViewModel.startRow && col == gameViewModel.startCol) {
            return
        }

        val shipCells = gameViewModel.buildShipCells(row, col)

        // Check if the ship cells overlap
        if (gameViewModel.shipCellsOverlap(shipCells)) {
            return
        }

        gameViewModel.placeShip(shipCells)

        // Show the full ship
        highlightFullShip(shipCells, gameViewModel.currentShip().colorRes)

        gameViewModel.popShip()
        gameViewModel.isSelectingStart = true
        resetBoardUI()
        checkIfPlayerDonePlacing()
    }

    private fun highlightStartTile(row: Int, col: Int) {
        val tile = tileButtons[row][col]
        val shipColorRes = gameViewModel.currentShip().colorRes

        // Turn the resource ID into a real color int
        val shipColor = ContextCompat.getColor(requireContext(), shipColorRes)

        tile?.setImageResource(R.drawable.ship_tile)
        tile?.setColorFilter(shipColor)
    }

    private fun highlightFullShip(shipCells: List<Pair<Int, Int>>, colorRes: Int) {
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

        val validEnds = mutableListOf<Pair<Int, Int>>()

        // Right
        if (startCol + remainingLength <= 9) {
            val endRow = startRow
            val endCol = startCol + remainingLength
            val cells = gameViewModel.buildShipCells(endRow, endCol)
            if (!gameViewModel.shipCellsOverlap(cells)) {
                validEnds.add(endRow to endCol)
            }
        }

        // Left
        if (startCol - remainingLength >= 0) {
            val endRow = startRow
            val endCol = startCol - remainingLength
            val cells = gameViewModel.buildShipCells(endRow, endCol)
            if (!gameViewModel.shipCellsOverlap(cells)) {
                validEnds.add(endRow to endCol)
            }
        }

        // Up
        if (startRow - remainingLength >= 0) {
            val endRow = startRow - remainingLength
            val endCol = startCol
            val cells = gameViewModel.buildShipCells(endRow, endCol)
            if (!gameViewModel.shipCellsOverlap(cells)) {
                validEnds.add(endRow to endCol)
            }
        }

        // Down
        if (startRow + remainingLength <= 9) {
            val endRow = startRow + remainingLength
            val endCol = startCol
            val cells = gameViewModel.buildShipCells(endRow, endCol)
            if (!gameViewModel.shipCellsOverlap(cells)) {
                validEnds.add(endRow to endCol)
            }
        }

        // Disable all tiles first
        for (row in 0 until 10) {
            for (col in 0 until 10) {
                tileButtons[row][col]?.apply {
                    isEnabled = false
                    alpha = 0.3f
                }
            }
        }

        // Enable only valid ends
        for (end in validEnds) {
            val (row, col) = end
            tileButtons[row][col]?.apply {
                isEnabled = true
                alpha = 1f
            }
        }

        // Keep the start tile enabled for visibility
        tileButtons[startRow][startCol]?.apply {
            isEnabled = false
            alpha = 1f
        }
    }


    private fun handlePassDevice() {
        val p1Done = gameViewModel.currentPlayer.value == Player.PLAYER1 &&
                gameViewModel.p1ShipsToPlace.isEmpty()

        val p2Done = gameViewModel.currentPlayer.value == Player.PLAYER2 &&
                gameViewModel.p2ShipsToPlace.isEmpty()

        if (p1Done) {
            gameViewModel.switchToPlayer2()

        } else if (p2Done) {
            findNavController().navigate(R.id.action_shipPlacement_to_gameplay)
        }
    }


}
