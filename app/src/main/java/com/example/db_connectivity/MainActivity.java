package com.example.db_connectivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity {
    EditText RollNo,Name,Percentage;
    Button Insert,Delete,Update,ViewAll;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db=openOrCreateDatabase("studentDB",Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS students (rollno VARCHAR NOT NULL,name VARCHAR,marks VARCHAR);");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        RollNo=findViewById(R.id.rollNo);
        Name=findViewById(R.id.Name);
        Percentage=findViewById(R.id.Percentage);
        Insert=findViewById(R.id.insert);
        Delete=findViewById(R.id.delete);
        Update=findViewById(R.id.update);
        ViewAll=findViewById(R.id.ViewAll);

        Insert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(validate(RollNo.getText().toString(),Name.getText().toString(),Percentage.getText().toString())){
                    showMessage("Require","All Field must be filled");
                }
                else {
                    db.execSQL("insert into students values('"+RollNo.getText()+"','"+Name.getText()+"','"+Percentage.getText()+"');");
                    showMessage("Success","Recode added");
                    clearText();
                }

            }
        });

        ViewAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Cursor c = db.rawQuery("SELECT * FROM students", null);
                if (c.getCount() == 0)
                {
                    showMessage("Error", "No records found"); return;
                }

                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext())
                {
                    buffer.append("Rollno: " + c.getString(0) + "\n"); buffer.append("Name: " + c.getString(1) + "\n"); buffer.append("Marks: "
                        + c.getString(2) + "\n");

                }
                showMessage("Students Details", buffer.toString());

            }
        });

        Delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(android.view.View v) {
                db.execSQL("DELETE FROM students WHERE rollno='" + RollNo.getText() + "'");
                showMessage("Deleted Successfully","RollNo "+RollNo.getText());
                clearText();
            }
        });

        Update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(validate(RollNo.getText().toString(),Name.getText().toString(),Percentage.getText().toString())){
                    showMessage("Require","All Field Required");
                }
                else{
                db.execSQL("UPDATE students SET name='" + Name.getText() +
                        "',marks='" + Percentage.getText() + "' WHERE rollno='" + RollNo.getText() + "'");
                showMessage("Updated ","Successfully Updated");
                clearText();
                }
            }
        });

    }
    public boolean validate(String rollNo,String name, String percentage){
        if(rollNo.isEmpty() || name.isEmpty() || percentage.isEmpty())
        {
            return true;
        }
        else {
            return false;
        }
    }
    public void clearText() {
        RollNo.setText("");
        Name.setText("");
        Percentage.setText("");
        RollNo.requestFocus();
    }
    public void showMessage( String title,String msg) {
        Builder builder=new Builder(this);
        builder.setCancelable(true); builder.setTitle(title);
        builder.setMessage(msg); builder.show();
    }
}