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

    private RecyclerView recyclerView; // RecyclerView para mostrar la lista de Pokémon
    private PokemonAdapter adapter; // Adaptador del RecyclerView
    private List<Pokemon> pokemonList = new ArrayList<>(); // Lista de Pokémon
    private Toast activeToast; // Referencia al Toast activo, para evitar superposición

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pokedex, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_pokedex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el adaptador con un listener para capturar Pokémon
        adapter = new PokemonAdapter(pokemonList, this::addPokemonToFirestore);
        recyclerView.setAdapter(adapter);

        // Cargar los datos desde la API
        loadPokemon();
    }

    /**
     * Carga la lista de Pokémon desde la API de Pokémon usando Retrofit.
     */
    private void loadPokemon() {
        PokemonApi api = ApiClient.getClient().create(PokemonApi.class);

        api.getPokemonList(0, 150).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (PokemonResponse.Result result : response.body().getResults()) {
                        // Extraer ID del Pokémon y cargar detalles
                        String pokemonId = extractPokemonId(result.getUrl());
                        fetchPokemonDetails(pokemonId);
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error al cargar los datos: " + t.getMessage());
            }
        });
    }

    /**
     * Carga los detalles de un Pokémon específico desde la API.
     *
     * @param pokemonId ID del Pokémon.
     */
    private void fetchPokemonDetails(String pokemonId) {
        PokemonApi api = ApiClient.getClient().create(PokemonApi.class);

        api.getPokemonDetails(pokemonId).enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail details = response.body();

                    // Procesar tipos de Pokémon
                    StringBuilder tipos = new StringBuilder();
                    for (PokemonDetail.TypeWrapper typeWrapper : details.getTypes()) {
                        if (tipos.length() > 0) tipos.append(", ");
                        tipos.append(typeWrapper.getType().getName());
                    }

                    // Crear objeto Pokémon con los detalles
                    Pokemon pokemon = new Pokemon(
                            details.getName(),
                            String.valueOf(details.getId()),
                            tipos.toString(),
                            String.valueOf(details.getWeight()),
                            String.valueOf(details.getHeight()),
                            details.getSprites().getFrontDefault()
                    );

                    // Añadir a la lista y actualizar el adaptador
                    pokemonList.add(pokemon);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                Log.e("API_ERROR", "Error al cargar detalles del Pokémon: " + t.getMessage());
            }
        });
    }

    /**
     * Extrae el ID de un Pokémon a partir de su URL.
     *
     * @param url URL del Pokémon.
     * @return ID del Pokémon como cadena.
     */
    private String extractPokemonId(String url) {
        String[] segments = url.split("/");
        return segments[segments.length - 1];
    }

    /**
     * Añade un Pokémon capturado a Firestore.
     *
     * @param pokemon Pokémon a capturar.
     */
    private void addPokemonToFirestore(Pokemon pokemon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemon")
                .whereEqualTo("nombre", pokemon.getNombre())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        showToast(getString(R.string.pokemon_already_captured, pokemon.getNombre()));
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
                                    showToast(getString(R.string.pokemon_captured, pokemon.getNombre()));
                                })
                                .addOnFailureListener(e -> Log.e("FIRESTORE", "Error al capturar Pokémon", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("FIRESTORE", "Error al verificar la colección", e));
    }

    /**
     * Muestra un Toast con el mensaje especificado.
     *
     * @param message Mensaje a mostrar.
     */
    private void showToast(String message) {
        if (!isAdded()) {
            Log.w("PokedexFragment", "El fragmento no está adjunto. No se puede mostrar el Toast.");
            return;
        }

        if (activeToast != null) {
            activeToast.cancel();
        }

        activeToast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT);
        activeToast.show();
    }
}
