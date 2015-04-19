package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.dbc.DatabaseControl;


public class Login_Activity extends Activity {
    DatabaseControl dbc=new DatabaseControl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);



        Button Login_button = (Button) findViewById(R.id.BtnLogin);
        Button Forget_button = (Button) findViewById(R.id.BtnForget);
        Button Exit_button = (Button) findViewById(R.id.BtnExit);
        Button Add_button = (Button) findViewById(R.id.btn_send);

        Login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();//Call the login function (*)


            }
        });
        dbc.insertToLogin("RP01","grp12", dbc.password_encoder("2012"),"Year of joined ", dbc.password_encoder("2012"));
        dbc.insertToLogin("abcd","pqr", dbc.password_encoder("pqr"),"pass is pqr ans is xyz ", dbc.password_encoder("lmn"));
        dbc.insertToLogin("abcd","pqrs", dbc.password_encoder("pqr"),"pass is pqr ans is xyz ", dbc.password_encoder("lmn"));
        Forget_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgot();
            }
        });

////Buttons and methods for test purposes
        Add_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dbc.insertToLogin("RP01","grp12", dbc.password_encoder("2012"),"Year of joined ", dbc.password_encoder("2012"));
                dbc.insertToLogin("abcd","pqr", dbc.password_encoder("pqr"),"pass is pqr ans is xyz ", dbc.password_encoder("lmn"));
                dbc.insertToLogin("abcd","pqrs", dbc.password_encoder("pqr"),"pass is pqr ans is xyz ", dbc.password_encoder("lmn"));
            }
        });

        Button see_btn=(Button)findViewById(R.id.btn_submit);
        see_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dbc.like();
            }
        });




    }

public void login(){

        String emp_id_cons=""; //variables to construct user/rep object
        String username_cons="";
        String password_cons="";
        String Question_cons="";
        String Answer_cons="";

        EditText username_ins=(EditText) findViewById(R.id.Edit_UserName);
        EditText password_ins=(EditText) findViewById(R.id.Edit_Password);
        String password_input=password_ins.getText().toString();
        String enc_password=dbc.password_encoder(password_input);

        boolean correct=false;
        Cursor c=dbc.getLoginInfo(username_ins.getText().toString());
        if (c.getCount()==0){
            Toast.makeText(getApplicationContext(), "Invalid credintials Please try again", Toast.LENGTH_SHORT).show();
            username_ins.setBackgroundColor(Color.RED);//If credentials wrong
            password_ins.setBackgroundColor(Color.RED);


        }

        else{
            if (c.moveToFirst()) {
                do {
                    //Toast.makeText(getApplicationContext(), "id "+c.getString(c.getColumnIndex("EmpId"))+ " user "+c.getString(c.getColumnIndex("UserName"))+ " pass "+c.getString(c.getColumnIndex("Password"))+ " quest "+c.getString(c.getColumnIndex("Question"))+ " update "+c.getString(c.getColumnIndex("LastUpdate")), Toast.LENGTH_SHORT).show();
                    if (c.getString(c.getColumnIndex("Password")).equals(enc_password)){
                        correct=true;
                        //Toast.makeText(getApplicationContext(), "done "+c.getString(c.getColumnIndex("Question")), Toast.LENGTH_SHORT).show();
                        emp_id_cons=(c.getString(c.getColumnIndex("EmpId")));
                        username_cons=(c.getString(c.getColumnIndex("UserName")));
                        password_cons=(c.getString(c.getColumnIndex("Password")));
                        Question_cons=(c.getString(c.getColumnIndex("Question")));
                        Answer_cons=(c.getString(c.getColumnIndex("Answer")));


                    }
                } while (c.moveToNext());
            }

        }
        if (correct){
                //Go to selection window.
            Toast.makeText(getApplicationContext(),"Welcome to the AGENT MATE",Toast.LENGTH_SHORT).show();
            
           Intent intent = new Intent(Login_Activity.this, Select_Menu.class);
            Representative logged_rep=new Representative(emp_id_cons, username_cons, password_cons, Question_cons, Answer_cons);
            intent.putExtra("Logged_User", logged_rep);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid Please try again", Toast.LENGTH_SHORT).show();
            username_ins.setBackgroundColor(Color.RED);
            password_ins.setBackgroundColor(Color.RED);
        }
    }


public void forgot(){
    EditText username_ins=(EditText) findViewById(R.id.Edit_UserName);
    Cursor c=dbc.getLoginInfo(username_ins.getText().toString());


    //String k=c.getString();

    if (c.getCount()==0){
        Toast.makeText(getApplicationContext(), "UserName not available ", Toast.LENGTH_SHORT).show();

    }

    else{
        ///Go to recover window
        int limit=c.getCount();
        String[] values_to_send=new String[limit];
        String[] id_to_send=new String[limit];
        c.moveToFirst();
        int i=0;


       Intent intent = new Intent(Login_Activity.this, Recover.class);
        Representative log_user=new Representative(c.getString(c.getColumnIndex("EmpId")), c.getString(c.getColumnIndex("UserName")), c.getString(c.getColumnIndex("Password")), c.getString(c.getColumnIndex("Question")), c.getString(c.getColumnIndex("Question")));
        //String k= c.getString(c.getColumnIndex("Question")).toString();
        intent.putExtra("logged_user",log_user);
        intent.putExtra("Username_is", c.getString(c.getColumnIndex("UserName")));
        intent.putExtra("Question_is", c.getString(c.getColumnIndex("Question")));
        intent.putExtra("Answer_is", c.getString(c.getColumnIndex("Answer")));
        intent.putExtra("EmpId_is", c.getString(c.getColumnIndex("EmpId")));
        intent.putExtra("Pwd_is", c.getString(c.getColumnIndex("Password")));


        //intent.putExtra("obj", new Bill("Check"));
        startActivity(intent);
    }
}




}





