package com.charlton.ds


/**
 * 2.)
 * Given: Array A of n-1 integer
 * all values v where 1 <= v <= n and contains no duplicates
 * Task: find the missing number
 * Contraints: O(n) times. memory space of only O(1) times
 */
object MissingNumber {


    fun findMissingNumberCondensed2(numbers: IntArray): Int {
        var xorResult = numbers.size
        // XOR all numbers from 1 to n
        for (i in 0..numbers.size-1) {
            print("current_xor: ${xorResult.toBinaryString()}, ")
            xorResult = xorResult.xor(i)
            print("i: ${i.toBinaryString()}, new_xor: ${xorResult.toBinaryString()}")

            xorResult = xorResult.xor(numbers[i])
            println(", num: ${numbers[i].toBinaryString()}, new_xor_2: ${xorResult.toBinaryString()}")

        }
        return xorResult
    }
    fun findMissingNumberCondensed(numbers: IntArray): Int {
        var xorResult = 0
        // XOR all numbers from 1 to n
        for (i in 1..<numbers.size + 2) {
            print("current_xor: ${xorResult.toBinaryString()}, ")
            xorResult = xorResult.xor(i)
            print("i: ${i.toBinaryString()}, new_xor: ${xorResult.toBinaryString()}")

            if (i - 1 < numbers.size) {
                xorResult = xorResult.xor(numbers[i - 1])
                println(", num: ${numbers[i - 1].toBinaryString()}, new_xor_2: ${xorResult.toBinaryString()}")
            } else {
                println()
            }
        }
        return xorResult
    }

    fun findMissingNumberTwo(numbers: IntArray): Int {
        var xorResult = 0

        // XOR all numbers from 1 to n
        for (i in 1..<numbers.size + 2) {
            print("current_xor: ${xorResult.toBinaryString()}, ")
            xorResult = xorResult.xor(i)
            println("i: ${i.toBinaryString()}, new_xor: ${xorResult.toBinaryString()}")
        }

        println()
        // XOR with all elements in the array
        for (number in numbers) {
            print("current_xor: ${xorResult.toBinaryString()}, ")
            xorResult = xorResult.xor(number)
            println("num: ${number.toBinaryString()}, new_xor: ${xorResult.toBinaryString()}")
        }

        return xorResult
    }


    fun findMissingNumber(arr: IntArray): Int {
        val expected_sum = ((arr.size + 1) * (arr.size + 2) / 2)
        var actual_sum = 0
        for (i in arr) {
            actual_sum += i
        }
        val missing_number = expected_sum - actual_sum
        return missing_number
    }
}

/**
 * Given: very long sorted array A of unknown length n
 * Task: Search it for target value x in time O(log n).
 * For A m >= n, A[m] === inf or null exception
 */
object BinarySearch {


    fun searchExample(arr: IntArray, target: Int): Int {
        var index = 1
        var left = 0
        var right = 0
        while (true) {
            try {
                if (arr[index] == target) {
                    return index
                } else if (arr[index] < target) {
                    index *= 2
                } else {
                    right = index
                    break
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                right = index
                break
            }
        }

        while (left < right) {
            val mid = (left + right) / 2
            try {
                val value = arr[mid]
                if (value == target) {
                    return mid
                } else if (target > value) {
                    left = mid + 1
                } else {
                    right = mid
                }
            } catch (e: ArrayIndexOutOfBoundsException) {
                right = mid
            }
        }
        return -1
    }


    /**
     * Binary Search
     */
    fun search(arr: IntArray, target: Int): Int {
        var left = 0
        var right = arr.size
        while (left < right) {
            val mid = (left + right) / 2
            val value = arr[mid]
            if (value == target) {
                return mid
            } else if (target > value) {
                left = mid + 1
            } else {
                right = mid
            }
        }
        return -1
    }
}

object Scheduler {

    fun run(p1: String, p2: String, time: Int) {
        var current_time_limit = time
        while(true) {
            val result_p1 = runWithTimeout(p1, current_time_limit)
            val result_p2 = runWithTimeout(p2, current_time_limit)

            if(result_p1 != null) {
                println("Meaning of life: $result_p1")
                break
            } else {
                println(result_p2)
                current_time_limit *= 2
            }
        }
    }

    fun run_two(p1: String, p2: String, time: Int) {
        var current_time_limit = time
        while(true) {
            val result_p1 = runWithTimeout(p1, current_time_limit)
            if(result_p1 != null) {
                println("Meaning of life: $result_p1")
                break
            } else {
                runWithTimeout(p2, current_time_limit)
                current_time_limit *= 2
            }
        }
    }

    private fun runWithTimeout(p1: String, time: Int): String? {
        return null
    }
}


fun Int.toBinaryString(): String {
    return "0b${this.toString(2).padStart(Byte.SIZE_BITS, '0')}"
}

fun main() {


    //val arr = intArrayOf(1, 2, 3, 8, 9, 10, 11, 12, 17, 18, 19, 20, 21, 22, 34, 35)//  # Assuming one number is missing
    //println(BinarySearch.searchExample(arr, 34))


    val arr = intArrayOf(1, 3, 4)//  # Assuming one number is missing
    //println(MissingNumber.find_missing_number(arr))
    println(MissingNumber.findMissingNumberCondensed2(arr))

}

