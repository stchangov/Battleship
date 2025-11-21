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

    private fun onTileClicked(row: Int, col: Int) {
        // If user is selecting the start of the ship
    }

    private fun handlePassDevice() {
        if (gameViewModel.currentPlayer.value == Player.PLAYER1 &&
            gameViewModel.p1ShipsToPlace.isEmpty()) {
            gameViewModel.switchToPlayer2()
        } else if (gameViewModel.currentPlayer.value == Player.PLAYER2 &&
            gameViewModel.p2ShipsToPlace.isEmpty()) {
            gameViewModel.switchToPlayer1()  // temp holder will implement soon
        }

    }

}
