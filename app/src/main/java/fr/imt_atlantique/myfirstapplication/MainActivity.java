package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity implements FormFragment.FormInterface, DateFragment.DateInterface {

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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main, new FormFragment())
                    .commit();
        }
    }

    @Override
    public void onSend(User user) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, DisplayFragment.newInstance(user))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDate(int year, int month, int day) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, DateFragment.newInstance(year, month, day))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void sendDate(String date) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, FormFragment.newInstance(date))
                .addToBackStack(null)
                .commit();
    }
}
