/**
 * This is the bufferPool of the system. Every time i try to search some record in the database, i first try to look through
 * this buffer pool to see if this pool contains a record with the same offset i am looking for. If it is, then i will simply
 * retrieve the gis record from the pool and bypass the disk(database) operation. If the record i am looking for is not in the pool
 * , then i will retrieve from the database, then i will insert that record into the pool for future use. This pool use LRU paradim
 * which put the most recently used record to the head of the pool. And the pool has a size limit of 10, if the number of records
 * in the pool reach beyond the pool i will simply remove the longest idle record in the pool.
 * 
 * @author weitao92
 *
 */
public class LRUpool {
	
	class Node
	{
		GISRecord record;
		Node next;
		Node prev;
		
		public Node(GISRecord record)
		{
			this.record = record;
			next = null;
			prev = null;
		}
		
		public String toString()
		{
			String result = new String();
			result += " ";
			result += record.offset;
			result += ": ";
			result += record.rawData;
			return result;
		}
	}
	
	static final int sizeLimit = 10;
	Node head;
	int currentSize;
	
	public LRUpool()
	{
		head = null;
		currentSize = 0;
	}
	
	/**
	 * Insert a record into the pool. Remove the longest idle record if reach the size limit.
	 * 
	 * @param record
	 */
	public void insert(GISRecord record)
	{
		Node node = searchInPool(record);
		
		if(node != null)
		{
			if(node == head)
			{
				
			}
			else if(node != head && node.next == null)
			{
				node.prev.next = null;
				node.prev = null;
				node.next = head;
				head.prev = node;
				head = node;
			}
			else
			{
				node.prev.next = node.next;
				node.next.prev = node.prev;
				node.prev = null;
				node.next = head;
				head.prev = node;
				head = node;
			}
		}
		else
		{
			Node newNode = new Node(record);
			
			if(currentSize == 0)
			{
				head = newNode;	
				currentSize++;
			}
			else
			{
				head.prev = newNode;
				newNode.next = head;
				head = newNode;
				currentSize++;
				
				if(currentSize > sizeLimit)
				{
					Node current = head;
					
					while(current.next != null)
					{
						current = current.next;
					}
					
					current.prev.next = null;
					current.prev = null;
					currentSize--;
				}
			}
		}
	}
	
	private Node searchInPool(GISRecord G)
	{
		if(currentSize == 0)
		{
			return null;
		}
		else
		{
			Node current = head;
			
			while(current != null)
			{
				if(current.record.getOffset().equals(G.getOffset()))
				{
					return current;
				}
				
				current = current.next;
			}
			
			return null;
		}
	}
	
	public boolean contains(Long offset)
	{
		Node current = head;
		while(current != null)
		{
			if(current.record.getOffset().equals(offset))
			{
				return true;
			}
			
			current = current.next;
		}
		
		return false;
	}
	
	public GISRecord retrieveGIS(Long offset)
	{
		Node current = head;
		while(current != null)
		{
			if(current.record.getOffset().equals(offset))
			{
				return current.record;
			}
			
			current = current.next;
		}
		
		return null;
	}
	
	public String display()
	{
		String result = new String();
		result += System.getProperty("line.separator");
		result += "MRU";
		result += System.getProperty("line.separator");
		
		Node current = head;
		
		while(current != null)
		{
			result += current.toString();
			result += System.getProperty("line.separator");
			current = current.next;
		}
		
		result += "LRU";
		return result;
	}
}
