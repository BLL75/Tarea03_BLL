package luquelafuente.bernardo.tarea_03bll;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyPokemonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private List<Pokemon> pokemonList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_pokemons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_my_pokemons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el adaptador
        adapter = new PokemonAdapter(pokemonList, pokemon -> {
            // Aquí puedes implementar acciones para "Mis Pokémon", si las necesitas
            Log.d("CLICK_EVENT", "Pokémon seleccionado: " + pokemon.getNombre());
        });
        recyclerView.setAdapter(adapter);

        // Cargar los Pokémon capturados desde Firestore
        loadCapturedPokemons();
    }

    private void loadCapturedPokemons() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Leer la colección "captured_pokemon" desde Firestore
        db.collection("captured_pokemon")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    pokemonList.clear(); // Limpiar la lista antes de cargar nuevos datos
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convertir cada documento a un objeto Pokemon
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
                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE", "Error al cargar los Pokémon capturados", e);
                });
    }
}
