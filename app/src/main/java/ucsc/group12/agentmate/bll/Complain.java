package ucsc.group12.agentmate.bll;

import java.io.Serializable;

/**
 * Created by NRV on 9/22/2014.
 */
public class Complain implements Serializable{


    String ComplainID;
    String ItemID;
    String Complain;
    String VendorNo;
    Boolean Sync;

    public Complain(String complainID, String itemID, String complain, String vendorNo, Boolean sync) {
        ComplainID = complainID;
        ItemID = itemID;
        Complain = complain;
        VendorNo = vendorNo;
        Sync = sync;
    }

    public String getComplainID() {
        return ComplainID;
    }

    public void setComplainID(String complainID) {
        ComplainID = complainID;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getComplain() {
        return Complain;
    }

    public void setComplain(String complain) {
        Complain = complain;
    }

    public String getVendorNo() {
        return VendorNo;
    }

    public void setVendorNo(String vendorNo) {
        VendorNo = vendorNo;
    }

    public Boolean getSync() {
        return Sync;
    }

    public void setSync(Boolean sync) {
        Sync = sync;
    }
}
