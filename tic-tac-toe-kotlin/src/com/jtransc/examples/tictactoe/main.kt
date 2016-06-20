package com.jtransc.examples.tictactoe

import com.jtransc.experimental.kotlin.JTranscKotlinReflectStripper
import com.jtransc.game.JTranscGame
import com.jtransc.media.libgdx.JTranscLibgdx
import com.jtransc.media.lime.JTranscLime

object TicTacToeGdx {
	@JvmStatic fun main(args: Array<String>) {
		JTranscLibgdx.init()
		TicTacToe.main(args)
	}
}

object TicTacToeTransc {
	@JvmStatic fun main(args: Array<String>) {
		JTranscLime.init()
		TicTacToe.main(args)
	}
}

object TicTacToe {
	@JvmStatic fun main(args: Array<String>) {
		JTranscKotlinReflectStripper.init()
		JTranscGame.init(512, 512) { game ->
			val ingameScene = IngameController(Views.Ingame(GameAssets(game)))
			ingameScene.start()
			game.root.addChild(ingameScene.ingameView)
		}
	}
}