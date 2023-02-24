package tanoshi.source.api.cryptoaes
// Thanks to Vlad on Stackoverflow: https://stackoverflow.com/a/63701411

import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoAES {

    private const val KEY_SIZE = 256
    private const val IV_SIZE = 128
    private const val HASH_CIPHER = "AES/CBC/PKCS5PADDING"
    private const val AES = "AES"
    private const val KDF_DIGEST = "MD5"

    fun decrypt(cipherText: String, password: String): String {
        try {
            val ctBytes = Base64.getDecoder().decode( /* src = */ cipherText )
            val saltBytes = Arrays.copyOfRange(ctBytes, 8, 16)
            val cipherTextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.size)
            val md5: MessageDigest = MessageDigest.getInstance("MD5")
            val keyAndIV = generateKeyAndIV(32, 16, 1, saltBytes, password.toByteArray(Charsets.UTF_8), md5)
            return decryptAES(cipherTextBytes,
                keyAndIV?.get(0) ?: ByteArray(32),
                keyAndIV?.get(1) ?: ByteArray(16))
        } catch (e: Exception) {
            return ""
        }
    }

    fun decrypt(cipherText: String, keyBytes: ByteArray, ivBytes: ByteArray): String {
        return try {
            val cipherTextBytes = Base64.getDecoder().decode(cipherText )
            decryptAES(cipherTextBytes, keyBytes, ivBytes)
        } catch (e: Exception) {
            ""
        }
    }

    private fun decryptAES(cipherTextBytes: ByteArray, keyBytes: ByteArray, ivBytes: ByteArray): String {
        return try {
            val cipher = Cipher.getInstance(HASH_CIPHER)
            val keyS = SecretKeySpec(keyBytes, AES)
            cipher.init(Cipher.DECRYPT_MODE, keyS, IvParameterSpec(ivBytes))
            cipher.doFinal(cipherTextBytes).toString(Charsets.UTF_8)
        } catch (e: Exception) {
            println( e )
            ""
        }
    }

    private fun generateKeyAndIV(keyLength: Int, ivLength: Int, iterations: Int, salt: ByteArray, password: ByteArray, md: MessageDigest): Array<ByteArray?>? {
        val digestLength = md.digestLength
        val requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength
        val generatedData = ByteArray(requiredLength)
        var generatedLength = 0
        return try {
            md.reset()

            while (generatedLength < keyLength + ivLength) {

                if (generatedLength > 0) md.update(generatedData, generatedLength - digestLength, digestLength)
                md.update(password)
                if (salt != null) md.update(salt, 0, 8)
                md.digest(generatedData, generatedLength, digestLength)

                for (i in 1 until iterations) {
                    md.update(generatedData, generatedLength, digestLength)
                    md.digest(generatedData, generatedLength, digestLength)
                }
                generatedLength += digestLength
            }

            val result = arrayOfNulls<ByteArray>(2)
            result[0] = generatedData.copyOfRange(0, keyLength)
            if (ivLength > 0) result[1] = generatedData.copyOfRange(keyLength, keyLength + ivLength)
            result
        } catch (e: Exception) {
            throw e
        } finally {
            Arrays.fill(generatedData, 0.toByte())
        }
    }
}
