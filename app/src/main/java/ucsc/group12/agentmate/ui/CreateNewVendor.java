package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.bll.Vendor;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 8/23/2014.
 */
public class CreateNewVendor extends Activity {
DatabaseControl dbc=new DatabaseControl(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Representative logged_rep=(Representative)getIntent().getExtras().getSerializable("logged_user");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_customer);

    Button submit_btn=(Button)findViewById(R.id.btn_create);


    submit_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           vender_create_func();        //Call to the vender creation function

        }
        });

       /* Button view_btn=(Button)findViewById(R.id.btn_view_test);


        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a();


            }
        });*/
        //Vendor vn=new Vendor("abcdV001","shop","vname","addrs","shopno","cnfno",0.1,false);
        //dbc.addVendor(vn);
       Cursor crs=dbc.getVendorTable();
       if (crs.getCount()!=0){
           crs.moveToFirst();
           String repID=logged_rep.Emp_id;
           String[] reps_vendors=new String[crs.getCount()];
           int max=0;
           do{
               String lst_vno=crs.getString(crs.getColumnIndex("venderno"));
               String[] splited_lst=lst_vno.split("V");

               if(splited_lst[0].equals(logged_rep.Emp_id)){
                   int temp=Integer.parseInt(splited_lst[1]);
                   if (temp>max){
                       max=temp;
                   }
               }
           }while(crs.moveToNext());
           String val=String.valueOf(max+1);
           if (val.length()==1){
               val="00"+val;
           }
           else if (val.length()==2){
               val="0"+val;
           }
           // Toast.makeText(getApplicationContext(),logged_rep.Emp_id+"V"+val,Toast.LENGTH_SHORT).show();
           EditText edit_vno=(EditText)findViewById(R.id.edit_vno);
           edit_vno.setText(logged_rep.Emp_id+"V"+val);
           edit_vno.setEnabled(false);
       }
        else{
           EditText edit_vno=(EditText)findViewById(R.id.edit_vno);
           edit_vno.setText(logged_rep.Emp_id+"V"+"001");
           edit_vno.setEnabled(false);
       }
    }


    public void vender_create_func(){//Table to create the vender by getting the data from user inputs.
//Create TextBox items
        EditText edit_vno_window=(EditText)findViewById(R.id.edit_vno);
        EditText edit_shopname_window=(EditText)findViewById(R.id.edit_ShopName);
        EditText edit_owner_window=(EditText)findViewById(R.id.edit_Owner);
        EditText edit_address_window=(EditText)findViewById(R.id.edit_Address);
        EditText edit_shoptel_window=(EditText)findViewById(R.id.edit_Shop_TelNo);
        EditText edit_cnftel_window=(EditText)findViewById(R.id.edit_Conf_Tel_No);
//Get inputs to strings
        String vno_ins=edit_vno_window.getText().toString();
        String shopname_ins=edit_shopname_window.getText().toString();
        String owner_ins=edit_owner_window.getText().toString();
        String address_ins=edit_address_window.getText().toString();
        String shoptel_ins=edit_shoptel_window.getText().toString();
        String cnftel_ins=edit_cnftel_window.getText().toString();
//Check for availability of data
        if (!shopname_ins.isEmpty() && !owner_ins.isEmpty() && !address_ins.isEmpty()&&(shoptel_ins.length()==10 || shoptel_ins.length()==0)&&(cnftel_ins.length()==10 || cnftel_ins.length()==0)){
        Vendor new_vendor=new Vendor(vno_ins,shopname_ins,owner_ins,address_ins,shoptel_ins,cnftel_ins,0.0,false);
        //Vendor new_vendor=new Vendor(1,"a","b","c",2,3,0.0,false);
        dbc.addVendor(new_vendor);
            Toast.makeText(getApplication(),"New User added Successfully",Toast.LENGTH_LONG).show();
        }
        else{
        //Check for the empty inputs which is required
            if (shopname_ins.isEmpty()){
                edit_shopname_window.setBackgroundColor(Color.CYAN);
            }
            else{
                edit_shopname_window.setBackgroundColor(Color.GREEN);
            }
        //Check for the empty inputs which is required
            if (owner_ins.isEmpty()){
                edit_owner_window.setBackgroundColor(Color.CYAN);
            }
            else{
                edit_owner_window.setBackgroundColor(Color.GREEN);
            }


            if (address_ins.isEmpty()){
               edit_address_window.setBackgroundColor(Color.CYAN);
            }
            else{
                edit_address_window.setBackgroundColor(Color.GREEN);
            }


            if (cnftel_ins.length()!=10 && cnftel_ins.length()!=0){
                edit_cnftel_window.setBackgroundColor(Color.CYAN);
            }
            else{
                edit_cnftel_window.setBackgroundColor(Color.GREEN);
            }


            if (shoptel_ins.length()!=10 && shoptel_ins.length()!=0){
                edit_shoptel_window.setBackgroundColor(Color.CYAN);
            }
            else{
                edit_shoptel_window.setBackgroundColor(Color.GREEN);
            }


            Toast.makeText(getApplicationContext(),"Please fill the required fields",Toast.LENGTH_SHORT).show();
        }

    }

  /*  public void a(){
        EditText et=(EditText)findViewById(R.id.edit_ShopName);
        String k=et.getText().toString();
        Cursor c=dbc.findVendor(k);
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this, "vno " + c.getString(c.getColumnIndex("venderno")) + " ShopName " + c.getString(c.getColumnIndex("ShopName")) + " VenderName " + c.getString(c.getColumnIndex("VenderName")) + " Address " + c.getString(c.getColumnIndex("Address")) + " TelNoShop " + c.getString(c.getColumnIndex("TelNoShop"))+ " TelNoConfirm " + c.getString(c.getColumnIndex("TelNoConfirm"))+ " Overdue " + c.getString(c.getColumnIndex("Overdue"))+ " Confirm " + c.getString(c.getColumnIndex("Confirm")), Toast.LENGTH_SHORT).show();

            } while (c.moveToNext());
        }
        if (c.getCount()==0){
            Toast.makeText(this,"0", Toast.LENGTH_SHORT);
        }
    }*/

}
