package ucsc.group12.agentmate.bll;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 9/20/2014.
 */
public class Order implements Serializable {
    public String getVender_no() {
        return vender_no;
    }

    public void setVender_no(String vender_no) {
        this.vender_no = vender_no;
    }

    public String getOrder_Date() {
        return Order_Date;
    }

    public void setOrder_Date(String order_Date) {
        Order_Date = order_Date;
    }

    public String getDeliver_Date() {
        return Deliver_Date;
    }

    public void setDeliver_Date(String deliver_Date) {
        Deliver_Date = deliver_Date;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    String vender_no;
    public String Order_Date;
    String Deliver_Date;
    String VenOrderID;
    String OrderID;
    public ArrayList<SellItem> list;

    boolean sync;

    public Order() {
        list = new  ArrayList<SellItem>();
        Calendar c = Calendar.getInstance();
        Order_Date =c.getTime().toString();
        //VenOrderID=
//        Calendar cal = Calendar.getInstance();
        //cmpID = logged_rep.getEmp_id()+String.valueOf(cal.get(Calendar.DATE))+String.valueOf(cal.get(Calendar.MONTH))+String.valueOf(cal.get(Calendar.YEAR))+String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+String.valueOf(cal.get(Calendar.MINUTE))+String.valueOf(cal.get(Calendar.SECOND));

    }

    public String getVenOrderID() {
        return VenOrderID;
    }

    public void setVenOrderID(String venOrderID) {
        VenOrderID = venOrderID;
    }

    public void addItem(SellItem item) {
        list.add(item);
    }

    public int findById(String itemID) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemID().equals(itemID)) {
                return i;
            }

        }
        return -1;

    }

    public int findQtyById(String itemID) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemID().equals(itemID)) {
                return list.get(i).getQty();
            }

        }
        return -1;

    }

    public SellItem findByIdObj(int i) {
        return list.get(i);
    }


    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
//ToDo write self database update code to all items from this class.
//
    }

    public void IdGenerator_VenOrder(Representative logged_rep){
        Calendar cal = Calendar.getInstance();
        String OrderID = "Or"+logged_rep.getEmp_id()+"Y"+String.valueOf(cal.get(Calendar.DATE))+String.valueOf(cal.get(Calendar.MONTH))+String.valueOf(cal.get(Calendar.YEAR))+String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+String.valueOf(cal.get(Calendar.MINUTE))+String.valueOf(cal.get(Calendar.SECOND));
    }

    public void VenOrderSubmitToDatabase(Context con){
        DatabaseControl dbc=new DatabaseControl(con);
        dbc.addToVenOrder(this);


    }
    public void OrderSubmitToDatabase(Context con){
        for (int i=0;i<this.list.size();i++){
            list.get(i).ItemSubmitToDB(con);//update the reamining qty
            list.get(i).ItemSubmitToOrderDB(con,this.VenOrderID);//Order items will record in order table.
        }

    }

    public void DemndOrderDB(Context con){
        for (int i=0;i<this.list.size();i++){
            list.get(i).ItemSubmitToDemandDB(con,this.getVenOrderID());
            //list.get(i).ItemSubmitToOrderDB(con,this.VenOrderID);//Order items will record in order table.
        }
    }


    public void ReturnSubmitToDB(Context con)

    {
        DatabaseControl dbc=new DatabaseControl(con);
        for (int i = 0; i < this.list.size(); i++) {
            dbc.addItemToReturnTable(this.list.get(i),vender_no);
        }
        if (this.list.size()>0){
            Toast.makeText(con,"Return Order Has been submitted successfully",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(con,"Return Order Submission error",Toast.LENGTH_LONG).show();
        }
    }



}
