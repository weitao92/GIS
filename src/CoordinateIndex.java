import java.util.ArrayList;

/**
 * This class is the geographic coordinate index class. It contains a prQuadtree to store the geographic locations record.
 * Everytime i import a record from the record file, i first check through the FID index to see if i have already inserted that
 * record. If i have not, then i will insert the record's geographic information and it's offset in the database into this 
 * coordinate index system. Every time i try to find some record by geographic coordinates, i will use this index system to search
 * for the specific coordiantes or in a specific area region in the prQuadTree and get the offsets of the records i am looking for
 * in the database.
 * 
 * @author weitao92
 *
 */
public class CoordinateIndex
{
	prQuadTree <Location> tree;
	GISRecord record;
	
	public CoordinateIndex(String xMin, String xMax, String yMin, String yMax)
	{		
		Long lonD1;
		if(xMin.startsWith("0"))
		{
			lonD1 = Long.parseLong(xMin.substring(1,3));
		}
		else
		{
			lonD1 = Long.parseLong(xMin.substring(0,3));
		}
		
		Long lonM1 = Long.parseLong(xMin.substring(3,5));
		Long lonS1 = Long.parseLong(xMin.substring(5,7));
		String lonDirection1 = xMin.substring(7, 8);
		
		Long x1 = lonD1 * 3600 + lonM1 * 60 + lonS1;
		
		if(lonDirection1.equals("W"))
		{
			x1 = 0 - x1;
		}
		
		Long lonD2;
		if(xMax.startsWith("0"))
		{
			lonD2 = Long.parseLong(xMax.substring(1,3));
		}
		else
		{
			lonD2 = Long.parseLong(xMax.substring(0,3));
		}
		
		Long lonM2 = Long.parseLong(xMax.substring(3,5));
		Long lonS2 = Long.parseLong(xMax.substring(5,7));
		String lonDirection2 = xMax.substring(7, 8);
		
		Long x2 = lonD2 * 3600 + lonM2 * 60 + lonS2;
		
		if(lonDirection2.equals("W"))
		{
			x2 = 0 - x2;
		}
		
		Long latD1 = Long.parseLong(yMin.substring(0,2));
		Long latM1 = Long.parseLong(yMin.substring(2,4));
		Long latS1 = Long.parseLong(yMin.substring(4,6));
		String latDirection1 = yMin.substring(6, 7);
		
		Long y1 = latD1 * 3600 + latM1 * 60 + latS1;
		
		if(latDirection1.equals("S"))
		{
			y1 = 0 - y1;
		}
		
		Long latD2 = Long.parseLong(yMax.substring(0,2));
		Long latM2 = Long.parseLong(yMax.substring(2,4));
		Long latS2 = Long.parseLong(yMax.substring(4,6));
		String latDirection2 = yMax.substring(6, 7);
		
		Long y2 = latD2 * 3600 + latM2 * 60 + latS2;
		
		if(latDirection2.equals("S"))
		{
			y2 = 0 - y2;
		}
		
		tree = new prQuadTree <Location> (x1, x2, y1, y2);
		record = new GISRecord();
	}
	
	public void setGIS(GISRecord record)
	{
		this.record = record;
	}
	
	public boolean importGIS()
	{
		Location location = new Location(record.getLonDEC(), record.getLatDEC(), record.getOffset());
		return tree.insert(location);
	}
	
	public ArrayList <Long> retrieve(String lat, String lon)
	{
		Long latD = Long.parseLong(lat.substring(0,2));
		Long latM = Long.parseLong(lat.substring(2,4));
		Long latS = Long.parseLong(lat.substring(4,6));
		String latDirection = lat.substring(6, 7);
		
		Long latDEC = latD * 3600 + latM * 60 + latS;
		
		if(latDirection.equals("S"))
		{
			latDEC = 0 - latDEC;
		}
		
		Long lonD;
		if(lon.startsWith("0"))
		{
			lonD = Long.parseLong(lon.substring(1,3));
		}
		else
		{
			lonD = Long.parseLong(lon.substring(0,3));
		}
		
		Long lonM = Long.parseLong(lon.substring(3,5));
		Long lonS = Long.parseLong(lon.substring(5,7));
		String lonDirection = lon.substring(7, 8);
		
		Long lonDEC = lonD * 3600 + lonM * 60 + lonS;
		
		if(lonDirection.equals("W"))
		{
			lonDEC = 0 - lonDEC;
		}
		
		Location location = new Location(lonDEC, latDEC);
		Location target = tree.find(location);
		
		if(target != null)
		{
			return target.getAll();
		}
		else
		{
			return null;
		}
	}
	
	public ArrayList <Location> areaSearch(String lat, String lon, String halfHeight, String halfWidth)
	{
		Long latD = Long.parseLong(lat.substring(0,2));
		Long latM = Long.parseLong(lat.substring(2,4));
		Long latS = Long.parseLong(lat.substring(4,6));
		String latDirection = lat.substring(6, 7);
		
		Long latDEC = latD * 3600 + latM * 60 + latS;
		
		if(latDirection.equals("S"))
		{
			latDEC = 0 - latDEC;
		}
		
		Long lonD;
		if(lon.startsWith("0"))
		{
			lonD = Long.parseLong(lon.substring(1,3));
		}
		else
		{
			lonD = Long.parseLong(lon.substring(0,3));
		}
		
		Long lonM = Long.parseLong(lon.substring(3,5));
		Long lonS = Long.parseLong(lon.substring(5,7));
		String lonDirection = lon.substring(7, 8);
		
		Long lonDEC = lonD * 3600 + lonM * 60 + lonS;
		
		if(lonDirection.equals("W"))
		{
			lonDEC = 0 - lonDEC;
		}
		
		Long halfH = Long.parseLong(halfHeight);
		Long halfW = Long.parseLong(halfWidth);
		
		return tree.find(lonDEC - halfW, lonDEC + halfW, latDEC - halfH, latDEC + halfH);
	}
	
	public void printFailed()
	{
		int size = tree.failed.size();
		
		System.out.println(size);
		
		for(int i = 0; i < size; i++)
		{
			System.out.println(tree.failed.get(i));
		}
	}
}
