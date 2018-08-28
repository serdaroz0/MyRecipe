package com.daxstyles.recipe.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CardModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;

    private int prepTime;

    private int serveTime;

    private ArrayList<String> imagesUri;

    private String ingredient;

    private String direction;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getServeTime() {
        return serveTime;
    }


    public void setServeTime(int serveTime) {
        this.serveTime = serveTime;

    }

    public ArrayList<String> getImagesUri() {
        return imagesUri;
    }

    public void setImagesUri(ArrayList<String> imagesUri) {
        this.imagesUri = imagesUri;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
