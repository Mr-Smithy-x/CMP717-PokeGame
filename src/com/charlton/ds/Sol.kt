package com.charlton.ds

class Sol {

    val binary = 0b00000000_00000000_00000000_00000000

    fun frequencySort(string: String): String {
        val hashMap = hashMapOf<Char, Int>()
        var maxSize = 0
        for (s in string) {
            val size = 1 + (hashMap[s]?.shr(8) ?: 0) shl 8
            if (size shr 8 > maxSize) maxSize = size shr 8
            hashMap[s] = s.code + size
        }
        val bucket = Array(maxSize+1) { "" }
        for (s in hashMap.values) {
            bucket[s shr 8] += Char(s and 0xFF).toString().repeat(s shr 8)
        }
        val s = StringBuilder()
        for (i in bucket.size-1 downTo  0) {
            if(bucket[i].isEmpty()) continue
            s.append(bucket[i])
        }
        return s.toString()
    }

    fun testCases(string: String): String {
        val treeMap = hashMapOf<Char,Int>()
        var maxSize = 0
        for (s in string) {
            val size = 1 + (treeMap[s]?.shr(8) ?: 0) shl 8
            if (size shr 8 > maxSize) maxSize = size shr 8
            treeMap[s] = s.code + size
        }
        val bucket = Array(maxSize+1) { "" }
        for (s in treeMap.values) {
            bucket[s shr 8] += Char(s and 0xFF).toString().repeat(s shr 8)
        }
        /*var s = ""
        for (a in )
        for (i in bucket.size-1 downTo  0) {
            if(bucket[i].isEmpty()) continue
            s += bucket[i]
        }*/
        return bucket.reversedArray().joinToString("")

        /*val sorted  = hashMap.values.sortedDescending()

        return sorted.joinToString("") { s ->
            Char(s and 0xFF).toString().repeat(s shr 8)
        }*/
    }

}