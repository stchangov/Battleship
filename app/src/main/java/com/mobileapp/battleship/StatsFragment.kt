package com.mobileapp.battleship

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.mobileapp.battleship.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private val statsVM : StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

        binding.btnBackStats.setOnClickListener {
            findNavController().navigateUp()
        }

        val historyString = statsVM.readFromFile(requireContext())

        if (!historyString.isEmpty()){
            binding.statTextView.text = historyString
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
