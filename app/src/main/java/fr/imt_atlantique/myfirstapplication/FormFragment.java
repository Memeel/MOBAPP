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

    private String date = "";


    public interface FormInterface {
        void onSend(User user);
        void onDate(int year, int month, int day);
    }

    public FormFragment() {
        // Required empty public constructor
    }

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    public static FormFragment newInstance(String date) {
        FormFragment fragment = new FormFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
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
            String d = getArguments().getString("date");
            if (d != null) {
                date = d;
            }
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

        if (date != null && !date.isEmpty()) {
            textDate.setText(date);
        }
        button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = textNom.getText().toString().trim();
                String prenom = textPrenom.getText().toString().trim();
                String ville = textVille.getText().toString().trim();
                String phone = textPhone.getText().toString().trim();

                date = textDate.getText().toString().trim();

                String department = spinner.getSelectedItem().toString().trim();

                String textToShow = getString(R.string.error);
                String defaultDept = getString(R.string.department);

                if (!(nom.isEmpty() || prenom.isEmpty() || date.isEmpty() || ville.isEmpty() || department.equals(defaultDept))) {
                    if (!phone.isEmpty()) {
                        if (!phoneList.contains(phone)) {
                            phoneList.add(phone);
                            textToShow = nom + ", " + prenom + ", " + date + ", " + ville + ", " + department +
                                    "\n" + getString(R.string.phone_added) + listContainer.getChildCount();

                        } else {
                            textToShow = getString(R.string.error_phone);
                        }
                        textPhone.setText("");
                    } else {textToShow = "OK";}

                    user = new User(nom, prenom, ville, date, department, phoneList.toArray(new String[0]));
                }

                else if (!(nom.isEmpty() || prenom.isEmpty() || date.isEmpty() || ville.isEmpty()) & department.equals(getString(R.string.department))) {
                    textToShow = getString(R.string.error_department);
                }

                if (!(textToShow.equals(getString(R.string.error)) || textToShow.equals(getString(R.string.error_phone)) || textToShow.equals(getString(R.string.error_department)))) {
                    if (activity != null) {
                        activity.onSend(user);
                    }
                } else {
                    String close = getString(R.string.close);

                    Snackbar.make(view.findViewById(R.id.main), textToShow, Snackbar.LENGTH_LONG)
                            .setAction(close, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Ferme le Snackbar
                                }
                            })
                            .show();
                }
            }
        });

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = textDate.getText().toString().trim();
                if (!date.isEmpty()) {
                    String[] splitDate = date.split("/");
                    if (splitDate.length == 3) {
                        try {
                            int day = Integer.parseInt(splitDate[0]);
                            int month = Integer.parseInt(splitDate[1]);
                            int year = Integer.parseInt(splitDate[2]);
                            activity.onDate(year, month - 1, day);

                            return;
                        } catch (NumberFormatException e) {
                            // ignore and use current date
                        }
                    }
                }

                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                activity.onDate(year, month, day);
            }
        });
    }
}