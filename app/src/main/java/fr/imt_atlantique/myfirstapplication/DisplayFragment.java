package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

    private TextView textNom, textPrenom, textVille, textDate, textDepartement;
    private LinearLayout listContainer;
    private User user;

    public DisplayFragment() {
        // Required empty public constructor
    }

    public static DisplayFragment newInstance(User user) {
        DisplayFragment fragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textNom = view.findViewById(R.id.nom);
        textPrenom = view.findViewById(R.id.prenom);
        textVille = view.findViewById(R.id.ville);
        textDate = view.findViewById(R.id.date);
        textDepartement = view.findViewById(R.id.department);

        listContainer = view.findViewById(R.id.phone);

        if (user != null) {
            String nom = getString(R.string.label_nom) + " " + user.getNom();
            String prenom = getString(R.string.label_prenom) + " " + user.getPrenom();
            String ville = getString(R.string.label_ville_naissance) + " " + user.getVille();
            String date = getString(R.string.label_date_naissance) + " " + user.getDate();
            String departement = getString(R.string.label_dep) + " : " + user.getDepartement();

            textNom.setText(nom);
            textPrenom.setText(prenom);
            textVille.setText(ville);
            textDate.setText(date);
            textDepartement.setText(departement);

            if (user.getPhone() != null) {
                for (String phone : user.getPhone()) {
                    displayPhone(phone);
                }
            }
        }
    }

    public void displayPhone(String phone) {
        LinearLayout linearLayout = new LinearLayout(requireContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView newPhoneEntry = new TextView(requireContext());
        newPhoneEntry.setText(phone);

        String dialText = getString(R.string.dial);

        Button dial = new Button(requireContext());
        dial.setText(dialText);

        dial.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(android.net.Uri.parse("tel:" + phone));
            startActivity(intent);
        });

        linearLayout.addView(newPhoneEntry);
        linearLayout.addView(dial);
        listContainer.addView(linearLayout);
    }
}