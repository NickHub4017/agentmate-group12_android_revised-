package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Vendor;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 8/28/2014.
 */
public class Edit_Customer extends Activity{

    EditText edit_new_shopname_window;
    EditText edit_new_owner_window;
    EditText edit_new_address_window;
    EditText edit_new_shopTel_window;
    EditText edit_new_confTel_window;
    AutoCompleteTextView no_edit_auto;
    AutoCompleteTextView shname_edit_auto;

    DatabaseControl dbc=new DatabaseControl(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_customer);

        Button btn_saveChanges=(Button)findViewById(R.id.btn_new_edit);
        btn_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_vendor(view);
            }
        });

        edit_new_shopname_window=(EditText)findViewById(R.id.autoComplete_vname);
        edit_new_owner_window=(EditText)findViewById(R.id.edit_new_Owner);
        edit_new_address_window=(EditText)findViewById(R.id.edit_new_Address);
        edit_new_shopTel_window=(EditText)findViewById(R.id.edit_new_Shop_TelNo);
        edit_new_confTel_window=(EditText)findViewById(R.id.edit_new_Conf_Tel_No);
        no_edit_auto = (AutoCompleteTextView) findViewById(R.id.autoComplete_vendor);
        shname_edit_auto = (AutoCompleteTextView) findViewById(R.id.autoComplete_vname);

        edit_new_owner_window.setEnabled(false);
        edit_new_address_window.setEnabled(false);
        edit_new_shopTel_window.setEnabled(false);
        edit_new_confTel_window.setEnabled(false);


        final Cursor cursor=dbc.getVendorTable();//Get the value 's of all vendors in the table
        //Toast.makeText(this, ""+cursor.getCount(), Toast.LENGTH_SHORT).show();
        final String[] str_arry_vno=new String[cursor.getCount()];
        final String[] str_arry_shname=new String[cursor.getCount()];
        int i=0;
        if (cursor.moveToFirst()){
            do{
                str_arry_vno[i]=cursor.getString(cursor.getColumnIndex("venderno"));
                str_arry_shname[i]=cursor.getString(cursor.getColumnIndex("ShopName"));
                //Toast.makeText(this, ""+cursor.getString(cursor.getColumnIndex("ShopName")), Toast.LENGTH_SHORT).show();
                i++;
            }while(cursor.moveToNext());
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,str_arry_vno);//Set Array adpter for the Vno

        no_edit_auto.setAdapter(adapter);
        no_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String)adapterView.getItemAtPosition(position);
                int real_pos=getArrayPosition(selection,str_arry_vno);
                cursor.moveToPosition(real_pos);
                //Toast.makeText(getApplicationContext(),selection,Toast.LENGTH_SHORT).show();
                setDataField(cursor);
                enableall();
                //cursor.close();
            }
        });

        ArrayAdapter adapter_shname = new ArrayAdapter(this,android.R.layout.simple_list_item_1,str_arry_shname);///Set aray adapter for shop names

        shname_edit_auto.setAdapter(adapter_shname);
        shname_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String)adapterView.getItemAtPosition(position);
                int real_pos=getArrayPosition(selection,str_arry_shname);
                cursor.moveToPosition(real_pos);
                //Toast.makeText(getApplicationContext(),""+cursor.getString(cursor.getColumnIndex("ShopName"))+"Shop name"+selection,Toast.LENGTH_SHORT).show();
                setDataField(cursor);
                enableall();
                //cursor.close();
            }
        });


    }
//Get the position of the key in the given array
    public static int getArrayPosition(String key,String[] array){
        for (int i=0;i<array.length;i++){
            if (array[i].equals(key)){
                return i;
            }
        }
        return -1;
    }
//Activate all fields when user select the vender.
    public void enableall(){
        edit_new_owner_window.setEnabled(true);
        edit_new_address_window.setEnabled(true);
        edit_new_shopTel_window.setEnabled(true);
        edit_new_confTel_window.setEnabled(true);

    }
    //Accroding to the selection other data fields will be populated
    public void setDataField(Cursor cursor){
        no_edit_auto.setText(cursor.getString(cursor.getColumnIndex("venderno")));
        shname_edit_auto.setText(cursor.getString(cursor.getColumnIndex("ShopName")));
        edit_new_owner_window.setText(cursor.getString(cursor.getColumnIndex("VenderName")));
        edit_new_address_window.setText(cursor.getString(cursor.getColumnIndex("Address")));
        edit_new_shopTel_window.setText(cursor.getString(cursor.getColumnIndex("TelNoShop")));
        edit_new_confTel_window.setText(cursor.getString(cursor.getColumnIndex("TelNoConfirm")));

    }

//Edit the vendor details and send to database
    public void edit_vendor(View v){

        String v_no=no_edit_auto.getText().toString();
        String newShopName=edit_new_shopname_window.getText().toString();
        String newOwnerName=edit_new_owner_window.getText().toString();
        String newAddressName=edit_new_address_window.getText().toString();
        String newShopTelName=edit_new_shopTel_window.getText().toString();
        String newConfTelName=edit_new_confTel_window.getText().toString();
        if(!newShopName.isEmpty() && !newOwnerName.isEmpty() && !newAddressName.isEmpty() &&(newShopTelName.length()==10 || newShopTelName.length()==0)&&(newConfTelName.length()==10 || newConfTelName.length()==0)) {
            Vendor edited_vendor = new Vendor(v_no, newShopName, newOwnerName, newAddressName, newShopTelName, newConfTelName, 0, false);
            dbc.editVendor(edited_vendor, getApplicationContext());
        }
        else{

            Toast.makeText(getApplicationContext(),"required fields are missing",Toast.LENGTH_SHORT).show();
            //Check for the empty inputs which is required
            if (newShopName.isEmpty()){
                edit_new_shopname_window.setBackgroundColor(Color.MAGENTA);
            }
            else{
                edit_new_shopname_window.setBackgroundColor(Color.GREEN);
            }

            if (newOwnerName.isEmpty()){
                edit_new_owner_window.setBackgroundColor(Color.MAGENTA);
            }
            else{
                edit_new_owner_window.setBackgroundColor(Color.GREEN);
            }

            if (newAddressName.isEmpty()){
                edit_new_address_window.setBackgroundColor(Color.MAGENTA);
            }
            else{
                edit_new_address_window.setBackgroundColor(Color.GREEN);
            }

            if (newConfTelName.length()!=10 && newConfTelName.length()!=0){
                edit_new_confTel_window.setBackgroundColor(Color.MAGENTA);
            }
            else{
                edit_new_confTel_window.setBackgroundColor(Color.GREEN);
            }

            if (newShopTelName.length()!=10 && newShopTelName.length()!=0){
                edit_new_shopTel_window.setBackgroundColor(Color.MAGENTA);
            }
            else{
                edit_new_shopTel_window.setBackgroundColor(Color.GREEN);
            }

        }

    }
}
