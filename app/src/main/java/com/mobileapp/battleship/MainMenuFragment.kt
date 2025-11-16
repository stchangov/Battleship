package com.mobileapp.battleship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobileapp.battleship.databinding.FragmentMainMenuBinding
import androidx.navigation.fragment.findNavController

/**
 * Main Menu screen.
 * Users can start a new game, view stats, or read instructions.
 */
class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate layout using ViewBinding
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)

        // Navigate to Ship Placement screen
        binding.btnStartGame.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenu_to_shipPlacement)
        }

        // Navigate to Stats screen
        binding.btnStats.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenu_to_stats)
        }

        // Navigate to Instructions screen
        binding.btnInstructions.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenu_to_instructions)
        }

        return binding.root
    }
}
