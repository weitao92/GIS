import java.util.ArrayList;

/**
 * This class is the HashTable data struction. Each table contains an array of HashEntries, this hash table use the quadraic
 * probing technique to insert and retrieve record.
 * 
 * @author weitao92
 *
 * @param <T> 
 */
public class HashTable <K, V>
{
	
	@SuppressWarnings("rawtypes")
	HashEntry[] hashtable;
	int numOfEntry;
	int longestProbe;
	int currentSize;
	int sizeLimit;
	
	public HashTable()
	{
		hashtable = new HashEntry[1024];
		numOfEntry = 0;
		currentSize = 1024;
		sizeLimit = (int) (currentSize * 0.7);
		longestProbe = 0;
	}
	/**
	 * Use quadraic probing to insert record, when the size of table > 70% of size limit, it will resize automatically.
	 * 
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void add(K key, V value)
	{
		HashEntry <K, V> newEntry = new HashEntry <K, V>(key, value);
		int index = newEntry.hashFunction() % currentSize;
		int n = 1;
		int probe = 0;
		
		while(hashtable[index] != null && !hashtable[index].equals(newEntry))
		{
			index = index + n;
			n++;
			index = index % currentSize;
			probe++;
		}
		
		if(hashtable[index] == null)
		{
			hashtable[index] = newEntry;
			numOfEntry++;
		}
		else
		{
			hashtable[index].addAnother(newEntry);
		}
		
		if(probe > longestProbe)
		{
			longestProbe = probe;
		}
		
		if(numOfEntry >= sizeLimit)
		{
			resize();
		}
		
	}
	
	/**
	 * The resize algorithms, simply create a new table with double size then import every entries in the original table into
	 * the new table.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void resize()
	{
		HashEntry[] temp = hashtable;
		currentSize = currentSize * 2;
		hashtable = new HashEntry[currentSize];
		sizeLimit = (int) (currentSize * 0.7);
		numOfEntry = 0;
		
		for(int i = 0; i < temp.length; i++)
		{
			if(temp[i] != null)
			{
				HashEntry <K, V> tempEntry = temp[i];
				
				K key = tempEntry.getKey();
				
				while(tempEntry.getSize() != 0)
				{
					add(key, tempEntry.removeFirst());
				}
			}
		}
	}
	
	/**
	 * Use the quadraic probing to find the entry with the specific Key.
	 * 
	 * @param key The key we looking for.
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList <V> find(K key)
	{
		HashEntry temp = new HashEntry(key);
		int index = temp.hashFunction() % currentSize;
		int n = 1;
		
		while(hashtable[index] != null && !hashtable[index].equals(temp))
		{
			index = index + n;
			n++;
			index = index % currentSize;
		}
		
		if(hashtable[index] == null)
		{
			return null;
		}
		else
		{
			return hashtable[index].getValues();
		}
	}
	
	/**
	 * Returen the longest probe during insertion of record.
	 * 
	 * @return
	 */
	public int getLongestProbe()
	{
		return longestProbe;
	}
	
	public void resetLongestProbe()
	{
		longestProbe = 0;
	}
	
	public String display()
	{
		String result = "";
		
		for(int i = 0; i < currentSize; i++)
		{
			if(hashtable[i] != null)
			{
				result += i;
				result += ":\t";
				result += hashtable[i];
				result += System.getProperty("line.separator");
			}
		}
		
		return result;
	}
}