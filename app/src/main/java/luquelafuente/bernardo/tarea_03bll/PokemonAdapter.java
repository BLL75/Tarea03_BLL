package luquelafuente.bernardo.tarea_03bll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private List<String> pokemonsList;

    public PokemonAdapter(List<String> pokemonsList) {
        this.pokemonsList = pokemonsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String pokemon = pokemonsList.get(position);
        holder.nameTextView.setText(pokemon);
        //        holder.indiceTextView.setText("indice");
        //         holder.tiposTextView.setText("tipos");
        //         holder.pesoTextView.setText("peso");
        //         holder.alturaTextView.setText("altura");
    }

    @Override
    public int getItemCount() {
        return pokemonsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView indiceTextView;
        TextView tiposTextView;
        TextView pesoTextView;
        TextView alturaTextView;
        ImageView pokemonImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_pokemon_name);
            //   indiceTextView = itemView.findViewById(R.id.text_view_pokemon_indice);
            //   tiposTextView = itemView.findViewById(R.id.text_view_pokemon_tipos);
            //   pesoTextView = itemView.findViewById(R.id.text_view_pokemon_peso);
            //  alturaTextView = itemView.findViewById(R.id.text_view_pokemon_altura);
            //    pokemonImageView = itemView.findViewById(R.id.image_view_pokemon);
        }
    }
}