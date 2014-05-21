import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashSet;
import java.util.Hashtable;

/***
 * Esta clase es la encargada de decodificar archivos en formato cap
 * @author rodrigo
 *
 */
public class Analyzer {

	private Macs_companies macs_companies;
	
	public Analyzer() throws IOException
	{
		macs_companies = new Macs_companies(); 
	}
	
	/***
	 * Decodifica el paquete y deja los resultados en un archivo de salida.
	 * Solo considera las mac de origen como macs válidas.
	 * La primera línea del archivo de salida tiene los resultados para las 3 mediciones 
	 * en el formato: "no_filtro:weak:strong".
	 * Las siguientes líneas son las macs encontradas (sin filtrar)
	 * @param path_in: ruta formato cap
	 * @param path_out: ruta archivo donde dejo los resultados
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void run(String path_in, String path_out) throws IOException, InterruptedException
	{
		//Decodifico con tshark
		call_tshark(path_in, path_out);
		
		//Aquí guardo las macs detectadas mediante cada tipo de filtro
		Hashtable<filter, HashSet<String>> macs = new Hashtable<filter, HashSet<String>>();
		macs.put(filter.noFilter, new HashSet<String>());
		macs.put(filter.strongFilter, new HashSet<String>());
		macs.put(filter.weakFilter, new HashSet<String>());

		//Leo el archivo generado
		BufferedReader reader = new BufferedReader(new FileReader(path_out));
		String line = null;

		//Separo los paquetes
		int id_frame = 1;
		String mac = null;
		HashSet<filter> filters = new HashSet<filter>(); 

		while ((line = reader.readLine()) != null) {
			if(line.contains("Frame " + id_frame + ":"))
			{
				if(mac != null)
					for(filter f:filters)
						macs.get(f).add(mac);

				id_frame++;
				mac = null;
				filters.clear();
			}
			else
			{
				if(line.contains("Source address:") || line.contains("Transmitter address:"))
				{
					String s1 = line.replace(" ", "").split("\\(")[1];
					mac = s1.split("\\)")[0];

					//solo por ser detectada va al no filtro
					filters.add(filter.noFilter);

					//Si además es de una compañía reconocida va al weak
					if(macs_companies.isPhone(mac))
						filters.add(filter.weakFilter);
				}

				//Si tengo certeza de que es un teléfono lo agrego al strong filter
				if(line.contains("Category: Telephone"))
					filters.add(filter.strongFilter);
			}
		}

		reader.close();
		
		//Guardo resultados (primera línea: sin filtro;weak;strong
		String summary =  macs.get(filter.noFilter).size() + ";" + macs.get(filter.weakFilter).size() + ";" + macs.get(filter.strongFilter).size();  
		save(path_out, summary, macs.get(filter.noFilter));
	}

	/***
	 * Realiza llamada a tshark para decodificar el archivo cap.
	 * @param path_in: Ruta hasta archivo en formato cap 
	 * @param path_out: Ruta donde dejo resultados de tshark
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void call_tshark(String path_in, String path_out) throws InterruptedException, IOException
	{
		File output = new File(path_out);
		String[] cmd = {"tshark", "-r", path_in,"-V"};

		Process p = new ProcessBuilder(cmd).redirectError(Redirect.INHERIT).redirectOutput(Redirect.to(output)).start();
		p.waitFor();
	}
	
	/***
	 * Guarda los resultados del análisis
	 * @param path: Ruta al archivo de salida
	 * @param first_line: Primera línea del archivo, aquí dejo estadísticas globales (conteo macs con cada tipo de filtro)
	 * @param macs: Lista con el total de macs encontradas
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void save(String path, String first_line, HashSet<String> macs) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(path, "UTF-8");

		writer.write(first_line);
		for(String m:macs)
			writer.write("\n" + m);

		writer.close();
	}
}
