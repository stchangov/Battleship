package com.mobileapp.battleship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobileapp.battleship.databinding.FragmentShipPlacementBinding
import androidx.navigation.fragment.findNavController

/**
 * Ship placement screen.
 * Eventually players will drag/drop ships, but for now this is UI-only.
 */
class ShipPlacementFragment : Fragment() {

    private lateinit var binding: FragmentShipPlacementBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentShipPlacementBinding.inflate(inflater, container, false)

        // When player is done placing their ships:
        // Navigate to gameplay phase
        binding.btnPassDevice.setOnClickListener {
            findNavController().navigate(R.id.action_shipPlacement_to_gameplay)
        }

        return binding.root
    }
}
