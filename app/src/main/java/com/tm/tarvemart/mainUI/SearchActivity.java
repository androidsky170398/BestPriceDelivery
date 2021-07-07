package com.tm.tarvemart.mainUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tm.tarvemart.R;

public class SearchActivity extends AppCompatActivity {
    EditText et_search;
    Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViewByIds();
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(SearchActivity.this, "Please enter product name...", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SearchActivity.this, ActivityProduct.class);
                    intent.putExtra("fromWhere", "Search");
                    intent.putExtra("key",et_search.getText().toString().trim());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    private void findViewByIds() {
        et_search = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchActivity.this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        //Dashboard.checkNetConnection(SearchActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Dashboard.checkNetConnection(SearchActivity.this);
    }
}
