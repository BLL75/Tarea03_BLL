package luquelafuente.bernardo.tarea_03bll;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private Button buttonLogout;
    private Button buttonAbout;
    private Switch swipeToDeleteSwitch;

    private FirebaseAuth auth;

    // Claves para SharedPreferences
    private static final String PREFS_NAME = "settings_preferences";
    private static final String KEY_SWIPE_TO_DELETE = "swipe_to_delete_enabled";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inicializar elementos de la vista
        buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonAbout = view.findViewById(R.id.button_about);
        swipeToDeleteSwitch = view.findViewById(R.id.switch_swipe_to_delete);

        auth = FirebaseAuth.getInstance();

        // Configurar SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, 0);
        boolean isSwipeEnabled = sharedPreferences.getBoolean(KEY_SWIPE_TO_DELETE, true);
        swipeToDeleteSwitch.setChecked(isSwipeEnabled);

        // Listener del Switch
        swipeToDeleteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Guardar el estado del switch en SharedPreferences
            sharedPreferences.edit().putBoolean(KEY_SWIPE_TO_DELETE, isChecked).apply();
        });

        // Listener del botón "Acerca de"
        buttonAbout.setOnClickListener(v -> showAboutDialog());

        // Listener del botón de cerrar sesión
        buttonLogout.setOnClickListener(view1 -> {
            auth.signOut(); // Cierra la sesión
            startActivity(new Intent(getContext(), LoginActivity.class)); // Redirige al LoginActivity
            requireActivity().finish(); // Finaliza la actividad actual
        });

        return view;
    }

    // Método para mostrar el diálogo "Acerca de"
    private void showAboutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Acerca de")
                .setMessage("Desarrollador: Bernardo Luque\nVersión: 1.0.0")
                .setPositiveButton("Cerrar", null)
                .show();
    }
}

