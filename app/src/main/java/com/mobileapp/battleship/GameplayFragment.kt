package com.mobileapp.battleship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobileapp.battleship.databinding.FragmentGameplayBinding
import com.mobileapp.battleship.databinding.FragmentShipPlacementBinding

/**
 * Gameplay attack phase screen.
 * Players will tap a grid to attack — for now this is only a visual layout.
 */
class GameplayFragment : Fragment() {

    private var _binding: FragmentGameplayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameplayBinding.inflate(inflater, container, false)
        val view = binding.root






        return view
    }
}
