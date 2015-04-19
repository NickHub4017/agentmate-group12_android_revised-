package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Bill;
import ucsc.group12.agentmate.bll.Order;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.bll.SellItem;
import ucsc.group12.agentmate.bll.Vendor;


/**
 * Created by NRV on 10/5/2014.
 */
public class PlaceOrderBill extends Activity {
    String delDate="";
    Button btn_mk_payment;
    Button btn_crt_bill;
    Representative logged_rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_bill);
        final Vendor sel_vendor = (Vendor) getIntent().getExtras().getSerializable("vendor");
        final Order new_order_done = (Order) getIntent().getExtras().getSerializable("select_order");
        btn_crt_bill=(Button)findViewById(R.id.btn_create_bill);
        logged_rep = (Representative) getIntent().getExtras().getSerializable("logged_user");
        final Bill curBill=new Bill(sel_vendor,logged_rep,new_order_done.getOrderID());
        Toast.makeText(getApplicationContext(),"OrderId detect in Place bill"+new_order_done.getOrderID()+"--> Ven"+new_order_done.getVenOrderID(),Toast.LENGTH_LONG).show();
        btn_mk_payment=(Button)findViewById(R.id.btn_make_payment_cr_bill);
        btn_mk_payment.setEnabled(false);
        double BillValue=0,Discount=0;
        SellItem temp_item;
       for (int i=0;i<new_order_done.list.size();i++){
           temp_item=new_order_done.list.get(i);
           double discount=temp_item.getRelavantDiscount(temp_item.getQty())*temp_item.getPrice() * temp_item.getQty()/100;
           double value = (100 - temp_item.getRelavantDiscount(temp_item.getQty())) * temp_item.getPrice() * temp_item.getQty()/100;
           BillValue=BillValue+value;
           Discount=Discount+discount;
       }
        curBill.setTotal(BillValue);
        curBill.setDiscount(Discount);
        TextView txt_billNo_windw=(TextView)findViewById(R.id.txt_billNo_bill);
        txt_billNo_windw.setText("Bill No:- "+curBill.getBillID());
        TextView txt_billVal_windw=(TextView)findViewById(R.id.txt_billVal_bill);
        txt_billVal_windw.setText("Total Bill Value:- RS "+curBill.getTotal());
        if (curBill.getTotal()==0){
            btn_crt_bill.setEnabled(false);
        }
        else{
            btn_crt_bill.setEnabled(true);
        }
        TextView txt_billtotal_high_windw=(TextView)findViewById(R.id.txt_billtotal_high_bill);
        txt_billtotal_high_windw.setText("Total To Paid :- Rs "+curBill.getTotal());
        TextView txt_dscnt_windw=(TextView)findViewById(R.id.txt_dscnt_bill);
        txt_dscnt_windw.setText("Total Discount:- Rs "+curBill.getDiscount());
        DatePicker del_date=(DatePicker)findViewById(R.id.dlvr_date_picker);
        del_date.init(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE),new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                TextView datetv=(TextView)findViewById(R.id.txt_date);
                delDate=datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth();
                datetv.setText("Your Payment date is "+delDate);
                String msg="You have make a order for "+sel_vendor.getShopName()+" with total "+curBill.getTotal()+" in "+curBill.getBillDate()+" THANK YOU D.N. DISTRIBUTORS";
                String number=sel_vendor.getTelNoConfm();
                if (number==null||number.length()!=10){
                    number="0777117110";
                }
                try {

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, msg, null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent",
                            Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                   // Toast.makeText(getApplicationContext(),
                            //ex.getMessage().toString(),
                            //Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        });

        btn_crt_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delDate!=null &&delDate!="") {
                new_order_done.VenOrderSubmitToDatabase(getApplicationContext());
                new_order_done.OrderSubmitToDatabase(getApplicationContext());
                    curBill.setPayDate(delDate);
                    curBill.BillSubmitToDB(getApplicationContext());
                    curBill.setVenOrderID(new_order_done.getOrderID());
                    Toast.makeText(getApplicationContext(),"Create bill VenorderID"+curBill.getVenderID(),Toast.LENGTH_LONG);
                    btn_mk_payment.setEnabled(true);

                }
            }
        });
        btn_mk_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delDate!=null &&delDate!="") {
                    Intent bill_payment_intent=new Intent(PlaceOrderBill.this,MakePayment.class);
                    bill_payment_intent.putExtra("logged_user", logged_rep);
                    startActivity(bill_payment_intent);
                }
            }
        });
    }
}