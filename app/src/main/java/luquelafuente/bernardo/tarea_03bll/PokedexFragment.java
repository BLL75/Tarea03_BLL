package luquelafuente.bernardo.tarea_03bll;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter adapter; // Adaptador del RecyclerView
    private List<Pokemon> pokemonList = new ArrayList<>(); // Lista de Pokémon

    private Toast activeToast; // Variable global para el Toast

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pokedex, container, false); // Infla el diseño del Fragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_pokedex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el adaptador con un listener para capturar Pokémon
        adapter = new PokemonAdapter(pokemonList, pokemon -> {
            addPokemonToFirestore(pokemon); // Llama al método para capturar Pokémon
        });
        recyclerView.setAdapter(adapter);

        // Cargar los datos desde la API
        loadPokemon();
    }

    private void loadPokemon() {
        PokemonApi api = ApiClient.getClient().create(PokemonApi.class);

        api.getPokemonList(0, 150).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (PokemonResponse.Result result : response.body().getResults()) {
                        Pokemon pokemon = new Pokemon(
                                result.getName(),
                                extractPokemonId(result.getUrl()),
                                "Desconocido",
                                "0",
                                "0",
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" +
                                        extractPokemonId(result.getUrl()) + ".png"
                        );
                        pokemonList.add(pokemon);
                    }
                    adapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error al cargar los datos: " + t.getMessage());
            }
        });
    }

    private String extractPokemonId(String url) {
        String[] segments = url.split("/"); // Divide la URL por "/"
        return segments[segments.length - 1]; // Devuelve el último segmento (ID del Pokémon)
    }

    private void addPokemonToFirestore(Pokemon pokemon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemon")
                .whereEqualTo("nombre", pokemon.getNombre())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        if (isAdded()) {
                            showToast("¡Ya tienes a " + pokemon.getNombre() + " en tu colección!");
                        }
                    } else {
                        Map<String, Object> pokemonData = new HashMap<>();
                        pokemonData.put("nombre", pokemon.getNombre());
                        pokemonData.put("indice", pokemon.getIndice());
                        pokemonData.put("tipos", pokemon.getTipos());
                        pokemonData.put("peso", pokemon.getPeso());
                        pokemonData.put("altura", pokemon.getAltura());
                        pokemonData.put("foto", pokemon.getFoto());

                        db.collection("captured_pokemon")
                                .add(pokemonData)
                                .addOnSuccessListener(documentReference -> {
                                    if (isAdded()) {
                                        showToast("¡Has capturado a " + pokemon.getNombre() + "!");
                                    }
                                })
                                .addOnFailureListener(e -> Log.e("FIRESTORE", "Error al capturar Pokémon", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("FIRESTORE", "Error al verificar la colección", e));
    }

    private void showToast(String message) {
        if (!isAdded()) {
            Log.w("PokedexFragment", "No se puede mostrar el Toast: el fragmento no está adjunto.");
            return; // No intentes mostrar el Toast si el fragmento no está adjunto
        }

        if (activeToast != null) {
            activeToast.cancel(); // Cancela el Toast anterior si existe
        }

        activeToast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT);
        activeToast.show();
    }
}
