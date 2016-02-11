/*
 * Copyright 2016 Carlos Ballesteros Velasco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example

import jtransc.*
import jtransc.game.canvas.Canvas
import jtransc.game.stage.Image
import jtransc.game.stage.Stage

object Example {
	@JvmStatic fun main(args: Array<String>) {
		val canvas = Canvas()
		JTranscEventLoop.init {
			println("Init")
			println("JTransc version:" + JTranscVersion.getVersion())
			println("Endian isLittleEndian:" + JTranscEndian.isLittleEndian())
			println("Endian isBigEndian:" + JTranscEndian.isBigEndian())
			val stage = Stage()
			val root = stage.root
			val texture = canvas.image("assets/jtransc.jpg", 200, 200)

			//JTranscGC.disable()

			val image = Image(texture, 0.5, 0.5).apply {
				x = 256.0
				y = 256.0
				scaleX = 2.0
				scaleY = 2.0
				rotationDegrees = 45.0
			}
			root.addChild(image)

			var lastTime = System.currentTimeMillis()
			JTranscEventLoop.loop({
				val currentTime = System.currentTimeMillis()
				val elapsed = currentTime - lastTime
				image.rotationDegrees += 0.1 * elapsed.toDouble()
				lastTime = currentTime
			}, {
				stage.render(canvas)
			})
		}
	}
}
