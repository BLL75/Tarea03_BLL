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
 * Clase para gestionar el registro de usuarios en la aplicación.
 */
public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView loginTextView;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                IdpResponse response = IdpResponse.fromResultIntent(result.getData());

                if (result.getResultCode() == RESULT_OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    showToast(R.string.register_success);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    showToast(R.string.register_failed);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Vincular vistas con sus IDs
        emailEditText = findViewById(R.id.editTextEmailRegister);
        passwordEditText = findViewById(R.id.editTextPasswordRegister);
        registerButton = findViewById(R.id.buttonRegister);
        loginTextView = findViewById(R.id.textViewLogin);

        // Configurar el botón de registro
        registerButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showToast(R.string.error_empty_fields);
                return;
            }

            // Crear un nuevo usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            showToast(R.string.register_success);
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            showToast(R.string.register_failed);
                        }
                    });
        });

        // Configurar el enlace para volver al login
        loginTextView.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }

    /**
     * Muestra un Toast con el mensaje especificado.
     *
     * @param messageResId ID del recurso de texto a mostrar.
     */
    private void showToast(int messageResId) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show();
    }
}
