package com.example.t5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView studentListView;
    List<Student> students;
    StudentListAdapter adapter;
    StudentDbHelper dbHelper;

    private long lastClickTime = 0;
    private int lastClickPosition = -1;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;

    int[] avatars = {
            R.drawable.ic1, R.drawable.ic2, R.drawable.ic3,
            R.drawable.ic4, R.drawable.ic5, R.drawable.ic6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentListView = findViewById(R.id.androidVersionsList);
        dbHelper = new StudentDbHelper(this);

        loadStudentsFromDb();

        if (students == null) {
            students = new ArrayList<>();
        }

        adapter = new StudentListAdapter(this, students);
        studentListView.setAdapter(adapter);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long clickTime = System.currentTimeMillis();

                if (position == lastClickPosition && (clickTime - lastClickTime) < DOUBLE_CLICK_TIME_DELTA) {
                    Student selectedStudent = students.get(position);
                    Intent intent = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intent.putExtra(StudentDetailActivity.EXTRA_STUDENT, selectedStudent);
                    startActivity(intent);

                    lastClickTime = 0;
                    lastClickPosition = -1;
                }
                lastClickTime = clickTime;
                lastClickPosition = position;
            }
        });
    }

    private void loadStudentsFromDb() {
        if (dbHelper.getStudentCount() == 0) {
            populateInitialData();
        }
        students = dbHelper.getAllStudents();
        if (students == null) {
            students = new ArrayList<>();
        }
    }

    private void populateInitialData() {
        int avatar1 = (avatars.length > 0) ? avatars[0] : R.mipmap.ic_launcher;
        int avatar2 = (avatars.length > 1) ? avatars[1] : R.mipmap.ic_launcher;
        int avatar3 = (avatars.length > 2) ? avatars[2] : R.mipmap.ic_launcher;
        int avatar4 = (avatars.length > 3) ? avatars[3] : R.mipmap.ic_launcher;
        int avatar5 = (avatars.length > 4) ? avatars[4] : R.mipmap.ic_launcher;
        int avatar6 = (avatars.length > 5) ? avatars[5] : R.mipmap.ic_launcher;

        dbHelper.addStudent(new Student("Nguyen Duy Phuc", "21200331", avatar1, "21200331@student.hcmus.edu.vn", "Electronics and Telecommunications", "30/09/2003"));
        dbHelper.addStudent(new Student("Tran Van Tien", "21180023", avatar2, "21180023@student.hcmus.edu.vn", "Physical", "22/07/2003"));
        dbHelper.addStudent(new Student("Le Thu Van", "22001013", avatar3, "22001013@student.hcmus.edu.vn", "Information Technology", "10/11/2004"));
        dbHelper.addStudent(new Student("Tran Hung Dao", "21801234", avatar4, "21801234@student.hcmus.edu.vn", "Data Science", "05/01/2003"));
        dbHelper.addStudent(new Student("Nguyen Thi Ha", "21180234", avatar5, "21180234@student.hcmus.edu.vn", "Biology", "30/09/2003"));
        dbHelper.addStudent(new Student("Nguyen Quang Tuan", "21160204", avatar6, "21160204@student.hcmus.edu.vn", "Chemistry", "12/06/2003"));
    }


    class StudentListAdapter extends ArrayAdapter<Student> {
        Context context;
        List<Student> studentList;
        LayoutInflater inflater;

        StudentListAdapter(Context context, List<Student> studentList) {
            super(context, R.layout.student_list_item, studentList);
            this.context = context;
            this.studentList = studentList;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.student_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.avatarView = convertView.findViewById(R.id.imageViewAvatar);
                viewHolder.nameText = convertView.findViewById(R.id.textViewStudentName);
                viewHolder.idText = convertView.findViewById(R.id.textViewStudentId);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Student currentStudent = studentList.get(position);

            if (currentStudent != null) {
                viewHolder.avatarView.setImageResource(currentStudent.getAvatarResId());
                viewHolder.nameText.setText(currentStudent.getName());
                viewHolder.idText.setText("ID: " + currentStudent.getStudentId());
            } else {
                viewHolder.nameText.setText("N/A");
                viewHolder.idText.setText("ID: N/A");
                viewHolder.avatarView.setImageResource(R.mipmap.ic_launcher);
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView avatarView;
            TextView nameText;
            TextView idText;
        }
    }
}