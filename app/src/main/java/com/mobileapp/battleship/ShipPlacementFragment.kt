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

/**
 * Ship placement screen.
 * Eventually players will drag/drop ships, but for now this is UI-only.
 */
class ShipPlacementFragment : Fragment() {
    private var _binding: FragmentShipPlacementBinding? = null
    private val binding get() = _binding!!

    private lateinit var tileButtons: Array<Array<Button>>


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

    private fun setupBoard(grid: GridLayout) {

    }
}
