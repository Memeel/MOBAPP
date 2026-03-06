package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private EditText textNom, textPrenom, textDate, textVille, textPhone;
    private User user;
    private Set<String> phoneList = new HashSet<>();
    SharedPreferences sharedPreferences;
    private LinearLayout listContainer;
    private Spinner spinner;
    private ActivityResultLauncher<Intent> dateLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textNom = findViewById(R.id.editTextText3);
        textPrenom = findViewById(R.id.editTextText4);
        textDate = findViewById(R.id.editTextText5);
        textVille = findViewById(R.id.editTextText6);
        textPhone = findViewById(R.id.editTextText7);
        listContainer = findViewById(R.id.phone);
        spinner = findViewById(R.id.department);

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);

        textNom.setText(sharedPreferences.getString("nom", ""));
        textPrenom.setText(sharedPreferences.getString("prenom", ""));
        textDate.setText(sharedPreferences.getString("date", ""));
        textVille.setText(sharedPreferences.getString("ville", ""));
        spinner.setSelection(sharedPreferences.getInt("department", 0));

        phoneList = new HashSet<>(sharedPreferences.getStringSet("phoneListSet", new HashSet<>()));

        listContainer.removeAllViews();
        for (String phone : phoneList) {
            addPhoneAction(phone);
        }

        dateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String date = data.getStringExtra("date");
                        textDate.setText(date);
                    }
                }
        });

        textDate.setOnClickListener(v -> {
            Intent dateIntent = new Intent(Intent.ACTION_PICK);
            String currentDate = textDate.getText().toString().trim();

            if (!currentDate.isEmpty()) {
                String[] date = currentDate.split("/");
                dateIntent.putExtra("day", date[0]);
                dateIntent.putExtra("month", date[1]);
                dateIntent.putExtra("year", date[2]);
            } else {
                Calendar c = Calendar.getInstance();
                dateIntent.putExtra("day", String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
                dateIntent.putExtra("month", String.valueOf(c.get(Calendar.MONTH) + 1));
                dateIntent.putExtra("year", String.valueOf(c.get(Calendar.YEAR)));}

            dateLauncher.launch(dateIntent);
        });

        Log.i("Lifecycle", "onCreate method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lifecycle", "onStart method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", "onResume method");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", "onPause method");
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("nom", textNom.getText().toString());
        editor.putString("prenom", textPrenom.getText().toString());
        editor.putString("date", textDate.getText().toString());
        editor.putString("ville", textVille.getText().toString());
        editor.putInt("department", spinner.getSelectedItemPosition());
        editor.putStringSet("phoneListSet", phoneList);

        editor.apply();

        Log.i("Lifecycle", "onStop method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy method");
    }

    public void validateAction(View v) {
        // Récupération du texte
        String nom = textNom.getText().toString().trim();
        String prenom = textPrenom.getText().toString().trim();
        String date = textDate.getText().toString().trim();
        String ville = textVille.getText().toString().trim();
        String phone = textPhone.getText().toString().trim();

        String department = spinner.getSelectedItem().toString().trim();

        String textToShow = getString(R.string.error);
        String defaultDept = getString(R.string.department);

        if (!(nom.isEmpty() || prenom.isEmpty() || date.isEmpty() || ville.isEmpty() || department.equals(defaultDept))) {
            if (!phone.isEmpty()) {
                if (!phoneList.contains(phone)) {
                    phoneList.add(phone);
                    addPhoneAction(phone);
                    textToShow = nom + ", " + prenom + ", " + date + ", " + ville + ", " + department +
                            "\n" + getString(R.string.phone_added) + listContainer.getChildCount();

                } else {
                    textToShow = getString(R.string.error_phone);
                }
                textPhone.setText("");
            } else {textToShow = "OK";}

            user = new User(nom, prenom, ville, date, department, phoneList.toArray(new String[0]));
        }

        else if (!(nom.isEmpty() || prenom.isEmpty() || date.isEmpty() || ville.isEmpty()) & department.equals("Sélectionner un département")) {
            textToShow = getString(R.string.error_department);
        }

        if (!(textToShow.equals(getString(R.string.error)) || textToShow.equals(getString(R.string.error_phone)) || textToShow.equals(getString(R.string.error_department)))) {
            Intent intent = new Intent(this, DisplayActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            String close = getString(R.string.close);

            Snackbar.make(findViewById(R.id.main), textToShow, Snackbar.LENGTH_LONG)
                    .setAction(close, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Ferme le Snackbar
                        }
                    })
                    .show();
        }
    }

    public void resetAction(MenuItem item) {
        textNom.setText("");
        textPrenom.setText("");
        textDate.setText("");
        textVille.setText("");

        listContainer.removeAllViews();

        spinner.setSelection(0);
    }

    public void openWiki(MenuItem item) {
        String ville = textVille.getText().toString().trim();
        String close = getString(R.string.close);

        if (!(ville.isEmpty())) {
            String url =  "http://fr.wikipedia.org/?search=" + ville;
            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            try {
                startActivity(intent);
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.main), getString(R.string.errorAction), Snackbar.LENGTH_LONG)
                        .setAction(close, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Ferme le Snackbar
                            }
                        })
                        .show();
            }

        } else {
            Snackbar.make(findViewById(R.id.main), getString(R.string.errorVille), Snackbar.LENGTH_LONG)
                    .setAction(close, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Ferme le Snackbar
                        }
                    })
                    .show();
        }

    }

    public void share(MenuItem item) {
        String ville = textVille.getText().toString().trim();

        String close = getString(R.string.close);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        if (!(ville.isEmpty())) {
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareText) + ville);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.main), getString(R.string.errorAction), Snackbar.LENGTH_LONG)
                        .setAction(close, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Ferme le Snackbar
                            }
                        })
                        .show();
            }

        } else {
            Snackbar.make(findViewById(R.id.main), getString(R.string.errorVille), Snackbar.LENGTH_LONG)
                    .setAction(close, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Ferme le Snackbar
                        }
                    })
                    .show();
        }

    }

    public void addPhoneAction(String phone) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        TextView newPhoneEntry = new TextView(this);
        newPhoneEntry.setText(phone);

        String del = getString(R.string.delete);

        Button delete = new Button(this);
        delete.setText(del);

        delete.setOnClickListener(view -> {
            listContainer.removeView(row);
            phoneList.remove(phone);

            String message = getString(R.string.delete_success);
            String close = getString(R.string.close);

            Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nom", textNom.getText().toString());
        outState.putString("prenom", textPrenom.getText().toString());
        outState.putString("date", textDate.getText().toString());
        outState.putString("ville", textVille.getText().toString());
        outState.putInt("department", spinner.getSelectedItemPosition());
        outState.putStringArrayList("phoneList", new ArrayList<>(phoneList));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        textNom.setText(savedInstanceState.getString("nom"));
        textPrenom.setText(savedInstanceState.getString("prenom"));
        textDate.setText(savedInstanceState.getString("date"));
        textVille.setText(savedInstanceState.getString("ville"));
        spinner.setSelection(savedInstanceState.getInt("department"));

        ArrayList<String> listFromBundle = savedInstanceState.getStringArrayList("phoneList");
        if (listFromBundle != null) {
            phoneList = new HashSet<>(listFromBundle);
            listContainer.removeAllViews();
            for (String phone : phoneList) {
                addPhoneAction(phone);
            }
        }
    }
}
