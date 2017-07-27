import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is the core of the entire system. When i run the main method of the system, it will run this class and it's excute
 * method. It will create IOs to different file we want to read and write. It will read commands from the script file and write
 * logs to the log file, and write rawData to the database file. It also contains 3 indexs i use for insertion and searching.
 * 
 * @author weitao92
 *
 */
public class Controller
{
	NameIndex indexN;
	CoordinateIndex indexC;
	FIDIndex indexF;
	DataBaseManager dataManager;
	logWriter logger;
	GISParser parser;
	LRUpool pool;
	RandomAccessFile random;
	Scanner scan;
	File database;
	File commands;
	File log;
	
	public Controller(String database1, String commands1, String log1)
	{
		database = new File(database1);
		commands = new File(commands1);
		log = new File(log1);
		indexN = new NameIndex();
		indexF = new FIDIndex();
		pool = new LRUpool();
		dataManager = new DataBaseManager();
		dataManager.setUp(database);
		logger = new logWriter();
		logger.setUp(log);
		
		try {
			random = new RandomAccessFile(commands, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execute()
	{
		try {
			int num = 1;
			
			while(true)
			{
				String line = random.readLine();
				
				if(line != null)
				{
					if(!(line == null || line.startsWith(";")))
					{
						scan = new Scanner(line);
						scan.useDelimiter("\\t");
						String initial = scan.next();
						String content = new String();
						content += "Commands ";
						content += num;
						content += ": ";
						content += line;
						
						if(initial.equals("world"))
						{
							String xMin = scan.next();
							String xMax = scan.next();
							String yMin = scan.next();
							String yMax = scan.next();
							
							initiateWorld(xMin, xMax, yMin, yMax, content);
							num++;
						}
						else if(initial.equals("import"))
						{
							File record = new File(scan.next());
							importRecords(record, content);
							num++;
						}
						else if(initial.equals("what_is_at"))
						{
							String lat = scan.next();
							String lon = scan.next();
							coordinateSearch(lat, lon, content);
							num++;
						}
						else if(initial.equals("what_is"))
						{
							String name = scan.next();
							String state = scan.next();
							nameSearch(name, state, content);
							num++;
						}
						else if(initial.equals("what_is_in"))
						{
							String next = scan.next();
							
							if(next.equals("-l"))
							{
								String lat = scan.next();
								String lon = scan.next();
								String halfHeight = scan.next();
								String halfWidth = scan.next();
								whatisinL(lat, lon, halfHeight, halfWidth, content);
								num++;
							}
							else if(next.equals("-c"))
							{
								String lat = scan.next();
								String lon = scan.next();
								String halfHeight = scan.next();
								String halfWidth = scan.next();
								whatisinC(lat, lon, halfHeight, halfWidth, content);
								num++;
							}
							else
							{
								String lon = scan.next();
								String halfHeight = scan.next();
								String halfWidth = scan.next();
								whatisin(next, lon, halfHeight, halfWidth, content);
								num++;
							}
						}
						else if(initial.equals("debug"))
						{
							debug(scan.next(), content);
							num++;
						}
						else if(initial.equalsIgnoreCase("quit"))
						{
							logger.write(content);
							logger.close();
							dataManager.close();
							scan.close();
							random.close();
							break;
						}
					}
				}
				else
				{
					break;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initiateWorld(String xMin, String xMax, String yMin, String yMax, String content)
	{
		indexC = new CoordinateIndex(xMin, xMax, yMin, yMax);
		
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
		
		logger.write(content);
		logger.write(System.getProperty("line.separator"));
		logger.write("          ");
		logger.write(Long.toString(y2));
		logger.write(System.getProperty("line.separator"));
		logger.write(Long.toString(x1));
		logger.write("          ");
		logger.write(Long.toString(x2));
		logger.write(System.getProperty("line.separator"));
		logger.write("          ");
		logger.write(Long.toString(y1));
		logger.write(System.getProperty("line.separator"));
		logger.write("--------------------------------------------------------------------------------");
		logger.write(System.getProperty("line.separator"));
	}
	
	private void importRecords(File records, String content) throws IOException
	{
		try {
			RandomAccessFile random1 = new RandomAccessFile(records, "r");
			String headLine = random1.readLine();
			dataManager.writer.writeBytes(headLine);
			dataManager.writer.writeBytes(System.getProperty("line.separator"));
			int numN = 0;
			int numC = 0;
			
			while(true)
			{
				String record = random1.readLine();
				
				if(!(record == null))
				{
					DataEntry entry = dataManager.importData(record);
					parser = new GISParser(entry.getRawData(), entry.getOffset());
					GISRecord gis = parser.parse();
					
					if(!(indexF.contains(gis)))
					{
						numN++;
						indexN.setGIS(gis);
						indexN.importGIS();
						
						if(!(gis.getStringLat().equals("") || gis.getStringLon().equals("")))
						{
							indexC.setGIS(gis);
							if(indexC.importGIS())
							{
								numC++;
							}
						}
						
						indexF.importFID(gis);
					}
				}
				else
				{
					break;
				}
			}
			
			logger.write(content);
			logger.write(System.getProperty("line.separator"));
			logger.write(System.getProperty("line.separator"));
			
			String text = new String();
			text += "Imported by name: ";
			text += numN;
			text += System.getProperty("line.separator");
			
			int longest = indexN.getLongestProbe();
			text += "Longest probe: ";
			text += longest;
			text += System.getProperty("line.separator");
			
			text += "Imported by location: ";
			text += numC;
			
			logger.write(text);
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
			indexN.resetLongestProbe();
			
			//indexC.printFailed();
			random1.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void coordinateSearch(String lat, String lon, String content)
	{
		logger.write(content);
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		
		ArrayList <Long> offsets = indexC.retrieve(lat, lon);
		
		if(offsets != null)
		{
			int size = offsets.size();
			logger.write(Integer.toString(size));
			logger.write(" number of features have been found.");
			logger.write(System.getProperty("line.separator"));
			logger.write(System.getProperty("line.separator"));
			
			for(int i = 0; i < size; i++)
			{
				String result = new String();
				Long offset = offsets.get(i);
				
				
				if(pool.contains(offset))
				{
					GISRecord record = pool.retrieveGIS(offset);
					result += record.getOffset();
					result += ": ";
					result += record.getName();
					result += " ";
					result += record.getCounty();
					result += " ";
					result += record.getState();
					pool.insert(record);
				}
				else
				{
				
					try {
						DataEntry entry = dataManager.retrieve(offset);
						parser = new GISParser(entry.getRawData(), entry.getOffset());
						GISRecord record = parser.parse();
						
						result += record.getOffset();
						result += ": ";
						result += record.getName();
						result += " ";
						result += record.getCounty();
						result += " ";
						result += record.getState();
						pool.insert(record);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				logger.write(result);
				logger.write(System.getProperty("line.separator"));
			}
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
		}
		else
		{
			logger.write("No record has been found at this coordinate.");
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
		}
	}
	
	private void nameSearch(String name, String state, String content)
	{
		logger.write(content);
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		String key = name + "-" + state;
		ArrayList <Long> offsets = indexN.retrieve(key);
		
		if(offsets != null)
		{
			int size = offsets.size();
			logger.write(Integer.toString(size));
			logger.write(" number of features have been found.");
			logger.write(System.getProperty("line.separator"));
			logger.write(System.getProperty("line.separator"));
			
			for(int i = 0; i < size; i++)
			{
				String result = new String();
				Long offset = offsets.get(i);
				
				if(pool.contains(offset))
				{
					GISRecord record = pool.retrieveGIS(offset);
					result += record.getOffset();
					result += ": ";
					result += record.getCounty();
					result += " ";
					result += record.getStringLat();
					result += " ";
					result += record.getStringLon();
					pool.insert(record);
				}
				else
				{
					try {
						DataEntry entry = dataManager.retrieve(offset);
						parser = new GISParser(entry.getRawData(), entry.getOffset());
						GISRecord record = parser.parse();
						result += record.getOffset();
						result += ": ";
						result += record.getCounty();
						result += " ";
						result += record.getStringLat();
						result += " ";
						result += record.getStringLon();
						pool.insert(record);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				logger.write(result);
				logger.write(System.getProperty("line.separator"));
			}
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
		}
		else
		{
			logger.write("No record has been found at this Name.");
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
		}
	}
	
	private void whatisin(String lat, String lon, String halfHeight, String halfWidth, String content)
	{
		logger.write(content);
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		ArrayList <Location> locations = indexC.areaSearch(lat, lon, halfHeight, halfWidth);
		
		int num = 0;
		int out = locations.size();
		for(int i = 0; i < out; i++)
		{
			ArrayList <Long> inner = locations.get(i).getAll();
			int in = inner.size();
			for(int j = 0; j < in; j++)
			{
				num++;
			}
		}
		
		logger.write(Integer.toString(num));
		logger.write(" number of features have been found.");
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		int size = locations.size();
		
		for(int i = 0; i < size; i++)
		{
			Location location = locations.get(i);
			ArrayList <Long> offsets = location.getAll();
			int size1 = offsets.size();
			
			for(int j = 0; j < size1; j++)
			{
				Long offset = offsets.get(j);
				
				String result = new String();
				if(pool.contains(offset))
				{
					GISRecord record = pool.retrieveGIS(offset);
					result += "Offset: ";
					result += record.getOffset();
					result += " ";
					result += "Name: ";
					result += record.getName();
					result += " ";
					result += "State: ";
					result += record.getState();
					result += " ";
					result += "Latitude: ";
					result += record.getStringLat();
					result += " ";
					result += "Longitude: ";
					result += record.getStringLon();
					pool.insert(record);
				}
				else
				{
					try {
						DataEntry entry = dataManager.retrieve(offset);
						parser = new GISParser(entry.getRawData(), entry.getOffset());
						GISRecord record = parser.parse();
						result += "Offset: ";
						result += record.getOffset();
						result += " ";
						result += "Name: ";
						result += record.getName();
						result += " ";
						result += "State: ";
						result += record.getState();
						result += " ";
						result += "Latitude: ";
						result += record.getStringLat();
						result += " ";
						result += "Longitude: ";
						result += record.getStringLon();
						
						pool.insert(record);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				logger.write(result);
				logger.write(System.getProperty("line.separator"));
			}
		}
		
		logger.write("--------------------------------------------------------------------------------");
		logger.write(System.getProperty("line.separator"));
	}
	
	private void whatisinL(String lat, String lon, String halfHeight, String halfWidth, String content)
	{
		logger.write(content);
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		ArrayList <Location> locations = indexC.areaSearch(lat, lon, halfHeight, halfWidth);
		
		int num = 0;
		int out = locations.size();
		for(int i = 0; i < out; i++)
		{
			ArrayList <Long> inner = locations.get(i).getAll();
			int in = inner.size();
			for(int j = 0; j < in; j++)
			{
				num++;
			}
		}
		
		logger.write(Integer.toString(num));
		logger.write(" number of features have been found.");
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		int size = locations.size();
		
		for(int i = 0; i < size; i++)
		{
			Location location = locations.get(i);
			ArrayList <Long> offsets = location.getAll();
			int size1 = offsets.size();
			
			for(int j = 0; j < size1; j++)
			{
				Long offset = offsets.get(j);
				
				if(pool.contains(offset))
				{
					GISRecord record = pool.retrieveGIS(offset);
					logger.write(record.toString());
					logger.write(System.getProperty("line.separator"));
					pool.insert(record);
				}
				else
				{
					try {
						DataEntry entry = dataManager.retrieve(offset);
						parser = new GISParser(entry.getRawData(), entry.getOffset());
						GISRecord record = parser.parse();
						logger.write(record.toString());
						logger.write(System.getProperty("line.separator"));
						
						pool.insert(record);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		logger.write("--------------------------------------------------------------------------------");
		logger.write(System.getProperty("line.separator"));
	}
	
	private void whatisinC(String lat, String lon, String halfHeight, String halfWidth, String content)
	{
		logger.write(content);
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		ArrayList <Location> locations = indexC.areaSearch(lat, lon, halfHeight, halfWidth);
		
		int num = 0;
		int size = locations.size();
		
		for(int i = 0; i < size; i++)
		{
			Location location = locations.get(i);
			ArrayList <Long> offsets = location.getAll();
			int size1 = offsets.size();
			
			for(int j = 0; j < size1; j++)
			{
				num++;
			}
		}
		
		String result = new String();
		result += "Number of records fall in this area: ";
		result += num;
		logger.write(result);
		logger.write(System.getProperty("line.separator"));
		logger.write("--------------------------------------------------------------------------------");
		logger.write(System.getProperty("line.separator"));
	}
	
	private void debug(String type, String content)
	{
		logger.write(content);
		logger.write(System.getProperty("line.separator"));
		logger.write(System.getProperty("line.separator"));
		
		if(type.equals("hash"))
		{
			HashTable<String, Long> table = indexN.table;
			for(int i = 0; i < table.currentSize; i++)
			{
				if(table.hashtable[i] != null)
				{
					logger.write(table.hashtable[i].toString());
					logger.write(System.getProperty("line.separator"));
				}
			}
			//logger.write(hashTable);
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
		}
		else if(type.equals("quad"))
		{
			StringBuilder quadTree = indexC.tree.display();
			logger.write(quadTree.toString());
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
		}
		else if(type.equals("pool"))
		{
			String bufferPool = pool.display();
			logger.write(bufferPool);
			logger.write(System.getProperty("line.separator"));
			logger.write("--------------------------------------------------------------------------------");
			logger.write(System.getProperty("line.separator"));
		}
	}
}
