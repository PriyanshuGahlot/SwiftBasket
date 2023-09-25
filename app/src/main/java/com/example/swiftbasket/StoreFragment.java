package com.example.swiftbasket;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_store, container, false);

        TextView addressText = root.findViewById(R.id.addressText);
        SearchView searchBox = root.findViewById(R.id.searchBox);
        ProgressBar progressBar = root.findViewById(R.id.progressBar);

        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        users.child(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(runnable -> {
           addressText.setText("📍"+runnable.child("address").getValue().toString());
        });

        searchBox.setQueryHint("Search Store");
        searchBox.setIconifiedByDefault(false);

        ArrayList<String> itemNames = new ArrayList<>();
        ArrayList<String> secondaries = new ArrayList<>();
        ArrayList<Integer> prizes = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        ArrayList<Boolean> exclusive = new ArrayList<>();
        ArrayList<Boolean> bestseller = new ArrayList<>();
        ArrayList<Boolean> vegi = new ArrayList<>();
        ArrayList<Boolean> fruit = new ArrayList<>();

        ArrayList<String> titles = new ArrayList<>();

        DatabaseReference allItems = FirebaseDatabase.getInstance().getReference("allItems");
        allItems.get().addOnSuccessListener(runnable -> {
            for(int i=0;i<runnable.getChildrenCount();i++){
                String itemName = runnable.child(String.valueOf(i)).child("name").getValue().toString();
                String secondary = runnable.child(String.valueOf(i)).child("secondary").getValue().toString();
                int prize = Integer.parseInt(runnable.child(String.valueOf(i)).child("prize").getValue().toString());
                String image = runnable.child(String.valueOf(i)).child("image").getValue().toString();
                boolean isExclusive  = (runnable.child(String.valueOf(i)).child("exclusive").getValue().toString()=="true")? true:false;
                boolean isbestSeller  = (runnable.child(String.valueOf(i)).child("bestSeller").getValue().toString()=="true")? true:false;
                boolean isVegi = false;
                boolean isFruit = false;
                for(DataSnapshot tag : runnable.child(String.valueOf(i)).child("tags").getChildren()){
                    String currTag = tag.getValue().toString();
                    if(currTag.equals("fruit")) {
                        isFruit = true;
                        break;
                    }
                    if(currTag.equals("vegetable")) {
                        isVegi= true;
                        break;
                    }
                }
                vegi.add(isVegi);
                fruit.add(isFruit);
                itemNames.add(itemName);
                secondaries.add(secondary);
                prizes.add(prize);
                images.add(image);
                exclusive.add(isExclusive);
                bestseller.add(isbestSeller);
            }
        }).addOnCompleteListener(runnable -> {

            progressBar.setVisibility(View.GONE);

            titles.add("Exclusive Offers");
            titles.add("Best Sellers");
            titles.add("Fruits & Vegetables");
            titles.add("Grocery");

            LinearLayout storeList = root.findViewById(R.id.storeList);
            for (int x = 0;x<titles.size();x++){
                View view = inflater.inflate(R.layout.store_list_item,null);
                TextView titleText = view.findViewById(R.id.title);
                LinearLayout cardList = view.findViewById(R.id.list);

                for(int i = 0;i<itemNames.size();i++){

                    switch (x){
                        case 0:
                            if(exclusive.get(i)) addview(inflater,itemNames,secondaries,prizes,images,cardList,i);
                            break;
                        case 1:
                            if(bestseller.get(i)) addview(inflater,itemNames,secondaries,prizes,images,cardList,i);
                            break;
                        case 2:
                            if(vegi.get(i) || fruit.get(i)) addview(inflater,itemNames,secondaries,prizes,images,cardList,i);
                            break;
                        case 3:
                            addview(inflater,itemNames,secondaries,prizes,images,cardList,i);
                            break;
                    }

                }

                titleText.setText(titles.get(x));
                storeList.addView(view);
            }
        });

        return root;
    }

    void addview(LayoutInflater inflater,ArrayList<String> itemNames,ArrayList<String> secondaries,ArrayList<Integer> prizes,ArrayList<String> images, LinearLayout cardList, int i){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        View view1 = inflater.inflate(R.layout.item_card,null);
        TextView itemName = view1.findViewById(R.id.itemNameCardItem);
        TextView secondary = view1.findViewById(R.id.secondaryTextCardItem);
        TextView prize = view1.findViewById(R.id.prizeTextCardItem);
        ImageView image = view1.findViewById(R.id.imageCardItem);
        ImageButton addBtn = view1.findViewById(R.id.addBtnCardItem);
        itemName.setText(itemNames.get(i));
        secondary.setText(secondaries.get(i));
        prize.setText("₹"+prizes.get(i));
        Glide.with(getContext()).load(images.get(i)).into(image);
        addBtn.setOnClickListener(view -> {
            DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
            users.get().addOnSuccessListener(runnable -> {
                int cartCount = Integer.parseInt(runnable.child(auth.getUid()).child("cartCount").getValue().toString());
                boolean exits= false;
                for(int j = 0;j<runnable.child(auth.getUid()).child("cart").getChildrenCount();j++){
                    if(runnable.child(auth.getUid()).child("cart").child(String.valueOf(j)).child("name").getValue().toString().equals(itemNames.get(i))){
                        int quantity = Integer.parseInt(runnable.child(auth.getUid()).child("cart").child(String.valueOf(j)).child("quantity").getValue().toString());
                        users.child(auth.getUid()).child("cart").child(String.valueOf(j)).child("quantity").setValue(quantity+1);
                        users.child(auth.getUid()).child("cartCount").setValue(cartCount+1);
                        exits = true;
                        break;
                    }
                }
                if(!exits){
                    DatabaseReference cart = users.child(auth.getUid()).child("cart");
                    int diffItemsInCart = (int)runnable.child(auth.getUid()).child("cart").getChildrenCount();
                    users.child(auth.getUid()).child("cart").child(String.valueOf(diffItemsInCart)).child("name").setValue(itemNames.get(i));
                    users.child(auth.getUid()).child("cart").child(String.valueOf(diffItemsInCart)).child("secondary").setValue(secondaries.get(i));
                    users.child(auth.getUid()).child("cart").child(String.valueOf(diffItemsInCart)).child("prize").setValue(prizes.get(i));
                    users.child(auth.getUid()).child("cart").child(String.valueOf(diffItemsInCart)).child("image").setValue(images.get(i));
                    users.child(auth.getUid()).child("cart").child(String.valueOf(diffItemsInCart)).child("quantity").setValue(1);
                    users.child(auth.getUid()).child("cartCount").setValue(cartCount+1);
                }

            }).addOnCompleteListener(runnable -> {Toast.makeText(getContext(), itemNames.get(i)+" added to cart", Toast.LENGTH_SHORT).show();})
                    .addOnFailureListener(runnable -> {Toast.makeText(getContext(), "Failed to add "+itemNames.get(i)+" to cart", Toast.LENGTH_SHORT).show();});
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int marginInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                25,
                getResources().getDisplayMetrics()
        );
        layoutParams.setMargins(0, 0, marginInDp, 0);
        view1.setLayoutParams(layoutParams);
        cardList.addView(view1);
    }

}
