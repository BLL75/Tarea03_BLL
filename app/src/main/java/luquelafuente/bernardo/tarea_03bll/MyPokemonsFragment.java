package luquelafuente.bernardo.tarea_03bll;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;

public class MyPokemonsFragment extends Fragment {

    private static final String TAG = "MyPokemonsFragment";
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private List<Pokemon> myPokemonList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_pokemons, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_my_pokemons);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_my_pokemons);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Pasar 'false' para indicar que no es la Pokédex general
        adapter = new PokemonAdapter(myPokemonList, false, getContext(), this::showPokemonDetails);
        recyclerView.setAdapter(adapter);

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Configurar el Swipe to Delete
        setupSwipeToDelete();

        // Cargar los Pokémon capturados desde Firestore
        loadCapturedPokemons();

        swipeRefreshLayout.setOnRefreshListener(this::loadCapturedPokemons);

        return view;
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No se usa en este caso
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Pokemon pokemon = myPokemonList.get(position);

                // Obtener el estado actual del Switch de SharedPreferences
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                boolean allowSwipeToDelete = sharedPreferences.getBoolean(SettingsFragment.KEY_SWIPE_TO_DELETE, true);

                if (allowSwipeToDelete) {
                    // Eliminar el Pokémon de Firestore
                    deletePokemonFromFirestore(pokemon.getNombre(), position);

                    // Mostrar mensaje de confirmación
                    Snackbar.make(recyclerView, "Pokémon " + pokemon.getNombre() + " eliminado", Snackbar.LENGTH_LONG).show();
                } else {
                    // Si el Swipe to Delete está desactivado, refrescar el item
                    adapter.notifyItemChanged(position);
                    // Mostrar un Toast indicando que la opción está desactivada
                    Toast.makeText(getContext(), R.string.swipe_to_delete_disabled, Toast.LENGTH_SHORT).show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void deletePokemonFromFirestore(String pokemonName, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = auth.getCurrentUser().getUid();

        db.collection("captured_pokemon").document(userId)
                .collection("user_pokemon")
                .whereEqualTo("nombre", pokemonName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection("captured_pokemon").document(userId)
                                    .collection("user_pokemon").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Pokemon eliminado correctamente de Firestore");
                                        // Eliminar de la lista local solo si se eliminó de Firestore
                                        myPokemonList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error al eliminar Pokémon de Firestore", e);
                                        adapter.notifyItemChanged(position); // Restaurar el ítem si hay un error
                                    });
                        }
                    } else {
                        Log.e(TAG, "No se encontró el Pokémon para eliminar en Firestore");
                        adapter.notifyItemChanged(position); // Restaurar el ítem si no se encuentra
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al buscar Pokémon en Firestore", e);
                    adapter.notifyItemChanged(position); // Restaurar el ítem si hay un error
                });
    }

    private void loadCapturedPokemons() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = auth.getCurrentUser().getUid();

        db.collection("captured_pokemon").document(userId)
                .collection("user_pokemon")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        myPokemonList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Pokemon pokemon = new Pokemon(
                                    document.getString("nombre"),
                                    document.getString("indice"), // Aquí obtenemos el índice
                                    document.getString("tipos"),
                                    document.getString("peso"),
                                    document.getString("altura"),
                                    document.getString("foto") // Asegúrate de que el campo se llama "foto"
                            );
                            myPokemonList.add(pokemon);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
    }
    private void showPokemonDetails(Pokemon pokemon) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pokemon_details, null);

        ImageView imageView = dialogView.findViewById(R.id.pokemon_image);
        TextView nameTextView = dialogView.findViewById(R.id.pokemon_name);
        TextView detailsTextView = dialogView.findViewById(R.id.pokemon_details);

        // Cargar la imagen del Pokémon con Glide
        Glide.with(this).load(pokemon.getFoto()).into(imageView);

        // Configurar el nombre del Pokémon
        nameTextView.setText(pokemon.getNombre());

        // Configurar los detalles del Pokémon
        String details = getString(
                R.string.pokemon_details_format,
                pokemon.getIndice() != null && !pokemon.getIndice().isEmpty() ? pokemon.getIndice() : "-",
                pokemon.getTipos() != null ? pokemon.getTipos() : getString(R.string.unknown_type),
                pokemon.getPeso() != null ? pokemon.getPeso() : "-",
                pokemon.getAltura() != null ? pokemon.getAltura() : "-"
        );
        detailsTextView.setText(details);

        // Mostrar el cuadro de diálogo
        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton(R.string.close_button, null)
                .show();
    }
}