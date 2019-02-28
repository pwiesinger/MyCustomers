package at.lab73.paulwiesinger.mycustomers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;

    // list view data source
    private List<Customer> items = new ArrayList<>();
    private List<Customer> itemsBackup = new ArrayList<>();
    private ArrayAdapter<Customer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);

        setupData();

        setupAdapter();

        setupSearch();
    }


    private void setupData() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("customers_data.csv")))) {
            br.readLine(); // skip titles
            String l = br.readLine();
            while(l != null) {
                items.add(new Customer(l));
                l = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        itemsBackup.addAll(items);
    }

    private void setupAdapter() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }


            // we use live search with instant results
            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.isEmpty()) {
                    items.clear();
                    items.addAll(itemsBackup);
                } else {
                    items.clear();
                    items.addAll(itemsBackup.stream().filter((a) -> a.toString().toUpperCase().contains(newText.toUpperCase())).collect(Collectors.toList()));
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}
