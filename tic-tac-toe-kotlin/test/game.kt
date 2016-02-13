import example.ChipType
import example.Model
import org.junit.Assert.assertEquals

class GameTest {
	@org.junit.Test
	fun testIntegrationSimpleGame() {
		val game = Model.Game()
		assertEquals(true, game.isEmpty(0, 0))
		game[0, 0] = ChipType.O
		assertEquals(false, game.isEmpty(0, 0))
		assertEquals(null, game.getWin())
		game[1, 0] = ChipType.O
		game[2, 0] = ChipType.O
		assertEquals(listOf("C(0,0,O)", "C(1,0,O)", "C(2,0,O)"), game.getWin()?.map { "$it" })
		game.reset()
		assertEquals(null, game.getWin())
	}
}