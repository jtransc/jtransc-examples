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

class StackPool<T>(private val capacity: Int = 0, private val generator: (index: Int) -> T) {
	private val values = arrayListOf<T>()
	private var index = 0

	init {
		ensure(capacity)
	}

	private fun ensure(count: Int) {
		while (values.size < count) values.add(generator(values.size))
	}

	val length: Int get() = index

	fun push(): T {
		ensure(index)
		return values[index++]
	}

	fun pop(): T {
		return values[--index]
	}

	fun clear() {
		index = 0
	}
}
