package com.mobileapp.battleship

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobileapp.battleship.databinding.FragmentGameOverBinding
import kotlin.properties.Delegates

class GameOverFragment : Fragment() {
    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!

    private val FILE_NAME = "stats.txt"

    private val factoryVM: GameOverViewModelFactory by lazy {
        GameOverViewModelFactory(fileName = FILE_NAME)
    }
    private val viewModel: GameOverViewModel by viewModels() { factoryVM }

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
            viewModel.writeToFile(requireContext())

            activity?.viewModelStore?.clear()
            findNavController().navigate(R.id.action_gameOverFragment_to_mainMenuFragment)
        }

        val res = resources

        binding.playerWinTextBox.text = getString(R.string.who_won_text, viewModel.getWinner())
        binding.player1HitsBox.text = res.getQuantityString(R.plurals.hits_p1_text, viewModel.args.hitsMadeByP1, viewModel.args.hitsMadeByP1)
        binding.player1MissBox.text = res.getQuantityString(R.plurals.misses_p1_text, viewModel.args.missMadeByP1, viewModel.args.missMadeByP1)
        binding.player2HitsBox.text = res.getQuantityString(R.plurals.hits_p2_text, viewModel.args.hitsMadeByP2, viewModel.args.hitsMadeByP2)
        binding.player2MissBox.text = res.getQuantityString(R.plurals.misses_p2_text, viewModel.args.missMadeByP2, viewModel.args.missMadeByP2)

        binding.textViewScrollBox.text = viewModel.readFromFile(requireContext())

        return binding.root
    }
}