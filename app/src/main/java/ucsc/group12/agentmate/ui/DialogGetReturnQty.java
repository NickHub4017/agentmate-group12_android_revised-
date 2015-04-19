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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.mapper;


public class DialogGetReturnQty extends DialogFragment {
    Button btn_submit, btn_cancel, btn_submit_demand;

    GetQtyCommunicator cm;
    EditText txt_get_dmnd_qty;
    TextView txt_item_reamin;
    mapper Umaps;
    String[] QtyArray,QtyArray_demand;
    int tot=0;
    EditText edit_TrueretPriceEach;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cm=(GetQtyCommunicator)activity;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_load_dialog, null);

        String cur_id=getArguments().getString("itemid");
        Umaps=(mapper)getArguments().getSerializable("umapname");
        //radioBtnCreator(view,Umaps.getUnitNames());
        //radioBtnCreator_demand(view,Umaps.getUnitNames());
        textboxCreator(view,Umaps.getUnitNames());
        btn_submit=(Button)view.findViewById(R.id.btn_sbmt_dialog);
        btn_cancel=(Button)view.findViewById(R.id.btn_cancel_dialog);

        btn_submit_demand=(Button)view.findViewById(R.id.btn_sbmt_with_demand_dialog);
        btn_submit_demand.setVisibility(View.INVISIBLE);

        EditText editDate=(EditText)view.findViewById(R.id.editdate);
        editDate.setVisibility(View.INVISIBLE);

        TextView txtRemain=(TextView)view.findViewById(R.id.text_remain);
        txtRemain.setVisibility(View.INVISIBLE);


        txt_get_dmnd_qty=(EditText) view.findViewById(R.id.edit_demand_qty);
        txt_item_reamin=(TextView)view.findViewById(R.id.text_remain);

        edit_TrueretPriceEach=(EditText)view.findViewById(R.id.edit_demand_qty);


        QtyArray=new String[Umaps.getUnitNames().length];
        QtyArray_demand=new String[Umaps.getUnitNames().length];



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//ToDo set the method to avoid null inputs.

                for (int i=0;i<Umaps.u_map.length;i++){
                    try{
                        int x=Integer.parseInt(QtyArray[i]);
                        tot=tot+Umaps.getQtyInMinUnit(Umaps.u_map[i].getUnit(),x);
                    }
                    catch (Exception e){
                        tot=tot+0;

                    }
                }
                String each=edit_TrueretPriceEach.getText().toString();
                try {
                    cm.onGetData(tot, 0, "1", Double.parseDouble(each));
                    dismiss();
                }
                catch (Exception e){

                }

                //EditText tvt=(EditText)view.getRootView().findViewById(1801);
                //tvt.setText("jjgjgj");
                //Log.d("TExt",tvt.getText().toString());
                //tvt.setText("");

               /* tempQty = Integer.parseInt(txt_get_qty.getText().toString());
                int tempDemandQty = Integer.parseInt(txt_get_dmnd_qty.getText().toString());
                mapped = Umaps.getQtyInMinUnit(Selectedunit, tempQty);
                mappedDemand = Umaps.getQtyInMinUnit(Selected_demndedunit, tempDemandQty);
                if (mapped > cur_store_Qty) {
                    show_warning();
                } else {
                    cm.onGetData(tempQty, tempDemandQty, Selectedunit, Selected_demndedunit);
                    dismiss();
                }
                Log.d("MAPpped Qty", "" + mapped + " " + mappedDemand);*/

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;

    }

    public void textboxCreator(View view,String[] Unitlist){
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.Radiolink1);
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
            tv.setId(i+1800);
            tv.setPadding(5, 5, 5, 5);
            tv.setInputType(InputType.TYPE_CLASS_NUMBER);
            tv.setGravity(Gravity.CENTER);
            tv.setHint(Unitlist[i]);
            if((i%2)==0){
                tv.setBackgroundColor(Color.DKGRAY);
                tv.setTextColor(Color.LTGRAY);
            }
            tv.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {

                    QtyArray[view.getId()-1800]=tv.getText().toString();
                    Log.d("QtyArray",""+QtyArray[0]+" "+QtyArray[1]);
                    return false;
                }
            });
            layout.addView(tv, 100, 40);

        }


    }

    interface GetQtyCommunicator{
        public void onGetData(int qty,int demandQty,String demand_DateToDeliver,double Price);
    }

}