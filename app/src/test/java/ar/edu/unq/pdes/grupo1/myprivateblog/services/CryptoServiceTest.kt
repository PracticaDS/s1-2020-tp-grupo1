package ar.edu.unq.pdes.grupo1.myprivateblog.services


import org.junit.Assert.assertEquals
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
}

