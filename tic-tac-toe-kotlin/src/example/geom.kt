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

class Point(
	@JvmField var x: Double = 0.0,
	@JvmField var y: Double = 0.0
) {
	fun setTo(x: Double, y: Double): Point {
		this.x = x
		this.y = y
		return this
	}
}

data class Rectangle(
	@JvmField var left:Double,
	@JvmField var top:Double,
	@JvmField var right:Double,
	@JvmField var bottom:Double
) {
	constructor() : this(0.0, 0.0, 0.0, 0.0)

	var width: Double get() = right - left; set(value) { right = left + value }
	var height: Double get() = bottom - top; set(value) { bottom = top + value }

	private fun _setToBounds(left:Double, top:Double, right:Double, bottom:Double): Rectangle {
		this.left = left
		this.top = top
		this.right = right
		this.bottom = bottom
		return this
	}

	fun setToBounds(left:Double, top:Double, right:Double, bottom:Double): Rectangle {
		return _setToBounds(left, top, right, bottom)
	}

	fun setToSize(x:Double, y:Double, width:Double, height:Double): Rectangle {
		return _setToBounds(x, y, x + width, y + height)
	}

	fun copyFrom(that: Rectangle): Rectangle = setToBounds(that.left, that.top, that.right, that.bottom)
}
