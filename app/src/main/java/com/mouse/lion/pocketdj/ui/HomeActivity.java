package com.mouse.lion.pocketdj.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mouse.lion.pocketdj.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        RecyclerView grid = findViewById(R.id.grid);
        grid.setLayoutManager(new LinearLayoutManager(this));
        grid.setAdapter(new MyAdapter());
    }

    class MyVh extends RecyclerView.ViewHolder {
        MyVh(View view) {
            super(view);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyVh> {

        @Override
        public MyVh onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_home_content, parent, false);
            return new MyVh(view);
        }

        @Override
        public void onBindViewHolder(MyVh holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }
}
