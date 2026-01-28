package com.mobileapp.battleship

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

/**
 * Gameplay attack phase screen.
 * Players will tap a grid to attack.
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

        loadGameBoard()

        // checks if there was an app state change
        if (gameViewModel.lastHitPos != null) {
            val lastx = gameViewModel.lastHitPos!!.first
            val lasty = gameViewModel.lastHitPos!!.second

            if (gameViewModel.getEnemyBoard()[lastx][lasty] == CellState.MISS || gameViewModel.isGameComplete()) {
                disableBoard()
                binding.btnPassAfterAttack.visibility = View.VISIBLE
                if (gameViewModel.isGameComplete()) binding.attackTextView.text =
                    getString(R.string.congrat_text)
            }
        }

        return view
    }

    private fun setupBoard(gameBoard: GridLayout) {
        val size = 10
        tileButtons = Array(size) { arrayOfNulls<ImageView>(size) }

        val margin = (2 * resources.displayMetrics.density).toInt()

        for (row in 0 until size) {
            for (col in 0 until size) {
                val tile = ImageView(requireContext()).apply {
                    setImageResource(R.drawable.circle)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE

                    // Use Weights (1f) to let Android handle the size distribution
                    layoutParams = GridLayout.LayoutParams(
                        GridLayout.spec(row, 1f),
                        GridLayout.spec(col, 1f)
                    ).apply {
                        width = 0
                        height = 0
                        setMargins(margin, margin, margin, margin)
                    }

                    setOnClickListener {
                        onTileClicked(row, col)
                    }
                }

                tileButtons[row][col] = tile
                gameBoard.addView(tile)
            }
        }
    }

    private fun onTileClicked(row: Int, col: Int) {
        gameViewModel.registerHit(row, col)
        gameViewModel.lastHitPos = row to col

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

    private fun missSplash(view: View) {
        view.animate()
            .alpha(0.6f)
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .alpha(0.85f)
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(120)
                    .withEndAction {
                        view.animate()
                            .alpha(0.40f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(120)
                            .start()
                    }.start()
            }.start()
    }

    private fun fadeOutTile(view: ImageView) {
        view.animate()
            .alpha(0.35f)
            .setDuration(600)
            .start()
    }

    private fun fadeOutShipAllAtOnce(shipCells: List<Pair<Int, Int>>) {
        for ((r, c) in shipCells) {
            tileButtons[r][c]?.let { fadeOutTile(it) }
        }
    }

    private fun screenShake() {
        val root = binding.root
        root.animate()
            .translationX(2f)
            .setDuration(45)
            .withEndAction {
                root.animate()
                    .translationX(0f)
                    .setDuration(45)
                    .start()
            }
            .start()
    }

    private fun impactPulse(view: ImageView) {
        view.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .alpha(1f)
            .setDuration(110)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(0.6f)
                    .setDuration(140)
                    .start()
            }
            .start()
    }

    private fun animateKillTileAndSinkShip(
        killTile: ImageView,
        shipCells: List<Pair<Int, Int>>
    ) {
        killTile.animate()
            .alpha(0.8f)
            .setDuration(220)
            .withEndAction {
                fadeOutShipAllAtOnce(shipCells)
                screenShake()
            }
            .start()
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
                        }
                    }
                    CellState.HIT -> {
                        tile?.apply {
                            val shipIndex = currentShipLocs[rowIndex to colIndex]
                            val shipDestroyed = shipIndex != null &&
                                    currentPlacedShip[shipIndex].health == 0

                            val idx = shipIndex
                            setImageResource(R.drawable.ship_tile)

                            if (idx != null) {
                                val colorToShow = if (shipDestroyed) {
                                    ContextCompat.getColor(
                                        requireContext(),
                                        currentPlacedShip[idx].shipType.colorRes
                                    )
                                } else {
                                    ContextCompat.getColor(requireContext(), R.color.ship_hit_neutral)
                                }
                                setColorFilter(colorToShow)
                            }

                            if (shipDestroyed) {
                                isEnabled = false
                                alpha = 0.30f
                                if (gameViewModel.lastHitPos != null &&
                                    gameViewModel.lastHitPos == Pair(rowIndex, colIndex)) {
                                    animateKillTileAndSinkShip(this, currentPlacedShip[idx!!].cells)
                                }
                                return@apply
                            }

                            alpha = 0.50f
                            isEnabled = false
                            if (gameViewModel.lastHitPos != null &&
                                gameViewModel.lastHitPos == Pair(rowIndex, colIndex)) {
                                impactPulse(this)
                            }
                        }
                    }

                    CellState.MISS -> {
                        tile?.apply {
                            isEnabled = false
                            setImageResource(R.drawable.miss_icon)
                            alpha = 0.40f
                            if (gameViewModel.lastHitPos == Pair(rowIndex, colIndex)) {
                                missSplash(this)
                            }
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
            gameViewModel.lastHitPos = null
            clearColor()
            loadGameBoard()
        } else {
            val statsP1 = gameViewModel.hitsMadeByP1()
            val statsP2 = gameViewModel.hitsMadeByP2()

            val action = GameplayFragmentDirections.actionGameplayFragmentToGameOverFragment(
                statsP1.first,
                statsP2.first,
                statsP1.second,
                statsP2.second,
                gameViewModel.whoWon()
            )
            findNavController().navigate(action)
        }
    }
}
