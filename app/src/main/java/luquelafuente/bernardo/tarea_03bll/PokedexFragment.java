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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PokedexFragment extends Fragment implements PokemonAdapter.OnItemClickListener {

    private static final String TAG = "PokedexFragment";
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private PokemonApi pokemonApi;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toast activeToast;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        // Usar LinearLayoutManager para una lista vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pasar 'true' para indicar que es la Pokédex general y 'this' como listener para el clic
        adapter = new PokemonAdapter(pokemonList, true, getContext(), this);
        recyclerView.setAdapter(adapter);

        pokemonApi = ApiClient.getClient().create(PokemonApi.class);
        fetchPokemonList();

        swipeRefreshLayout.setOnRefreshListener(this::fetchPokemonList);

        return view;
    }

    // Implementar el método onItemClick de la interfaz OnItemClickListener
    @Override
    public void onItemClick(Pokemon pokemon) {
        // Acción a realizar al hacer clic en un Pokémon (capturarlo)
        addPokemonToFirestore(pokemon);
    }

    private void fetchPokemonList() {
        Call<PokemonResponse> call = pokemonApi.getPokemonList(150, 0);
        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(@NonNull Call<PokemonResponse> call, @NonNull Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList.clear();
                    // Usar PokemonResponse.Result correctamente
                    List<PokemonResponse.Result> results = response.body().getResults();
                    for (PokemonResponse.Result result : results) {
                        fetchPokemonDetails(result.getName());
                    }
                } else {
                    Log.e(TAG, "Error fetching Pokemon list: " + response.code());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<PokemonResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching Pokemon list", t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fetchPokemonDetails(String name) {
        Call<PokemonDetail> call = pokemonApi.getPokemonDetails(name);
        call.enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetail> call, @NonNull Response<PokemonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail detail = response.body();
                    String imageUrl = "https://img.pokemondb.net/artwork/large/" + name + ".jpg";
                    String type1 = detail.getTypes().get(0).getType().getName();
                    String type2 = detail.getTypes().size() > 1 ? detail.getTypes().get(1).getType().getName() : "";
                    String weight = String.valueOf(detail.getWeight());
                    String height = String.valueOf(detail.getHeight());

                    // Asegúrate de que el constructor de Pokemon acepta los argumentos en el orden correcto
                    Pokemon pokemon = new Pokemon(
                            name,
                            String.valueOf(detail.getId()), // Pasando el índice como String
                            type1 + (type2.isEmpty() ? "" : ", " + type2), // Concatenando los tipos
                            weight,
                            height,
                            imageUrl
                    );

                    pokemonList.add(pokemon);
                    adapter.notifyItemInserted(pokemonList.size() - 1);
                } else {
                    Log.e(TAG, "Error fetching details for Pokemon: " + name + ", error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonDetail> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching Pokemon details for: " + name, t);
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = auth.getCurrentUser().getUid(); // Obtener el UID del usuario actual

        db.collection("captured_pokemon").document(userId)
                .collection("user_pokemon")
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

                        db.collection("captured_pokemon").document(userId)
                                .collection("user_pokemon")
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