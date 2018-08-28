package com.daxstyles.recipe.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxstyles.recipe.adapter.MyAdapter;
import com.daxstyles.recipe.helper.Util;
import com.daxstyles.recipe.model.CardModel;
import com.daxstyles.recipe.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<CardModel> cardModels = null;
    TextView tvTitle, tvNoNote, tvCulinary;
    ImageView ivImage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);
        tvTitle = findViewById(R.id.tvTitle);
        tvNoNote = findViewById(R.id.tvNoNote);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Lobster.otf");
        tvCulinary = findViewById(R.id.tvCulinary);
        tvCulinary.setTypeface(type);
        ivImage = findViewById(R.id.ivImage);
        Util.setSoftKeys(this);
        cardModels = Util.loadCards(this, cardModels);
        if (cardModels.size() < 1) {
            tvNoNote.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new MyAdapter(cardModels, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void newNote(View view) {
        startActivity(new Intent(this, CulinaryActivity.class));
    }


}

