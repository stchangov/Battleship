package com.mobileapp.battleship

import android.app.GameState
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
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

    private fun shakeTile(view: View) {
        view.animate()
            .translationXBy(6f)     // smaller movement
            .setDuration(60)
            .withEndAction {
                view.animate()
                    .translationXBy(-12f)
                    .setDuration(60)
                    .withEndAction {
                        view.animate()
                            .translationXBy(6f)
                            .setDuration(60)
                            .start()
                    }.start()
            }.start()
    }

    private fun missSplash(view: View) {
        // Shrink first
        view.animate()
            .alpha(0.6f)        // soften the tile a bit
            .scaleX(0.9f)       // slight shrink
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {

                // Phase 2 — Slightly expand
                view.animate()
                    .alpha(0.85f)       // back toward full
                    .scaleX(1.1f)       // slight expansion
                    .scaleY(1.1f)
                    .setDuration(120)
                    .withEndAction {

                        // Settle back to original size
                        view.animate()
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(120)
                            .start()

                    }.start()
            }.start()
    }

    private fun fadeOutTile(view: ImageView) {
        view.animate()
            .alpha(0.35f)    // underwater look
            .setDuration(600) // slow + dramatic
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
            .translationX(2f)   // very small nudge
            .setDuration(45)
            .withEndAction {
                root.animate()
                    .translationX(0f)   // return to normal
                    .setDuration(45)
                    .start()
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
                            setColorFilter(Color.GREEN) // TODO remove debug coloring
                        }
                    }
                    CellState.HIT -> {
                        tile?.apply {
                            val shipIndex = currentShipLocs[Pair(rowIndex, colIndex)]
                            val shipIsDestroyed = shipIndex != null &&
                                    currentPlacedShip[shipIndex].health == 0

                            if (shipIsDestroyed) {

                                val idx = shipIndex!!

                                setImageResource(R.drawable.ship_tile)

                                val shipColor = ContextCompat.getColor(
                                    requireContext(),
                                    currentPlacedShip[idx].shipType.colorRes
                                )
                                setColorFilter(shipColor)

                                if (gameViewModel.lastHitPos == Pair(rowIndex, colIndex)) {
                                    fadeOutShipAllAtOnce(currentPlacedShip[idx].cells)
                                    screenShake()
                                }

                                return@apply
                            }

                            // NORMAL HIT TILE — does not apply to fatal hit
                            setImageResource(R.drawable.hit_icon)
                            alpha = 1f
                            isEnabled = false

                            if (gameViewModel.lastHitPos == Pair(rowIndex, colIndex)) {
                                shakeTile(this)
                            }
                        }
                    }

                    CellState.MISS -> {
                        tile?.apply {
                            isEnabled = false
                            alpha = 0.3f
                            setImageResource(R.drawable.miss_icon)

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
