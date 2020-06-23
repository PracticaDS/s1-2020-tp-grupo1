package ar.edu.unq.pdes.grupo1.myprivateblog.services

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

@OptIn(ExperimentalStdlibApi::class)
class CryptoService {
    private val keySpecAlgorithm: String = "AES"
    private val keyFactoryAlgorithm = "PBKDF2WithHmacSHA1"
    private val transformations: String = "AES/CBC/PKCS5Padding"
    private val keySpecIterationCount = 65536
    private val keySpecKeyLength = 256
    private val password = "pepita"

    fun encrypt(input: String): String {
        return toString(encrypt(input.toByteArray()))
    }

    fun encrypt(input: ByteArray): ByteArray {
        val istream = input.inputStream()
        val ostream = ByteArrayOutputStream()

        encrypt(istream, ostream)

        return ostream.toByteArray()
    }

    fun encrypt(input: InputStream, os: OutputStream) {
        val cipher = Cipher.getInstance(transformations)

        val iv = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(iv)
        os.write(iv)

        val salt = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(salt)
        os.write(salt)

        val secret = getSecretKeySpec(password, salt)
        cipher.init(Cipher.ENCRYPT_MODE, secret, IvParameterSpec(iv))

        val cos = CipherOutputStream(os, cipher)

        input.copyTo(cos)

        cos.close()
    }

    fun decrypt(input: String): String {
        return String(decrypt(toByteArray(input)))
    }

    fun decrypt(input: ByteArray): ByteArray {
        val istream = input.inputStream()
        val ostream = ByteArrayOutputStream()


        decrypt(
            istream,
            ostream
        )

        return ostream.toByteArray()
    }

    fun decrypt(input: InputStream, output: OutputStream) {
        val cipher = Cipher.getInstance(transformations)

        val iv = ByteArray(cipher.blockSize)
        input.read(iv)

        val salt = ByteArray(cipher.blockSize)
        input.read(salt)

        val secret = getSecretKeySpec(password, salt)
        cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))

        val cis = CipherInputStream(input, cipher)
        cis.copyTo(output)
    }

    private fun getSecretKeySpec(
        password: String,
        salt: ByteArray
    ): SecretKeySpec {
        val spec: KeySpec = PBEKeySpec(
            password.toCharArray(),
            salt,
            keySpecIterationCount,
            keySpecKeyLength
        )
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(keyFactoryAlgorithm)
        val secretKey: SecretKey = factory.generateSecret(spec)
        return SecretKeySpec(secretKey.encoded, keySpecAlgorithm)
    }

    private fun toByteArray(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len) result[i] = Integer.valueOf(
            hexString.substring(2 * i, 2 * i + 2),
            16
        ).toByte()
        return result
    }

    private fun toString(buf: ByteArray): String {
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
