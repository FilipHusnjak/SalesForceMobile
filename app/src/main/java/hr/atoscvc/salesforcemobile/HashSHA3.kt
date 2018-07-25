package hr.atoscvc.salesforcemobile

import org.walleth.sha3.SHA3Parameter
import org.walleth.sha3.calculateSHA3

object HashSHA3 {
    fun getHashedValue(value: String): String {
        val hexChars = "0123456789abcdef"
        val valueHashed = value.calculateSHA3(SHA3Parameter.SHA3_512)
        val stringBuilder = StringBuilder(valueHashed.size * 2)

        for (i in 0 until valueHashed.size) {
            val byte = valueHashed[i]
            val left = byte.toInt().ushr(4).and(0b1111)
            val right = byte.toInt().and(0b1111)
            stringBuilder.append(hexChars[left]).append(hexChars[right])
        }

        return stringBuilder.toString()
    }
}