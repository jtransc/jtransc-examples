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

data class Matrix(
	@JvmField var a: Double = 1.0,
	@JvmField var b: Double = 0.0,
	@JvmField var c: Double = 0.0,
	@JvmField var d: Double = 1.0,
	@JvmField var tx: Double = 0.0,
	@JvmField var ty: Double = 0.0
) {
	public fun identity(): Matrix {
		return setTo(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
	}

	public fun isIdentity(): Boolean = (a == 1.0) && (b == 0.0) && (c == 0.0) && (d == 1.0) && (tx == 0.0) && (ty == 0.0)

	public fun rotate(theta: Double): Matrix {
		val cos = Math.cos(theta)
		val sin = Math.sin(theta)

		val a1 = a * cos - b * sin
		b = a * sin + b * cos
		a = a1

		val c1 = c * cos - d * sin
		d = c * sin + d * cos
		c = c1

		val tx1 = tx * cos - ty * sin
		ty = tx * sin + ty * cos
		tx = tx1

		return this
	}

	public fun translate(dx: Double, dy: Double): Matrix {
		tx += dx
		ty += dy
		return this
	}

	public fun pretranslate(dx: Double, dy: Double): Matrix {
		tx += a * dx + c * dy
		ty += b * dx + d * dy
		return this
	}

	fun clone() = Matrix(a, b, c, d, tx, ty)
	fun copyFrom(that: Matrix) = setTo(that.a, that.b, that.c, that.d, that.tx, that.ty)

	public fun invert(): Matrix {
		var norm = a * d - b * c

		if (norm == 0.0) {
			a = 0.0
			b = 0.0
			c = 0.0
			d = 0.0
			tx = -tx
			ty = -ty
		} else {
			norm = 1.0 / norm
			var a1 = d * norm
			d = a * norm
			a = a1
			b *= -norm
			c *= -norm

			var tx1 = -a * tx - c * ty
			ty = -b * tx - d * ty
			tx = tx1
		}

		//checkProperties()

		return this
	}

	public fun setTo(a: Double, b: Double, c: Double, d: Double, tx: Double, ty: Double): Matrix {
		this.a = a
		this.b = b
		this.c = c
		this.d = d
		this.tx = tx
		this.ty = ty
		return this
	}

	fun scale(sx: Double, sy: Double): Matrix = setTo(a * sx, b * sx, c * sy, d * sy, tx * sx, ty * sy)

	fun prescale(sx: Double, sy: Double): Matrix = setTo(a * sx, b * sx, c * sy, d * sy, tx, ty)


	public fun prerotate(angle: Double): Matrix {
		val sin = Math.sin(angle)
		val cos = Math.cos(angle)

		return setTo(
			a * cos + c * sin, b * cos + d * sin,
			c * cos - a * sin, d * cos - b * sin,
			tx, ty
		)
	}

	public fun preskew(skewX: Double, skewY: Double): Matrix {
		val sinX = Math.sin(skewX)
		val cosX = Math.cos(skewX)
		val sinY = Math.sin(skewY)
		val cosY = Math.cos(skewY)

		return setTo(
			a * cosY + c * sinY,
			b * cosY + d * sinY,
			c * cosX - a * sinX,
			d * cosX - b * sinX,
			tx, ty
		)
	}

	public fun skew(skewX: Double, skewY: Double): Matrix {
		val sinX = Math.sin(skewX)
		val cosX = Math.cos(skewX)
		val sinY = Math.sin(skewY)
		val cosY = Math.cos(skewY)

		return setTo(
			a * cosY - b * sinX,
			a * sinY + b * cosX,
			c * cosY - d * sinX,
			c * sinY + d * cosX,
			tx * cosY - ty * sinX,
			tx * sinY + ty * cosX
		)
	}

	fun multiply(l: Matrix, r: Matrix): Matrix = setTo(
		l.a * r.a + l.b * r.c,
		l.a * r.b + l.b * r.d,
		l.c * r.a + l.d * r.c,
		l.c * r.b + l.d * r.d,
		l.tx * r.a + l.ty * r.c + r.tx,
		l.tx * r.b + l.ty * r.d + r.ty
	)

	fun concat(that: Matrix): Matrix = multiply(this, that)
	fun preconcat(that: Matrix): Matrix = multiply(that, this)

	fun pretransform(a:Double, b:Double, c:Double, d:Double, tx:Double, ty:Double): Matrix {
		return preconcat(temp.setTo(a, b, c, d, tx, ty))
	}

	companion object {
		//@JvmStatic private val temp = Matrix()
		private val temp = Matrix()
	}

	public fun transform(px: Double, py: Double, result: Point = Point()): Point {
		return result.setTo(
			this.a * px + this.c * py + this.tx,
			this.d * py + this.b * px + this.ty
		)
	}
}