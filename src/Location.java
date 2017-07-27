import java.util.ArrayList;

/**
 * This class represent a location in the prQuadTree, it has a pair of coordinates and a list of offsets which points to different
 * record with same coordinates.
 * 
 * @author weitao92
 *
 */
public class Location implements Compare2D<Location>
{
	private long xCord;
	private long yCord;
	
	ArrayList <Long> offsets;
	
	public Location(long x, long y)
	{
		xCord = x;
		yCord = y;
		offsets = new ArrayList <Long> ();
	}
	
	public Location(long x, long y, long offset)
	{
		this(x, y);
		offsets.add(offset);
	}
	
	public void set(long x, long y)
	{
		xCord = x;
		yCord = y;
	}
	
	@Override
	public long getX()
	{
		return xCord;
	}

	@Override
	public long getY()
	{
		return yCord;
	}

	@Override
	public Direction directionFrom(long X, long Y)
	{
		long length = xCord - X;
		long height = yCord - Y;
		
		if(length == 0 && height == 0)
		{
			return Direction.NOQUADRANT;
		}
		else if(length > 0 && height >= 0)
		{
			return Direction.NE;
		}
		else if(length <= 0 && height > 0)
		{
			return Direction.NW;
		}
		else if(length < 0 && height <= 0)
		{
			return Direction.SW;
		}
		else
		{
			return Direction.SE;
		}		
	}

	@Override
	public Direction inQuadrant(long xLo, long xHi, long yLo, long yHi)
	{
		long originX = (long) ((xHi + xLo) / 2);
		long originY = (long) ((yHi + yLo) / 2);
		
		if(xCord > xHi || xCord < xLo || yCord > yHi || yCord < yLo)
		{
			return Direction.NOQUADRANT;
		}
		else if(((xCord > originX && xCord <= xHi) && (yCord >= originY && yCord <= yHi)) 
				|| (xCord == originX && yCord == originY))
		{
			return Direction.NE;
		}
		else if((xCord >= xLo && xCord <= originX) && (yCord <= yHi && yCord > originY))
		{
			return Direction.NW;
		}
		else if((xCord >= xLo && xCord < originX) && (yCord <= originY && yCord >= yLo))
		{
			return Direction.SW;
		}
		else
		{
			return Direction.SE;
		}	
	}

	@Override
	public boolean inBox(long xLo, long xHi, long yLo, long yHi)
	{
		return  ((xLo <= xCord && xCord <= xHi) && (yLo <= yCord && yCord <= yHi));
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Location)
		{
			return (((Location) o).getX() == xCord) && (((Location) o).getY() == yCord);
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		String result = "(" + xCord + "," + yCord + "," + " offsets: ";
		
		int size = offsets.size();
		for(int i = 0; i < size; i++)
		{
			result += offsets.get(i).toString();
			result += ",";
		}
		
		int length = result.length();
		result = result.substring(0, length - 1);
		result += ")";
		return result;
	}
	
	public void insert(long offset)
	{
		offsets.add(offset);
	}
	
	public ArrayList <Long> getAll()
	{
		return offsets;
	}
}