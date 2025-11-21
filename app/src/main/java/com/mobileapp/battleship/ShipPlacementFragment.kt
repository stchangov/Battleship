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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShipPlacementBinding.inflate(inflater, container, false)
        val view = binding.root

        val gameBoard = binding.gridGameBoard
        setupBoard(gameBoard)





        // When player is done placing their ships: Navigate to gameplay phase
        binding.btnPassDevice.setOnClickListener {
            findNavController().navigate(R.id.action_shipPlacement_to_gameplay)
        }

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
                }

                // Store this tile so we can update it later
                tileButtons[row][col] = tile

                // for testing with the ship tile
                if (row == 0 && col == 0) {
                    tile.setImageDrawable(null) // remove circle
                    tile.setBackgroundResource(R.drawable.ship_tile) // ship tile
                }
                if (row == 0 && col == 1) {
                    tile.setImageDrawable(null) // remove circle
                    tile.setBackgroundResource(R.drawable.ship_tile) // ship tile
                }

                // Add the tile to the GridLayout so it becomes visible
                gameBoard.addView(tile)
            }

        }




    }
}
