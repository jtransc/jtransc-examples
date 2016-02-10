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

class Batch {
	@JvmField var startIndex = 0
	@JvmField var triangleCount = 0 // triangleCount
	@JvmField var texture = 0 // texture(int)
	@JvmField var blendMode = 1 // blendMode(int) BLEND_NORMAL=1, BLEND_ADD=8
	@JvmField var maskType = 0
	@JvmField var stencilIndex = 0
	@JvmField var scissors = Rectangle()

	fun reset() {
		startIndex = 0
		triangleCount = 0
		texture = 0
		blendMode = 1
		maskType = 0
		stencilIndex = 0
		scissors.setToBounds(0.0, 0.0, 0.0, 0.0)
	}

	fun write(batches:IntArray, offset:Int) {
		batches[offset + 0] = startIndex
		batches[offset + 1] = triangleCount
		batches[offset + 2] = texture
		batches[offset + 3] = blendMode
		batches[offset + 4] = maskType
		batches[offset + 5] = stencilIndex
		batches[offset + 6] = scissors.left.toInt()
		batches[offset + 7] = scissors.top.toInt()
		batches[offset + 8] = scissors.right.toInt()
		batches[offset + 9] = scissors.bottom.toInt()
		batches[offset + 10] = 0
		batches[offset + 11] = 0
		batches[offset + 12] = 0
		batches[offset + 13] = 0
		batches[offset + 14] = 0
		batches[offset + 15] = 0
	}
}

class BatchBuilder {
	val vertices = FastMemory(4 * 6 * 16 * 1024)
	val verticesFloat = FastMemory4Float(vertices)
	val verticesInt = FastMemory4Int(vertices)
	val indices = ShortArray(6 * 1024 * 6)
	val batches = IntArray(16 * 256)

	var verticesIndex = 0
	var indicesIndex = 0
	var batchCount = 0

	// Data
	var current = Batch()

	fun start() {
		Mem.select(vertices)
	}

	fun reset() {
		verticesIndex = 0
		indicesIndex = 0
		batchCount = 0
		current.reset()
	}

	private fun flush() {
		if (current.triangleCount <= 0) return
		current.write(batches, batchCount * 16)
		batchCount++
		current.startIndex = indicesIndex
		current.triangleCount = 0
	}

	fun quad(texture:Int, p0: Point, p1: Point, p2: Point, p3: Point, tx0: Float, ty0: Float, tx1: Float, ty1: Float) {
		if (current.texture != texture) {
			flush()
			current.texture = texture
		}
		val vii = verticesIndex
		val vi = vii * 6
		val i = indices
		val ii = indicesIndex

		//println("${p0.x}, ${p0.y}, ${p1.x}, ${p1.y} :: $x, $y, $w, $h :: $matrix")
		Mem.sf32(vi +  0, p0.x.toFloat())
		Mem.sf32(vi +  1, p0.y.toFloat())
		Mem.sf32(vi +  2, tx0)
		Mem.sf32(vi +  3, ty0)
		Mem.si32(vi +  4, 0)
		Mem.si32(vi +  5, 0)

		Mem.sf32(vi +  6, p1.x.toFloat())
		Mem.sf32(vi +  7, p1.y.toFloat())
		Mem.sf32(vi +  8, tx1)
		Mem.sf32(vi +  9, ty0)
		Mem.si32(vi + 10, 0)
		Mem.si32(vi + 11, 0)

		Mem.sf32(vi + 12, p2.x.toFloat())
		Mem.sf32(vi + 13, p2.y.toFloat())
		Mem.sf32(vi + 14, tx0)
		Mem.sf32(vi + 15, ty1)
		Mem.si32(vi + 16, 0)
		Mem.si32(vi + 17, 0)

		Mem.sf32(vi + 18, p3.x.toFloat())
		Mem.sf32(vi + 19, p3.y.toFloat())
		Mem.sf32(vi + 20, tx1)
		Mem.sf32(vi + 21, ty1)
		Mem.si32(vi + 22, 0)
		Mem.si32(vi + 23, 0)

		i[ii + 0] = (vii + 0).toShort()
		i[ii + 1] = (vii + 1).toShort()
		i[ii + 2] = (vii + 2).toShort()
		i[ii + 3] = (vii + 1).toShort()
		i[ii + 4] = (vii + 3).toShort()
		i[ii + 5] = (vii + 2).toShort()

		verticesIndex += 4
		indicesIndex += 6
		current.triangleCount += 2
	}

	fun render() {
		flush()
		//println(vertices.slice(0 until 4 * 6))
		//println(indices.slice(0 until 6))
		//println(batches[0])
		//println(batches[1])
		//println(batches[2])
		//println(batches[3])
		JTranscRender.render(vertices, verticesIndex, indices, indicesIndex, batches, batchCount)
	}
}
