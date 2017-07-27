/**
 * This class contains the rawData of a record in the database, and it's offset in the database. I will use this class as raw
 * material to create a GIS record.
 * 
 * @author weitao92
 *
 */
public class DataEntry
{
	String rawData;
	Long offset;
	
	public DataEntry(String rawData, Long offset)
	{
		this.rawData = rawData;
		this.offset = offset;
	}
	
	public String getRawData()
	{
		return rawData;
	}
	
	public Long getOffset()
	{
		return offset;
	}

}
