package com.example.swiftbasket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");

        TextView nameText = root.findViewById(R.id.nameText);
        TextView emailText = root.findViewById(R.id.emailText);
        ListView menuList = root.findViewById(R.id.menuList);

        users.child(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(runnable -> {
            String name = runnable.child("name").getValue().toString();
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
            nameText.setText(name);
            emailText.setText(email);
        });

        String[] texts = {"Orders","My Details","Payment Methods","Register as a Seller","Help","About"};
        int[] images = {R.drawable.orders,R.drawable.account,R.drawable.payment,R.drawable.rupee,R.drawable.help,R.drawable.about};

        CustomAdapter adapter = new CustomAdapter(getContext(),texts,images);
        menuList.setAdapter(adapter);

        return root;
    }
}

class CustomAdapter extends ArrayAdapter<String>{

    Context context;
    String[] texts;
    int[] images;

    public CustomAdapter(Context context, String[] texts, int[] images) {
        super(context, R.layout.image_and_text_list, R.id.text,texts);
        this.context = context;
        this.texts = texts;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        View view = layoutInflater.inflate(R.layout.image_and_text_list,parent,false);
        TextView text = view.findViewById(R.id.text);
        ImageView image = view.findViewById(R.id.image);
        text.setText(texts[position]);
        image.setImageResource(images[position]);
        return view;
    }
}