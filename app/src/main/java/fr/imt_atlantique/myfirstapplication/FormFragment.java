package fr.imt_atlantique.myfirstapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class FormFragment extends Fragment {

    private EditText textNom, textPrenom, textDate, textVille, textPhone;
    private User user;
    private Set<String> phoneList = new HashSet<>();
    private LinearLayout listContainer;
    private Spinner spinner;
    private Button button;
    private FormInterface activity;

    public interface FormInterface {
        void onSend(User user);
        void onDate(User user);
    }

    public FormFragment() {
        // Required empty public constructor
    }

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    public static FormFragment newInstance(User user) {
        FormFragment fragment = new FormFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof FormInterface) {
            activity = (FormInterface) context;
        }
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
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textNom = view.findViewById(R.id.editTextText3);
        textPrenom = view.findViewById(R.id.editTextText4);
        textDate = view.findViewById(R.id.editTextText5);
        textVille = view.findViewById(R.id.editTextText6);
        textPhone = view.findViewById(R.id.editTextText7);
        listContainer = view.findViewById(R.id.phone);
        spinner = view.findViewById(R.id.department);

        Calendar c = Calendar.getInstance();
        String today = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        if (user != null) {
            textNom.setText(user.getNom());
            textPrenom.setText(user.getPrenom());
            textVille.setText(user.getVille());
            textDate.setText(user.getDate() == null ? today : user.getDate());

            if (user.getDepartement() != null && spinner.getAdapter() != null) {
                for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
                    if (spinner.getAdapter().getItem(i).equals(user.getDepartement())) {
                        spinner.setSelection(i);
                    }
                }
            }
        } else {
            textDate.setText(today);
        }

        button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();

                String phone = textPhone.getText().toString().trim();
                String close = getString(R.string.close);
                String defaultDept = getString(R.string.department);

                if (user.getNom().isEmpty() || user.getPrenom().isEmpty() || user.getDate().isEmpty() || user.getVille().isEmpty()) {
                    Snackbar.make(view, getString(R.string.error), Snackbar.LENGTH_LONG)
                            .setAction(close, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Ferme le Snackbar
                                }
                            })
                            .show();
                    return;
                }

                if (user.getDepartement().equals(defaultDept)) {
                    Snackbar.make(view, getString(R.string.error_department), Snackbar.LENGTH_LONG)
                            .setAction(close, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Ferme le Snackbar
                                }
                            })
                            .show();
                    return;
                }

                if (!phone.isEmpty()) {
                    if (!phoneList.contains(phone)) {
                        phoneList.add(phone);
                        addPhoneAction(phone);
                        textPhone.setText("");
                        updateUser();
                    } else {
                        Snackbar.make(view, getString(R.string.error_phone), Snackbar.LENGTH_LONG)
                                .setAction(close, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Ferme le Snackbar
                                    }
                                })
                                .show();
                        return;
                    }
                }

                if (activity != null) {
                    activity.onSend(user);
                }
            }
        });

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
                if (activity != null) {
                    activity.onDate(user);
                }
            }
        });
    }

    public void updateUser() {
        String nom = textNom.getText().toString().trim();
        String prenom = textPrenom.getText().toString().trim();
        String ville = textVille.getText().toString().trim();
        String date = textDate.getText().toString().trim();
        String department = spinner.getSelectedItem().toString().trim();
        String[] phone = phoneList.toArray(new String[0]);

        user = new User(nom, prenom, ville, date, department, phone);
    }

    public void addPhoneAction(String phone) {
        Context context = requireContext();

        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);

        TextView newPhoneEntry = new TextView(context);
        newPhoneEntry.setText(phone);

        String del = getString(R.string.delete);

        Button delete = new Button(context);
        delete.setText(del);

        delete.setOnClickListener(view -> {
            listContainer.removeView(row);
            phoneList.remove(phone);

            String message = getString(R.string.delete_success);
            String close = getString(R.string.close);

            Snackbar.make(view.findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
                    .setAction(close, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Ferme le Snackbar
                        }
                    })
                    .show();
        });

        row.addView(newPhoneEntry);
        row.addView(delete);
        listContainer.addView(row);

        textPhone.setText("");
    }
}