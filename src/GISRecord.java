/**
 * This class represents the GIS record we use to store, search, insert in the system. It contains the most important informations
 * about one single GIS record. It has a raw data which is the String line we retrieve from the import record file. Other than 
 * the basic informations, it also contains an offset which point to the this record's position in the database file.
 * @author weitao92
 *
 */
public class GISRecord
{
	String rawData;
	Long FID;
	String name;
	String state;
	String county;
	Long offset;
	String stringLon;
	String stringLat;
	Long lonDEC;
	Long LatDEC;
	
	public GISRecord()
	{
		stringLon = new String();
		stringLat = new String();
	}
	
	public void setRaw(String rawData)
	{
		this.rawData = rawData;
	}
	
	public String rawData()
	{
		return rawData;
	}
	
	public void setFID(Long FID)
	{
		this.FID = FID;
	}
	
	public Long getFID()
	{
		return FID;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public String getState()
	{
		return state;
	}
	
	public void setCounty(String county)
	{
		this.county = county;
	}
	
	public String getCounty()
	{
		return county;
	}
	
	public void setLonDEC(Long lonDEC)
	{
		this.lonDEC = lonDEC;
	}
	
	public Long getLonDEC()
	{
		return lonDEC;
	}
	
	public void setLatDEC(Long LatDEC)
	{
		this.LatDEC = LatDEC;
	}
	
	public Long getLatDEC()
	{
		return LatDEC;
	}
	
	public void setOffset(Long offset)
	{
		this.offset = offset;
	}
	
	public Long getOffset()
	{
		return offset;
	}
	
	public void setStringLat(String stringLat)
	{
		this.stringLat = stringLat;
	}
	
	public String getStringLat()
	{
		return stringLat;
	}
	
	public void setStringLon(String stringLon)
	{
		this.stringLon = stringLon;
	}
	
	public String getStringLon()
	{
		return stringLon;
	}
	
	public String toString()
	{
		String result = new String();
		result += "Offset: ";
		result += offset;
		result += " ";
		result += "FID: ";
		result += FID;
		result += " ";
		result += "Name: ";
		result += name;
		result += " ";
		result += "State: ";
		result += state;
		result += " ";
		result += "County: ";
		result += county;
		result += " ";
		result += "Latitude: ";
		result += stringLat;
		result += " ";
		result += "Longitude: ";
		result += stringLon;
		return result;
	}
}
