package ar.edu.unq.pdes.grupo1.myprivateblog.screens.sign_in

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.grupo1.myprivateblog.BaseFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {

                // Write a message to the database
                val database = Firebase.database
                val myRef = database.getReference("message")

                myRef.setValue("Hello, World!")

                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                navigateToBlogEntriesList()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private fun navigateToBlogEntriesList() {
        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToPostsListingFragment())
    }
}
