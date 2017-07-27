import java.util.ArrayList;

/**
 * This class is the HashEntry of HashTable, each hash entry contains the key which is the name + the state of the location,
 * and a list of offsets which points to the locations with the same name.
 * 
 * @author weitao92
 *
 * @param <K> The Key we use to import into hash function.
 * @param <V> Each hash entry contains a list of V which is value.
 */
public class HashEntry <K, V>
{
	K key;
	ArrayList <V> values;
	
	public HashEntry(K key, V value)
	{
		this.key = key;
		values = new ArrayList <V> ();
		values.add(value);
	}
	
	public HashEntry(K key)
	{
		this.key = key;
		values = new ArrayList <V> ();
	}
	
	public boolean equals(HashEntry <K, V> another)
	{
		return this.hashFunction() == another.hashFunction();
	}
	
	/**
	 * The hash fuction for the string.
	 * @return
	 */
	public int hashFunction()
	{
		String toHash = (String) key;
		
		int hashValue = 0;
		
		for (int Pos = 0; Pos < toHash.length(); Pos++)
		{
			hashValue = (hashValue << 4) + toHash.charAt(Pos);
			
			long hiBits = hashValue & 0xF0000000L;
			
			if (hiBits != 0)
			{
				hashValue ^= hiBits >> 24;
			}
			
			hashValue &= ~hiBits;
		}
		 
		return hashValue;
	}
	
	public K getKey()
	{
		return key;
	}
	
	public ArrayList <V> getValues()
	{
		return values;
	}
	
	public void addAnother(HashEntry <K, V> another)
	{
		values.add(another.getValues().get(0));
	}
	
	public int getSize()
	{
		return values.size();
	}
	
	public V removeFirst()
	{
		return values.remove(0);
	}
	
	public String toString()
	{
		String result = "[";
		result += key;
		result += ", [";
		
		int size = values.size();
		for(int i = 0; i < size; i++)
		{
			result += values.get(i);
			result += ",";
		}
		
		int length = result.length();
		result = result.substring(0, length - 1);
		result += "]]";
		return result;
	}
}