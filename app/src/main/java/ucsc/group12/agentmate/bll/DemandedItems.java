package ucsc.group12.agentmate.bll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NRV on 9/20/2014.
 */
public class DemandedItems implements Serializable{
    public ArrayList<SellItem> demand_list = new ArrayList<SellItem>();
    String venderno;
    Date reqquested_date;
}
