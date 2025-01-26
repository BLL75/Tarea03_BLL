package luquelafuente.bernardo.tarea_03bll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Adaptador para mostrar una lista de Pokémon en un RecyclerView.
 */
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    // Lista de Pokémon que se mostrará
    private List<Pokemon> pokemonList;
    // Listener para manejar los clics en los elementos de la lista
    private OnItemClickListener listener;

    /**
     * Interfaz para manejar eventos de clic en los elementos.
     */
    public interface OnItemClickListener {
        /**
         * Método que se ejecutará cuando se haga clic en un Pokémon.
         *
         * @param pokemon El Pokémon seleccionado.
         */
        void onItemClick(Pokemon pokemon);
    }

    /**
     * Constructor del adaptador.
     *
     * @param pokemonList Lista de Pokémon a mostrar.
     * @param listener    Listener para manejar eventos de clic.
     */
    public PokemonAdapter(List<Pokemon> pokemonList, OnItemClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del elemento
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);

        // Configurar los textos usando getContext().getString() para los placeholders
        holder.nameTextView.setText(pokemon.getNombre());
        holder.detailsTextView.setText(
                holder.itemView.getContext().getString(
                        R.string.pokemon_details_format,
                        pokemon.getIndice() != null ? pokemon.getIndice() : "-",
                        pokemon.getTipos() != null ? pokemon.getTipos() : "Desconocido",
                        pokemon.getPeso() != null ? pokemon.getPeso() : "-",
                        pokemon.getAltura() != null ? pokemon.getAltura() : "-"
                )
        );

        holder.weightHeightTextView.setText(
                holder.itemView.getContext().getString(
                        R.string.pokemon_weight_height_format,
                        pokemon.getPeso(),
                        pokemon.getAltura()
                )
        );

        // Configurar la imagen
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getFoto())
                .into(holder.imageView);

        // Configurar clics en el elemento
        holder.itemView.setOnClickListener(v -> listener.onItemClick(pokemon));
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    /**
     * Actualiza la lista de Pokémon y refresca el adaptador.
     *
     * @param newPokemonList Nueva lista de Pokémon.
     */
    public void updateList(List<Pokemon> newPokemonList) {
        this.pokemonList = newPokemonList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para manejar las vistas de cada elemento de la lista.
     */
    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, detailsTextView, weightHeightTextView;
        ImageView imageView;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas del diseño
            nameTextView = itemView.findViewById(R.id.pokemon_name);
            detailsTextView = itemView.findViewById(R.id.pokemon_details);
            weightHeightTextView = itemView.findViewById(R.id.pokemon_weight_height); // Nueva referencia
            imageView = itemView.findViewById(R.id.pokemon_image);
        }
    }
}
