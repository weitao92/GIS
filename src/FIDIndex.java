/**
 * This is the class which i use to determine whether i have already inserted the same record into the hashtable or prQuadtree.
 * You see same records has same FID, you can have same name or same coordinate but still be different locations. But when you have
 * different FID that means these two locations are different. So everytime i import one record, i import it's FID into the class 
 * which contains an AVL tree to store the FID. Then everytime before i import, i search through the AVL tree to see if i have 
 * already inserted this record. If its true, then i will simply disregard that record.
 * @author weitao92
 *
 */
public class FIDIndex
{
	AVL <Long> tree;
	
	public FIDIndex()
	{
		tree = new AVL <Long> ();
	}
	
	public boolean contains(GISRecord record)
	{
		return tree.find(record.getFID()) != null;
	}
	
	public void importFID(GISRecord record)
	{
		tree.insert(record.getFID());
	}

}
