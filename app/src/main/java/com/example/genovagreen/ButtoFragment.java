package com.example.genovagreen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class ButtoFragment extends Fragment {
    private AdapterClass adapter;
    private List<Oggetto> list = new ArrayList<>();
    DatabaseReference ref;
    View view=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_butto, container,  false);
        fillList();
    return view;
    }

    private void fillList() {
        for(int i=0;i<100000;i++) {
            String rif="rifiuto"+i;
            String cas="cassonetto"+i;
            Oggetto ogg = new Oggetto(rif, cas);
            list.add(ogg);
        }
        RecyclerView recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new AdapterClass(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ref=FirebaseDatabase.getInstance().getReference().child("genovagreen-e27e2-default-rtdb/DoveLoButto");
        ref.addValueEventListener(new ValueEventListener() {
            //Non entra qua
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String rifiuto=ds.child("rifiuto").getValue().toString();
                    String cassonetto=ds.child("cassonetto").getValue().toString();
                    Oggetto ogg=new Oggetto(rifiuto, cassonetto);
                    list.add(ogg);
                    Toast.makeText(getContext(),"fillList",Toast.LENGTH_LONG).show();
                }
                RecyclerView recyclerView=view.findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
                adapter=new AdapterClass(list);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                Toast.makeText(getContext(),"adapter settato",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"errore db",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        search();
    }

    public boolean search() {
        SearchView searchView=view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

}