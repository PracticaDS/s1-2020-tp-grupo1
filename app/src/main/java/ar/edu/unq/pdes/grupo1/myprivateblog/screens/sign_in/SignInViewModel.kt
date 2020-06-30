package ar.edu.unq.pdes.grupo1.myprivateblog.screens.sign_in

import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.services.CryptoService
import javax.inject.Inject


class SignInViewModel @Inject constructor(
    private val cryptoService: CryptoService
) : ViewModel() {

    fun generatePassword():Unit{
        cryptoService.generatePassword()
    }

}
