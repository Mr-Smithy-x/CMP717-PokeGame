package com.charlton.ds

/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 * Input: s = "ADOBECODEBANC", t = "ABC"
 *
 * A - 0, 10
 * D - 1, 7
 * O - 2, 6
 * B - 3, 9
 * E - 4, 8
 * C - 5, 12
 * N - 11
 *
 * A -
 * B -
 * C -
Output: "BANC"
Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
 */
class Solutions {
    fun minWindow(s: String, t: String): String {
        val check = t.all { it in s }
        if (!check) return ""
        var list = arrayListOf<ArrayList<Int>>()
        var range = arrayListOf<Int>()
        var subList = t.toCharArray().toMutableList()
        var position = s.indices
        var i = 0
        while (i <= position.last) {
            val currentChar = s[i]
            range.add(i)
            if (currentChar in subList) {
                if (s[range[0]] !in t) {
                    range.clear()
                    range.add(i)
                }
                subList.remove(currentChar)
                if (subList.size == 0) {
                    subList = t.toCharArray().toMutableList()
                    list.add(range.clone() as ArrayList<Int>)
                    range.clear()

                    val first = position.first

                    i = first
                    position = IntRange(first + 1, position.last)
                }
            }
            i++
        }


        list.sortBy { it.size }

        return if (list.isEmpty()) return "" else list[0].map { s[it] }.joinToString("")
    }
}