package ucsc.group12.agentmate.bll;

import android.content.Context;

import java.io.Serializable;
import java.util.Calendar;

import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 8/23/2014.
 */
public class Payment implements Serializable {
    String ReceiptID;
    double PayAmount;
    String PayDate;
    String Type;
    boolean Sync;
    String venderNo;

    public String getVenderNo() {
        return venderNo;
    }

    public void setVenderNo(String venderNo) {
        this.venderNo = venderNo;
    }

    public Payment (Representative rep){
        ReceiptID= rep.getEmp_id()+"V"+Calendar.getInstance().getTime();
        PayDate=String.valueOf(Calendar.getInstance().get(Calendar.DATE))+"-"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH))+"-"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public String getReceiptID() {
        return ReceiptID;
    }

    public void setReceiptID(String receiptID) {
        ReceiptID = receiptID;
    }

    public double getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(double payAmount) {
        PayAmount = payAmount;
    }

    public String getPayDate() {
        return PayDate;
    }

    public void setPayDate(String payDate) {
        PayDate = payDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isSync() {
        return Sync;
    }

    public void setSync(boolean sync) {
        Sync = sync;
    }

    public void SubmitToDB(Context con){
        DatabaseControl dbc=new DatabaseControl(con);
        dbc.savePayment(this);
    }
}
