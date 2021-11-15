package com.torres.edgar.contacts;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    Button btnAdd,btnDelete;
    EditText txtFullname,txtContacts;
    ListView lstviewContacts;
    ArrayAdapter adapterContacts;




    private DataBaseHelper dbhelper = new DataBaseHelper(MainActivity.this, "ContactsDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd=(Button)findViewById(R.id.buttonAdd);
        btnDelete=(Button)findViewById(R.id.buttonDelete);
        txtFullname=(EditText)findViewById(R.id.editTextFullName);
        txtContacts=(EditText)findViewById(R.id.editTextPhoneNum);
        lstviewContacts=(ListView) findViewById(R.id.ListViewContacts);
        reloadContacts();
        btnAddListener();



    }

    private void btnAddListener() {

        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sqlStr = "INSERT INTO tblcontacts (fullname, phonenumber) VALUES ('"
                        + txtFullname.getText() + "', '" +txtContacts.getText() + "')";
                db.execSQL(sqlStr);
                reloadContacts();
            }
        });

    }


    private void reloadContacts() {

        SQLiteDatabase dbContacts = dbhelper.getWritableDatabase();
        //get table from sqlite_master
        Cursor cContacts = dbContacts.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblcontacts'", null);
        cContacts.moveToNext();
        if (cContacts.getCount() == 0) { //check if the database is exisitng
            SQLite.FITCreateTable("ContactsDatabase", this, "tblcontacts", "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fullname VARCHAR(90),phonenumber VARCHAR(500)"); //create table
        } else {
            cContacts = dbContacts.rawQuery("SELECT id, fullname, phonenumber  FROM tblcontacts order by id desc", null);


            String valueContacts[] = new String[cContacts.getCount()];

            int ctrl = 0;
            while (cContacts.moveToNext()) {
                String strFor = "";
                strFor += "Full Name : " + cContacts.getString(cContacts.getColumnIndex("fullname"));
                strFor += System.lineSeparator() + "Contact # : " + cContacts.getString(cContacts.getColumnIndex("phonenumber"));

                valueContacts[ctrl] = strFor;
                ctrl++;
            }
            //display to the listview for each Vote
            adapterContacts = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, valueContacts);
            try {
                lstviewContacts.setAdapter(adapterContacts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}
