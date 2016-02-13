package com.jtransc.examples.tictactoe

import jtransc.game.JTranscGame

object TicTacToeGdx {
	@JvmStatic fun main(args: Array<String>) {
		JTranscLibgdx.config();
		TicTacToe.main(args)
	}
}

object TicTacToeTransc {
	@JvmStatic fun main(args: Array<String>) {
		TicTacToe.main(args)
	}
}

object TicTacToe {
	@JvmStatic fun main(args: Array<String>) {
		JTranscGame.init { game ->
			val ingameScene = IngameController(Views.Ingame(GameAssets(game)))
			ingameScene.start()
			game.root.addChild(ingameScene.ingameView)
		}
	}
}