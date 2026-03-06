package fr.imt_atlantique.myfirstapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DateActivity extends AppCompatActivity {
    private TextView textDate;
    private int y, m, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_date);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.date), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textDate = findViewById(R.id.displayDate);

        Intent dateIntent = getIntent();
        String year = dateIntent.getStringExtra("year");
        String month = dateIntent.getStringExtra("month");
        String day = dateIntent.getStringExtra("day");

        if (year != null && month != null && day != null) {
            y = Integer.parseInt(year);
            m = Integer.parseInt(month) - 1;
            d = Integer.parseInt(day);

            String dateString = day + "/" + month + "/" + year;
            textDate.setText(dateString);
        }

        textDate.setOnClickListener(v -> showPicker());
    }

    public void abandon(View v) {
        Intent dateIntent = new Intent();
        setResult(RESULT_CANCELED, dateIntent);
        finish();
    }

    public void validate(View v) {
        String date = textDate.getText().toString().trim();

        Intent dateIntent = new Intent();
        dateIntent.putExtra("date", date);
        setResult(RESULT_OK, dateIntent);
        finish();
    }

    public void showPicker() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                y = year;
                m = month;
                d = day;

                String result = day + "/" + (month + 1) + "/" + year;
                textDate.setText(result);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(DateActivity.this, dateListener, y, m, d);
        datePickerDialog.show();
    }
}
