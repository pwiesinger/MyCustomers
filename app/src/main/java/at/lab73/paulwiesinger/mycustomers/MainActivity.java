package at.lab73.paulwiesinger.mycustomers;

import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private FloatingActionButton fab;

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
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            EditText et = new EditText(getApplicationContext());
            TextView tv = new TextView(getApplicationContext());
            tv.setText("id,firstname,lastname,title,email,marital_status,creditcard");

            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(tv);
            layout.addView(et);


            new AlertDialog.Builder(this)
                    .setView(layout)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        // do something with picker.getValue()
                        itemsBackup.add(new Customer(et.getText().toString()));
                        addData(et.getText().toString());
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });

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

        // read from file
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("adds.csv")));
            String line = br.readLine();
            while (line != null) {
                items.add(new Customer(line));
                line = br.readLine();
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
                // does nothing because we search when the user types.
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

    private void addData(String line) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getApplicationContext().openFileOutput("adds.csv", MODE_APPEND)));
            bw.write(line);
            bw.newLine();
            bw.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
