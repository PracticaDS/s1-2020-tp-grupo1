package ar.edu.unq.pdes.grupo1.myprivateblog.services


import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class CryptoServiceTest {

    lateinit var criptoService: CryptoService
    lateinit var password: String
    lateinit var clearText: String

    @Before
    fun initCryptoService() {
        criptoService = CryptoService()
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

        criptoService.encrypt(istream, ostream)

        val encripted = ostream.toByteArray()

        val istream2 = ByteArrayInputStream(encripted)
        val ostream2 = ByteArrayOutputStream()


        criptoService.decrypt(
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
        val actual = criptoService.decrypt(criptoService.encrypt(clearText.toByteArray()))

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
            criptoService.encrypt(clearTextByteArray)
        )
    }
    @Test
    fun decriptingAByteArrayShouldReturnANonEqualByteArray() {
        val clearTextByteArray = clearText.toByteArray()

        assertNotEquals(
            clearTextByteArray,
            criptoService.decrypt(clearTextByteArray)
        )
    }

    @Test
    fun encriptingAStringAndDecriptingItShouldReturnAnEqualString() {
        assertEquals(
            clearText,
            criptoService.decrypt(criptoService.encrypt(clearText))
        )
    }

    @Test
    fun encriptingAStringShouldReturnANonEqualString() {
        assertNotEquals(
            clearText,
            criptoService.encrypt(clearText)
        )
    }
    @Test
    fun decriptingAStringShouldReturnANonEqualString() {
        assertNotEquals(
            clearText,
            criptoService.decrypt(clearText)
        )
    }

    @ExperimentalStdlibApi
    @Test
    fun pepita() {
        val str = "super secrets"
        assertEquals(
            str,
            str.toByteArray().decodeToString()
        )
    }
}

