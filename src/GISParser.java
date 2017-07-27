import java.util.Scanner;

/**
 * This record is a GIS record creator. I simply import the raw data which is the string line i got for each record from the record
 * file. And this class will convert each String into the specific information of the GIS record. Like it will change the String
 * latitude and longitude into the number version.
 * @author weitao92
 *
 */
public class GISParser
{
	String rawData;
	long offset;
	GISRecord record;
	Scanner scan;
	
	public GISParser(String rawData, long offset)
	{
		this.rawData = rawData;
		this.offset = offset;
		scan = new Scanner(rawData);
		record = new GISRecord();
	}
	
	public GISRecord parse()
	{
		scan.useDelimiter("\\|");
		record.setRaw(rawData);
		String FID = scan.next();
		if(!(FID == null))
		{
			record.setFID(Long.parseLong(FID));
		}
		
		String name = scan.next();
		if(!(name == null))
		{
			record.setName(name);
		}
		
		scan.next();
		String state = scan.next();
		if(!(state == null))
		{
			record.setState(state);
		}
		
		scan.next();
		String county = scan.next();
		if(!county.equals(""))
		{
			record.setCounty(county);
		}
		
		record.setOffset(offset);
		
		scan.next();
		String lat = scan.next();
		String lon = scan.next();
		
		if(!(lat == null) && !(lat.equals("Unknown")))
		{
			record.setStringLat(lat);
			try{
			long latD = Long.parseLong(lat.substring(0,2));
			long latM = Long.parseLong(lat.substring(2,4));
			long latS = Long.parseLong(lat.substring(4,6));
			String latDirection = lat.substring(6, 7);
			
			long latDEC = latD * 3600 + latM * 60 + latS;
			
			if(latDirection.equals("S"))
			{
				latDEC = 0 - latDEC;
			}
			
			record.setLatDEC(latDEC);
			}
			catch (Exception e)
			{
				System.out.println(lat);
			}
		}
		
		if(!(lon == null) && !(lon.equals("Unknown")))
		{
			record.setStringLon(lon);
			long lonD;
			if(lon.startsWith("0"))
			{
				lonD = Long.parseLong(lon.substring(1,3));
			}
			else
			{
				lonD = Long.parseLong(lon.substring(0,3));
			}
			
			long lonM = Long.parseLong(lon.substring(3,5));
			long lonS = Long.parseLong(lon.substring(5,7));
			String lonDirection = lon.substring(7, 8);
			
			long lonDEC = lonD * 3600 + lonM * 60 + lonS;
			
			if(lonDirection.equals("W"))
			{
				lonDEC = 0 - lonDEC;
			}
			
			record.setLonDEC(lonDEC);
		}
		
		scan.close();
		
		return record;
	}
}
