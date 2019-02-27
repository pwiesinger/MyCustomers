package at.lab73.paulwiesinger.mycustomers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);

        setupData();
    }


    private void setupData() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("customers_data.csv")))) {
            String l = br.readLine();
            while(l != null) {
                System.out.println(l);
                l = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
