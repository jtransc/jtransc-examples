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

import jtransc.JTranscRender

class Canvas {
	internal val batches = BatchBuilder()
	val context2D = Context2D(this)
	val width = 640
	val height = 480

	fun image(path:String, width:Int, height:Int): Texture {
		return Texture(BaseTexture(JTranscRender.createTexture(path, width, height), width, height))
	}

	fun start() {
		batches.start();
	}

	fun draw() {
		batches.render()
		batches.reset()
		context2D.reset()
	}
}

class Context2D(val canvas: Canvas) {
	private val matrix = Matrix()
	private val stack = StackPool(128) { Matrix() }
	private val batches = canvas.batches
	private var _globalAlpha = 1.0

	var globalAlpha: Double
		get() = _globalAlpha
		set(value) {

		}

	fun reset() {
		_globalAlpha = 1.0
		matrix.identity()
		stack.clear()
		batches.reset()
	}

	inline fun save(callback: () -> Unit) {
		save()
		callback()
		restore()
		//try {
		//	callback()
		//} finally {
		//	restore()
		//}
	}

	fun save() {
		stack.push().copyFrom(matrix)
	}

	fun restore() {
		matrix.copyFrom(stack.pop())
	}

	fun translate(dx: Double, dy: Double): Context2D {
		matrix.pretranslate(dx, dy)
		return this
	}

	fun scale(sx: Double, sy: Double = sx): Context2D {
		matrix.prescale(sx, sy)
		return this
	}

	fun rotate(angle: Double): Context2D {
		matrix.prerotate(angle)
		return this
	}

	fun transform(a:Double, b:Double, c:Double, d:Double, tx:Double, ty:Double): Context2D {
		matrix.pretransform(a, b, c, d, tx, ty)
		return this
	}

	fun setTransform(a:Double, b:Double, c:Double, d:Double, tx:Double, ty:Double): Context2D {
		matrix.setTo(a, b, c, d, tx, ty)
		return this
	}

	fun fillRect(x: Double, y: Double, w: Double, h: Double) {
		val p0 = matrix.transform(x + 0, y + 0)
		val p1 = matrix.transform(x + w, y + 0)
		val p2 = matrix.transform(x + 0, y + h)
		val p3 = matrix.transform(x + w, y + h)

		batches.quad(0, p0, p1, p2, p3, 0f, 0f, 1f, 1f)
	}

	companion object {
		private val t0 = Point()
		private val t1 = Point()
		private val t2 = Point()
		private val t3 = Point()
	}

	fun drawImage(image: Texture, x: Double = 0.0, y: Double = 0.0, w:Double = image.width.toDouble(), h:Double = image.height.toDouble()) {
		val i = image

		val p0 = matrix.transform(x + 0, y + 0, t0)
		val p1 = matrix.transform(x + w, y + 0, t1)
		val p2 = matrix.transform(x + 0, y + h, t2)
		val p3 = matrix.transform(x + w, y + h, t3)

		batches.quad(i.base.id, p0, p1, p2, p3, i.tx0, i.ty0, i.tx1, i.ty1)
	}
}

class BaseTexture internal constructor(@JvmField val id: Int, @JvmField val width: Int, @JvmField val height: Int) {
	fun dispose() {
		JTranscRender.disposeTexture(id)
	}
}

class Texture private constructor(
	@JvmField val base: BaseTexture,
	@JvmField val left: Int, val top: Int,
	@JvmField val right: Int, val bottom: Int,
	@JvmField val tx0: Float = (left.toDouble() / base.width.toDouble()).toFloat(),
	@JvmField val ty0: Float = (top.toDouble() / base.height.toDouble()).toFloat(),
	@JvmField val tx1: Float = (right.toDouble() / base.width.toDouble()).toFloat(),
	@JvmField val ty1: Float = (bottom.toDouble() / base.height.toDouble()).toFloat()
) {
	val width:Int get() = right - left
	val height:Int get() = bottom - top

	constructor(base: BaseTexture) : this(
		base, 0, 0, base.width, base.height, 0f, 0f, 1f, 1f
	)

	fun slice(x: Int, y: Int, width: Int, height: Int): Texture {
		val l2 = Math2.clamp(left + x, left, right)
		val r2 = Math2.clamp(left + x + width, left, right)
		val t2 = Math2.clamp(top + y, top, bottom)
		val b2 = Math2.clamp(top + y + height, top, bottom)
		return Texture(base, l2, r2, t2, b2)
	}
}
