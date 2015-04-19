package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Payment;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.dbc.DatabaseControl;


public class MakePayment extends Activity {
    DatabaseControl dbc;
    AutoCompleteTextView ven_id;
    String selection_venid;
    TextView txt_areas;
    Payment pay;
    RadioButton rbtn_check;
    RadioButton rbtn_cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_payment);
        txt_areas=(TextView)findViewById(R.id.txt_areas_payment);
        rbtn_check=(RadioButton) findViewById(R.id.radio_check);
        rbtn_cash=(RadioButton) findViewById(R.id.radio_cash);
        rbtn_cash.setChecked(true);
        final Representative logged_rep=(Representative) getIntent().getExtras().getSerializable("logged_user");
        dbc=new DatabaseControl(MakePayment.this);
        ven_id=(AutoCompleteTextView)findViewById(R.id.autotxt_venid);
        Cursor cursor_ven_id=dbc.getVendorTable();

        final String[] str_arry_vno=new String[cursor_ven_id.getCount()];
        final String[] str_arry_shname=new String[cursor_ven_id.getCount()];
        pay=new Payment(logged_rep);
        int i=0;
        if (cursor_ven_id.moveToFirst()){      //Add items to selectable list
            do{
                str_arry_vno[i]=cursor_ven_id.getString(cursor_ven_id.getColumnIndex("venderno"));
                str_arry_shname[i]=cursor_ven_id.getString(cursor_ven_id.getColumnIndex("ShopName"));

                i++;
            }while(cursor_ven_id.moveToNext());
        }

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,str_arry_vno);
        ven_id.setAdapter(adapter);
        ven_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String)adapterView.getItemAtPosition(position);
                String areas=String.valueOf(getAreas(selection));
                txt_areas.setText("Your Current Areas is Rs "+areas);
                pay.setVenderNo(selection);

                //Toast.makeText(getApplicationContext(), selection, Toast.LENGTH_SHORT).show();


            }
        });
        final Button btn_create_payment=(Button)findViewById(R.id.btn_make_payment);   //When click the btn relavnt details will set in the pay table.
        btn_create_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pay.getVenderNo()!=null) {
                    EditText edit_payamount = (EditText) view.getRootView().findViewById(R.id.edit_payamount);
                    try {
                        pay.setPayAmount(Double.parseDouble(edit_payamount.getText().toString()));
                        btn_create_payment.setEnabled(false);
                    }
                    catch (NumberFormatException e){
                        Toast.makeText(getApplicationContext(),"Please enter the amount",Toast.LENGTH_LONG).show();
                        btn_create_payment.setEnabled(true);
                    }
                    if (rbtn_check.isChecked()) {
                        pay.setType("chk");
                    } else if (rbtn_cash.isChecked()) {
                        pay.setType("csh");
                    }
                    pay.SubmitToDB(view.getRootView().getContext());
                    String msg="You ("+pay.getVenderNo()+ ") have make payment of Rs "+pay.getPayAmount()+" in "+pay.getPayDate()+" via "+ pay.getType()+" THANK YOU. D.N. DISTRIBUTORS.";
                    String number=dbc.getVendorConfNumberByID(pay.getVenderNo());
                    if (number==null||number.length()!=10){      //If the vendor doesnt have confirmation number sms willl redirect to agents mobile.
                        number="0777117110";
                    }
                    try {

                        SmsManager smsManager = SmsManager.getDefault();
                        //Toast.makeText(getApplicationContext(),"a "+(smsManager==null),Toast.LENGTH_LONG).show();
                        smsManager.sendTextMessage(number, null, msg, null, null);
                        Toast.makeText(getApplicationContext(), "Message Sent",
                                Toast.LENGTH_LONG).show();


                    } catch (Exception ex) {
                        /*Toast.makeText(getApplicationContext(),
                                ex.getMessage().toString(),
                                Toast.LENGTH_LONG).show();*/
                        ex.printStackTrace();
                    }
                    Intent intent = new Intent();

                    if (pay.getType().equals("chk")){
                        intent.putExtra("mnh",0.0);
                        intent.putExtra("tot",pay.getPayAmount());
                    }
                    else{
                        intent.putExtra("mnh",pay.getPayAmount());
                        intent.putExtra("tot",pay.getPayAmount());
                    }

                    intent.setAction("group12.tutorialspoint.PaymentIntent");

                    sendBroadcast(intent);
                }

                }

        });


    }


    public String[] vendors(DatabaseControl dbc){
        Cursor cur=dbc.getVendorTable();
        String[] vendrs=new String[cur.getCount()];
        int i=0;
        if(cur.moveToFirst()){
            do{
                vendrs[i]=cur.getString(cur.getColumnIndex("venderno"));
            }while(cur.moveToNext());
        }
        return vendrs;
    }

    public String[] getbillVenOrderNo(Cursor cur){

        String[] bills=new String[cur.getCount()];
        int i=0;
        if(cur.moveToFirst()){
            do{
                bills[i]=cur.getString(cur.getColumnIndex("VenOrderID"));
            }while(cur.moveToNext());
        }
        return bills;
    }

    //get the areas of the vendor.
    public double getAreas(String vendorno){
        return dbc.getAreasForVendor(vendorno);
    }

}
