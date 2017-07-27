import java.util.LinkedList;

/***
 * A self-balanced binary search tree i use to store record's FIDs. I implemented the insertion, find and remove method of AVL tree.
 * But i will only use insertion to insert FID everytime i try to import and find method to see if the tree already contains this 
 * record.
 * 
 * @author weitao92
 *
 * @param <T>
 */
public class AVL <T extends Comparable <? super T>>
{
	class AVLNode
	{
		T elem;
		AVLNode left;
		AVLNode right;
		int height;
		
		AVLNode(T elem, AVLNode left, AVLNode right)
		{
			this.elem = elem;
			this.left = left;
			this.right = right;
			height = 0;
		}
		
		AVLNode(T elem)
		{
			this(elem, null, null);
		}
	}
	
	AVLNode root;
	int size;
	boolean isInserted = false;
	boolean isRemoved = false;
	
	public AVL()
	{
		root = null;
		size = 0;
	}
	
	public T find(T elem)
	{
		if(elem == null)
		{
			System.out.println("Please search something exist.");
			return null;
		}
		else
		{
			return findHelper(root, elem);
		}
	}

	private T findHelper(AVLNode sub, T elem)
	{
		if(sub == null)
		{
			return null;
		}
		else
		{
			int result = elem.compareTo(sub.elem);
			
			if(result == 0)
			{
				return sub.elem;
			}
			else if(result < 0)
			{
				return findHelper(sub.left, elem);
			}
			else
			{
				return findHelper(sub.right, elem);
			}
		}
	}
	
	public boolean insert(T elem)
	{
		if(elem == null)
		{
			System.out.println("Please insert something exist.");
			return false;
		}
		else
		{
			isInserted = false;
			root = insertHelper(root, elem);
			return isInserted;
		}
	}
	
	private AVLNode insertHelper(AVLNode sub, T elem)
	{
		if(sub == null)
		{
			AVLNode leaf = new AVLNode(elem);
			isInserted = true;
			return leaf;
		}
		else
		{
			int result = elem.compareTo(sub.elem);
			
			if(result == 0)
			{
				System.out.println("AVL tree doesn't allow duplicate elements.");
			}
			else if(result < 0)
			{
				sub.left = insertHelper(sub.left, elem);
			}
			else
			{
				sub.right = insertHelper(sub.right, elem);
			}
			
			return balance(sub);
		}
	}
	
	private AVLNode balance(AVLNode sub)
	{
		int result = height(sub.left) - height(sub.right);
		
		if(result >= 2)
		{
			if(height(sub.left.left) >= height(sub.left.right))
			{
				sub = singleRotateLeft(sub);
			}
			else
			{
				sub = doubleRotateLeft(sub);
			}
		}
		else if(result <= -2)
		{
			if(height(sub.right.right) >= height(sub.right.left))
			{
				sub = singleRotateRight(sub);
			}
			else
			{
				sub = doubleRotateRight(sub);
			}
		}
		
		return updateHeight(sub);
	}
	
	public boolean remove(T elem)
	{
		if(elem == null)
		{
			System.out.println("Please try to remove something exist.");
			return false;
		}
		else
		{
			isRemoved = false;
			root = removeHelper(root, elem);
			return isRemoved;
		}
	}
	
	private AVLNode removeHelper(AVLNode sub, T elem)
	{
		if(sub == null)
		{
			return null;
		}
		else
		{
			int result = elem.compareTo(sub.elem);
			
			if(result < 0)
			{
				sub.left = removeHelper(sub.left, elem);
			}
			else if(result > 0)
			{
				sub.right = removeHelper(sub.right, elem);
			}
			else
			{
				if(sub.left == null && sub.right == null)
				{
					return null;
				}
				else if(sub.left != null && sub.right != null)
				{
					sub.elem = findMin(sub.right).elem;
					sub.right = removeHelper(sub.right, sub.elem);
				}
				else
				{
					return sub.left == null ? sub.right : sub.left;
				}
			}
			
			return balance(sub);
		}
	}
	
	private AVLNode findMin(AVLNode sub)
	{
		if(sub.left == null)
		{
			return sub;
		}
		else
		{
			return findMin(sub.left);
		}
	}
	
	private AVLNode singleRotateLeft(AVLNode sub)
	{
		AVLNode temp = sub.left;
		sub.left = temp.right;
		temp.right = sub;
		sub = updateHeight(sub);
		temp = updateHeight(temp);
		return temp;
	}
	
	private AVLNode singleRotateRight(AVLNode sub)
	{
		AVLNode temp = sub.right;
		sub.right = temp.left;
		temp.left = sub;
		sub = updateHeight(sub);
		temp = updateHeight(temp);
		return temp;
	}
	
	private AVLNode doubleRotateLeft(AVLNode sub)
	{
		sub.left = singleRotateRight(sub.left);
		return singleRotateLeft(sub);
	}
	
	private AVLNode doubleRotateRight(AVLNode sub)
	{
		sub.right = singleRotateLeft(sub.right);
		return singleRotateRight(sub);
	}
	
	private int height(AVLNode node)
	{
		return node == null ? -1 : node.height;
	}
	
	private AVLNode updateHeight(AVLNode node)
	{
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		return node;
	}
	
	public void display()
	 {
		 textDisplay(root);
		 System.out.println("");
		 System.out.println("");
		 visualDisplay(root);
		 System.out.println("");
	 }
	 
	 private void textDisplay(AVLNode subRoot)
	 {
		 if(subRoot != null)
		 {
			 textDisplay(subRoot.left);
			 System.out.print(subRoot.elem + " ");
			 textDisplay(subRoot.right);
		 }
	 }
	 
	 private void visualDisplay(AVLNode root)
	 {
		 if(root == null)
		 {
			 System.out.println("AVL Tree is empty now.");
		 }
		 else
		 {
			 LinkedList <AVLNode> queue = new LinkedList <AVLNode> ();
			 LinkedList <AVLNode> rowQueue = new LinkedList <AVLNode> ();
			 
			 queue.addLast(root);
			 
			 while(!queue.isEmpty())
			 {
				 
				 while(!queue.isEmpty())
				 {
					 rowQueue.addLast(queue.removeFirst());
				 }
				 
				 while(!rowQueue.isEmpty())
				 {
					 AVLNode node = rowQueue.removeFirst();
					 
					 System.out.print(node.elem + "(" + node.height + ") ");
					 
					 if(node.left != null)
					 {
						 queue.addLast(node.left);
					 }
					 if(node.right != null)
					 {
						 queue.addLast(node.right);
					 }
				 }
				 
				 System.out.println("");
			 }
		 }
		 
	 }
}
