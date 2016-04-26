package com.pringstudio.agnosthings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pringstudio.agnosthings.model.Saklar;
import com.pringstudio.agnosthings.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by sucipto on 4/14/16.
 */
public class FragmentSaklar extends Fragment {

    // Main View
    View mainView;

    // RecyclerView Saklar
    private RecyclerView recyclerView;

    // Data adapter recyclerview
    SaklarAdapter saklarAdapter;

    // Data Saklar
    List<Saklar> saklarList = new ArrayList<>();

    // Realm
    Realm realm;

    /**
     * =============================================================================================
     */

    // Empty Constructor
    public FragmentSaklar() {
        // Nothing
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init Realm
        RealmConfiguration config = new RealmConfiguration.Builder(getContext())
                .name("saklar.realm")
                .schemaVersion(1)
                .build();

        realm = Realm.getInstance(config);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_saklar, container, false);

        // Init Saklar RecyclerView
        setupSaklarRecycler();

        return mainView;
    }

    private void setupSaklarRecycler() {
        // Init The View
        recyclerView = (RecyclerView) mainView.findViewById(R.id.saklar_recycler);

        // Set Adapter
        saklarAdapter = new SaklarAdapter(saklarList);
        recyclerView.setAdapter(saklarAdapter);

        // Set Layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        // Item Decorator / Divider on list
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        // Populate data
        getDataSaklar();

    }

    /**
     * Get Data Saklar dari API
     */

    private void getDataSaklar() {

        // Clear Item
        saklarList.clear();

        // Get The Data
        // ----------------------------------

        RealmResults<Saklar> results = realm.where(Saklar.class).findAll();

        if (results.size() == 0) {

            realm.beginTransaction();

            Saklar saklar1 = realm.createObject(Saklar.class);

            saklar1.setId("lampu_depan");
            saklar1.setName("Lampu Depan");
            saklar1.setValue(1);

            Saklar saklar2 = realm.createObject(Saklar.class);

            saklar2.setId("lampu_teras");
            saklar2.setName("Lampu Teras");
            saklar2.setValue(0);

            Saklar saklar3 = realm.createObject(Saklar.class);

            saklar3.setId("lampu_taman");
            saklar3.setName("Lampu Taman");
            saklar3.setValue(1);

            realm.commitTransaction();

            results = realm.where(Saklar.class).findAll();

        }

        saklarList.addAll(results);



        // Notify the adapter
        saklarAdapter.notifyDataSetChanged();
    }
}