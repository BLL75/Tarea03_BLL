package luquelafuente.bernardo.tarea_03bll;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Clase MainActivity
 *
 * Actúa como contenedor principal para la aplicación. Gestiona la autenticación del usuario
 * y configura la navegación entre los fragments utilizando un BottomNavigationView.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Verificar si el usuario está autenticado
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            // Si no hay usuario, redirigir al LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        // Configurar el BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Inicializar NavHostFragment y NavController
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) {
            navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentByTag("nav_host_fragment_tag");
        }
        if (navHostFragment == null) {
            navHostFragment = NavHostFragment.create(R.navigation.nav_graph);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, navHostFragment, "nav_host_fragment_tag")
                    .setPrimaryNavigationFragment(navHostFragment)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Asegurarse de que NavHostFragment esté inicializado
        if (navHostFragment == null) {
            navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (navHostFragment == null) {
                navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentByTag("nav_host_fragment_tag");
            }
            if (navHostFragment == null) {
                navHostFragment = NavHostFragment.create(R.navigation.nav_graph);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, navHostFragment, "nav_host_fragment_tag")
                        .setPrimaryNavigationFragment(navHostFragment)
                        .commit();
            }
        }

        // Configurar el NavController con el BottomNavigationView
        navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
