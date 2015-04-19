package ucsc.group12.agentmate.dbc;
/**
 * Created by NRV on 8/23/2014.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import ucsc.group12.agentmate.bll.Bill;
import ucsc.group12.agentmate.bll.Complain;
import ucsc.group12.agentmate.bll.Order;
import ucsc.group12.agentmate.bll.Payment;
import ucsc.group12.agentmate.bll.SellItem;
import ucsc.group12.agentmate.bll.UnitMap;
import ucsc.group12.agentmate.bll.Vendor;


public class DatabaseControl extends SQLiteOpenHelper{
    SQLiteDatabase dbase;
    Context con;
    public DatabaseControl(Context context) {

        super(context, "datacollection.db", null, 1);
        con=context;



    }

    @Override
    public void onCreate(SQLiteDatabase database) {

//Create login table
        String create_login_query = "CREATE TABLE login (EmpId VARCHAR(4),UserName VARCHAR(10) PRIMARY KEY,Password TEXT,Question VARCHAR(50),Answer VARCHAR(20),LastUpdate datetime default current_timestamp)";
        database.execSQL(create_login_query);
        dbase=database;
//Create vendor table
        String create_vendorTable_query = "CREATE TABLE vendor (venderno VARCHAR(6) PRIMARY KEY,ShopName VARCHAR(20) ,VenderName VARCHAR(30) , " +
                "Address VARCHAR(100),TelNoShop VARCHAR(10),TelNoConfirm VARCHAR(10),Overdue FLOAT,Confirm BOOLEAN)";
        database.execSQL(create_vendorTable_query);

//Create item table
        String create_itemTable_query = "CREATE TABLE item (ItemID VARCHAR(5) PRIMARY KEY, ItemName VARCHAR(20) , " +
                "Price FLOAT,StoreQty INTEGER ,CompanyDiscount FLOAT,MinUnit VARCHAR(3),MinOrderUnit VARCHAR(3),CategoryID VARCHAR(2),Sync BOOLEAN)";
        database.execSQL(create_itemTable_query);

//Create venorder table
        String create_venOrderTable_query = "CREATE TABLE venOrder (VenOrderID VARCHAR(16) PRIMARY KEY, VendorNo VARCHAR(6) , " +
                "OrderDate datetime default current_timestamp,DeliverDate datetime,Sync BOOLEAN)";
        database.execSQL(create_venOrderTable_query);

//Create discount table
        String create_discount_Table_query = "CREATE TABLE discount (ItemID VARCHAR(5), MinQty INTEGER, " +
                "MaxQty INTEGER,Discount FLOAT,Sync BOOLEAN,PRIMARY KEY (ItemID, MinQty, MaxQty))";
        database.execSQL(create_discount_Table_query);

//Create measure table
        String create_unit_mapping_Table_query = "CREATE TABLE measure (ItemID VARCHAR(5), unit VARCHAR(3),MapQty INTEGER,PRIMARY KEY (ItemID,unit))";
        database.execSQL(create_unit_mapping_Table_query);
//Create complain table
        String create_complain_Table_query = "CREATE TABLE complain (ComplainID VARCHAR(17) PRIMARY KEY, ItemID VARCHAR(5),Complain TEXT,VendorNo VARCHAR(6),Sync BOOLEAN)";
        database.execSQL(create_complain_Table_query);
//Create bill table
        String create_bill_Table_query = "CREATE TABLE bill (BillID VARCHAR(17) PRIMARY KEY, VenOrderID VARCHAR(16),BillDate date,PayDate date,Total INTEGER,venderno VARCHAR(6),Sync BOOLEAN)";
        database.execSQL(create_bill_Table_query);
//Create Myorder table
        String create_order_Table_query = "CREATE TABLE Myorder (VenOrderID VARCHAR(16),ItemID VARCHAR(5),Qty INTEGER,DiscountAMT FLOAT,Sync BOOLEAN, PRIMARY KEY (VenOrderID, ItemID))";
        database.execSQL(create_order_Table_query);
//Create payment table
        String create_payment_Table_query = "CREATE TABLE payment (ReceiptID VARCHAR(16) PRIMARY KEY,BillID VARCHAR(17),PayDate datetime default current_timestamp,PayAmount FLOAT,type VARCHAR(3),venderno VARCHAR(6),Sync BOOLEAN)";
        database.execSQL(create_payment_Table_query);
//Create return table
        String create_return_Table_query = "CREATE TABLE return (SubItemID VARCHAR(11) PRIMARY KEY,Qty INTEGER,Date datetime default current_timestamp,ActualPrice FLOAT,Sync BOOLEAN)";
        database.execSQL(create_return_Table_query);
//Create discount table
//        Toast.makeText(con,"DONE",Toast.LENGTH_SHORT).show();

//Create demandvenOrder table//ToDo Implement thess tables methods
        String create_demand_venOrderTable_query = "CREATE TABLE demandvenOrder (DemOrderID VARCHAR(16) PRIMARY KEY, VendorNo VARCHAR(6) , " +
                "OrderDate datetime default current_timestamp,Sync BOOLEAN)";
        database.execSQL(create_demand_venOrderTable_query);
//Create demandMyorder table
        String create_demand_order_Table_query = "CREATE TABLE demandMyorder (DemOrderID VARCHAR(16),ItemID VARCHAR(5),Qty INTEGER,DeliverDate datetime,Sync BOOLEAN, PRIMARY KEY (DemOrderID, ItemID))";
        database.execSQL(create_demand_order_Table_query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public void k(){
        SQLiteDatabase database = this.getWritableDatabase();
        String create_return_Table_query = "CREATE TABLE return (SubItemID VARCHAR(11) PRIMARY KEY,Qty INTEGER,Date datetime default current_timestamp,ActualPrice FLOAT,Sync BOOLEAN)";
        database.execSQL(create_return_Table_query);

        //database.delete("complain", null, null);


        //String create_unit_mapping_Table_query = "CREATE TABLE measure (ItemID VARCHAR(5), unit VARCHAR(3),MapQty INTEGER,PRIMARY KEY (ItemID,unit))";
        //database.execSQL(create_unit_mapping_Table_query);
        //Toast.makeText(con, "id ", Toast.LENGTH_SHORT).show();

        //String create_complain_Table_query = "CREATE TABLE complain (ComplainID VARCHAR(12) PRIMARY KEY, ItemID VARCHAR(5),Complain TEXT,VendorNo VARCHAR(6),Sync BOOLEAN)";
        //database.execSQL(create_complain_Table_query);

       // String create_bill_Table_query = "CREATE TABLE bill (BillID VARCHAR(17) PRIMARY KEY, VenOrderID VARCHAR(16),BillDate date,Total INTEGER,Sync BOOLEAN)";
        //database.execSQL(create_bill_Table_query);

        /*ContentValues values = new ContentValues();
        values.put("ItemID","123");
        values.put("unit","pkt");
        values.put("MapQty",15);

        database.insert("measure",null, values);

        ContentValues values2 = new ContentValues();
        values2.put("ItemID","123");
        values2.put("unit","box");
        values2.put("MapQty",180);

        database.insert("measure",null, values2);

        ContentValues values3 = new ContentValues();
        values3.put("ItemID","123");
        values3.put("unit","bulk");
        values3.put("MapQty",50);

        database.insert("measure",null, values3);
        Toast.makeText(con, "****//*///*//**** ", Toast.LENGTH_SHORT).show();*/
        /*SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("StoreQty", 80);
        database.update("item", values,"ItemID"+" = ?",new String[] {"123"});*/
        try {
            //String create_payment_Table_query = "CREATE TABLE payment (ReceiptID VARCHAR(16) PRIMARY KEY,BillID VARCHAR(17),PayDate datetime default current_timestamp,PayAmount FLOAT,type VARCHAR(3),Sync BOOLEAN)";
            //database.execSQL(create_payment_Table_query);

            String create_bill_Table_query = "ALTER TABLE payment ADD COLUMN venderno VARCHAR(6)";
            database.execSQL(create_bill_Table_query);
        }
        catch (Exception e){
            Log.d("Table create error bill",e.toString());
        }

        try {
            String create_order_Table_query = "CREATE TABLE Myorder (VenOrderID VARCHAR(16),ItemID VARCHAR(5),Qty INTEGER,DiscountAMT FLOAT,Sync BOOLEAN,PRIMARY KEY (VenOrderID, ItemID))";
            database.execSQL(create_order_Table_query);
        }
        catch (Exception e){
            Log.d("Table create error ",e.toString());
        }
        try {
            ShowOrderTable();
        }
        catch (Exception e){
            Log.d("Table print error ",e.toString());
        }
        try{
        ShowVenOrderTable();
        }
        catch (Exception e){
            Log.d("Table print error ",e.toString());
        }
        try{
        ShowBillTable();
        }
        catch (Exception e){
            Log.d("Table print error ",e.toString());
        }
    }//This function is used to do the modificiation to the current database structure.

    ///Queries runs on login table

    //In this function given details will insert to table if empId is not exsists.
    //Will update the database if the record exsits.
    public void insertToLogin(String EmpId_ins,String username_ins,String encpassword_ins,String Question_ins,String enc_Ans_ins){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EmpId", EmpId_ins);
        values.put("UserName", username_ins);
        values.put("Password", encpassword_ins);
        values.put("Question", Question_ins);
        values.put("Answer", enc_Ans_ins);


        if(this.getLoginInfo(username_ins).getCount()!=0){
            try {
                database.update("login", values, "EmpId" + " = ?", new String[]{EmpId_ins});
            }catch(Exception e)
                {
                    Log.d("DATABASE ERROR",e.toString());
                }
            //If already exsits member login do update
        }
        else{

            database.insert("login",null, values);
             //Query if member id does not exsists insert.
        }
    }

    public Cursor getLoginInfo(String Username_ins) {//Get the login info with relavant to the given user name.
        //HashMap<String, String> LoginMap = new HashMap<String, String>();

     SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM login  where UserName='"+Username_ins+"'";

        Cursor cursor = database.rawQuery(selectQuery,null);
        return cursor;
    }

    public String password_encoder (String password){////To encode the password.
        int base=10;									///Get the password and encode it.
        //char at position will add as a string if int value exceed margin
        //Then that value save as string and do the remaining part.
        int margin=99999;
        int cur_val;
        int cur_tot=0;
        String final_value="";
        for (int i=0;i<password.length();i++){
            cur_val=password.charAt(i);
            cur_tot=(int) (cur_tot+cur_val*Math.pow(base,i));
            if (cur_tot>=99999){
                final_value=final_value+cur_tot;
                cur_tot=0;
            }
        }
        final_value=final_value+cur_tot;
        return final_value;

    }

    public void change_Password(String UserName,String new_Password){
        // Toast.makeText(con,"Updated succefully "+UserName+" "+new_Password, Toast.LENGTH_SHORT).show();
        SQLiteDatabase database = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        int seconds = cal.get(Calendar.DATE);
        String now_time=""+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DATE)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        ContentValues values = new ContentValues();
        values.put("Password", password_encoder(new_Password));
        //Toast.makeText(con,"---> "+seconds+"  "+new_Password, Toast.LENGTH_SHORT).show();
        values.put("LastUpdate",now_time);//////SET TO GET THE DATE

        ///Set the date of the update when this happens

        int k=database.update("login", values,"UserName"+" = ?",new String[] {UserName});

        Cursor c=getLoginInfo(UserName);
        c.moveToFirst();
        //Toast.makeText(con,k+" Count "+c.getCount()+" "+password_encoder(new_Password), Toast.LENGTH_SHORT).show();
        String pw_in_db2=c.getString(c.getColumnIndex("Password"));
        if (pw_in_db2.equals(password_encoder(new_Password))){
            //Updated correctly
            Toast.makeText(con,"Updated succefully "+UserName, Toast.LENGTH_SHORT).show();
        }
        else{
            //Not updated
            Toast.makeText(con,"Updating process failed", Toast.LENGTH_SHORT).show();

        }
    }


//Queries to the item table
    //Get the item details when itemId given
    public String finditemByID(String ItemID){
        SQLiteDatabase database = this.getReadableDatabase();

        String select_ven_id_Query = "SELECT * FROM item " +
                "WHERE ItemID= '"+ItemID+"'";

        //Toast.makeText(con,select_ven_id_Query,Toast.LENGTH_SHORT).show();
        Cursor cursor = database.rawQuery(select_ven_id_Query,null);
        cursor.moveToFirst();
       return cursor.getString(cursor.getColumnIndex("ItemName"));

    }

    //Get the item details when itemName given
    public String finditemByName(String ItemName){
        SQLiteDatabase database = this.getReadableDatabase();

        String select_ven_id_Query = "SELECT * FROM item " +
                "WHERE ItemName= '"+ItemName+"'";

        //Toast.makeText(con,select_ven_id_Query,Toast.LENGTH_SHORT).show();
        Cursor cursor = database.rawQuery(select_ven_id_Query,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("ItemID"));

    }

    //Update the item details
    public void itemQtyUpdate(SellItem item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StoreQty", item.getStoreQty());
        values.put("Sync","false");
        database.update("item", values,"ItemID"+" = ?",new String[] {item.getItemID()});

    }

    //Add the item to the item table.Execute on the loader activity.
    public void AddItem (SellItem item){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//    Toast.makeText(con,item.getItemID(),Toast.LENGTH_SHORT).show();
        values.put("ItemID",item.getItemID());
        values.put("ItemName",item.getItemName());
        values.put("Price",item.getPrice());
        values.put("StoreQty",item.getStoreQty());
        values.put("CompanyDiscount",item.getCompanyDiscount());
        values.put("MinUnit",item.getMinUnit());
        values.put("MinOrderUnit",item.getMinOrderUnit());
        values.put("CategoryID",item.getCategoryID());
        values.put("Sync",item.isSync());

        database.insert("item",null, values);


    }

//Get the Item when ItemId given
    public Cursor getExactItemByID(String ItemID){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_exact_item_id_Query = "SELECT * FROM item where ItemID='"+ItemID+"'";
        Cursor cursor = database.rawQuery(select_exact_item_id_Query,null);
        return cursor;

    }

//Get the all item when the name is given
    public Cursor getAllItemByName (){//
        SQLiteDatabase database = this.getReadableDatabase();
        String select_item_id_Query = "SELECT ItemID,ItemName FROM item";

        Cursor cursor = database.rawQuery(select_item_id_Query,null);
        return cursor;
    }


    public void confirm_data(Vendor vendor_conf){
        String ven_id=vendor_conf.getVenderNo();
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Confirm", vendor_conf.isConfirm());
        database.update("vendor", values, "venderno" + " = ?", new String[]{ven_id});
    }

//Queries to the vendor table.
//Add the new vendor to the table.//In the Create new customer activity
  public void addVendor(Vendor new_vendor){
      SQLiteDatabase database = this.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put("venderno",new_vendor.getVenderNo());
      values.put("ShopName",new_vendor.getShopName());
      values.put("VenderName",new_vendor.getVenderName());
      values.put("Address",new_vendor.getAddress());
      values.put("Overdue",new_vendor.getOverdue());
      values.put("TelNoShop",new_vendor.getTelNoShop());
      values.put("TelNoConfirm",new_vendor.getTelNoConfm());
      values.put("Confirm",false);
      database.insert("vendor",null, values);
  }

    //Get the vendor from the table
    public Cursor findVendor(String ven_id){
        SQLiteDatabase database = this.getReadableDatabase();

        String select_ven_id_Query = "SELECT * FROM vendor " +
                "WHERE ShopName LIKE '"+ven_id+"%'";

        //Toast.makeText(con,select_ven_id_Query,Toast.LENGTH_SHORT).show();
        Cursor cursor = database.rawQuery(select_ven_id_Query,null);
        return cursor;
    }

//Edit the vendor details in the table//Edit customer activity
    public void editVendor(Vendor editVendor,Context con){
        String edited_venderno=editVendor.getVenderNo();

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ShopName",editVendor.getShopName());
        values.put("VenderName",editVendor.getVenderName());
        values.put("Address",editVendor.getAddress());
        values.put("TelNoShop",editVendor.getTelNoShop());
        values.put("TelNoConfirm",editVendor.getTelNoConfm());
        values.put("Overdue",editVendor.getOverdue());
        values.put("Confirm",false);
        int updted_rows=database.update("vendor", values, "venderno" + " = ?", new String[]{edited_venderno});
        if (updted_rows==1){
            Toast.makeText(con,"Update Successfull",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(con,"Update Error",Toast.LENGTH_SHORT).show();
        }

    }

//Get all vendors in the table.//In all activity
    public Cursor getVendorTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_ven_name_Query = "SELECT * FROM vendor";
        Cursor cursor = database.rawQuery(select_ven_name_Query,null);
        return cursor;

    }


    public void add_complain(String CompID_ins,String ItemID_ins,String Complain_ins,String VendorID_ins,Boolean synced_ins){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ComplainID",CompID_ins);
        values.put("ItemID",ItemID_ins);
        values.put("Complain",Complain_ins);
        values.put("VendorID",VendorID_ins);
        values.put("Synced",synced_ins);
        database.insert("complain",null, values);
    }

    public void add_bill(String BillID_ins,String VenorderID_ins,String BillDate_ins,String pay_Date_ins,double paid_amount_ins,String venno){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("BillID",BillID_ins);
        values.put("VenOrderID",VenorderID_ins);
        values.put("BillDate",BillDate_ins);
        values.put("Total",paid_amount_ins);
        values.put("venderno",venno);
        values.put("PayDate",pay_Date_ins);
        values.put("Sync","false");

        database.insert("bill",null, values);
    }

public void like(){

    SQLiteDatabase database = this.getReadableDatabase();
    String selectQuery = "SELECT * FROM login " +
            "WHERE UserName LIKE 'pq%'";
    Cursor cursor = database.rawQuery(selectQuery,null);
    //Toast.makeText(con, "no of rows", Toast.LENGTH_SHORT).show();
    //Toast.makeText(con, ""+ cursor.getCount(), Toast.LENGTH_SHORT).show();
}

public Cursor findComplain(){
    SQLiteDatabase database = this.getReadableDatabase();

    String select_item_id_Query = "SELECT * FROM complain";
    Cursor cursor = database.rawQuery(select_item_id_Query,null);
    return cursor;
}

public Cursor findComplainByID(String comp_id_ins){
        SQLiteDatabase database = this.getReadableDatabase();

        String select_item_id_Query = "SELECT * FROM complain WHERE ComplainID='"+comp_id_ins+"'";
        Cursor cursor = database.rawQuery(select_item_id_Query,null);
        return cursor;
    }

public void setStateItem(SellItem itemEdit){
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();

    values.put("StoreQty",itemEdit.getStoreQty());
    values.put("Sync",itemEdit.isSync());

    database.update("item", values,"ItemID"+" = ?",new String[] {itemEdit.getItemID()});

}

public Cursor getAllDiscounts(String itemID){
    SQLiteDatabase database = this.getReadableDatabase();
    String select_discount_id_Query = "SELECT * FROM discount where ItemID='"+itemID+"'";

    Cursor cursor = database.rawQuery(select_discount_id_Query,null);
    return cursor;
}

public void AddDiscount (String id,int max,int min,double disc) {
    SQLiteDatabase database = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    //Toast.makeText(con,item.getItemID(),Toast.LENGTH_SHORT).show();
    values.put("ItemID", id);
    values.put("MinQty", min);
    values.put("MaxQty", max);
    values.put("Discount", disc);

    database.insert("discount", null, values);
}

    public void test(){
        String create_discount_Table_query = "CREATE TABLE discount (ItemID VARCHAR(5), MinQty INTEGER, " +
                "MaxQty INTEGER,Discount FLOAT,Sync BOOLEAN,PRIMARY KEY (ItemID, MinQty, MaxQty))";
        dbase.execSQL(create_discount_Table_query);
        Toast.makeText(con,"DONE DONE",Toast.LENGTH_SHORT).show();
    }

    public UnitMap[] findQtyMap(String itemID){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_discount_id_Query = "SELECT * FROM measure where ItemID='"+itemID+"' order by MapQty desc";

        Cursor cursor = database.rawQuery(select_discount_id_Query,null);
        UnitMap[] mapset=new UnitMap[cursor.getCount()];
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                mapset[i]=new UnitMap(cursor.getInt(cursor.getColumnIndex("MapQty")),cursor.getString(cursor.getColumnIndex("unit")));
                i++;
            } while (cursor.moveToNext());
            return mapset;
        }
        return null;
    }

    public void addToComplain(Complain cmp){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ComplainID",cmp.getComplainID());
        values.put("ItemID",cmp.getItemID());
        values.put("Complain",cmp.getComplain());
        values.put("VendorNo",cmp.getVendorNo());
        values.put("Sync",cmp.getSync());
        database.insert("complain",null, values);

    }

    public void getComplainID(){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_id_complain_Query = "SELECT * FROM complain";
        Cursor cursor = database.rawQuery(select_id_complain_Query,null);

        if (cursor.moveToFirst() && cursor.getCount() != 0) {
            do {

                Toast.makeText(con,cursor.getString(cursor.getColumnIndex("ComplainID"))+"-->"+cursor.getString(cursor.getColumnIndex("ItemID"))+"-->"+cursor.getString(cursor.getColumnIndex("Complain"))+"-->"+cursor.getString(cursor.getColumnIndex("VendorNo")),Toast.LENGTH_SHORT).show();


            } while (cursor.moveToNext());

        }

    }

    public Cursor findBillByID(String BillID_ins){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_discount_id_Query = "SELECT * FROM bill where BillID='"+BillID_ins+"' order by name desc";

        Cursor cursor = database.rawQuery(select_discount_id_Query,null);
        return cursor;
    }

    public Cursor findVenOrderNoByvendor(String vendorno){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_discount_id_Query = "SELECT * FROM bill where venderno='"+vendorno+"' order by BillDate desc";

        Cursor cursor = database.rawQuery(select_discount_id_Query,null);
        return cursor;
    }

    public Cursor findOrderByVenorder(String vendorno){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_discount_id_Query = "SELECT * FROM venOrder where VenOrderID='"+vendorno+"'";

        Cursor cursor = database.rawQuery(select_discount_id_Query,null);
        return cursor;
    }

    public void addToVenOrder(Order order){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("VenOrderID",order.getVenOrderID());
        values.put("VendorNo",order.getVender_no());
//        values.put("OrderDate",);//Order date will set by default in SQL database
      //  values.put("DeliverDate",);
        values.put("Sync","false");
        database.insert("venOrder",null, values);
    }

    public void ItemAddToOrderTable(SellItem item,String OrderID){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("VenOrderID",OrderID);
        values.put("ItemID",item.getItemID());
        values.put("Qty",item.getQty());
        values.put("DiscountAMT",item.getRelavantDiscount(item.getQty()));
        values.put("Sync","false");
        database.insert("Myorder",null, values);
    }

    public void ShowOrderTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM Myorder";

        Cursor cursor = database.rawQuery(select_order_Query,null);
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                Log.d("OrderTable DATA",cursor.getString(cursor.getColumnIndex("VenOrderID"))+" - "+cursor.getString(cursor.getColumnIndex("ItemID"))+" - "+cursor.getString(cursor.getColumnIndex("Qty"))+" - "+cursor.getString(cursor.getColumnIndex("DiscountAMT"))+" - "+cursor.getString(cursor.getColumnIndex("Sync")));
                i++;
            } while (cursor.moveToNext());

        }

    }

    public void ShowVenOrderTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM venOrder";

        Cursor cursor = database.rawQuery(select_order_Query,null);
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                Log.d("VenOrderTable DATA",cursor.getString(cursor.getColumnIndex("VenOrderID"))+" - "+cursor.getString(cursor.getColumnIndex("VendorNo"))+" - "+cursor.getString(cursor.getColumnIndex("OrderDate"))+" - "+cursor.getString(cursor.getColumnIndex("DeliverDate"))+" - "+cursor.getString(cursor.getColumnIndex("Sync")));
                i++;
            } while (cursor.moveToNext());

        }

    }

    public void ShowBillTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM bill";

        Cursor cursor = database.rawQuery(select_order_Query,null);
        int i=0;
        if (cursor.moveToFirst()) {
            do {
                Log.d("Bill DATA",cursor.getString(cursor.getColumnIndex("BillID"))+" - "+cursor.getString(cursor.getColumnIndex("VenOrderID"))+" - "+cursor.getString(cursor.getColumnIndex("BillDate"))+" - "+cursor.getString(cursor.getColumnIndex("PayDate"))+" - "+cursor.getString(cursor.getColumnIndex("Total"))+" - "+cursor.getString(cursor.getColumnIndex("Sync"))+" - "+cursor.getString(cursor.getColumnIndex("venderno")));
                i++;
            } while (cursor.moveToNext());

        }

    }

    public double getAreasForVendor(String vendorno){
        double total=0;
        double paid=0;
        SQLiteDatabase database = this.getReadableDatabase();
        String select_vender_no_Query = "SELECT * FROM bill where venderno='"+vendorno+"'";
        Cursor cursor = database.rawQuery(select_vender_no_Query,null);
        if(cursor.moveToFirst()){
            do{
                total=total+cursor.getDouble(cursor.getColumnIndex("Total"));
            }while (cursor.moveToNext());
        }
        String select_payment_Query = "SELECT * FROM payment where venderno='"+vendorno+"'";
        Cursor cursor2 = database.rawQuery(select_payment_Query,null);
        Log.d("Cursor values",String.valueOf(cursor2.getCount()));
        if(cursor2.moveToFirst()){
            do{
                paid=paid+Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("PayAmount")));
                Log.d("PAYMENT TABLE DATA",cursor2.getString(cursor2.getColumnIndex("PayAmount"))+cursor2.getString(cursor2.getColumnIndex("ReceiptID"))+cursor2.getString(cursor2.getColumnIndex("PayDate"))+cursor2.getString(cursor2.getColumnIndex("type"))+cursor2.getString(cursor2.getColumnIndex("venderno")));
            }while (cursor2.moveToNext());

        }
        return total-paid;
    }

    public void savePayment(Payment pay){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ReceiptID",pay.getReceiptID());
        values.put("PayAmount",pay.getPayAmount());
        values.put("type",pay.getType());
        values.put("venderno",pay.getVenderNo());
        values.put("PayDate",pay.getPayDate());
        values.put("Sync","false");

        database.insert("payment",null, values);
    }

    public Bill[] getBillArray(String vendorno){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM bill where venderno='"+vendorno+"'";

        Cursor cursor = database.rawQuery(select_order_Query,null);
        Bill[] bl=new Bill[cursor.getCount()];

        int i=0;
        if (cursor.moveToFirst()) {
            do {
                Log.d("Bill DATA",cursor.getString(cursor.getColumnIndex("BillID"))+" - "+cursor.getString(cursor.getColumnIndex("VenOrderID"))+" - "+cursor.getString(cursor.getColumnIndex("BillDate"))+" - "+cursor.getString(cursor.getColumnIndex("PayDate"))+" - "+cursor.getString(cursor.getColumnIndex("Total"))+" - "+cursor.getString(cursor.getColumnIndex("Sync"))+" - "+cursor.getString(cursor.getColumnIndex("venderno")));
                Bill temp=new Bill();
                temp.setBillDate(cursor.getString(cursor.getColumnIndex("BillDate")));
                temp.setTotal(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Total"))));
                temp.setBillID(cursor.getString(cursor.getColumnIndex("BillID")));
                bl[i]=temp;
                i++;
            } while (cursor.moveToNext());

        }
       return bl;
    }

    public String getVenOrderIDByBillID(String BillID){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM bill where BillID='"+BillID+"'";
        Cursor cursor = database.rawQuery(select_order_Query,null);
        if (cursor.moveToFirst()) {

            Toast.makeText(con,"db--> "+cursor.getString(cursor.getColumnIndex("VenOrderID")),Toast.LENGTH_LONG).show();
            return cursor.getString(cursor.getColumnIndex("VenOrderID"));
        }
        return null;
    }

    public Cursor getOrderByVenOrderID(String venOrderID){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM Myorder where VenOrderID='"+venOrderID+"'";
        Cursor cursor = database.rawQuery(select_order_Query,null);
     return cursor;
    }


    public void LoadToVenOrder(String VenOrderID,String VendorNo,String OrderDate,String DeliverDate,boolean Sync){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("VenOrderID",VenOrderID);
        values.put("VendorNo",VendorNo);
        values.put("OrderDate",OrderDate);
        values.put("DeliverDate",DeliverDate);
        values.put("Sync",Sync);
        database.insert("venOrder",null, values);
    }

    public void LoadToDiscount(String ItemID,int MinQty,int MaxQty,double Discount,boolean Sync){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ItemID",ItemID);
        values.put("MinQty",MinQty);
        values.put("MaxQty",MaxQty);
        values.put("Discount",Discount);
        values.put("Sync",Sync);
        database.insert("discount",null, values);
    }

    public void LoadToMeasure(String ItemID,String unit,int MapQty){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ItemID",ItemID);
        values.put("unit",unit);
        values.put("MapQty",MapQty);

        database.insert("measure",null, values);
    }

    public void LoadToBill(String BillID,String VenOrderID,String BillDate,String PayDate,int Total,String venderno,boolean Sync){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("BillID",BillID);
        values.put("VenOrderID",VenOrderID);
        values.put("BillDate",BillDate);
        values.put("PayDate",PayDate);
        values.put("Total",Total);
        values.put("venderno",venderno);
        values.put("Sync",Sync);


        database.insert("bill",null, values);
    }

    public void LoadToMyorder(String VenOrderID,String ItemID,int Qty,double DiscountAMT,boolean Sync){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("VenOrderID",VenOrderID);
        values.put("ItemID",ItemID);
        values.put("Qty",Qty);
        values.put("DiscountAMT",DiscountAMT);
        values.put("Sync",Sync);

        database.insert("Myorder",null, values);
    }

    public void LoadTopayment(String ReceiptID,String BillID,String PayDate,double PayAmount,String type,String venderno,boolean Sync){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ReceiptID",ReceiptID);
        values.put("BillID",BillID);
        values.put("PayDate",PayDate);
        values.put("PayAmount",PayAmount);
        values.put("type",type);
        values.put("venderno",venderno);
        values.put("Sync",Sync);

        database.insert("payment",null, values);
    }

    public void DeleteTableData(String table_name){
    SQLiteDatabase database = this.getWritableDatabase();
    database.delete(table_name, null, null);
}

    public String getVendorConfNumberByID(String VenID){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM vendor where venderno='"+VenID+"'";
        Cursor cursor = database.rawQuery(select_order_Query,null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("TelNoConfirm"));
        }
        return null;
    }

    public Cursor OutTableData(String table_name){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_id_complain_Query = "SELECT * FROM "+table_name;
        Cursor cursor = database.rawQuery(select_id_complain_Query,null);
        return cursor;
    }

    public Bill findBillonVenOrderID(String VenOrderID){
        SQLiteDatabase database = this.getReadableDatabase();
        //"CREATE TABLE bill (BillID VARCHAR(17) PRIMARY KEY, VenOrderID VARCHAR(16),BillDate date,PayDate date,Total INTEGER,venderno VARCHAR(6),Sync BOOLEAN)";
        String select_order_Query = "SELECT * FROM bill where VenOrderID='"+VenOrderID+"'";
        Cursor cursor = database.rawQuery(select_order_Query,null);
        if (cursor.moveToFirst()) {
            Bill temp=new Bill();
            temp.setTotal(Double.parseDouble(cursor.getString(cursor.getColumnIndex("Total"))));
            temp.setBillID(cursor.getString(cursor.getColumnIndex("BillID")));
            temp.setBillDate(cursor.getString(cursor.getColumnIndex("BillDate")));
            temp.setVenderID(cursor.getString(cursor.getColumnIndex("venderno")));
            return temp;
        }
        return null;
    }

    public Cursor getOrderfromMyorderOnVenOrderID(String VenorderID){
        SQLiteDatabase database = this.getReadableDatabase();
        String select_order_Query = "SELECT * FROM Myorder where VenOrderID='"+VenorderID+"'";
        Cursor cursor = database.rawQuery(select_order_Query,null);
        return cursor;
    }

public void addItemToReturnTable(SellItem item,String vendorno){
    SQLiteDatabase database = this.getReadableDatabase();
    String get_item_Query = "SELECT * FROM item where ItemID='"+item.getItemID()+"'";
    Cursor cursor = database.rawQuery(get_item_Query,null);
    if (cursor.moveToFirst()){
            double curprice=Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
            if (curprice==item.getPrice()){
                //change the qty of item table
                ContentValues values = new ContentValues();
                int curqty=Integer.parseInt(cursor.getString(cursor.getColumnIndex("StoreQty")));//Current items price
                values.put("StoreQty",item.getQty()+curqty);
                database.update("item", values,"ItemID"+" = ?",new String[] {item.getItemID()});

            }
            addReturnTable(item,database,vendorno);//Anything will upload to the vendor table due to calculate the price of the return order.
    }
        }

    public void addReturnTable(SellItem item,SQLiteDatabase database,String vendorno){
        item.setItemID(item.getItemID()+"#"+vendorno);
        ContentValues values = new ContentValues();
        values.put("SubItemID",item.getItemID());
        values.put("Qty",item.getQty());
        values.put("ActualPrice",item.getPrice());
        database.insert("return",null, values);
    }

    public int getItemQtyByItemID(String ItemID){
     Cursor c=getExactItemByID(ItemID);
        c.moveToFirst();
        return  c.getInt(c.getColumnIndex("StoreQty"));

    }

    public void addToDemandVenOrder(Order order){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DemOrderID",order.getVenOrderID());
        values.put("VendorNo",order.getVender_no());
//        values.put("OrderDate",);//Order date will set by default in SQL database
        //  values.put("DeliverDate",);
        values.put("Sync","false");
        database.insert("demandvenOrder",null, values);
    }

    public void ItemAddToDemandOrderTable(SellItem item,String OrderID){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DemOrderID",OrderID);
        values.put("ItemID",item.getItemID());
        values.put("Qty",item.getQty());

        values.put("Sync","false");
        database.insert("demandMyorder",null, values);
    }


    }
