package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.SellItem;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 9/23/2014.
 */
public class ReamainVan extends Activity {
    DatabaseControl dbc=new DatabaseControl(ReamainVan.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_remain_van);

        table_hdr();//draw the header names in column.

        Cursor itm_cur = dbc.getAllItemByName();
                                                    //Add the venders to the selection list to select
        final String[] str_arry_item_id = new String[itm_cur.getCount()];
        int j = 0;
        if (itm_cur.moveToFirst() && itm_cur.getCount() != 0) {
            do {
                str_arry_item_id[j] = itm_cur.getString(itm_cur.getColumnIndex("ItemID"));
                j++;
            } while (itm_cur.moveToNext());
        }
        for (int i=0;i<str_arry_item_id.length;i++){
            SellItem item= new SellItem(str_arry_item_id[i],getApplicationContext());
            RowCreator(item,R.id.view_remain_table,i);
        }

    }

    public void table_hdr() {
        TableLayout tl = (TableLayout) findViewById(R.id.view_remain_table);// MAke layout
        final TableRow tr_head = new TableRow(this);
        tr_head.setId(10);//Add elements
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
        label_Price.setId(23);// define id that must be unique
        label_Price.setText("Price"); // set the text for the header
        label_Price.setTextColor(Color.WHITE); // set the color
        label_Price.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_Price); // add the column to the table row here

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

    }

    public void RowCreator(SellItem item, int layout,int rw) {

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
        labelQty.setText(String.valueOf(item.getStoreQty()));
        labelQty.setTextColor(Color.BLACK);
        tr.addView(labelQty);

        TextView labelPrice = new TextView(this);
        labelPrice.setId(500 + rw);
        labelPrice.setText(String.valueOf(item.getPrice()));
        labelPrice.setTextColor(Color.BLACK);
        tr.addView(labelPrice);


// finally add this to the table row
        tl.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));


    }
}
