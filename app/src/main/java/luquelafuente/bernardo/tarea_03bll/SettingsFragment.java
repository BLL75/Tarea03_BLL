package luquelafuente.bernardo.tarea_03bll;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

/**
 * Fragmento para gestionar los ajustes de la aplicación, incluyendo idioma,
 * swipe para eliminar, información "Acerca de" y cerrar sesión.
 */
public class SettingsFragment extends Fragment {

    private Button buttonLogout;
    private Button buttonAbout;
    private Switch swipeToDeleteSwitch;
    private Spinner languageSpinner;

    private FirebaseAuth auth;

    // Claves para SharedPreferences
    private static final String PREFS_NAME = "settings_preferences";
    private static final String KEY_SWIPE_TO_DELETE = "swipe_to_delete_enabled";
    private static final String KEY_LANGUAGE = "app_language";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inicializar vistas
        buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonAbout = view.findViewById(R.id.button_about);
        swipeToDeleteSwitch = view.findViewById(R.id.switch_swipe_to_delete);
        languageSpinner = view.findViewById(R.id.language_spinner);

        auth = FirebaseAuth.getInstance();

        // Configurar SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, 0);

        // Configurar el estado del switch de Swipe to Delete
        boolean isSwipeEnabled = sharedPreferences.getBoolean(KEY_SWIPE_TO_DELETE, true);
        swipeToDeleteSwitch.setChecked(isSwipeEnabled);
        swipeToDeleteSwitch.setText(getString(R.string.switch_swipe_to_delete));

        swipeToDeleteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_SWIPE_TO_DELETE, isChecked).apply();
        });

        // Configurar el Spinner de idioma
        String currentLanguage = sharedPreferences.getString(KEY_LANGUAGE, "es");
        languageSpinner.setSelection(currentLanguage.equals("es") ? 0 : 1);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (position == 0) ? "es" : "en";
                Log.d("LANGUAGE", "Idioma seleccionado: " + selectedLanguage);

                // Solo cambiar el idioma si es diferente del actual
                if (!selectedLanguage.equals(currentLanguage)) {
                    sharedPreferences.edit().putString(KEY_LANGUAGE, selectedLanguage).apply();
                    setLocale(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
        });

        // Configurar el botón "Acerca de"
        buttonAbout.setOnClickListener(v -> showAboutDialog());

        // Configurar el botón "Cerrar sesión"
        buttonLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }

    /**
     * Cambia el idioma de la aplicación y reinicia la actividad actual para aplicar el cambio.
     *
     * @param languageCode Código del idioma seleccionado (e.g., "es", "en").
     */
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        requireActivity().getResources().updateConfiguration(config, requireActivity().getResources().getDisplayMetrics());

        Log.d("LANGUAGE", "Idioma cambiado a: " + languageCode);

        // Reiniciar la actividad para aplicar los cambios
        requireActivity().recreate();
    }

    /**
     * Muestra un cuadro de diálogo "Acerca de" con información sobre el desarrollador y la versión.
     */
    private void showAboutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.about_title))
                .setMessage(getString(R.string.about_message))
                .setPositiveButton(getString(android.R.string.ok), null)
                .show();
    }
}
