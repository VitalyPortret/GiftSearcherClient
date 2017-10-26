package com.giftsearcher.giftsearcherclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<Gift> adapter;
    private ListView giftsListView;

    private final String URL_SERVER = "http://192.168.0.103:8080";
    private final String URL_BEST_RATING_GIFTS = URL_SERVER + "/api/gifts/";
    private final String URL_POPULAR_GIFTS = URL_SERVER + "/api/gifts/popular";
    private final String URL_NEW_GIFTS = URL_SERVER + "/api/gifts/new";
    private final String URL_CHEAP_GIFTS = URL_SERVER + "/api/gifts/cheap";
    private final String URL_EXPENSIVE_GIFTS = URL_SERVER + "/api/gifts/expensive";

    private String currentUrl = URL_BEST_RATING_GIFTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        giftsListView = (ListView) findViewById(R.id.giftsListView);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        giftsListView.setAdapter(adapter);
        new JSONTask().execute(currentUrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONTask extends AsyncTask<String, Void, List<Gift>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Gift> doInBackground(String... params) {
            return JSONHelper.getJsonFromRemoteApi(params[0]);
        }

        @Override
        protected void onPostExecute(List<Gift> result) {
            super.onPostExecute(result);
            adapter.addAll(result);
        }
    }
}
