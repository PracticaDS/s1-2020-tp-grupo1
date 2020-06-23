package ar.edu.unq.pdes.grupo1.myprivateblog.services


import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class CryptoServiceTest {

    lateinit var cryptoService: CryptoService
    lateinit var password: String
    lateinit var clearText: String

    @Before
    fun initCryptoService() {
        cryptoService = CryptoService()
    }

    @Before
    fun initPassword() {
        password = "pepita123456789frgtyhuidcvbnmertyu"
    }

    @Before
    fun initClearText() {
        clearText = "super secret"
    }

    @ExperimentalStdlibApi
    @Test
    fun encriptingAndDecriptingAStringShouldReturnAnEqualString() {
        val istream = ByteArrayInputStream(clearText.toByteArray())
        val ostream = ByteArrayOutputStream()

        cryptoService.encrypt(istream, ostream)

        val encripted = ostream.toByteArray()

        val istream2 = ByteArrayInputStream(encripted)
        val ostream2 = ByteArrayOutputStream()


        cryptoService.decrypt(
            istream2,
            ostream2
        )

        val decripted = ostream2.toByteArray()

        assertEquals(
            clearText,
            decripted.decodeToString()
        )
    }

    @Test
    fun encriptingAByteArrayAndDecriptingItShouldReturnAnEqualByteArray() {
        val expected = clearText.toByteArray()
        val actual = cryptoService.decrypt(cryptoService.encrypt(clearText.toByteArray()))

        assertArrayEquals(
            expected,
            actual
        )
    }

    @Test
    fun encriptingAByteArrayShouldReturnANonEqualByteArray() {
        val clearTextByteArray = clearText.toByteArray()

        assertNotEquals(
            clearTextByteArray,
            cryptoService.encrypt(clearTextByteArray)
        )
    }
    @Test
    fun decriptingAByteArrayShouldReturnANonEqualByteArray() {
        val clearTextByteArray = clearText.toByteArray()

        assertNotEquals(
            clearTextByteArray,
            cryptoService.decrypt(clearTextByteArray)
        )
    }

    @Test
    fun encriptingAStringAndDecriptingItShouldReturnAnEqualString() {
        assertEquals(
            clearText,
            cryptoService.decrypt(cryptoService.encrypt(clearText))
        )
    }

    @Test
    fun encriptingAStringShouldReturnANonEqualString() {
        assertNotEquals(
            clearText,
            cryptoService.encrypt(clearText)
        )
    }
}

