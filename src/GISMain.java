/**
 * This is the starter of the system. You simply run the main method and give the three files as parameters then the entire system
 * will run automatically. It contains a controller class which will excute the program by itself.
 * 
 * @author weitao92
 *
 */
public class GISMain
{
	static Controller control;
	
	public static void main(String args[])
	{
		control = new Controller(args[0], args[1], args[2]);
		control.execute();
	}
}
