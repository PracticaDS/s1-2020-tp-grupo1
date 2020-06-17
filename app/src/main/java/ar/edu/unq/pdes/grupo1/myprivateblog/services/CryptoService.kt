package ar.edu.unq.pdes.grupo1.myprivateblog.services

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.*
import java.security.*
import java.security.spec.KeySpec
import java.util.stream.Stream
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class CryptoService {
    private val keySpecAlgorithm: String = "AES"
    private val keyFactoryAlgorithm = "PBKDF2WithHmacSHA1"
    private val transformations: String = "AES/CBC/PKCS5Padding"
    private val keySpecIterationCount = 65536
    private val keySpecKeyLength = 256
    private val password = "pepita"

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
