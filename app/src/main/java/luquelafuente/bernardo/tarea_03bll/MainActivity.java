package luquelafuente.bernardo.tarea_03bll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                IdpResponse response = IdpResponse.fromResultIntent(result.getData());

                if (result.getResultCode() == RESULT_OK) {
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(getBaseContext(), "Sesión iniciada", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Login incorrecto", Toast.LENGTH_LONG).show();
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                }
            }
    );
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            createSignInIntent();
        } else {
            Toast.makeText(getBaseContext(), "Sesión iniciada", Toast.LENGTH_LONG).show();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Find the NavHostFragment, initialize the navController, and bind it to the bottomNav
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if(navHostFragment == null){
            navHostFragment =  (NavHostFragment) getSupportFragmentManager().findFragmentByTag("nav_host_fragment_tag");
        }
        if(navHostFragment == null) {
            navHostFragment = NavHostFragment.create(R.navigation.nav_graph);
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, navHostFragment, "nav_host_fragment_tag").setPrimaryNavigationFragment(navHostFragment).commit();
        }
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(navHostFragment == null) {
            navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if(navHostFragment == null){
                navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentByTag("nav_host_fragment_tag");
            }
            if(navHostFragment == null){
                navHostFragment = NavHostFragment.create(R.navigation.nav_graph);
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, navHostFragment, "nav_host_fragment_tag").setPrimaryNavigationFragment(navHostFragment).commit();
            }
        }
        navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) ->{
            if(destination.getId() == R.id.myPokemonsFragment){
                bottomNavigationView.getMenu().findItem(R.id.myPokemonsFragment).setTitle("Mis Pokémons");
            } else if (destination.getId() == R.id.pokedexFragment) {
                bottomNavigationView.getMenu().findItem(R.id.pokedexFragment).setTitle("Pokédex");
            }else if (destination.getId() == R.id.settingsFragment) {
                bottomNavigationView.getMenu().findItem(R.id.settingsFragment).setTitle("Ajustes");
            }
        });
    }
    private void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }
}