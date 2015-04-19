package ucsc.group12.agentmate.bll;

/**
 * Created by NRV on 8/23/2014.
 */


        import java.io.Serializable;

public class Vendor implements Serializable{
    String venderNo;
    String venderName;
    String address;
    String telNoShop;
    String telNoConfm;
    double overdue;
    boolean confirm;
    String shopName;
    //(2,shopname_ins,owner_ins,address_ins,Long.getLong(shoptel_ins),Long.getLong(cnftel_ins),0,false);
    public Vendor(String venderNo_cons,String shopName_cons,String venderName_cons, String address_cons,
                  String telNoShop_cons, String telNoConfm_cons, double overdue_cons, boolean confirm_cons) {
        super();
        this.venderNo = venderNo_cons;
        this.venderName = venderName_cons;
        this.address = address_cons;
        this.telNoShop = telNoShop_cons;
        this.telNoConfm = telNoConfm_cons;
        this.overdue = overdue_cons;
        this.confirm = confirm_cons;
        this.shopName=shopName_cons;

    }
    public Vendor(){

    }
    public String getVenderNo() {
        return venderNo;
    }

    public String getVenderName() {
        return venderName;
    }

    public String getAddress() {
        return address;
    }

    public String getTelNoShop() {
        return telNoShop;
    }

    public String getTelNoConfm() {
        return telNoConfm;
    }

    public double getOverdue() {
        return overdue;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setVenderNo(String venderNo_set) {
        this.venderNo = venderNo_set;
    }

    public void setVenderName(String venderName_set) {
        this.venderName = venderName_set;
    }

    public void setAddress(String address_set) {
        this.address = address_set;
    }

    public void setTelNoShop(String telNoShop_set) {
        this.telNoShop = telNoShop_set;
    }

    public void setTelNoConfm(String telNoConfm_set) {
        this.telNoConfm = telNoConfm_set;
    }

    public void setOverdue(double overdue_set) {
        this.overdue = overdue_set;
    }

    public void setConfirm() {
        this.confirm = true;
    }

    public void updateVendor(Vendor edited_vendor){

    }
    public void setShopName(String shp_nm){
        this.shopName=shp_nm;
    }
    public String getShopName(){
        return this.shopName;
    }




}

