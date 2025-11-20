package com.mobileapp.battleship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.mobileapp.battleship.databinding.FragmentShipPlacementBinding
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton


/**
 * Ship placement screen.
 * Eventually players will drag/drop ships, but for now this is UI-only.
 */
class ShipPlacementFragment : Fragment() {
    private var _binding: FragmentShipPlacementBinding? = null
    private val binding get() = _binding!!

    private lateinit var tileButtons: Array<Array<Button?>>
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
        // Initialize the tile buttons
        tileButtons = Array(10) { arrayOfNulls<Button>(10) }

        for (row in 0 until size) {
            for (col in 0 until size) {
                // Create the tile button and apply its initial appearance and behavior
                val btn = Button(requireContext()).apply {
                    setBackgroundColor(emptyTileColor)

                    // layoutParams use px, so convert dp sizes to px
                    val tileSize = (37 * resources.displayMetrics.density).toInt()
                    val margin = (2 * resources.displayMetrics.density).toInt()

                    // set this tile's size, grid position, and spacing within the GridLayout
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = tileSize
                        height = tileSize
                        // assign this tile to its specific row and column in the GridLayout
                        rowSpec = GridLayout.spec(row)
                        columnSpec = GridLayout.spec(col)
                        setMargins(margin, margin, margin, margin)
                    }
                }

                // store this tile so we can update it later
                tileButtons[row][col] = btn

                // add the tile to the GridLayout so it becomes visible
                gameBoard.addView(btn)
            }

        }




    }
}
