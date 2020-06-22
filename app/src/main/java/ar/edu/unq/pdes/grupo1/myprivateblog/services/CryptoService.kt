package ar.edu.unq.pdes.grupo1.myprivateblog.services

import java.io.ByteArrayInputStream
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
    private val charset = Charsets.UTF_8

    fun encrypt(input: String): String {
        return encrypt(input.toByteArray(charset)).toString(charset)
    }

    fun encrypt(input: ByteArray): ByteArray {
        val istream = ByteArrayInputStream(input)
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
return decrypt(input.toByteArray(charset)).toString(charset)
    }

    fun decrypt(input: ByteArray): ByteArray {
        val istream = ByteArrayInputStream(input)
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
}
