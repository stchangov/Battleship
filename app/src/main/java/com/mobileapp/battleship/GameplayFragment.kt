package com.mobileapp.battleship

import android.app.GameState
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

        gameViewModel.currentPlayer.observe(viewLifecycleOwner) { player ->
            val playerNum = if (player == Player.PLAYER1) 1 else 2
            binding.currentPlayerTextView.text =
                getString(R.string.current_player_placing, playerNum)
        }

        val gameBoard = binding.gridGameplay
        binding.btnPassAfterAttack.setOnClickListener {
            handlePassDevice()
        }

        setupBoard(gameBoard)

        gameViewModel.switchToPlayer1()
        loadGameBoard()

        return view
    }

    private fun setupBoard(gameBoard: GridLayout) {
        val size = 10
        // initialize the tiles
        tileButtons = Array(10) { arrayOfNulls<ImageView>(10) }

        for (row in 0 until size) {
            for (col in 0 until size) {
                // Create the tile and apply its initial appearance and behavior
                val tile = ImageView(requireContext()).apply {
                    setImageResource(R.drawable.circle)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE

                    val tileSize = (37.3 * resources.displayMetrics.density).toInt()
                    val margin = (2 * resources.displayMetrics.density).toInt()

                    layoutParams = GridLayout.LayoutParams().apply {
                        width = tileSize
                        height = tileSize
                        rowSpec = GridLayout.spec(row)
                        columnSpec = GridLayout.spec(col)
                        setMargins(margin, margin, margin, margin)
                    }

                    // Attach listener
                    setOnClickListener {
                        onTileClicked(row, col)
                    }
                }

                // Store this tile
                tileButtons[row][col] = tile

                // Add the tile to the GridLayout so it becomes visible
                gameBoard.addView(tile)
            }
        }
    }

    private fun onTileClicked(row: Int, col: Int) {

        gameViewModel.registerHit(row,col)

        loadGameBoard()
        if (gameViewModel.getEnemyBoard()[row][col] == CellState.MISS || gameViewModel.isGameComplete()) {
            disableBoard()
            binding.btnPassAfterAttack.visibility = View.VISIBLE
            if (gameViewModel.isGameComplete()) binding.attackTextView.text = getString(R.string.congrat_text)
        }
    }

    private fun disableBoard() {
        for (row in 0 until 10) {
            for (col in 0 until 10) {
                tileButtons[row][col]?.apply {
                    isEnabled = false
                }
            }
        }
    }

    // Colors are being reset for icons
    private fun clearColor() {
        for (row in 0 until 10) {
            for (col in 0 until 10) {
                val tile = tileButtons[row][col]
                tile?.apply {
                    setColorFilter(ContextCompat.getColor(requireContext(), R.color.empty_tile))
                    alpha = 1.0f
                }
            }
        }
    }

    fun loadGameBoard() {
        val currentBoard : Array<Array<CellState>> = gameViewModel.getEnemyBoard()
        val currentPlacedShip = gameViewModel.getEnemyPlacedShip()
        val currentShipLocs = gameViewModel.getEnemyShipLocs()

        tileButtons.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, tile ->
                when (currentBoard[rowIndex][colIndex]) {
                    CellState.EMPTY -> {
                        tile?.apply {
                            isEnabled = true
                            alpha = 1.0f
                            setImageResource(R.drawable.circle)
                        }
                    }
                    CellState.SHIP -> {
                        tile?.apply {
                            isEnabled = true
                            alpha = 1.0f
                            setImageResource(R.drawable.circle)
                            setColorFilter(Color.GREEN) // TODO remove debug coloring
                        }
                    }
                    CellState.HIT -> {
                        tile?.apply {
                            isEnabled = false
                            alpha = 1.0f
                            setImageResource(R.drawable.hit_icon)

                            if (currentShipLocs[Pair(rowIndex, colIndex)] != null &&
                                currentPlacedShip[currentShipLocs[Pair(rowIndex, colIndex)]!!].health == 0) {

                                setImageResource(R.drawable.death_skull)
                                val shipColor = ContextCompat.getColor(requireContext(), currentPlacedShip[currentShipLocs[Pair(rowIndex, colIndex)]!!].shipType.colorRes) // oh no
                                setColorFilter(shipColor)
                            }
                        }
                    }
                    CellState.MISS -> {
                        tile?.apply {
                            isEnabled = false
                            alpha = 0.3f
                            setImageResource(R.drawable.miss_icon)
                        }
                    }
                }
            }
        }
    }

    private fun handlePassDevice() {

        if (!gameViewModel.isGameComplete()) {
            if (gameViewModel.currentPlayer.value == Player.PLAYER1) {
                gameViewModel.switchToPlayer2()
            } else {
                gameViewModel.switchToPlayer1()
            }

            binding.btnPassAfterAttack.visibility = View.GONE
            clearColor()
            loadGameBoard()
        } else {
            findNavController().navigate(R.id.action_gameplayFragment_to_gameOverFragment)
        }
    }
}
