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
    private val keySpecIterationCount = 65536
    private val keySpecKeyLength = 256

    fun encrypt(input: String, password: String): String {
        val cipher = Cipher.getInstance(transformations)

        val salt = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(salt)

        val secret = getSecretKeySpec(password, salt)

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

        val secret = getSecretKeySpec(password, salt)

        cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))
        return cipher.doFinal(msg).toString()
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
