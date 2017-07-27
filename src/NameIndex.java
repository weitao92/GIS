import java.util.ArrayList;

/**
 * This class is the name index of the system, it contains a Hashtable to store name and offsets of records. Every time i import
 * a record into the database, if the record isn't already in the database(same FID), then i import a hashEntry with name and offset
 * of that record into the hash table. Every time i try to retrieve a record by name, i use this class to find the specific name in
 * the table and the offsets, then use the offset to search the record back in the database file.
 * 
 * @author weitao92
 *
 */
public class NameIndex
{
	HashTable <String, Long> table;
	GISRecord record;
	
	public NameIndex()
	{
		table = new HashTable <String, Long> ();
		record = new GISRecord();
	}
	
	public void setGIS(GISRecord record)
	{
		this.record = record;
	}
	
	/**
	 * Method i use to import GIS information into the table.
	 */
	public void importGIS()
	{
		String name = record.getName() + "-" + record.getState();
		table.add(name, record.getOffset());
	}
	
	/**
	 * Method i use to retrieve offsets of the record which has the name i am looking for.
	 * @param name
	 * @return
	 */
	public ArrayList <Long> retrieve(String name)
	{
		return table.find(name);
	}
	
	public int getLongestProbe()
	{
		return table.getLongestProbe();
	}
	
	public void resetLongestProbe()
	{
		table.resetLongestProbe();
	}

}
