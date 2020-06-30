package ar.edu.unq.pdes.grupo1.myprivateblog.services


import android.content.Context
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.mockito.Mockito.`when`
import java.io.File
import java.io.FileOutputStream

@RunWith(MockitoJUnitRunner.Silent::class)
class CryptoServiceTest {

    lateinit var cryptoService: CryptoService
    lateinit var clearText: String
    @Mock
    private lateinit var context: Context

    @Before
    fun initCryptoService() {
        `when`(context.openFileOutput("password",Context.MODE_PRIVATE))
            .thenReturn(FileOutputStream("password"))

        cryptoService = CryptoService(context)
        cryptoService.generatePassword()
    }

    @After
    fun tearDown(){
       File(context.filesDir,"password").delete()
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

