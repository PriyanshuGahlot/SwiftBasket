package com.example.swiftbasket;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        ProgressBar progressBar = root.findViewById(R.id.progressBar);

        Button checkoutBtn = root.findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(view -> {

        });

        ArrayList<String> itemNames = new ArrayList<>();
        ArrayList<String> secondaries = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> prices = new ArrayList<>();
        ArrayList<String> quantities = new ArrayList<>();

        DatabaseReference cart = FirebaseDatabase.getInstance().getReference("users").child(auth.getUid()).child("cart");
        cart.get().addOnSuccessListener(runnable -> {
            for (int i =0;i<runnable.getChildrenCount();i++){
                itemNames.add(runnable.child(String.valueOf(i)).child("name").getValue().toString());
                secondaries.add(runnable.child(String.valueOf(i)).child("secondary").getValue().toString());
                prices.add(runnable.child(String.valueOf(i)).child("prize").getValue().toString());
                quantities.add(runnable.child(String.valueOf(i)).child("quantity").getValue().toString());
                images.add(runnable.child(String.valueOf(i)).child("image").getValue().toString());
            }
        }).addOnCompleteListener(runnable -> {
            CartCustomAdapter customAdapter = new CartCustomAdapter(getContext(),itemNames,secondaries,images,prices,quantities);
            ListView cartList = root.findViewById(R.id.cartList);
            View footerView = new View(getContext());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()); // set the height of the extra space you want to add
            footerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            cartList.addFooterView(footerView);
            cartList.setAdapter(customAdapter);
            progressBar.setVisibility(View.GONE);
        });

        return root;
    }
}

class CartCustomAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> itemNames;
    ArrayList<String> secondaries;
    ArrayList<String> Images;
    ArrayList<String> prices;
    ArrayList<String> quantities;

    public CartCustomAdapter(Context context, ArrayList<String> itemNames, ArrayList<String> secondaries, ArrayList<String> images, ArrayList<String> prices, ArrayList<String> quantities) {
        super(context, R.layout.cart_list_item, R.id.itemName, itemNames);
        this.context = context;
        this.itemNames = itemNames;
        this.secondaries = secondaries;
        Images = images;
        this.prices = prices;
        this.quantities = quantities;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.cart_list_item, parent, false);
        TextView itemName = view.findViewById(R.id.itemName);
        TextView secondary = view.findViewById(R.id.secondaryText);
        TextView prize = view.findViewById(R.id.prizeText);
        TextView quantity = view.findViewById(R.id.quantity);
        ImageButton incrimentBtn = view.findViewById(R.id.incrimentBtn);
        ImageButton decrimentBtn = view.findViewById(R.id.decrimentBtn);
        ImageButton removeBtn = view.findViewById(R.id.removeBtn);
        ImageView itemImage = view.findViewById(R.id.image);
        itemName.setText(itemNames.get(position));
        secondary.setText(secondaries.get(position));
        prize.setText(prices.get(position));
        quantity.setText(quantities.get(position));
        Glide.with(context).load(Images.get(position)).into(itemImage);
        return view;
    }
}