import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class is simply a writer to the log file. I will use it in controller to write every information to the log file.
 * @author weitao92
 *
 */
public class logWriter
{
	RandomAccessFile random;
	FileWriter writer;
	
	public void setUp(File file)
	{
		try {
			random = new RandomAccessFile(file, "rw");
			try {
				writer = new FileWriter(file);
				writer.write("");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String content)
	{
		try {
			random.writeBytes(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try {
			random.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
