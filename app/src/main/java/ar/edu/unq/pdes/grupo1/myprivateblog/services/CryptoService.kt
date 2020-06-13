package ar.edu.unq.pdes.grupo1.myprivateblog.services

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class CryptoService {
    private val keySpecAlgorithm: String = "AES"
    private val keyFactoryAlgorithm = "PBKDF2WithHmacSHA1"
    private val transformations: String = "AES/CBC/PKCS5Padding"

    fun encrypt(input: String, password: String): String {
        val cipher = Cipher.getInstance(transformations)

        val salt = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(salt)

        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(keyFactoryAlgorithm)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        val secret = SecretKeySpec(tmp.encoded, keySpecAlgorithm)

        val iv = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secret, IvParameterSpec(iv))

        val os = ByteArrayOutputStream()
        os.write(iv)
        os.write(salt)
        os.write(
            cipher.doFinal(input.toByteArray())
        )
        return os.toString()
    }

    fun decrypt(encrypted: String, password: String): String {
        val cipher = Cipher.getInstance(transformations)

        val encryptedByteArray = encrypted.toByteArray()

        val instream = ByteArrayInputStream(encryptedByteArray, 0, encryptedByteArray.size)
        val iv = ByteArray(cipher.blockSize)
        instream.read(iv)
        val salt = ByteArray(cipher.blockSize)
        instream.read(salt)
        val msg = ByteArray(encryptedByteArray.size - cipher.blockSize - cipher.blockSize)
        instream.read(msg)

        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(keyFactoryAlgorithm)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        val secret = SecretKeySpec(tmp.encoded, keySpecAlgorithm)

        cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))
        return cipher.doFinal(msg).toString()
    }

    private fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(
            hexString.substring(2 * i, 2 * i + 2),
            16
        ).toByte()
        return result
    }

    private fun toHex(buf: ByteArray?): String {
        if (buf == null) return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i])
        }
        return result.toString()
    }

    private val HEX = "0123456789ABCDEF"

    private fun appendHex(sb: StringBuffer, b: Byte) {
        sb.append(HEX[(b.toInt() shr 4) and 0x0f]).append(HEX[0x0f.and(b.toInt())])
    }
}

