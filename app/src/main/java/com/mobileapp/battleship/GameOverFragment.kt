package com.mobileapp.battleship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val res = resources

        binding.playerWinTextBox.text = getString(R.string.who_won_text, if (args.winner == Player.PLAYER1) 1 else 2)
        binding.player1HitsBox.text = res.getQuantityString(R.plurals.hits_p1_text, args.hitsMadeByP1, args.hitsMadeByP1)
        binding.player1MissBox.text = res.getQuantityString(R.plurals.misses_p1_text, args.missMadeByP1, args.missMadeByP1)
        binding.player2HitsBox.text = res.getQuantityString(R.plurals.hits_p2_text, args.hitsMadeByP2, args.hitsMadeByP2)
        binding.player2MissBox.text = res.getQuantityString(R.plurals.misses_p2_text, args.missMadeByP2, args.missMadeByP2)

        return binding.root
    }

}