package com.example.administrator.myapplication;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;

import java.util.ArrayList;

public class SubscribeActivity extends AppCompatActivity {
    /*private TextView textView5, textView6, textView7,textView8;
    private CheckBox checkBox,checkBox2, checkBox3, checkBox4;*/
    private ListView lvSubscribe;
    private Button btnGoHome;
    private ArrayList subscribeArrayList;
    private SubscribeAdapter adapter;
    private final String LOG_TAG = SubscribeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        lvSubscribe = (ListView) findViewById(R.id.lvSubscribe);
        btnGoHome = (Button) findViewById(R.id.btnSubscribeGoHome);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubscribeActivity.this, MainActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, 0);
                else
                    Log.e(LOG_TAG, "Error on changing view with GoHome btn");
            }
        });

        fillArrayList();
        setListViewAdapter();
    }

    public void onClickGoHomeView(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
        else {
            Log.e(LOG_TAG, "Error on changing view with go home");
        }
    }


    /*
     * Private methods
     */
    private void fillArrayList() {
        subscribeArrayList = new ArrayList<ListModelSubscribe>();
        /*subscribeArrayList.add(new ListModelSubscribe("BBC", false));
        subscribeArrayList.add(new ListModelSubscribe("New York Times", true));
        subscribeArrayList.add(new ListModelSubscribe("The times", false));*/
        DBHelper db = DBHelper.getInstance(this);
        Cursor cursor = db.select_subcategories();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String subcat = cursor.getString(cursor.getColumnIndex(Contract.TNews.COLUMN_SUBCATEGORY));
                    Boolean subscribed = db.isSubscribed(subcat);
                    subscribeArrayList.add(new ListModelSubscribe(subcat, subscribed));
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e) {
            Log.d(LOG_TAG, "Error reading subcategories \n\t\tfrom DB");
        }
    }

    private void setListViewAdapter() {
        adapter = new SubscribeAdapter(this, subscribeArrayList);
        lvSubscribe.setAdapter(adapter);
    }
}
