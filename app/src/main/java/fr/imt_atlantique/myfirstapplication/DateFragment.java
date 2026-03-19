package fr.imt_atlantique.myfirstapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class DateFragment extends Fragment {

    private TextView textDate;
    private Button abandon, validate;
    private DateInterface activity;
    private User user;

    public interface DateInterface {
        void sendDate(User user);
    }

    public DateFragment() {
        // Required empty public constructor
    }

    public static DateFragment newInstance(User user) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof DateInterface) {
            activity = (DateInterface) context;
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
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        abandon = view.findViewById(R.id.abandon);
        abandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.sendDate(user);
            }
        });

        validate = view.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = textDate.getText().toString().trim();
                user.setDate(date);
                activity.sendDate(user);
            }
        });

        textDate = view.findViewById(R.id.displayDate);
        textDate.setText(user.getDate());

        textDate.setOnClickListener(v -> showPicker());
    }


    public void showPicker() {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String result = day + "/" + (month + 1) + "/" + year;
                textDate.setText(result);
            }
        };

        String[] date = user.getDate().split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]) - 1;
        int year = Integer.parseInt(date[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), dateListener, year, month, day);
        datePickerDialog.show();
    }
}