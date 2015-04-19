package ucsc.group12.agentmate.bll;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;

import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 9/9/2014.
 */
public class SellItem implements Serializable {
    //DatabaseControl dbc;

    String ItemID;
    String ItemName;
    double Price;
    int StoreQty;
    double CompanyDiscount;
    String MinUnit;
    String MinOrderUnit;
    String CategoryID;
    boolean Sync;
    Discount discount[];
    int QtyInMinUnit;
    String selectedUnit;
    String deliverDate;
    double currentPrice;

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(String selectedUnit) {
        this.selectedUnit = selectedUnit;
    }



    public SellItem(String itemID, String itemName, double price, int storeQty, double companyDiscount, String minUnit, String minOrderUnit, String categoryID, boolean sync) {
        ItemID = itemID;
        ItemName = itemName;
        Price = price;
        StoreQty = storeQty;
        CompanyDiscount = companyDiscount;
        MinUnit = minUnit;
        MinOrderUnit = minOrderUnit;
        CategoryID = categoryID;

        Sync = sync;

    }

    public Discount[] getDiscount() {
        return discount;
    }
public SellItem(String itemID){
    ItemID = itemID;
}
    public  SellItem(String itemID,Context con){
        DatabaseControl dbc=new DatabaseControl(con);
        Cursor cur=dbc.getExactItemByID(itemID);
        cur.moveToFirst();
        ItemID = itemID;
        ItemName = cur.getString(cur.getColumnIndex("ItemName"));
        Price = Double.parseDouble(cur.getString(cur.getColumnIndex("Price")));
        StoreQty = Integer.parseInt(cur.getString(cur.getColumnIndex("StoreQty")));
        CompanyDiscount = Double.parseDouble(cur.getString(cur.getColumnIndex("CompanyDiscount")));
        MinUnit =cur.getString(cur.getColumnIndex("MinUnit"));
        MinOrderUnit = cur.getString(cur.getColumnIndex("MinOrderUnit"));
        CategoryID = cur.getString(cur.getColumnIndex("CategoryID"));
        discount=this.getAlldiscounts(con);


    }
    public void setItemNameViaDatabase(Context con){
        DatabaseControl dbc=new DatabaseControl(con);
        Cursor cur=dbc.getExactItemByID(this.ItemID);
        cur.moveToFirst();
        ItemName = cur.getString(cur.getColumnIndex("ItemName"));
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getStoreQty() {
        return StoreQty;
    }

    public void setStoreQty(int storeQty) {
        StoreQty = storeQty;
    }

    public double getCompanyDiscount() {
        return CompanyDiscount;
    }

    public void setCompanyDiscount(double companyDiscount) {
        CompanyDiscount = companyDiscount;
    }

    public String getMinUnit() {
        return MinUnit;
    }

    public void setMinUnit(String minUnit) {
        MinUnit = minUnit;
    }

    public String getMinOrderUnit() {
        return MinOrderUnit;
    }

    public void setMinOrderUnit(String minOrderUnit) {
        MinOrderUnit = minOrderUnit;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public boolean isSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
    }

    public void setQty(int qty){
        QtyInMinUnit=qty;
    }

    public int getQty(){
        return QtyInMinUnit;
    }


    public Discount[] getAlldiscounts(Context con) {
        DatabaseControl dbc=new DatabaseControl(con);
        Cursor cur = dbc.getAllDiscounts(this.ItemID);
        Discount[] allDiscount=new Discount[cur.getCount()];
        int i=0;
        if (cur.moveToFirst()) {
            do {
                Discount temp_disc=new Discount();
                temp_disc.setMax_amount(Integer.parseInt(cur.getString(cur.getColumnIndex("MaxQty"))));
                temp_disc.setMin_amount(Integer.parseInt(cur.getString(cur.getColumnIndex("MinQty"))));
                temp_disc.setDiscount(Float.parseFloat(cur.getString(cur.getColumnIndex("Discount"))));
                allDiscount[i]=temp_disc;
                i++;
            } while (cur.moveToNext());
        }
        return allDiscount;
    }

    public double getRelavantDiscount(int QtyInMinUnit){
        int i=0,j=-1;
        if (QtyInMinUnit==0){
            return 0;
        }
        for (i=0;i<discount.length;i++){
            if ((discount[i].getMax_amount()>=QtyInMinUnit) &&(discount[i].getMin_amount()<=QtyInMinUnit)){
                return discount[i].getDiscount();
            }
            if (discount[i].getMax_amount()==-1){
                j=i;
            }
        }

        if (j!= -1) {
            return discount[j].getDiscount();
        }
        else{
            return 0;
        }
    }
    public void resetStoreQty(){
        this.setStoreQty(getStoreQty()+getQty());
    }

    public void reserveQty(int Qtymin){
        this.StoreQty=this.StoreQty-Qtymin;
        this.setQty(Qtymin);
    }

    public void ItemSubmitToDB(Context con){
        DatabaseControl dbc=new DatabaseControl(con);
        dbc.itemQtyUpdate(this);
    }

    public void ItemSubmitToOrderDB(Context con,String orderID){
        DatabaseControl dbc=new DatabaseControl(con);
        dbc.ItemAddToOrderTable(this,orderID);
    }

    public void ItemSubmitToDemandDB(Context con,String orderID){
        DatabaseControl dbc=new DatabaseControl(con);
        dbc.ItemAddToDemandOrderTable(this,orderID);

    }

}
