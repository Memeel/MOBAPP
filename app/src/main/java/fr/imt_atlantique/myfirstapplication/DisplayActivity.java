package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    private TextView textNom, textPrenom, textVille, textDate, textDepartement;
    private LinearLayout listContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.display), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textNom = findViewById(R.id.nom);
        textPrenom = findViewById(R.id.prenom);
        textVille = findViewById(R.id.ville);
        textDate = findViewById(R.id.date);
        textDepartement = findViewById(R.id.department);

        listContainer = findViewById(R.id.phone);

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

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
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView newPhoneEntry = new TextView(this);
        newPhoneEntry.setText(phone);

        String dialText = getString(R.string.dial);

        Button dial = new Button(this);
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
