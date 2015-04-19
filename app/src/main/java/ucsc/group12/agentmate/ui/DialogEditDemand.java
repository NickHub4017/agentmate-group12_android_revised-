package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.SellItem;
import ucsc.group12.agentmate.bll.UnitMap;
import ucsc.group12.agentmate.bll.mapper;
import ucsc.group12.agentmate.bll.Order;


/**
 * Created by NRV on 9/20/2014.
 */
public class DialogEditDemand extends DialogFragment{

    EditCommDemand ecm;
    String[] itemid;
    String chooseID;
    SellItem toEditItem;
    int selected_Qty,remainQty,total;
    TextView tv_selectitemID,tv_selectitemName,tv_qtymsg;
    Button btnEdit;
    View rootview;
    String[] demandqtys;
    LinearLayout lout;
    mapper mp;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ecm = (EditCommDemand)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.select_list_edit, null);
        chooseID=getArguments().getString("ItemID");
        selected_Qty=getArguments().getInt("Qty");

        mp=new mapper(getActivity().getApplicationContext(),chooseID);

        demandqtys=new String[mp.u_map.length];

        String preferedUnit=UnitFinder(selected_Qty,mp);
        textboxCreator(view,mp.u_map,preferedUnit,selected_Qty);

        tv_selectitemID=(TextView)view.findViewById(R.id.edit_item_id);
        tv_selectitemName=(TextView)view.findViewById(R.id.edit_item_name);
        tv_selectitemID.setText(chooseID);
        tv_qtymsg=(TextView)view.findViewById(R.id.txt_edit_order2);
        tv_qtymsg.setText("Current demanded qty is "+total+" .....");
        btnEdit=(Button)view.findViewById(R.id.btn_order_edit_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temptot=collectdata();
                if (temptot>total){
                    show_warning();
                }
                else{
                    ecm.onEditMessage(chooseID, temptot,total);
                    dismiss();
                }


            }
        });


        return view;

    }

    interface EditCommDemand{
        public void onEditMessage(String ItemID,int qty,int tot);
    }


    public String UnitFinder(int qty,mapper umap){
        for (int i=0;i<umap.u_map.length;i++){
            int cur_q=umap.u_map[i].getQtyMap();
            if ((qty%cur_q)==0){
                return umap.u_map[i].getUnit();
            }
        }
        return "";
    }

    public int Unitsolver(int qty,mapper umap){
        for (int i=0;i<umap.u_map.length;i++){
            int cur_q=umap.u_map[i].getQtyMap();
            if ((qty%cur_q)==0){
                return qty/cur_q;
            }
        }
        return -1;
    }

    public void textboxCreator(View view,UnitMap[] Unitlist,String preferedUnit,int qty){
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.data_text_layout);
        layout.setBackgroundColor(Color.LTGRAY);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        for (int i=0;i<Unitlist.length;i++) {
            final EditText tv = new EditText(view.getContext());
            //tv.setText(Unitlist[i]);
            tv.setId(i+2100);
            tv.setPadding(5, 5, 5, 5);
            tv.setInputType(InputType.TYPE_CLASS_NUMBER);
            tv.setGravity(Gravity.CENTER);
            tv.setHint(Unitlist[i].getUnit());
            if((i%2)==0){
                tv.setBackgroundColor(Color.DKGRAY);
                tv.setTextColor(Color.LTGRAY);
            }
            if (Unitlist[i].getUnit()==preferedUnit){
                tv.setText(String.valueOf(Unitsolver(selected_Qty,mp)));
                demandqtys[i]=String.valueOf(Unitsolver(selected_Qty,mp));
            }
            tv.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    demandqtys[view.getId()-2100]=tv.getText().toString();
                    Log.d("EDIT QtyArray_demand EDIT",""+demandqtys[0]+" "+demandqtys[1]);
                    return false;
                }
            });
            layout.addView(tv, 100, 40);

        }


    }


    public String[] getData(int dataid){
        int len=PlaceOrderSecond.new_order.list.size();
        String[] reslt_list=new String[len];
        for (int i=0;i<len;i++){
            if (dataid==0){
                reslt_list[i]=PlaceOrderSecond.new_order.list.get(i).getItemID();
            }
            else{
                reslt_list[i]=PlaceOrderSecond.new_order.list.get(i).getItemName();
            }
        }
        return reslt_list;


    }

    public int GetRealPos(String key){
        for (int i=0;i<itemid.length;i++){
            if (itemid[i].equals(key)){
                return i;
            }
        }
        return -1;
    }

    public void show_warning(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Quantity limit exceed")
                .setMessage("Quantity limit exceeds Maximum is " + total + " Set it as qty?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked, do something
                        ecm.onEditMessage(chooseID, total, total);
                        dismiss();
                    }

                })	.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })					//Do nothing on no
                .show();


    }

    public int collectdata(){



        int newtot=0;
        String bestUnit;
        for (int i=0;i<demandqtys.length;i++){

            String new_qty=demandqtys[i];
            try{
                newtot=newtot+(Integer.parseInt(new_qty)*mp.u_map[i].getQtyMap());
            }
            catch (Exception e){
                newtot=newtot+0;
            }

        }

        return newtot;
    }

}
