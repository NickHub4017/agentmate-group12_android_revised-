package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Order;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.bll.SellItem;
import ucsc.group12.agentmate.bll.UnitMap;
import ucsc.group12.agentmate.bll.Vendor;
import ucsc.group12.agentmate.bll.mapper;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 10/6/2014.
 */
public class GetReturnItems extends Activity implements DialogGetReturnQty.GetQtyCommunicator,DialogEditReturnQty.EditComm{

    Order return_order=new Order(); //Create new Order
    DatabaseControl dbc = new DatabaseControl(this);
    UnitMap[] map;
    mapper mpUnitnames=null;
    AutoCompleteTextView itemID_edit_auto;
    AutoCompleteTextView itemName_edit_auto;
    SellItem currentItem;
    boolean select_exsist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order_item_add);

        Spinner spnr=(Spinner)findViewById(R.id.spinner_demanditem_id);
        spnr.setVisibility(View.INVISIBLE);

        itemID_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_comp_item_id);
        itemName_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_comp_item_name);

        EditText edit_new_qty=(EditText)findViewById(R.id.edit_demand_qty);
        edit_new_qty.setVisibility(View.INVISIBLE);

        TextView txt_demand_label=(TextView)findViewById(R.id.textView4);
        txt_demand_label.setVisibility(View.INVISIBLE);

        final Representative logged_rep = (Representative) getIntent().getExtras().getSerializable("logged_user");
        final Vendor sel_vendor = (Vendor) getIntent().getExtras().getSerializable("vendor");
        //return_order.setVenOrderID(logged_rep.Emp_id+"Or"+ Calendar.getInstance().getTime().toString());  No need a VenOrderID

        return_order.setVender_no(sel_vendor.getVenderNo());

        TextView logged_vendor_tv = (TextView) findViewById(R.id.txt_vname_order_b);
        logged_vendor_tv.setText("Selected Vendor is :- " + sel_vendor.getShopName());
        TextView txt_title_tv = (TextView) findViewById(R.id.txt_place_title);
        txt_title_tv.setText("Get Return Items");

        table_hdr();///Draw headers of the tables


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
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_id);
        itemID_edit_auto.setAdapter(adapter);
        itemID_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);//
                mpUnitnames=new mapper(getApplicationContext(),selection);//GET the unit maps
                selector(selection);
                itemName_edit_auto.setText(currentItem.getItemName());

            }

        });

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_name);
        itemName_edit_auto.setAdapter(adapter2);
        itemName_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);
                String ItemID=dbc.finditemByName(selection);
                mpUnitnames=new mapper(getApplicationContext(),ItemID);//GET the unit maps
                itemID_edit_auto.setText(ItemID);
                selector(ItemID);
            }
        });

        Button btset=(Button)findViewById(R.id.button_testing);
        btset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbc.k();
            }
        });
        btset.setVisibility(View.INVISIBLE);


        Button btn_submit=(Button)findViewById(R.id.btn_submit_edit_demand);
        btn_submit.setVisibility(View.INVISIBLE);


        Button btn_next =(Button)findViewById(R.id.btn_go_to_bill);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_order.ReturnSubmitToDB(GetReturnItems.this);
            }
        });


    }

    public void selector(String selection){
        int pos = return_order.findById(selection);

        //Toast.makeText(getApplicationContext(), dmnd_new_order.list.size() + "**", Toast.LENGTH_SHORT).show();

        if (pos != -1) {
            Toast.makeText(getApplicationContext(),"it is in list",Toast.LENGTH_SHORT).show();
            currentItem = return_order.findByIdObj(pos);
            select_exsist=true;
        }
        else{
            Toast.makeText(getApplicationContext(),"it is not in list",Toast.LENGTH_SHORT).show();
            select_exsist=false;
            currentItem = new SellItem(selection);
            currentItem.setItemNameViaDatabase(getApplicationContext());
        }


        FragmentManager fm = getFragmentManager();
        DialogGetReturnQty md = new DialogGetReturnQty();
        Bundle args = new Bundle();


        args.putString("itemid", currentItem.getItemID());
        args.putSerializable("umapname", mpUnitnames);
        md.setArguments(args);
        md.show(fm, "dialog3");


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

    public void DrawTable(ArrayList<SellItem> arls){
        try {
            TableLayout ttt = (TableLayout) findViewById(R.id.selected_table1);
            //for (int i = 1; i < new_order.list.size(); i++) {
            ttt.removeAllViewsInLayout();
            //}
        }
        catch (Exception e){}
        table_hdr();
        for (int i=0;i<arls.size();i++){
            RowCreator(arls.get(i), R.id.selected_table1,i);
            Log.d("PlaceOrderArray", arls.get(i).getItemID() + "**" + arls.get(i).getQty());
        }
    }

    public void RowCreator(SellItem item, int layout,int rw) {

        TableLayout tl = (TableLayout) findViewById(layout);

// Create the table row
        final TableRow tr = new TableRow(this);


        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow idrw=(TableRow) view.findViewById(view.getId());
                TextView idtv=(TextView)idrw.getVirtualChildAt(0);
                String chooseID=idtv.getText().toString();
                Bundle args2 = new Bundle();

                args2.putString("ItemID",chooseID);
                args2.putInt("Qty", return_order.findQtyById(chooseID));
                args2.putDouble("Price",return_order.findByIdObj(return_order.findById(chooseID)).getPrice());

                FragmentManager fm = getFragmentManager();
                DialogEditReturnQty md = new DialogEditReturnQty();
                md.setArguments(args2);
                md.show(fm, "edit2");

            }
        });
        tr.setLongClickable(true);

        tr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                TableRow idrwdel=(TableRow) view.findViewById(view.getId());
                TextView idtvdel=(TextView)idrwdel.getVirtualChildAt(0);

                String chooseIDDel=idtvdel.getText().toString();
                Log.d("ON LONG",chooseIDDel);
                show_warning_delete(chooseIDDel,return_order.findQtyById(chooseIDDel));
                return false;
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
        labelQty.setText(String.valueOf(item.getQty()) + " " + item.getMinOrderUnit());
//        labelQty.setText(String.valueOf(item.getQty())+" "+item.getSelectedUnit());
        labelQty.setTextColor(Color.BLACK);
        //tr.addView(labelQty);
        //labelDiscount.setText(String.valueOf(item.getRelavantDiscount(item.getQty())));
        tr.addView(labelQty);
        TextView labelPrice = new TextView(this);
        labelPrice.setId(600 + rw);
        //double value = (100 - item.getRelavantDiscount(item.getQty())) * item.getPrice() * item.getQty();
        labelPrice.setText(String.valueOf(item.getPrice()*item.getQty()));//(String.valueOf(value / 100));
        labelPrice.setTextColor(Color.BLACK);
        tr.addView(labelPrice);


// finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


    }

    @Override
    public void onGetData(int qty, int demandQty,String demand_DeliverDate,double Price) {
        Toast.makeText(getApplicationContext(),"//////////////"+qty+" "+demandQty+" "+" " ,Toast.LENGTH_SHORT).show();
        //to select item
        if((!select_exsist) &&(qty!=0)){
            currentItem.setQty(qty);
            currentItem.setPrice(Price);
            currentItem.setStoreQty(currentItem.getStoreQty()-qty);
            return_order.addItem(currentItem);
        }
        else{
            int temp=currentItem.getQty();
            currentItem.setPrice(Price);
            currentItem.resetStoreQty();
            currentItem.setQty(temp+qty);
            currentItem.setStoreQty(currentItem.getStoreQty()-currentItem.getQty());
        }
        //to demand item



        for (int i=0;i<return_order.list.size();i++){
            String c=return_order.list.get(i).getItemID()+" "+return_order.list.get(i).getItemName()+" "+return_order.list.get(i).getStoreQty();
            Log.d("new order",c);
            Toast.makeText(getApplicationContext(),c,Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(),"***********",Toast.LENGTH_SHORT).show();

        DrawTable(return_order.list);

    }

    @Override
    public void onEditMessage(String ItemID, int qty,double price) {
        SellItem item=return_order.findByIdObj(return_order.findById(ItemID));
        item.setQty(qty);
        item.setPrice(price);
        DrawTable(return_order.list);

    }

    public String getBestUnit(int qty){
        for (int i=0;i<mpUnitnames.u_map.length;i++){
            if ((qty%mpUnitnames.u_map[i].getQtyMap())==0){
                return mpUnitnames.u_map[i].getUnit();
            }
        }
        return "";
    }

    public void show_warning_delete(final String ItmID,int Qty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Delete this ITEM?")
                .setMessage("Click Delete "+ ItmID+" if you want to delete this item "+Qty+" Qty(s)")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return_order.list.remove(return_order.findById(ItmID));
                        DrawTable(return_order.list);

                    }
                })
                .setNegativeButton("Don't Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })                        //Do nothing on no
                .show();


    }

}