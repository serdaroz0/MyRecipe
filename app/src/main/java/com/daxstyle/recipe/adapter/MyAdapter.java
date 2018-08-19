package com.daxstyle.recipe.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daxstyle.recipe.activity.CulinaryActivity;
import com.daxstyle.recipe.helper.Util;
import com.daxstyle.recipe.model.CardModel;
import com.daxstyle.recipe.R;

import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<CardModel> cardModels;
    public Context context;
    private ArrayList<Uri> uri = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivTitle, btnShare, btnDelete;

        private ViewHolder(View v) {
            super(v);
            this.btnDelete = v.findViewById(R.id.btnDelete);
            this.tvTitle = v.findViewById(R.id.tvTitle);
            this.ivTitle = v.findViewById(R.id.ivImage);
            this.btnShare = v.findViewById(R.id.btnShare);
        }
    }

    public MyAdapter(ArrayList<CardModel> cardModels, Context context) {
        this.cardModels = cardModels;
        this.context = context;

    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardModel dp = cardModels.get(position);
        holder.tvTitle.setText(dp.getTitle());
        if (cardModels.get(position).getImagesUri().size() != 0) {
            Uri uri = Uri.parse(cardModels.get(position).getImagesUri().get(0));
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.ivTitle.setImageBitmap(photo);
        }
        holder.ivTitle.setOnClickListener(view ->
        {
            Intent culinaryIntent = new Intent(context, CulinaryActivity.class);
            culinaryIntent.putExtra("count", position);
            culinaryIntent.putExtra("title", cardModels.get(position).getTitle());
            culinaryIntent.putExtra("serveTime", cardModels.get(position).getServeTime());
            culinaryIntent.putExtra("prepTime", cardModels.get(position).getPrepTime());
            culinaryIntent.putExtra("ingredients", cardModels.get(position).getIngredient());
            culinaryIntent.putExtra("directions", cardModels.get(position).getDirection());
            culinaryIntent.putExtra("uris", cardModels.get(position).getImagesUri());
            culinaryIntent.putExtra("fromEdit", true);
            context.startActivity(culinaryIntent);
        });
        holder.tvTitle.setOnClickListener(view -> {
            Intent culinaryIntent = new Intent(context, CulinaryActivity.class);
            culinaryIntent.putExtra("count", position);
            culinaryIntent.putExtra("title", cardModels.get(position).getTitle());
            culinaryIntent.putExtra("serveTime", cardModels.get(position).getServeTime());
            culinaryIntent.putExtra("prepTime", cardModels.get(position).getPrepTime());
            culinaryIntent.putExtra("ingredients", cardModels.get(position).getIngredient());
            culinaryIntent.putExtra("directions", cardModels.get(position).getDirection());
            culinaryIntent.putExtra("uris", cardModels.get(position).getImagesUri());
            culinaryIntent.putExtra("fromEdit", true);
            context.startActivity(culinaryIntent);
        });

        holder.btnDelete.setOnClickListener(view ->
        {

            android.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new android.app.AlertDialog.Builder(context);
            }
            builder.setTitle(R.string.delete)
                    .setPositiveButton(R.string.No, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .
                            setNegativeButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (cardModels.get(position) != null) {
                                        cardModels = Util.loadCards(context, cardModels);
                                        cardModels.remove(position);
                                        Util.saveObject(context, cardModels, "CardModels.obj");
                                        notifyDataSetChanged();
                                    }
                                }
                            });
            builder.show();

        });


        holder.btnShare.setOnClickListener(view ->

        {

            android.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.app.AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new android.app.AlertDialog.Builder(context);
            }
            builder.setTitle(R.string.choose)
                    .setPositiveButton(R.string.whatsapp, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            PackageManager pm = context.getPackageManager();
                            try {

                                Intent waIntent = new Intent(Intent.ACTION_SEND);
                                waIntent.setType("text/plain");
                                String mailText = "Başlık : " + cardModels.get(position).getTitle() + "\nHazırlanma süresi : " + cardModels.get(position).getPrepTime() + " dk" + "\nServis Süresi : " + cardModels.get(position).getServeTime() + " dk" + "\nMalzemeler : " + cardModels.get(position).getIngredient() + "\nHazırlanışı : " + cardModels.get(position).getDirection();
                                PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                //Check if package exists or not. If not then code
                                //in catch block will be called
                                waIntent.setPackage("com.whatsapp");
                                waIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                waIntent.putExtra(Intent.EXTRA_TEXT, mailText);
                                context.startActivity(Intent.createChooser(waIntent, context.getText(R.string.send_mail)));

                            } catch (PackageManager.NameNotFoundException e) {
                                Toast.makeText(context, R.string.whatsapp_message, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton(R.string.gmail, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL);
                            Uri urit;
                            String mailText = "Başlık : " + cardModels.get(position).getTitle() + "\nHazırlanma süresi : " + cardModels.get(position).getPrepTime() + " dk" + "\nServis Süresi : " + cardModels.get(position).getServeTime() + " dk" + "\nMalzemeler : " + cardModels.get(position).getIngredient() + "\nHazırlanışı : " + cardModels.get(position).getDirection();
                            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                            emailIntent.setType("message/rfc822");
                            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, cardModels.get(position).getTitle());
                            for (int i = 0; i < cardModels.get(position).getImagesUri().size(); i++) {
                                urit = Uri.parse(cardModels.get(position).getImagesUri().get(i));
                                uri.add(urit);
                            }
                            if (uri != null) {
                                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uri);
                            }
                            emailIntent.putExtra(Intent.EXTRA_TEXT, mailText);
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                            context.startActivity(Intent.createChooser(emailIntent, context.getText(R.string.send_mail)));


                        }
                    });
            builder.show();

        });
    }

    @Override
    public int getItemCount() {
        return (cardModels == null) ? 0 : cardModels.size();

    }

}