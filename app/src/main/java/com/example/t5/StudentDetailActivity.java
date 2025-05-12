package com.example.t5;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentDetailActivity extends AppCompatActivity {

    public static final String EXTRA_STUDENT = "com.example.t5.EXTRA_STUDENT";

    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView idTextView;
    private TextView emailTextView;
    private TextView majorTextView;
    private TextView dobTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        avatarImageView = findViewById(R.id.detailImageViewAvatar);
        nameTextView = findViewById(R.id.detailTextViewName);
        idTextView = findViewById(R.id.detailTextViewStudentId);
        emailTextView = findViewById(R.id.detailTextViewEmail);
        majorTextView = findViewById(R.id.detailTextViewMajor);
        dobTextView = findViewById(R.id.detailTextViewDOB);

        Student student = getIntent().getParcelableExtra(EXTRA_STUDENT);

        if (student != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(student.getName());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            avatarImageView.setImageResource(student.getAvatarResId());
            nameTextView.setText(nameTextView.getText() + student.getName());
            idTextView.setText(idTextView.getText() + student.getStudentId());
            emailTextView.setText(emailTextView.getText() + student.getEmail());
            majorTextView.setText(majorTextView.getText() + student.getMajor());
            dobTextView.setText(dobTextView.getText() + student.getDob());
        } else {
            Toast.makeText(this, "Error: Student data not found.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}