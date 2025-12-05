package com.mobileapp.battleship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobileapp.battleship.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!
    private val args: GameOverFragmentArgs by navArgs()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameOverBinding.inflate(inflater,container,false)

        binding.backMenuButton.setOnClickListener {
            /*TODO
            *  Currently the game ViewModel does not get destroyed,
            *  so as a temp solution this will clear all ViewModels.
            *  Need to find a way to only clear the game ViewModel.
            * */
            activity?.viewModelStore?.clear()
            findNavController().navigate(R.id.action_gameOverFragment_to_mainMenuFragment)
        }

        val res = resources

        binding.playerWinTextBox.text = getString(R.string.who_won_text, if (args.winner == Player.PLAYER1) 1 else 2)
        binding.player1HitsBox.text = res.getQuantityString(R.plurals.hits_p1_text, args.hitsMadeByP1, args.hitsMadeByP1)
        binding.player1MissBox.text = res.getQuantityString(R.plurals.misses_p1_text, args.missMadeByP1, args.missMadeByP1)
        binding.player2HitsBox.text = res.getQuantityString(R.plurals.hits_p2_text, args.hitsMadeByP2, args.hitsMadeByP2)
        binding.player2MissBox.text = res.getQuantityString(R.plurals.misses_p2_text, args.missMadeByP2, args.missMadeByP2)



        return binding.root
    }

}