package ucsc.group12.agentmate.bll;

import java.io.Serializable;

/**
 * Created by NRV on 8/23/2014.
 */
public class Discount implements Serializable{
    public int min_amount;
    public int max_amount;
    public String unit_of_measure;
    public float discount;

    public void setMin_amount(int min_amount) {
        this.min_amount = min_amount;
    }

    public void setMax_amount(int max_amount) {
        this.max_amount = max_amount;
    }

    public int getMin_amount() {
        return min_amount;
    }

    public int getMax_amount() {
        return max_amount;
    }

    public String getUnit_of_measure() {
        return unit_of_measure;
    }

    public float getDiscount() {
        return discount;
    }

    public void setUnit_of_measure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
