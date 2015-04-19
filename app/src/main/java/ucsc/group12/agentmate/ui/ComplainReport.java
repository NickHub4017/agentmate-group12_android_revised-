
package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Complain;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 9/22/2014.
 */
public class ComplainReport extends Activity {
    DatabaseControl dbc = new DatabaseControl(this);

    AutoCompleteTextView vno_edit_auto_comp;
    AutoCompleteTextView itemcode_edit_auto_comp;
    AutoCompleteTextView itemname_edit_auto_comp;
    EditText edit_comment;
    Button btn_cmp_submit;
    Cursor cursor_ven_id, cursor_item;
    Representative logged_rep;

    String vendor_id=null;
    String itemcode=null;
    String itemname;
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain_report);


        logged_rep=(Representative)getIntent().getExtras().getSerializable("logged_user");
        //Toast.makeText(getApplicationContext(),logged_rep.getEmp_id(),Toast.LENGTH_SHORT).show();
        vno_edit_auto_comp = (AutoCompleteTextView) findViewById(R.id.txtauto_cmp_vendor);
        itemcode_edit_auto_comp = (AutoCompleteTextView) findViewById(R.id.textauto_cmp_itemcode);
        itemname_edit_auto_comp = (AutoCompleteTextView) findViewById(R.id.txtauto_cmp_itemname);
        edit_comment = (EditText) findViewById(R.id.edit_cmp_complain);
        btn_cmp_submit = (Button) findViewById(R.id.btn_compalin_submit);

        cursor_ven_id = dbc.getVendorTable();//Load the items to select list to select
        final String[] str_arry_vno = new String[cursor_ven_id.getCount()];
        final String[] str_arry_shname = new String[cursor_ven_id.getCount()];
        int i = 0;
        if (cursor_ven_id.moveToFirst()) {
            do {
                str_arry_vno[i] = cursor_ven_id.getString(cursor_ven_id.getColumnIndex("venderno"));
                str_arry_shname[i] = cursor_ven_id.getString(cursor_ven_id.getColumnIndex("ShopName"));

                i++;
            } while (cursor_ven_id.moveToNext());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_vno);
        vno_edit_auto_comp.setAdapter(adapter);
        vno_edit_auto_comp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                vendor_id = (String) adapterView.getItemAtPosition(position);


            }
        });

        cursor_item = dbc.getAllItemByName();//Add item list to select to select field.
        final String[] str_arry_item_id = new String[cursor_item.getCount()];
        final String[] str_arry_item_name = new String[cursor_item.getCount()];
        int j = 0;
        if (cursor_item.moveToFirst() && cursor_item.getCount() != 0) {
            do {
                str_arry_item_id[j] = cursor_item.getString(cursor_item.getColumnIndex("ItemID"));
                str_arry_item_name[j] = cursor_item.getString(cursor_item.getColumnIndex("ItemName"));
                j++;
            } while (cursor_item.moveToNext());
            ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_id);
            itemcode_edit_auto_comp.setAdapter(adapter2);
            itemcode_edit_auto_comp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    itemcode = (String) adapterView.getItemAtPosition(position);
                    Cursor cur = dbc.getExactItemByID(itemcode);
                    cur.moveToFirst();

                    itemname=cur.getString(cur.getColumnIndex("ItemName"));
                    itemname_edit_auto_comp.setText(itemname);
                }

            });

            ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_name);
            itemname_edit_auto_comp.setAdapter(adapter3);
            itemname_edit_auto_comp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    itemname = (String) adapterView.getItemAtPosition(position);
                    itemcode=str_arry_item_id[srch(itemname,str_arry_item_name)];
                    itemcode_edit_auto_comp.setText(itemcode);
                }

            });


        }

        btn_cmp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cmpID=null;
                Calendar cal = Calendar.getInstance();
                cmpID = logged_rep.getEmp_id()+String.valueOf(cal.get(Calendar.DATE))+String.valueOf(cal.get(Calendar.MONTH))+String.valueOf(cal.get(Calendar.YEAR))+String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+String.valueOf(cal.get(Calendar.MINUTE))+String.valueOf(cal.get(Calendar.SECOND));
                //Toast.makeText(getApplicationContext(),cmpID,Toast.LENGTH_SHORT).show();
                String complain=edit_comment.getText().toString();
                if (cmpID!=null && itemcode!=null && edit_comment.length()!=0 && vendor_id!=null ) {
                    Complain cmp = new Complain(cmpID, itemcode, complain, vendor_id, false);
                    dbc.addToComplain(cmp);
                    Toast.makeText(getApplicationContext(), "Complain is sucessfully submitted", Toast.LENGTH_SHORT).show();
                    Log.d("Via complain report",complain);
                }
                else{
                    Log.d("Via complain report",complain+edit_comment.length()+" cmpID"+cmpID+"itemcode "+itemcode+"vendor_id "+vendor_id);
                    Toast.makeText(getApplicationContext(), "You need to fill all field in the form", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public int srch(String key,String[] array){//Get the position of the given string

        for (int i=0;i<array.length;i++){
            if (array[i].equals(key)){
                return i;
            }
        }
        return -1;
    }
    public void testme(View view){
        DatabaseControl d=new DatabaseControl(this);
        d.getComplainID();
    }
}
