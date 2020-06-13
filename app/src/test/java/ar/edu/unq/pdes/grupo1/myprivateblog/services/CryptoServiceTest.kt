package ar.edu.unq.pdes.grupo1.myprivateblog.services


import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

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

    @Test
    fun encriptingAndDecriptingAStringShouldReturnAnEqualSring() {
        assertEquals(
            criptoService.decrypt(
                criptoService.encrypt(
                    clearText,
                    password
                ),
                password
            ),
            clearText
        )
    }
}

