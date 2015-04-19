
package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Order;
import ucsc.group12.agentmate.bll.SellItem;
import ucsc.group12.agentmate.bll.UnitMap;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 9/7/2014.
 */
public class PlaceOrderSecond extends Activity {
    static Order new_order = new Order();
    static Order new_temp_order = new Order();
    DatabaseControl dbc = new DatabaseControl(this);
    AutoCompleteTextView itemID_edit_auto;
    AutoCompleteTextView itemName_edit_auto;
    Cursor itm_cur;
    SellItem currentItem;
    public static SellItem t_selItem;
    public int count = 0;
    public static int cur_st_value = 0;
    Bundle cur_bun;
    int demandQty_global;
    int demandUnitIndex_global;
    public static UnitMap u_map[];
    boolean exsist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order_item_add);
/*

        itemID_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_comp_item_id);
        itemName_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_comp_item_name);

        final Representative logged_rep = (Representative) getIntent().getExtras().getSerializable("logged_user");
        final Vendor sel_vendor = (Vendor) getIntent().getExtras().getSerializable("vendor");

        TextView logged_vendor_tv = (TextView) findViewById(R.id.txt_vname_order_b);
        logged_vendor_tv.setText("Selected Vendor is :- " + sel_vendor.getShopName());



        table_hdr();///Draw headers of the tables
        demand_table_hdr();

        itm_cur = dbc.getAllItemByName();

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

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_id);
        itemID_edit_auto.setAdapter(adapter);
        itemID_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);//

                int pos = new_order.findById(selection);
                Toast.makeText(getApplicationContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
                if (pos != -1) {
                    currentItem = new_order.findByIdObj(pos);
                    exsist=true;
                    //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "OK DA?", Toast.LENGTH_SHORT).show();
                    Cursor cur = dbc.getExactItemByID(selection);
                    cur.moveToFirst();
                    exsist=false;

                    currentItem = new SellItem(selection, PlaceOrderSecond.this);
                }
                //if object exsits in the array array will update else new will create

                cur_st_value = currentItem.getStoreQty();//reference to the dialog box say about qty in stock
                u_map = dbc.findQtyMap(selection);


                FragmentManager fm = getFragmentManager();
                DialogGetQty md = new DialogGetQty();
                md.show(fm, "dialog");///After this object(full) will create in onDialog method.

                ///TODO write an constructor for item class which retrieve its data in database when the ItemId gives as constructor parameter.

            }
        });
//////LOAD ARRAY ADAPTERS

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_name);
        itemName_edit_auto.setAdapter(adapter2);
        //itemName_edit_auto.setOnItemClickListener(new );

//////LOAD ARRAY ADAPTER

        Button b2 = (Button) findViewById(R.id.button_testing);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(1);
                DatabaseControl d = new DatabaseControl(getApplicationContext());
                d.k();
            }
        });
        b2.setVisibility(View.INVISIBLE);
    }

    public void RowCreator(SellItem item, int layout,int rw) {

        TableLayout tl = (TableLayout) findViewById(layout);

// Create the table row
        final TableRow tr = new TableRow(this);


        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                DialogEditQty md = new DialogEditQty();
                md.show(fm, "edit");

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
        labelID.setText(item.getItemID());
        labelID.setPadding(2, 0, 5, 0);
        labelID.setTextColor(Color.BLACK);
        tr.addView(labelID);

        TextView labelName = new TextView(this);
        labelName.setId(300 + rw);
        labelName.setText(String.valueOf(item.getItemName()));
        labelName.setTextColor(Color.BLACK);
        tr.addView(labelName);

        TextView labelQty = new TextView(this);
        labelQty.setId(400 + rw);
//        labelQty.setText(String.valueOf(item.getQty())+" "+item.getSelectedUnit());
        labelQty.setTextColor(Color.BLACK);
        //tr.addView(labelQty);

        TextView labelDiscount = new TextView(this);
        labelDiscount.setId(500 + rw);
        //labelDiscount.setText(String.valueOf(item.getRelavantDiscount(item.getQty())));
        labelDiscount.setTextColor(Color.BLACK);

        if (layout == R.id.selected_table1) {
            labelQty.setText(String.valueOf(item.getQty()) + " " + item.getSelectedUnit());
            tr.addView(labelQty);
            labelDiscount.setText(String.valueOf(item.getRelavantDiscount(item.getQty())));
            tr.addView(labelDiscount);
            TextView labelPrice = new TextView(this);
            labelPrice.setId(600 + rw);
            double value = (100 - item.getRelavantDiscount(item.getQty())) * item.getPrice() * item.getQty();
            labelPrice.setText(String.valueOf(value / 100));
            labelPrice.setTextColor(Color.BLACK);
            tr.addView(labelPrice);
        } else {
            labelQty.setText(String.valueOf(demandQty_global) + " " + u_map[demandUnitIndex_global].getUnit());
            tr.addView(labelQty);

            labelDiscount.setText("Today");
            tr.addView(labelDiscount);
        }

// finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


    }

    public void table_hdr() {
        TableLayout tl = (TableLayout) findViewById(R.id.selected_table1);
        final TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLACK);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView label_Item_ID = new TextView(this);
        label_Item_ID.setId(20);
        label_Item_ID.setText("Item ID");
        label_Item_ID.setTextColor(Color.WHITE);
        label_Item_ID.setPadding(5, 5, 5, 5);
        tr_head.addView(label_Item_ID);// add the column to the table row here

        TextView label_Item_Name = new TextView(this);
        label_Item_Name.setId(21);// define id that must be unique
        label_Item_Name.setText("Item Name"); // set the text for the header
        label_Item_Name.setTextColor(Color.WHITE); // set the color
        label_Item_Name.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Item_Name); // add the column to the table row here

        TextView label_Qty = new TextView(this);
        label_Qty.setId(22);// define id that must be unique
        label_Qty.setText("Qty"); // set the text for the header
        label_Qty.setTextColor(Color.WHITE); // set the color
        label_Qty.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Qty); // add the column to the table row here

        TextView label_Discount = new TextView(this);
        label_Discount.setId(23);// define id that must be unique
        label_Discount.setText("Discount"); // set the text for the header
        label_Discount.setTextColor(Color.WHITE); // set the color
        label_Discount.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Discount); // add the column to the table row here

        TextView label_Price = new TextView(this);
        label_Price.setId(24);// define id that must be unique
        label_Price.setText("Price"); // set the text for the header
        label_Price.setTextColor(Color.WHITE); // set the color
        label_Price.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Price); // add the column to the table row here

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

    }

    public void demand_table_hdr() {
        TableLayout tl = (TableLayout) findViewById(R.id.demanded_table);
        final TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.BLACK);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        TextView label_Item_ID = new TextView(this);
        label_Item_ID.setId(30);
        label_Item_ID.setText("Item ID");
        label_Item_ID.setTextColor(Color.WHITE);
        label_Item_ID.setPadding(5, 5, 5, 5);
        tr_head.addView(label_Item_ID);// add the column to the table row here

        TextView label_Item_Name = new TextView(this);
        label_Item_Name.setId(31);// define id that must be unique
        label_Item_Name.setText("Item Name"); // set the text for the header
        label_Item_Name.setTextColor(Color.WHITE); // set the color
        label_Item_Name.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Item_Name); // add the column to the table row here

        TextView label_Qty = new TextView(this);
        label_Qty.setId(32);// define id that must be unique
        label_Qty.setText("Qty"); // set the text for the header
        label_Qty.setTextColor(Color.WHITE); // set the color
        label_Qty.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Qty); // add the column to the table row here

        TextView label_Discount = new TextView(this);
        label_Discount.setId(33);// define id that must be unique
        label_Discount.setText("Date"); // set the text for the header
        label_Discount.setTextColor(Color.WHITE); // set the color
        label_Discount.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Discount); // add the column to the table row here


        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public void onDialogMessage(int qty, int demandQty, int qtyUnitindex, int demandQtyUnitIndex) {
        currentItem.setQty(qty+currentItem.getQty());//ToDo repeat of the same item row qty must change
        currentItem.setStoreQty(currentItem.getStoreQty() - qty);
        currentItem.setSelectedUnit(u_map[qtyUnitindex].getUnit());
        demandQty_global = demandQty;
        demandUnitIndex_global = demandQtyUnitIndex;
        if (qty != 0)
            if (!exsist) {
                //RowCreator(currentItem, R.id.selected_table1);
                new_order.addItem(currentItem);
            }//else means exsits item loaded alredy and we updated it.

        if (demandQty != 0) {
            SellItem temp_item=new SellItem(currentItem.getItemID(),PlaceOrderSecond.this);
            temp_item.setQty(demandQty);
            temp_item.setStoreQty(-1);
            new_temp_order.addItem(temp_item);
            Log.d("PlaceSecond",String.valueOf(new_temp_order.list.size()));
            DrawTable(R.id.demanded_table,new_temp_order.list);
            //Toast.makeText(getApplicationContext(),new_temp_order.list.size(),Toast.LENGTH_SHORT).show();


        }
        count++;


        DrawTable(R.id.selected_table1,new_order.list);

        //RowCreator(currentItem, R.id.demanded_table, 1);
    }

    public void DrawTable(int layout,ArrayList<SellItem> arls){
        try {
            TableLayout ttt = (TableLayout) findViewById(layout);
            //for (int i = 1; i < new_order.list.size(); i++) {
            ttt.removeAllViewsInLayout();
            //}
        }
        catch (Exception e){}
        if (layout==R.id.selected_table1) {
            table_hdr();
        }
        else{
            demand_table_hdr();
        }
        for (int i=0;i<arls.size();i++){
            RowCreator(arls.get(i), layout,i);
            Log.d("PlaceOrderArray",arls.get(i).getItemID()+"**"+arls.get(i).getQty());
        }
    }

    public static String[] getStringUnits() {
        String[] array = new String[u_map.length];
        for (int i = 0; i < u_map.length; i++) {
            array[i] = u_map[i].getUnit();
        }
        return array;
    }

    //ToDO complete this method
    @Override
    public void onEditMessage() {
        //DrawTable(R.id.selected_table1,new_order.list);
        Toast.makeText(getApplicationContext(),"**********",Toast.LENGTH_SHORT).show();
        DrawTable(R.id.selected_table1,new_order.list);

    }




}
*/
    }
}