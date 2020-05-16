package ar.edu.unq.pdes.myprivateblog.screens.sign_in

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_sign_in

    private val RC_SIGN_IN = 123

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
        } else {
            // not signed in
        }

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(AuthUI.IdpConfig.GoogleBuilder().build())
                )
                .build(),
            RC_SIGN_IN
        )

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == RC_SIGN_IN) {
                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    // Successfully signed in
                    val user = FirebaseAuth.getInstance().currentUser
                    //pasar al otro layout
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                }
            }
        }


    }
}
