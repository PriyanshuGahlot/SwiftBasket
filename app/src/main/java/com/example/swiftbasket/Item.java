package com.example.swiftbasket;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Item {

    private int id;
    private String name;
    private String secondary;
    private int prize;
    private ArrayList<String> tags;
    private String image;
    private boolean bestSeller;
    private boolean exclusive;

    public Item(int id) {
        this.id = id;
        DatabaseReference allItems = FirebaseDatabase.getInstance().getReference("allItems");
        allItems.get().addOnSuccessListener(runnable -> {
            for(int i=0;i<runnable.getChildrenCount();i++){
                if(Integer.parseInt(runnable.child(String.valueOf(i)).child("id").getValue().toString())==id){
                    this.name = runnable.child(String.valueOf(i)).child("name").getValue().toString();
                    this.secondary = runnable.child(String.valueOf(i)).child("secondary").getValue().toString();
                    this.prize = Integer.parseInt(runnable.child(String.valueOf(i)).child("prize").getValue().toString());
                    this.image = runnable.child(String.valueOf(i)).child("image").getValue().toString();
                    this.exclusive  = (runnable.child(String.valueOf(i)).child("exclusive").getValue().toString()=="true")? true:false;
                    this.bestSeller  = (runnable.child(String.valueOf(i)).child("bestSeller").getValue().toString()=="true")? true:false;
                    for(DataSnapshot tag : runnable.child(String.valueOf(i)).child("tags").getChildren()){
                        String currTag = tag.getValue().toString();
                        this.tags.add(currTag);
                    }
                    break;
                }
            }
        });
    }

    public Item(String name, String secondary, int prize, ArrayList<String> tags, String image, boolean bestSeller, boolean exclusive) {
        this.name = name;
        this.secondary = secondary;
        this.prize = prize;
        this.tags = tags;
        this.image = image;
        this.bestSeller = bestSeller;
        this.exclusive = exclusive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }
}
