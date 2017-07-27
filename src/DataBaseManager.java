import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is a reader and writer of the database file. Every time i read a line from the imported record file. I will use this
 * class to write that record into the database, then i will use this class to return the offset of that specific record in the
 * database and store the record information plus the offset into a DataEntry for future use(insertion into the FID index, name index
 * and coordinate index).
 * 
 * @author weitao92
 *
 */
public class DataBaseManager
{
	DataEntry entry;
	RandomAccessFile writer;
	FileWriter clear;
	
	public void setUp(File file)
	{
		try {
			writer = new RandomAccessFile(file, "rw");
			try {
				clear = new FileWriter(file);
				clear.write("");
				clear.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataEntry importData(String rawData)
	{
		try {
			long offset = (long) writer.getFilePointer();
			entry = new DataEntry(rawData, offset);
			writer.writeBytes(rawData);
			writer.writeBytes(System.getProperty("line.separator"));
			return entry;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public DataEntry retrieve(long offset) throws IOException
	{
		writer.seek(new Long(offset).longValue());
		String rawData = writer.readLine();
		entry = new DataEntry(rawData, offset);
		return entry;
	}
	
	public void close()
	{
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
