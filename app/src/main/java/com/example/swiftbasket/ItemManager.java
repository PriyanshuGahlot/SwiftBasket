package com.example.swiftbasket;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemManager {

    private DatabaseReference users;
    private DatabaseReference allItems;
    private DataSnapshot UserRunnable;

    public ItemManager() {
        this.users = FirebaseDatabase.getInstance().getReference("users");
        this.allItems = FirebaseDatabase.getInstance().getReference("allItems");
        users.get().addOnSuccessListener(runnable1 -> {
            UserRunnable = runnable1;
        });
    }

    void addToAllItems(Item item){}
    void removeFromAllItems(Item item){}
    void addToCart(String userId,Item item){
        DatabaseReference cart = users.child(userId).child("cart");
        int cartCount = Integer.parseInt(UserRunnable.child(userId).child("cartCount").getValue().toString());
        int diffItemsInCart = (int) UserRunnable.child(userId).child("cart").getChildrenCount();
        cart.child(String.valueOf(diffItemsInCart)).child("name").setValue(item.getName());
        cart.child(String.valueOf(diffItemsInCart)).child("secondary").setValue(item.getSecondary());
        cart.child(String.valueOf(diffItemsInCart)).child("prize").setValue(item.getPrize());
        cart.child(String.valueOf(diffItemsInCart)).child("image").setValue(item.getImage());
        cart.child(String.valueOf(diffItemsInCart)).child("quantity").setValue(1);
        users.child(userId).child("cartCount").setValue(cartCount+1);
    }
    void removeFromCart(String userId, Item item){}
    void updateQuantity(String userId,Item item,int quantity){}

}
