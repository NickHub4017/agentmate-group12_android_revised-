package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

/**
 * Created by NRV on 9/27/2014.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.Serializable;
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
import ucsc.group12.agentmate.ui.DialogGetQty.GetQtyCommunicator;
import ucsc.group12.agentmate.ui.DialogEditQty.EditComm;
import ucsc.group12.agentmate.ui.DialogEditDemand.EditCommDemand;

/**
 * Created by NRV on 9/27/2014.
 */
public class PlaceOrderSecond2 extends Activity implements GetQtyCommunicator,EditComm,EditCommDemand{
    Order new_order=new Order(); //Create new Order
    Order dmnd_new_order=new Order();

    DatabaseControl dbc = new DatabaseControl(this);
    UnitMap[] map;
    mapper mpUnitnames=null;
    AutoCompleteTextView itemID_edit_auto;
    AutoCompleteTextView itemName_edit_auto;
    SellItem currentItem,currentdemanditem;
    boolean select_exsist,demand_exsist;
    List<String> demandeditemsList=new ArrayList<String>();
    Spinner spin_itemID;
    String editabledemandedItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order_item_add);

        itemID_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_comp_item_id);
        itemName_edit_auto = (AutoCompleteTextView) findViewById(R.id.auto_comp_item_name);

        final Representative logged_rep = (Representative) getIntent().getExtras().getSerializable("logged_user");
        final Vendor sel_vendor = (Vendor) getIntent().getExtras().getSerializable("vendor");
        new_order.setVenOrderID(logged_rep.Emp_id+"Or"+Calendar.getInstance().getTime().toString());
        new_order.setOrderID(new_order.getVenOrderID());
        dmnd_new_order.setVenOrderID(logged_rep.Emp_id+"DemOr"+Calendar.getInstance().getTime().toString());
        new_order.setVender_no(sel_vendor.getVenderNo());
        dmnd_new_order.setVender_no(sel_vendor.getVenderNo());
        spin_itemID= (Spinner) findViewById(R.id.spinner_demanditem_id);
        TextView logged_vendor_tv = (TextView) findViewById(R.id.txt_vname_order_b);
        logged_vendor_tv.setText("Selected Vendor is :- " + sel_vendor.getShopName());

        table_hdr();///Draw headers of the tables
        demand_table_hdr();

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
                mpUnitnames=new mapper(getApplicationContext(),ItemID);//GET the unit maps
                itemID_edit_auto.setText(ItemID);
                selector(ItemID);
            }
        });


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str_arry_item_id);
        itemID_edit_auto.setAdapter(adapter);
        itemID_edit_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);//
                mpUnitnames=new mapper(getApplicationContext(),selection);//GET the unit maps

                selector(selection);
            }

        });

        Button btset=(Button)findViewById(R.id.button_testing);
        btset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    dbc.k();
            }
        });

        Button btn_submit=(Button)findViewById(R.id.btn_submit_edit_demand);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dmnd_new_order.list.size()>0) {
                    editabledemandedItemID = spin_itemID.getSelectedItem().toString();
                    EditText editedTv = (EditText) findViewById(R.id.edit_demand_qty);
                    String new_dmnd_qty = editedTv.getText().toString();
                    dmnd_new_order.findByIdObj(dmnd_new_order.findById(editabledemandedItemID)).setQty(Integer.parseInt(new_dmnd_qty));
                    DemandDrawTable(dmnd_new_order.list);
                }
            }
        });

        Button btn_next =(Button)findViewById(R.id.btn_go_to_bill);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbc.addToDemandVenOrder(dmnd_new_order);
                Intent next=new Intent(PlaceOrderSecond2.this,PlaceOrderBill.class);
                next.putExtra("vendor",sel_vendor);
                next.putExtra("logged_user",logged_rep);
                next.putExtra("select_order", new_order);
                dmnd_new_order.DemndOrderDB(getApplicationContext());
                startActivity(next);
            }
        });
    }

    public void selector(String selection){
        int pos = new_order.findById(selection);
        int pos_dmnd=dmnd_new_order.findById(selection);
        //Toast.makeText(getApplicationContext(),dmnd_new_order.list.size()+"**",Toast.LENGTH_SHORT).show();


        if (pos != -1) {
            //Toast.makeText(getApplicationContext(),"it is in list",Toast.LENGTH_SHORT).show();
            currentItem = new_order.findByIdObj(pos);
            select_exsist=true;
        }
        else{
            //Toast.makeText(getApplicationContext(),"it is not in list",Toast.LENGTH_SHORT).show();
            select_exsist=false;
            currentItem = new SellItem(selection, PlaceOrderSecond2.this);
        }
        itemName_edit_auto.setText(currentItem.getItemName());
        ///to demand item
        if (pos_dmnd != -1) {
            //Toast.makeText(getApplicationContext(),"it is in demand",Toast.LENGTH_SHORT).show();
            currentdemanditem = dmnd_new_order.findByIdObj(pos_dmnd);
            demand_exsist=true;
        }
        else{
            //Toast.makeText(getApplicationContext(),"it is not in demand",Toast.LENGTH_SHORT).show();
            demand_exsist=false;
            currentdemanditem = new SellItem(selection, PlaceOrderSecond2.this);
        }

        FragmentManager fm = getFragmentManager();
        DialogGetQty md = new DialogGetQty();
        Bundle args = new Bundle();

        args.putInt("qty", currentItem.getStoreQty());
        args.putString("itemid", currentItem.getItemID());
        args.putSerializable("umapname", mpUnitnames);
        md.setArguments(args);
        md.show(fm, "dialog2");

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
            Log.d("PlaceOrderArray",arls.get(i).getItemID()+"**"+arls.get(i).getQty());
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
                args2.putInt("Qty",new_order.findQtyById(chooseID));
                args2.putInt("RemQty",new_order.findByIdObj(new_order.findById(chooseID)).getStoreQty());

                FragmentManager fm = getFragmentManager();
                DialogEditQty md = new DialogEditQty();
                md.setArguments(args2);
                md.show(fm, "edit");

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
                show_warning_delete(chooseIDDel,new_order.findQtyById(chooseIDDel));
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
//        labelQty.setText(String.valueOf(item.getQty())+" "+item.getSelectedUnit());
        labelQty.setTextColor(Color.BLACK);
        //tr.addView(labelQty);

        TextView labelDiscount = new TextView(this);
        labelDiscount.setId(500 + rw);
        //labelDiscount.setText(String.valueOf(item.getRelavantDiscount(item.getQty())));
        labelDiscount.setTextColor(Color.BLACK);


            labelQty.setText(String.valueOf(item.getQty()) + " " + item.getMinOrderUnit());
            tr.addView(labelQty);
            labelDiscount.setText(String.valueOf(item.getRelavantDiscount(item.getQty())));
            tr.addView(labelDiscount);
            TextView labelPrice = new TextView(this);
            labelPrice.setId(600 + rw);
            double value = (100 - item.getRelavantDiscount(item.getQty())) * item.getPrice() * item.getQty();
            labelPrice.setText(String.valueOf(value / 100));
            labelPrice.setTextColor(Color.BLACK);
            tr.addView(labelPrice);


// finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


    }

    public void DemandDrawTable(ArrayList<SellItem> arls){
        try {
            TableLayout ttt = (TableLayout) findViewById(R.id.demanded_table);
            //for (int i = 1; i < new_order.list.size(); i++) {
            ttt.removeAllViewsInLayout();
            //}
        }
        catch (Exception e){}
        demand_table_hdr();
        for (int i=0;i<arls.size();i++){
            DemandRowCreator(arls.get(i), R.id.demanded_table,i);
            Log.d("PlaceOrderArray",arls.get(i).getItemID()+"**"+arls.get(i).getQty());
        }
    }

    public void DemandRowCreator(SellItem item, int layout,int rw) {

        TableLayout tl = (TableLayout) findViewById(layout);

// Create the table row
        final TableRow tr = new TableRow(this);




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
        labelQty.setText(String.valueOf(item.getQty())+" "+item.getMinOrderUnit());
        labelQty.setTextColor(Color.BLACK);
        tr.addView(labelQty);


        TextView labelDate = new TextView(this);
        labelDate.setId(600 + rw);
        labelDate.setText(item.getDeliverDate());
        labelDate.setTextColor(Color.BLACK);
        tr.addView(labelDate);
        tr.setLongClickable(true);
        tr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TableRow idrwdeldemand=(TableRow) view.findViewById(view.getId());
                TextView idtvdeldemand=(TextView)idrwdeldemand.getVirtualChildAt(0);

                String chooseIDDemandDel=idtvdeldemand.getText().toString();
                show_warning_delete_demand(chooseIDDemandDel,dmnd_new_order.findQtyById(chooseIDDemandDel));

                return false;
            }
        });


// finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


    }

    @Override
    public void onGetData(int qty, int demandQty,String demand_DeliverDate) {
       // Toast.makeText(getApplicationContext(),"//////////////"+qty+" "+demandQty+" "+" " ,Toast.LENGTH_SHORT).show();
        //to select item
        if((!select_exsist) &&(qty!=0)){
            currentItem.setQty(qty);
            currentItem.setStoreQty(currentItem.getStoreQty()-qty);
            new_order.addItem(currentItem);
        }
        else{
            int temp=currentItem.getQty();
            currentItem.resetStoreQty();
            currentItem.setQty(temp+qty);
            currentItem.setStoreQty(currentItem.getStoreQty()-currentItem.getQty());
        }
        //to demand item

        if((!demand_exsist)&&(demandQty!=0)){
            currentdemanditem.setQty(demandQty);
            currentdemanditem.setDeliverDate(demand_DeliverDate);
            dmnd_new_order.addItem(currentdemanditem);
            demandeditemsList.add(currentItem.getItemID());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, demandeditemsList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_itemID.setAdapter(dataAdapter);



        }
        else{
            currentdemanditem.setQty(demandQty);
        }


        for (int i=0;i<new_order.list.size();i++){
            String c=new_order.list.get(i).getItemID()+" "+new_order.list.get(i).getItemName()+" "+new_order.list.get(i).getStoreQty();
            Log.d("new order",c);
            //Toast.makeText(getApplicationContext(),c,Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(getApplicationContext(),"***********",Toast.LENGTH_SHORT).show();
        for (int i=0;i<dmnd_new_order.list.size();i++){
            String c=dmnd_new_order.list.get(i).getItemID()+" "+dmnd_new_order.list.get(i).getItemName()+" "+dmnd_new_order.list.get(i).getStoreQty();
            Log.d("dmnd_new_order",c);
            //Toast.makeText(getApplicationContext(),c,Toast.LENGTH_SHORT).show();
        }

        DrawTable(new_order.list);
        DemandDrawTable(dmnd_new_order.list);
    }

    @Override
    public void onEditMessage(String ItemID, int qty,int tot) {
        SellItem item=new_order.findByIdObj(new_order.findById(ItemID));
        item.resetStoreQty();
        item.setStoreQty(tot-qty);
        item.setQty(qty);
        DrawTable(new_order.list);

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
                        new_order.list.remove(new_order.findById(ItmID));
                        DrawTable(new_order.list);

                    }
                })
                .setNegativeButton("Don't Delete", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })                        //Do nothing on no
                .show();


    }

    public void show_warning_delete_demand(final String ItmID,int Qty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Delete this Demnded ITEM?")
                .setMessage("Click Delete "+ ItmID+" if you want to delete this demanded item "+Qty+" Qty(s)")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dmnd_new_order.list.remove(dmnd_new_order.findById(ItmID));
                        DemandDrawTable(dmnd_new_order.list);

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