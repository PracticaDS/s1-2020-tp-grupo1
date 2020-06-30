package ar.edu.unq.pdes.grupo1.myprivateblog.services

import android.content.Context
import java.io.*
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


@OptIn(ExperimentalStdlibApi::class)
class CryptoService @Inject constructor(private val context:Context){
    private val keySpecAlgorithm: String = "AES"
    private val keyFactoryAlgorithm = "PBKDF2WithHmacSHA1"
    private val transformations: String = "AES/CBC/PKCS5Padding"
    private val keySpecIterationCount = 65536
    private val keySpecKeyLength = 256
    private var password: String? = null

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

        val secret = getSecretKeySpec(getPasswordB(), salt)
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

        val secret = getSecretKeySpec(getPasswordB(), salt)
        cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))

        val cis = CipherInputStream(input, cipher)
        cis.copyTo(output)
    }

    private fun getSecretKeySpec(password: String,salt: ByteArray): SecretKeySpec {
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

    private val wordPool = listOf<String>(
        "crema",
        "café",
        "estrella",
        "explosión",
        "guitarra",
        "plástico",
        "navaja",
        "martillo",
        "libros",
        "lápiz",
        "lapicera",
        "aluminio",
        "embarcación",
        "letra",
        "agujeta",
        "ventana",
        "librería",
        "sonido",
        "universidad",
        "rueda",
        "perro",
        "llaves",
        "camisa",
        "pelo",
        "papá",
        "sillón",
        "felicidad",
        "catre",
        "teclado",
        "servilleta",
        "escuela",
        "pantalla",
        "sol",
        "codo",
        "tenedor",
        "estadística",
        "mapa",
        "agua",
        "mensaje",
        "lima",
        "cohete",
        "rey",
        "edificio",
        "césped",
        "presidencia",
        "hojas",
        "parlante",
        "colegio",
        "granizo",
        "pestaña",
        "lámpara",
        "mano",
        "monitor",
        "flor",
        "música",
        "hombre",
        "tornillo",
        "habitación",
        "velero",
        "abuela",
        "abuelo",
        "palo",
        "satélite",
        "templo",
        "lentes",
        "bolígrafo",
        "plato",
        "nube",
        "gobierno",
        "botella",
        "castillo",
        "enano",
        "casa",
        "libro",
        "persona",
        "planeta",
        "televisor",
        "guantes",
        "metal",
        "teléfono",
        "proyector",
        "mono",
        "remera",
        "muela",
        "petróleo",
        "percha",
        "remate",
        "debate",
        "anillo",
        "cuaderno",
        "ruido",
        "pared",
        "taladro",
        "herramienta",
        "cartas",
        "chocolate",
        "anteojos",
        "impresora",
        "caramelos",
        "living",
        "luces",
        "angustia",
        "zapato",
        "bomba",
        "lluvia",
        "ojo",
        "corbata",
        "periódico",
        "diente",
        "planta",
        "chupetín",
        "buzo",
        "oficina",
        "persiana",
        "puerta",
        "tío",
        "silla",
        "ensalada",
        "pradera",
        "zoológico",
        "candidato",
        "deporte",
        "recipiente",
        "diarios",
        "fotografía",
        "ave",
        "hierro",
        "refugio",
        "pantalón",
        "barco",
        "carne",
        "nieve",
        "tecla",
        "humedad",
        "pistola",
        "departamento",
        "celular",
        "tristeza",
        "hipopótamo",
        "sofá",
        "cama",
        "árbol",
        "mesada",
        "campera",
        "discurso",
        "auto",
        "cinturón",
        "rúcula",
        "famoso",
        "madera",
        "lentejas",
        "piso",
        "maletín",
        "reloj",
        "diputado",
        "cuchillo",
        "desodorante",
        "candado",
        "luz",
        "montañas",
        "computadora",
        "radio",
        "moño",
        "cuadro",
        "calor",
        "partido",
        "teatro",
        "bife",
        "fiesta",
        "bala",
        "auriculares"
    )


    private val wordsCount = 5

     fun generatePassword(): Unit {
         if (File(context.filesDir,"password").exists()){
             return
         }
         secureSavePassword(List(wordsCount){SecureRandom().nextInt(wordPool.size)})
    }

     private fun secureSavePassword(password: List<Int>): Unit {

        val fileName = "password"
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outputStreamWriter.use { it.write(password.joinToString(".")) }

    }

    fun getPassphrase(): String {
        return getPassword().map{
            wordPool[it]
        }.joinToString(".")
    }

    private fun getPassword():List<Int>{
        return getPasswordB().split(".").map {
            it.toInt()
        }
    }

    private fun getPasswordB():String{

        if(password == null){
            password = File(context.filesDir, "password").readText()
        }
        return password!!

    }

}
