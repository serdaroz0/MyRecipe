package com.daxstyles.recipe.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daxstyles.recipe.R;
import com.daxstyles.recipe.adapter.MyAdapter;
import com.daxstyles.recipe.helper.Util;
import com.daxstyles.recipe.model.CardModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        Date currentDate = new Date(System.currentTimeMillis());
        String dateStr = "04/05/2010";

        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long c = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        Log.d("onCreate1234: ", formattedDate);
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

