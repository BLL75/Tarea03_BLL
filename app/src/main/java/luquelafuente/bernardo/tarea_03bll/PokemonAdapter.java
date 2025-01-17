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

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList;
    private OnItemClickListener listener;

    // Interfaz para el listener
    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

    // Constructor con listener
    public PokemonAdapter(List<Pokemon> pokemonList, OnItemClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.nameTextView.setText(pokemon.getNombre());
        holder.detailsTextView.setText("ID: " + pokemon.getIndice() + ", Tipo: " + pokemon.getTipos());

        // Cargar la imagen con Glide
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getFoto())
                .into(holder.imageView);

        // Configurar el evento onClick
        holder.itemView.setOnClickListener(v -> listener.onItemClick(pokemon));
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void updateList(List<Pokemon> newPokemonList) {
        this.pokemonList = newPokemonList;
        notifyDataSetChanged();
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, detailsTextView;
        ImageView imageView;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemon_name);
            detailsTextView = itemView.findViewById(R.id.pokemon_details);
            imageView = itemView.findViewById(R.id.pokemon_image);
        }
    }
}
