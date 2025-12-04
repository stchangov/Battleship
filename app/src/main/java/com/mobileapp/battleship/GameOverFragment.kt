package com.mobileapp.battleship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobileapp.battleship.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameOverBinding.inflate(inflater,container,false)
        // TODO
        return binding.root
    }

}