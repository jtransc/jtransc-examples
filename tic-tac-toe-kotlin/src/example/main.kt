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

import JTranscLibgdx
import jtransc.game.JTranscGame
import jtransc.game.stage.Image

object ExampleGdx {
	@JvmStatic fun main(args: Array<String>) {
		JTranscLibgdx.config();
		Example.main(args)
	}
}

object ExampleJTransc {
	@JvmStatic fun main(args: Array<String>) {
		Example.main(args)
	}
}

object Example {
	@JvmStatic fun main(args: Array<String>) {
		JTranscGame.init { game ->
			val texture = game.image("assets/jtransc.jpg", 200, 200)
			game.root.addChild(Image(texture, 0.5, 0.5).apply {
				x = 256.0
				y = 256.0
				scaleX = 2.0
				scaleY = 2.0
				rotationDegrees = 45.0
				alpha = 0.0
				onUpdate.add { elapsed ->
					rotationDegrees += 0.1 * elapsed.toDouble()
					alpha += 0.001 * elapsed.toDouble()
				}
			})
		}
	}
}
