package com.example.t5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StudentDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_ID = "_id"; // Primary key
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AVATAR_RES_ID = "avatar_res_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_MAJOR = "major";
    public static final String COLUMN_DOB = "dob";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_STUDENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_ID + " TEXT UNIQUE, " + // Student ID should be unique
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_AVATAR_RES_ID + " INTEGER, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_MAJOR + " TEXT, " +
                    COLUMN_DOB + " TEXT" +
                    ");";

    public StudentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        Log.d("StudentDbHelper", "Database table created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_AVATAR_RES_ID, student.getAvatarResId());
        values.put(COLUMN_EMAIL, student.getEmail());
        values.put(COLUMN_MAJOR, student.getMajor());
        values.put(COLUMN_DOB, student.getDob());

        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return id; // Returns row ID of new insert, or -1 if error
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, null, null, null, null, COLUMN_NAME + " ASC"); // Order by name

        if (cursor.moveToFirst()) {
            do {
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int avatarResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AVATAR_RES_ID));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String major = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAJOR));
                String dob = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOB));

                students.add(new Student(name, studentId, avatarResId, email, major, dob));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return students;
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_STUDENTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}