package ucsc.group12.agentmate.ui;

/**
 * Created by NRV on 8/23/2014.
 */


import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.dbc.DatabaseControl;


public class Recover extends  Activity{
    DatabaseControl dbc=new DatabaseControl(this);
    String ans,Eid,Encoded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_window);
        final Representative logged_rep=(Representative)getIntent().getExtras().getSerializable("logged_user");
        String user=getIntent().getExtras().getString("Username_is");
        EditText ans_txt=(EditText)findViewById(R.id.edit_empid);
        ans_txt.setText("Your UserName is  "+(String)user);
        ans_txt.setEnabled(false);
        String qstn=getIntent().getExtras().getString("Question_is");

        TextView qsttv=(TextView)findViewById(R.id.lblqstn);
        qsttv.setText(qstn);
        ans=getIntent().getExtras().getString("Answer_is");
        Eid=getIntent().getExtras().getString("EmpId_is");
        Encoded=getIntent().getExtras().getString("Pwd_is");



        Button submit=(Button)findViewById(R.id.btn_submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText ans_txt=(EditText)findViewById(R.id.editanswer);
                String ent_ans=ans_txt.getText().toString();
               if (ans.equals(dbc.password_encoder(ent_ans))){

                    String phoneNo = "0719720470";//0712626607  //Password request send to agent.
                    String msg = "Employee "+Eid+" Request for password change And his code is "+Encoded;
                    try {

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                        Toast.makeText(getApplicationContext(),"Your submission is send to head office. Please check your email/sms inbox." , Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(),
                                ex.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Wrong answer. Try again. :0" , Toast.LENGTH_SHORT).show();

                }
            }
        });
        final EditText code_win=(EditText)findViewById(R.id.editcode);
        Button btn_code_win=(Button)findViewById(R.id.btn_submit_code);

        //If the code is right then user redirect to the next window.
        btn_code_win.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Encoded.equals(code_win.getText().toString())){
                    Intent reset=new Intent(Recover.this,SetPassword.class);
                    reset.putExtra("logged_user", logged_rep);
                    startActivity(reset);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Wrong code",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

