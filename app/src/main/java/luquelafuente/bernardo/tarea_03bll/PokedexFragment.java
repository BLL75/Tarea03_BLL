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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter adapter; // Nuestro adaptador para el RecyclerView
    private List<Pokemon> pokemonList = new ArrayList<>(); // Lista de Pokémon

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla el diseño del Fragment
        return inflater.inflate(R.layout.fragment_pokedex, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_pokedex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar el adaptador
        adapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(adapter);

        // Cargar los datos desde la API
        loadPokemon();
    }

    private void loadPokemon() {
        PokemonApi api = ApiClient.getClient().create(PokemonApi.class);

        // Llamar al endpoint con Retrofit
        api.getPokemonList(0, 150).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Procesar la lista de resultados
                    for (PokemonResponse.Result result : response.body().getResults()) {
                        // Convertir los datos de la API a nuestro modelo Pokemon
                        Pokemon pokemon = new Pokemon(
                                result.getName(),
                                "N/A", // Por ahora, índice vacío (puedes ajustarlo más tarde)
                                "Desconocido", // Por ahora, tipo vacío (puedes ajustarlo más tarde)
                                "0", // Peso temporal
                                "0", // Altura temporal
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + // URL de imagen
                                        extractPokemonId(result.getUrl()) + ".png"
                        );
                        pokemonList.add(pokemon);
                    }
                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error al cargar los datos: " + t.getMessage());
            }
        });
    }

    private String extractPokemonId(String url) {
        // Extraer el ID del Pokémon desde la URL (último número en la URL)
        String[] segments = url.split("/");
        return segments[segments.length - 1];
    }
}
