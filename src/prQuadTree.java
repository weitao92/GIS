import java.util.ArrayList;

/**
 * This is the prQuadTree data structure which i use to store geographic locations. This prQuadTree use bucket with size of 4
 * to store locations.
 * 
 * @author weitao92
 *
 * @param <T>
 */
public class prQuadTree < T extends Compare2D <? super T> >
{
	abstract class prQuadNode
	{		
		public prQuadNode()
		{
			
		}
	}
	
	/**
	 * prQuadLeaf which use to contain locations, each leaf has an arraylist(bucket) which can store up to 4 locations.
	 * 
	 * @author weitao92
	 *
	 */
	class prQuadLeaf extends prQuadNode
	{
		public prQuadLeaf(T x)
		{
			Elements = new ArrayList <T>();
			Elements.add(0, x);
		}
		
		public prQuadLeaf()
		{
			Elements = new ArrayList <T>();
		}
		
		public void insert(T x)
		{
			Elements.add(x);
		}
		
		/**
		public T get()
		{
			return Elements.get(0);
		}
		**/
		
		public boolean contains(T elem)
		{
			int size = Elements.size();
			for(int i = 0; i < size; i++)
			{
				T element = Elements.get(i);		
				
				if(element.equals(elem))
				{
					return true;
				}
			}
			
			return false;
		}
		
		public T get(T elem)
		{
			int size = Elements.size();
			for(int i = 0; i < size; i++)
			{
				if(Elements.get(i).equals(elem))
				{
					return Elements.get(i);
				}
			}
			
			return null;
		}
		
		public T get(int index)
		{
			return Elements.get(index);
		}
		
		ArrayList<T> Elements;
	}
	
	/**
	 * This is the prQuadInternal which i use to represent the boundry area, each internal node contains 4 sub-nodes each
	 * represents a specific quadrant region.
	 * 
	 * @author weitao92
	 *
	 */
	class prQuadInternal extends prQuadNode
	{
		
		public prQuadInternal()
		{
			NE = null;
			NW = null;
			SW = null;
			SE = null;
		}

		prQuadNode NW, NE, SE, SW;
	}
	
	prQuadNode root;
	boolean isInserted = false;
	boolean isRemoved = false;
	long xMin, xMax, yMin, yMax;
	StringBuilder outString = new StringBuilder("");
	StringBuilder pad = new StringBuilder("---");
	int BUCKETSIZE = 4;
	ArrayList <T> failed = new ArrayList <T> ();

	// Initialize QuadTree to empty state, representing the specified region.
	public prQuadTree(long xMin, long xMax, long yMin, long yMax)
	{
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		root = null;
	}

	// Pre: elem != null
	// Post: If elem lies within the tree's region, and elem is not already
	// present in the tree, elem has been inserted into the tree.
	// Return true iff elem is inserted into the tree.
	public boolean insert(T elem)
	{
		if(elem == null)
		{
			return false;
		}
		else if(!elem.inBox(xMin, xMax, yMin, yMax))
		{
			return false;
		}
		else
		{
			isInserted = false;
			root = insertHelper(root, elem, xMin, xMax, yMin, yMax);
			if(isInserted == false)
			{
				failed.add(elem);
			}
			return isInserted;
		}
	}
	
	/**
	 * private helper method which insert location into the quadTree recursively.
	 * 
	 * @param sub
	 * @param elem
	 * @param xl
	 * @param xh
	 * @param yl
	 * @param yh
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private prQuadNode insertHelper(prQuadNode sub, T elem, long xl, long xh, long yl, long yh)
	{
		if(sub == null)
		{
			prQuadLeaf leaf = new prQuadLeaf(elem);
			isInserted = true;
			return leaf;
		}
		else
		{
			if(sub instanceof prQuadTree.prQuadInternal)
			{
				prQuadInternal internal = (prQuadTree.prQuadInternal) sub;
				Direction direction = elem.inQuadrant(xl, xh, yl, yh);
				
				switch (direction)
				{
					case NE:
						internal.NE = insertHelper(internal.NE, elem, (xl+xh)/2, xh, (yl+yh)/2, yh);
						break;	
					case NW:
						internal.NW = insertHelper(internal.NW, elem, xl, (xl+xh)/2, (yl+yh)/2+1, yh);
						break;
					case SW:
						internal.SW = insertHelper(internal.SW, elem, xl, (xl+xh)/2-1, yl, (yl+yh)/2);
						break;
					case SE:
						internal.SE = insertHelper(internal.SE, elem, (xl+xh)/2, xh, yl, (yl+yh)/2-1);
						break;
					default:
						break;
				}
				
				return internal;
			}
			else
			{
				prQuadLeaf leaf = (prQuadTree.prQuadLeaf) sub;
				
				if(leaf.contains(elem))
				{
					isInserted = true;
					leaf.get(elem).insert(elem.getAll().get(0));
					return leaf;
				}
				else
				{
					if(leaf.Elements.size() == BUCKETSIZE)
					{
						prQuadNode internal = partition(leaf, xl, xh, yl, yh);
						internal = insertHelper(internal, elem, xl, xh, yl, yh);
						return internal;
					}
					else
					{
						leaf.Elements.add(elem);
						isInserted = true;
						return leaf;
					}
				}
			}
		}
	}
	
	/**
	 * Whenever a leaf contains more than 4 locations, i will use this method to partition the leaf into an internal node.
	 * 
	 * @param sub
	 * @param xl
	 * @param xh
	 * @param yl
	 * @param yh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private prQuadNode partition(prQuadNode sub, long xl, long xh, long yl, long yh)
	{
		prQuadLeaf leaf = (prQuadTree.prQuadLeaf) sub;
		prQuadNode internal = new prQuadInternal();
		T elem1 = leaf.get(0);
		T elem2 = leaf.get(1);
		T elem3 = leaf.get(2);
		T elem4 = leaf.get(3);
		
		internal = insertHelper(internal, elem1, xl, xh, yl, yh);
		internal = insertHelper(internal, elem2, xl, xh, yl, yh);
		internal = insertHelper(internal, elem3, xl, xh, yl, yh);
		internal = insertHelper(internal, elem4, xl, xh, yl, yh);
		return internal;
	}
	
	// Pre: elem != null
	// Returns reference to an element x within the tree such that
	// elem.equals(x)is true, provided such a matching element occurs within
	// the tree; returns null otherwise.
	public T find(T elem)
	{
		if(elem == null)
		{
			return null;
		}
		else if(!elem.inBox(xMin, xMax, yMin, yMax))
		{
			return null;
		}
		else
		{
			return findHelper(root, elem, xMin, xMax, yMin, yMax);
		}
	}
	
	/**
	 * private helper method which find location in the tree recursively.
	 * 
	 * @param sub
	 * @param elem
	 * @param xl
	 * @param xh
	 * @param yl
	 * @param yh
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private T findHelper(prQuadNode sub, T elem, long xl, long xh, long yl, long yh)
	{
		if(sub == null)
		{
			return null;
		}
		else
		{
			if(sub instanceof prQuadTree.prQuadLeaf)
			{
				prQuadLeaf leaf = (prQuadTree.prQuadLeaf) sub;
				if(leaf.contains(elem))
				{
					return leaf.get(elem);
				}
				else
				{
					return null;
				}
			}
			else
			{
				prQuadInternal internal = (prQuadTree.prQuadInternal) sub;
				Direction direction = elem.inQuadrant(xl, xh, yl, yh);
				
				switch (direction)
				{
					case NE:
						return findHelper(internal.NE, elem, (xl+xh)/2, xh, (yl+yh)/2, yh);	
					case NW:
						return findHelper(internal.NW, elem, xl, (xl+xh)/2, (yl+yh)/2+1, yh);
					case SW:
						return findHelper(internal.SW, elem, xl, (xl+xh)/2-1, yl, (yl+yh)/2);
					case SE:
						return findHelper(internal.SE, elem, (xl+xh)/2, xh, yl, (yl+yh)/2-1);
					default:
						return null;
				}
			}
		}
	}
	
	// Pre: xLo, xHi, yLo and yHi define a rectangular region
	// Returns a collection of (references to) all elements x such that x is
	//in the tree and x lies at coordinates within the defined rectangular
	// region, including the boundary of the region.
	public ArrayList<T> find(long xLo, long xHi, long yLo, long yHi)
	{
		ArrayList<T> ArrayList = new ArrayList<T> ();
		areaSearch(root, xLo, xHi, yLo, yHi, xMin, xMax, yMin, yMax, ArrayList);
		return ArrayList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void areaSearch(prQuadNode sub, long xl1, long xh1, long yl1, long yh1, 
			long xl2, long xh2, long yl2, long yh2, ArrayList<T> ArrayList)
	{
		if(sub == null)
		{
			return;
		}
		else
		{
			if(sub instanceof prQuadTree.prQuadLeaf)
			{
				prQuadLeaf leaf = (prQuadTree.prQuadLeaf) sub;
				int size = leaf.Elements.size();
				
				for(int i = 0; i < size; i++)
				{
					T elem = leaf.get(i);
					
					
						if(elem.inBox(xl1, xh1, yl1, yh1))
						{
							ArrayList.add(elem);
						}
					
				}
				
				return;
			}
			else
			{
				prQuadInternal internal = (prQuadTree.prQuadInternal) sub;
				
				if((xl2 <= xh1 && xh2 >= xl1) && (yl2 <= yh1 && yh2 >= yl1))
				{
					if(((xl2+xh2)/2 <= xh1 && xh2 >= xl1) && ((yl2+yh2)/2 <= yh1 && yh2 >= yl1))
					{
						areaSearch(internal.NE, xl1, xh1, yl1, yh1, (xl2+xh2)/2, xh2, (yl2+yh2)/2, yh2, ArrayList);
					}
					
					if((xl2 <= xh1 && (xl2+xh2)/2 >= xl1) && ((yl2+yh2)/2+1 <= yh1 && yh2 >= yl1))
					{
						areaSearch(internal.NW, xl1, xh1, yl1, yh1, xl2, (xl2+xh2)/2, (yl2+yh2)/2+1, yh2, ArrayList);
					}
					
					if((xl2 <= xh1 && (xl2+xh2)/2-1 >= xl1) && (yl2 <= yh1 && (yl2+yh2)/2 >= yl1))
					{
						areaSearch(internal.SW, xl1, xh1, yl1, yh1, xl2, (xl2+xh2)/2-1, yl2, (yl2+yh2)/2, ArrayList);
					}
					
					if(((xl2+xh2)/2 <= xh1 && xh2 >= xl1) && (yl2 <= yh1 && (yl2+yh2)/2-1 >= yl1))
					{
						areaSearch(internal.SE, xl1, xh1, yl1, yh1, (xl2+xh2)/2, xh2, yl2, (yl2+yh2)/2-1, ArrayList);
					}
				}
				
				return;
			}
		}
	}
	
	public StringBuilder display()
	{
    	
        if (root == null )
        {
            outString = new StringBuilder("  Empty tree.\n");
        }
        else
        {
        	 printTreeHelper(root, "");
        }
        
        StringBuilder result = outString;
        outString = new StringBuilder("");
    	pad = new StringBuilder("---");
    	return result;
	}

    /**
     * Recursive function used to traverse the tree and add its contents to outString which can be printed
     * into a file
     * 
     * @param sRoot - current node
     * @param Padding - padding used during each recursive call
     */
    @SuppressWarnings("unchecked")
	public void printTreeHelper(prQuadNode sRoot, String Padding) {

        	// Check for empty leaf
           if ( sRoot == null ) {
              outString = outString.append(" ");
              outString.append(Padding);
              outString.append("*\n");
              return;
           }
           // Check for and process SW and SE subtrees
           if ( sRoot instanceof prQuadTree.prQuadInternal ) {
              prQuadInternal p = (prQuadInternal) sRoot;
              printTreeHelper(p.SW, Padding + pad);
              printTreeHelper(p.SE, Padding + pad);
           }
   
           // Determine if at leaf or internal and display accordingly
           if ( sRoot instanceof prQuadTree.prQuadLeaf ) {
              prQuadLeaf p = (prQuadLeaf) sRoot;
              outString = outString.append(Padding);
              for (int pos = 0; pos < p.Elements.size(); pos++) { 
            	 outString.append("[");
            	 outString.append(p.Elements.get(pos));
            	 outString.append("]");
              }
              outString.append("\n");
           }
           else if ( sRoot instanceof prQuadTree.prQuadInternal )
           {
        	    outString.append(Padding);
           		outString.append("@\n");
           }

           // Check for and process NE and NW subtrees
           if (sRoot instanceof prQuadTree.prQuadInternal ) {
              prQuadInternal p = (prQuadInternal) sRoot;
              printTreeHelper( p.NE, Padding + pad);
              printTreeHelper( p.NW, Padding + pad);
           }
     }	
}