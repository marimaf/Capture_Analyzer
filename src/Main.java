import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;


public class Main {

	/**
	 * @param args
	 * arg[0] ruta a archivo .cap
	 * arg[1] ruta archivo donde dejo resultados
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		
		//El primer parámetro debe ser la ruta al archivo .cap
		String path_in = args[0];
		String path_out = args[1];
		
		/*
		String path_in = "./Tablet_Copec_1_2014-05-08T15:41:05-04:00android.cap";
		String path_out = "./out.txt";
		*/

		Analyzer a = new Analyzer();
		a.run(path_in, path_out);

	}

	/***
	 * Método obsoleto para hacer el análisis de los paquetes.
	 * Guarda en un archivo la lista de macs encontradas en paquetes 802.11
	 * tanto los de inicio como los de destino
	 * @param args
	 * args[0] ruta a archivo .cap
	 * args[1] ruta archivo donde dejo resultados
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void oldMethod(String[] args) throws IOException, InterruptedException
	{
		//El primer parámetro debe ser la ruta al archivo .cap
		String path_in = args[0];
		String path_out = args[1];

		/*
		String path_in = "./Tablet_Copec_1_2014-05-08T10:40:25-04:00android.cap";
		String path_out = "./out.txt";
		 */

		//Llamo a tshark
		File output = new File(path_out);
		String[] cmd = {"tshark", "-r", path_in,"-o", "column.format:\"Source\",\"%s\",\"Destination\",\"%d\",\"Protocol\",\"%p\"","-Ttext"};

		for(String s:cmd)
			System.out.println(s);

		Process p = new ProcessBuilder(cmd).redirectError(Redirect.INHERIT).redirectOutput(Redirect.to(output)).start();
		p.waitFor();

		BufferedReader reader = new BufferedReader(new FileReader(path_out));
		String line = null;

		List<String> macAddress=new ArrayList<String>();

		while ((line = reader.readLine()) != null) {
			if(!line.endsWith("802.11"))
			{
				continue;
			}

			String n_line = line.replace("802.11", "");
			String[] parts = n_line.split(" -> ");

			if(parts.length == 2)
			{
				addmac(parts[0], macAddress);
				addmac(parts[1], macAddress);	
			}


		}

		reader.close();

		PrintWriter writer = new PrintWriter(path_out, "UTF-8");
		for(int i=0;i<macAddress.size();i++)
		{
			if(i > 0)
				writer.write("\n");
			writer.write(macAddress.get(i));				
		}

		writer.close();

	}

	public static void addmac(String mac_total, List<String> macAddress)
	{
		String mac =  mac_total.replace(" ", "").replace("(PA)", "").replace("(RA)", "").replace("(TA)", "");

		if(!mac.isEmpty() && !macAddress.contains(mac))
		{
			macAddress.add(mac);
		}
	}

}
