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

class Stage {
	val root = Sprite()

	fun render(canvas: Canvas) {
		canvas.start();
		render(canvas.context2D)
		canvas.draw()
	}

	fun render(ctx: Context2D) {
		root.render(ctx)
	}
}

open class DisplayObject {
	var x = 0.0
	var y = 0.0
	var scaleX = 1.0
	var scaleY = 1.0
	var rotation = 0.0

	var rotationDegrees:Double get() = Math.toDegrees(rotation); set(value) { rotation = Math.toRadians(value) }

	fun render(ctx: Context2D) {
		ctx.save {
			ctx.translate(x, y)
			ctx.scale(scaleX, scaleY)
			ctx.rotate(rotation)
			internalRender(ctx)
		}
	}

	open fun internalRender(ctx: Context2D) {
	}
}

class Sprite : DisplayObject() {
	val children = arrayListOf<DisplayObject>()

	fun addChild(child: DisplayObject) = children.add(child).let { this }

	override fun internalRender(ctx: Context2D) {
		for (index in 0 until children.size) {
			children[index].render(ctx)
		}
	}
}

class Image(val image: Texture, var anchorX:Double = 0.0, var anchorY:Double = 0.0) : DisplayObject() {
	override fun internalRender(ctx: Context2D) {
		ctx.drawImage(image, -image.width * anchorX, -image.height * anchorY)
	}
}