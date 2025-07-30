package kc.thirdparty;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class POIExcelTestObj {
	
	public int Id;
    public String Name ;
    public boolean Sex ;
    public Date Birthday ;
    public double Price ;
}
