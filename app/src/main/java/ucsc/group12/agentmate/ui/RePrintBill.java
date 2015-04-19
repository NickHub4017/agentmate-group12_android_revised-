package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Bill;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 10/5/2014.
 */
public class RePrintBill extends Activity {
    DatabaseControl dbc=new DatabaseControl(this);
    AutoCompleteTextView autoVendor;
    Cursor cursor_ven_id;
    TextView tvSelectBill,tvCreateprogress;
    String Select_BillID=null;
    Button btn_RePrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reprint_bill);
        autoVendor=(AutoCompleteTextView)findViewById(R.id.auto_select_vendor);
        tvSelectBill=(TextView)findViewById(R.id.txt_select_bill);
        cursor_ven_id=dbc.getVendorTable();
        final String[] str_arry_vno=new String[cursor_ven_id.getCount()];
        final String[] str_arry_shname=new String[cursor_ven_id.getCount()];
        int i=0;
        if (cursor_ven_id.moveToFirst()){
            do{
                str_arry_vno[i]=cursor_ven_id.getString(cursor_ven_id.getColumnIndex("venderno"));
                str_arry_shname[i]=cursor_ven_id.getString(cursor_ven_id.getColumnIndex("ShopName"));

                i++;
            }while(cursor_ven_id.moveToNext());
        }

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,str_arry_vno);
        autoVendor.setAdapter(adapter);
        autoVendor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String)adapterView.getItemAtPosition(position);
                DrawTable(dbc.getBillArray(selection));

            }
        });
        btn_RePrint=(Button)findViewById(R.id.btn_reprint_bill);
        btn_RePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String VenOrderID=dbc.getVenOrderIDByBillID(Select_BillID);
                Toast.makeText(getApplicationContext(),"VenOrderId"+VenOrderID,Toast.LENGTH_LONG).show();
                CreateBill(VenOrderID);
            }
        });
    }

    public void table_hdr() {
        TableLayout tl = (TableLayout) findViewById(R.id.billTable);
        final TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLACK);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView label_Item_ID = new TextView(this);
        label_Item_ID.setId(20);
        label_Item_ID.setText("Bill ID");
        label_Item_ID.setTextColor(Color.WHITE);
        label_Item_ID.setPadding(5, 5, 5, 5);
        tr_head.addView(label_Item_ID);// add the column to the table row here

        TextView label_Item_Name = new TextView(this);
        label_Item_Name.setId(21);// define id that must be unique
        label_Item_Name.setText("Total"); // set the text for the header
        label_Item_Name.setTextColor(Color.WHITE); // set the color
        label_Item_Name.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Item_Name); // add the column to the table row here

        TextView label_Qty = new TextView(this);
        label_Qty.setId(22);// define id that must be unique
        label_Qty.setText("Date"); // set the text for the header
        label_Qty.setTextColor(Color.WHITE); // set the color
        label_Qty.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Qty); // add the column to the table row here

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

    }
    public void DrawTable(Bill bills[]){
        try {
            TableLayout ttt = (TableLayout) findViewById(R.id.billTable);
            //for (int i = 1; i < new_order.list.size(); i++) {
            ttt.removeAllViewsInLayout();
            //}
        }
        catch (Exception e){}
        table_hdr();
        for (int i=0;i<bills.length;i++){
            RowCreator(bills[i], R.id.billTable,i);
            Log.d("PlaceOrderArray", bills[i].getBillID() + "**" + bills[i].getPayDate());
        }
    }
   public void RowCreator(Bill item, int layout,int rw) {

        TableLayout tl = (TableLayout) findViewById(layout);

// Create the table row
        final TableRow tr = new TableRow(this);


        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow idrw=(TableRow) view.findViewById(view.getId());
                TextView idtv=(TextView)idrw.getVirtualChildAt(0);
                TextView dttv=(TextView)idrw.getVirtualChildAt(2);
                String chooseID=idtv.getText().toString();
                tvSelectBill.setText("Choosen Bill "+chooseID+" - Date "+dttv.getText().toString());
                Select_BillID=chooseID;

            }
        });


        if (rw % 2 != 0) tr.setBackgroundColor(Color.GRAY);
        tr.setId(100 + rw);
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
///TODO must write a method to get qty
//Create two columns to add as table data
        // Create a TextView to add date
        TextView labelID = new TextView(this);
        labelID.setId(200 + rw);
        labelID.setText(item.getBillID());
        labelID.setPadding(2, 0, 5, 0);
        labelID.setTextColor(Color.BLACK);
        tr.addView(labelID);

       TextView labelTotal = new TextView(this);
       labelTotal.setId(300 + rw);
       labelTotal.setText(String.valueOf(item.getTotal()));
       labelTotal.setPadding(2, 0, 5, 0);
       labelTotal.setTextColor(Color.BLACK);

        tr.addView(labelTotal);

       TextView labelDate = new TextView(this);
       labelDate.setId(400 + rw);
       labelDate.setTextColor(Color.BLACK);
       labelDate.setPadding(2, 0, 5, 0);
       labelDate.setText(String.valueOf(item.getBillDate()));
       tr.addView(labelDate);

// finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

    }

    public void CreateBill(String VenOrderID){//Get the data from My order table and make the printing file.
        //VenOrderID VARCHAR(16),ItemID VARCHAR(5),Qty INTEGER,DiscountAMT FLOAT,Sync BOOLEAN, PRIMARY KEY (VenOrderID, ItemID)

            Bill temp =dbc.findBillonVenOrderID(VenOrderID);
        String k=temp.getBillID().replaceAll(":","");
        String pathend= "/AgentMate/OUT/print.txt";
            File editFile = new File(Environment.getExternalStorageDirectory(),pathend);

        Log.i("HIIIIIIIII",String.valueOf(editFile.exists()));
        try {
            FileWriter fw = new FileWriter(editFile, false);

            fw.write("D.N.Distributors Sri Lanka\n");
            fw.write("Tel No:- 0719720470\n");
            fw.write("-----This bill is Reprinted\n");
            fw.write("reprint date:- "+Calendar.getInstance().getTime().toString()+"\n");
            fw.write("---------Bill Details------\n");
            fw.write("BillID :- "+temp.getBillID()+"\n");
            fw.write("VenOrderID :- "+VenOrderID+"\n");
            fw.write("getVenderID :- "+temp.getVenderID()+"\n");
            fw.write("getBillDate :- "+temp.getBillDate()+"\n");
            fw.write("getTotal :- "+temp.getTotal()+"\n");
            fw.write("ItemID\t"+"Qty\t"+"Discount Precentage\t"+"\n");
            Cursor cursor=dbc.getOrderfromMyorderOnVenOrderID(VenOrderID);

            if (cursor.moveToFirst()){
                do{

                    String line=cursor.getString(cursor.getColumnIndex("ItemID"))+" \t"+cursor.getString(cursor.getColumnIndex("Qty"))+" \t"+cursor.getString(cursor.getColumnIndex("DiscountAMT"))+"\n";
                    fw.write(line);
                }while (cursor.moveToNext());
            }
            else{

            }
            fw.write("D.N.Distributors"+"\n");
            fw.write("Developed by UCSC 2012 Group12"+"\n");
            fw.write("All Right reserved"+"\n");
            fw.flush();
            fw.close();
            Toast.makeText(getApplicationContext(),"Bill Has been created",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Log.i("HIIIIIIIII",e.toString()+"cannot write file");
        }

    }


}
