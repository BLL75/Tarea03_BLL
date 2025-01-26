package luquelafuente.bernardo.tarea_03bll;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento que muestra la lista de Pokémon capturados.
 * Proporciona funcionalidad para mostrar detalles y eliminar Pokémon mediante "Swipe to Delete".
 */
public class MyPokemonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private final List<Pokemon> pokemonList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_pokemons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar el RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_my_pokemons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el adaptador
        adapter = new PokemonAdapter(pokemonList, this::showPokemonDetails);
        recyclerView.setAdapter(adapter);

        // Configurar "Swipe to Delete"
        enableSwipeToDelete();

        // Cargar los Pokémon capturados
        loadCapturedPokemons();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCapturedPokemons(); // Recargar los datos al volver al fragmento
    }

    /**
     * Carga la lista de Pokémon capturados desde Firebase Firestore.
     */
    private void loadCapturedPokemons() {
        Log.d("MyPokemonsFragment", "Recargando datos de Pokémon capturados");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("captured_pokemon")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    pokemonList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Pokemon pokemon = new Pokemon(
                                document.getString("nombre"),
                                document.getString("indice"),
                                document.getString("tipos"),
                                document.getString("peso"),
                                document.getString("altura"),
                                document.getString("foto")
                        );
                        pokemonList.add(pokemon);
                    }
                    adapter.notifyDataSetChanged();
                    Log.d("MyPokemonsFragment", "Lista recargada: " + pokemonList.size() + " Pokémon.");
                })
                .addOnFailureListener(e -> Log.e("MyPokemonsFragment", "Error al cargar Pokémon", e));
    }

    /**
     * Muestra un cuadro de diálogo con los detalles de un Pokémon seleccionado.
     *
     * @param pokemon El Pokémon cuyos detalles se mostrarán.
     */
    private void showPokemonDetails(Pokemon pokemon) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pokemon_details, null);

        ImageView imageView = dialogView.findViewById(R.id.pokemon_image);
        TextView nameTextView = dialogView.findViewById(R.id.pokemon_name);
        TextView detailsTextView = dialogView.findViewById(R.id.pokemon_details);

        // Cargar la imagen del Pokémon
        Glide.with(this).load(pokemon.getFoto()).into(imageView);

        // Configurar el nombre del Pokémon
        nameTextView.setText(pokemon.getNombre());

        // Configurar los detalles del Pokémon con todos los argumentos
        String details = getString(
                R.string.pokemon_details_format,
                pokemon.getIndice() != null ? pokemon.getIndice() : "-",
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



    /**
     * Elimina un Pokémon de la base de datos y actualiza la lista local.
     *
     * @param pokemon El Pokémon a eliminar.
     */
    private void deletePokemon(Pokemon pokemon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemon")
                .whereEqualTo("nombre", pokemon.getNombre())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        int position = pokemonList.indexOf(pokemon);
                                        if (position >= 0) {
                                            pokemonList.remove(position);
                                            adapter.notifyItemRemoved(position);
                                        }
                                        Log.d("MyPokemonsFragment", "¡Pokémon eliminado!");
                                    })
                                    .addOnFailureListener(e -> Log.e("MyPokemonsFragment", "Error al eliminar el Pokémon", e));
                        }
                    } else {
                        Log.d("MyPokemonsFragment", "Pokémon no encontrado.");
                    }
                })
                .addOnFailureListener(e -> Log.e("MyPokemonsFragment", "Error al buscar el Pokémon", e));
    }

    /**
     * Configura la funcionalidad de "Swipe to Delete" en el RecyclerView.
     */
    private void enableSwipeToDelete() {
        if (!isSwipeEnabled()) {
            Log.d("MyPokemonsFragment", "Swipe to Delete deshabilitado");
            return;
        }

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No soportamos mover elementos
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Pokemon pokemonToDelete = pokemonList.get(position);
                deletePokemon(pokemonToDelete);
            }
        }).attachToRecyclerView(recyclerView);
    }

    /**
     * Comprueba si la funcionalidad de "Swipe to Delete" está habilitada.
     *
     * @return True si está habilitada, False en caso contrario.
     */
    private boolean isSwipeEnabled() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("settings_preferences", 0);
        return sharedPreferences.getBoolean("swipe_to_delete_enabled", true); // Por defecto habilitado
    }
}
