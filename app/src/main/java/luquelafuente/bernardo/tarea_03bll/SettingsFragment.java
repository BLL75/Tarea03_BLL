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
import androidx.preference.PreferenceManager;

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
    private SharedPreferences sharedPreferences;

    // Claves para SharedPreferences
    private static final String KEY_SWIPE_TO_DELETE = "swipe_to_delete_enabled";
    private static final String KEY_LANGUAGE = "language";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inicializar vistas
        buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonAbout = view.findViewById(R.id.button_about);
        swipeToDeleteSwitch = view.findViewById(R.id.switch_swipe_to_delete);
        languageSpinner = view.findViewById(R.id.language_spinner);

        auth = FirebaseAuth.getInstance();

        // Usar PreferenceManager para obtener SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Configurar el estado del switch de Swipe to Delete
        setupSwipeToDeleteSwitch();

        // Configurar el Spinner de idioma
        setupLanguageSpinner();

        // Configurar el botón "Acerca de"
        buttonAbout.setOnClickListener(v -> showAboutDialog());

        // Configurar el botón "Cerrar sesión"
        buttonLogout.setOnClickListener(v -> showLogoutDialog());

        return view;
    }

    private void setupSwipeToDeleteSwitch() {
        boolean isSwipeEnabled = sharedPreferences.getBoolean(KEY_SWIPE_TO_DELETE, true);
        swipeToDeleteSwitch.setChecked(isSwipeEnabled);
        swipeToDeleteSwitch.setText(getString(R.string.switch_swipe_to_delete));

        swipeToDeleteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_SWIPE_TO_DELETE, isChecked).apply();
        });
    }

    private void setupLanguageSpinner() {
        // Configurar el Spinner de idioma
        String currentLanguage = sharedPreferences.getString(KEY_LANGUAGE, "es");
        languageSpinner.setSelection(getLanguagePosition(currentLanguage));

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (position == 0) ? "es" : "en";
                Log.d("LANGUAGE", "Idioma seleccionado: " + selectedLanguage);

                // Obtener el idioma actual de las preferencias
                String currentLanguage = sharedPreferences.getString(KEY_LANGUAGE, "es");

                // Solo cambiar el idioma si es diferente del actual
                if (!selectedLanguage.equals(currentLanguage)) {
                    sharedPreferences.edit().putString(KEY_LANGUAGE, selectedLanguage).apply();
                    setLocale(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getLanguagePosition(String language) {
        return language.equals("es") ? 0 : 1;
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
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Reiniciar la actividad principal para que los cambios de idioma tengan efecto
        requireActivity().finish();
        startActivity(requireActivity().getIntent());
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

    /**
     * Muestra un cuadro de diálogo de confirmación para cerrar sesión.
     */
    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_title)) // Título del diálogo
                .setMessage(getString(R.string.logout_message)) // Mensaje del diálogo
                .setPositiveButton(getString(android.R.string.yes), (dialog, which) -> {
                    // Acción de cerrar sesión
                    auth.signOut();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    requireActivity().finish(); // Finaliza la actividad actual
                })
                .setNegativeButton(getString(android.R.string.no), null) // Botón para cancelar
                .show();
    }
}