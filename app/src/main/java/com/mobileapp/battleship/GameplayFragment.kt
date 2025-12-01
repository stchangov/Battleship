package com.mobileapp.battleship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobileapp.battleship.databinding.FragmentGameplayBinding
import com.mobileapp.battleship.databinding.FragmentShipPlacementBinding
import kotlin.getValue

/**
 * Gameplay attack phase screen.
 * Players will tap a grid to attack — for now this is only a visual layout.
 *
 * MISS = lower alpha + disable clicking (same pattern as disableInvalidEndTiles() in ShipPlacementFragment)
 */
class GameplayFragment : Fragment() {

    private var _binding: FragmentGameplayBinding? = null
    private val binding get() = _binding!!
    private lateinit var tileButtons: Array<Array<ImageView?>>
    private val gameViewModel: GameViewModel by activityViewModels()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameplayBinding.inflate(inflater, container, false)
        val view = binding.root

        //val gameBoard = binding.gridGameBoard
        //setupBoard(gameBoard)



        return view
    }
}
