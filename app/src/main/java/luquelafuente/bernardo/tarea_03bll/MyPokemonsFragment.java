package luquelafuente.bernardo.tarea_03bll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MyPokemonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_pokemons, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_pokemons);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Pokemon> pokemonList = new ArrayList<>();
        adapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(adapter);

        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("pokemons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            int i = 0;
                            while(i < querySnapshot.size()){
                                QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(i);
                                String nombre = document.get("nombre").toString();
                                String indice = document.get("indice").toString();
                                String tipos = document.get("tipos").toString();
                                String peso = document.get("peso").toString();
                                String altura = document.get("altura").toString();
                                String foto = "";
                                if(document.get("foto") != null) {
                                    foto = document.get("foto").toString();
                                }
                                Pokemon pokemon = new Pokemon(nombre,indice,tipos,peso,altura,foto);
                                pokemonList.add(pokemon);
                                adapter.notifyDataSetChanged();
                                i++;
                            }
                        } else {
                            Toast.makeText(getContext(), "Ha habido un error al obtener los datos de la base de datos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return view;
    }

}