package felix.andrea.iniciogoogle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task



class MainActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient

    val COD_LOGOUT = 123
    val RC_SIGN_IN = 456

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.sign_in_button)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        if (requestCode == COD_LOGOUT){
            signOut()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if(account != null){
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.putExtra("name", account.displayName)
            intent.putExtra("email", account.email)
            startActivityForResult(intent,COD_LOGOUT)
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }
    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                Toast.makeText(this,"Sesion terminada",Toast.LENGTH_SHORT).show()
            }
    }
}