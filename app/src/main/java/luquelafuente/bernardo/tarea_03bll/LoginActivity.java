package luquelafuente.bernardo.tarea_03bll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Clase LoginActivity
 *
 * Gestiona la autenticación de los usuarios mediante correo y contraseña,
 * o autenticación mediante Google.
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button loginGoogleButton;
    private TextView registerTextView;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                IdpResponse response = IdpResponse.fromResultIntent(result.getData());

                if (result.getResultCode() == RESULT_OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(getBaseContext(), getString(R.string.login_success), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Vincular elementos del layout
        emailEditText = findViewById(R.id.editTextEmailLogin);
        passwordEditText = findViewById(R.id.editTextPasswordLogin);
        loginButton = findViewById(R.id.buttonLogin);
        registerTextView = findViewById(R.id.textViewRegister);
        loginGoogleButton = findViewById(R.id.buttonLoginGoogle);

        // Configurar listeners para botones
        configureLoginButton();
        configureRegisterTextView();
        configureGoogleLoginButton();
    }

    /**
     * Configura el botón de inicio de sesión con correo y contraseña.
     */
    private void configureLoginButton() {
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_LONG).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(getBaseContext(), getString(R.string.login_success), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    /**
     * Configura el texto para registrar un nuevo usuario.
     */
    private void configureRegisterTextView() {
        registerTextView.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    /**
     * Configura el botón para iniciar sesión con Google.
     */
    private void configureGoogleLoginButton() {
        loginGoogleButton.setOnClickListener(view -> createSignInIntent());
    }

    /**
     * Lanza la actividad para autenticación con FirebaseUI.
     */
    private void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_pokemon_logo)
                .setTheme(R.style.FirebaseUIAuthTheme)
                .build();
        signInLauncher.launch(signInIntent);
    }
}
