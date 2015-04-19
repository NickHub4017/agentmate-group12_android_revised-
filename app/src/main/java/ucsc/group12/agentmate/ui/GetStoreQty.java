package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 10/6/2014.
 */
public class GetStoreQty extends Activity {
    DatabaseControl dbc = new DatabaseControl(this);
    AutoCompleteTextView itemName_edit_auto;
    AutoCompleteTextView itemID_edit_auto;
    TextView dataTv;
    String Item_ID_Select=null;
    AccelerationServiceReceiver accelerationReceiver = new AccelerationServiceReceiver();
    smsRecv smsbroad = new smsRecv();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(accelerationReceiver);
        unregisterReceiver(smsbroad);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_qty);

        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Get.Store.Intent");
        registerReceiver(accelerationReceiver, movementFilter);

        IntentFilter smsgetqtyintent;
        smsgetqtyintent = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsbroad, smsgetqtyintent);



        dataTv=(TextView)findViewById(R.id.txt_show_data);

        itemID_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_store_srch_qty_id);
        itemName_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_store_srch_qty_name);

        Cursor itm_cur = dbc.getAllItemByName();

        final String[] str_arry_item_id = new String[itm_cur.getCount()];
        final String[] str_arry_item_name = new String[itm_cur.getCount()];
        int j = 0;
        if (itm_cur.moveToFirst() && itm_cur.getCount() != 0) {
            do {
                str_arry_item_id[j] = itm_cur.getString(itm_cur.getColumnIndex("ItemID"));
                str_arry_item_name[j] = itm_cur.getString(itm_cur.getColumnIndex("ItemName"));
                j++;
            } while (itm_cur.moveToNext());
        }
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_name);
        itemName_edit_auto.setAdapter(adapter2);
        itemName_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);
                String ItemID=dbc.finditemByName(selection);
                Item_ID_Select=ItemID;
                itemID_edit_auto.setText(ItemID);

            }
        });


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_id);
        itemID_edit_auto.setAdapter(adapter);
        itemID_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);//
                String ItemName=dbc.finditemByID(selection);
                itemName_edit_auto.setText(ItemName);
                Item_ID_Select=selection;


            }

        });

        Button btn_get_data=(Button)findViewById(R.id.btn_get_data);
        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent in=new Intent("Req.Store.Intent");
                //in.putExtra("Store_QTY",100.0);
                in.putExtra("Get_Store_Item",Item_ID_Select);
                sendBroadcast(in);

                String phoneNo = "0719720470";//0712626607  //Password request send to agent.
                String msg = " StoreQty "+Item_ID_Select;
                try {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                    Toast.makeText(getApplicationContext(),"Requset has been send" , Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
           }
        });
    }

    public class AccelerationServiceReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)//this method receives broadcast messages. Be sure to modify AndroidManifest.xml file in order to enable message receiving
        {
            Double st_qty= intent.getDoubleExtra("Store_QTY", -1);
            String st_item = intent.getStringExtra("Store_Item");
            dataTv.setText("Current availbale quantity of "+st_item+" is "+st_qty);
            if (st_qty==0.0){
                dataTv.setBackgroundColor(Color.MAGENTA);
            }
            else{
                dataTv.setBackgroundColor(Color.GREEN);
            }
            //Toast.makeText(getApplication(),""+st_qty+" "+st_item,Toast.LENGTH_LONG).show();


        }
    }
    public class smsRecv extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "dyn got it.."+intent.getAction(), Toast.LENGTH_LONG).show();
        }
    }





}
