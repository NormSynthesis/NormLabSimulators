package es.csic.iiia.normlab.onlinecomm.batch;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author "Javier Morales (jmorales@iiia.csic.es)"
 *
 */
public class PopulationSwitcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String populationURL = args[0];
		String destiny = null;

		if (args.length <= 1){
			destiny = "files/onlinecomm/populations/population.xml";
		}else{
			destiny = args[1];
		}
		
		File origen = new File(populationURL);
		File destino = new File(destiny);
				
		InputStream in;
		OutputStream out;
		try {
			in = new FileInputStream(origen);
			out = new FileOutputStream(destino);
			byte[] buf = new byte[1024];
			int len;
		 
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			System.out.println("POPULATION COPIED....");
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
