import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/***
 * Clase usada para el 'weak filter'. Maneja una lista de macs que pertenecen a 
 * empresas relacionadas con celulares como Apple, Samsung y Nokia
 * @author rodrigo
 *
 */
public class Macs_companies {

	private String file_companies;
	private String file_macs;
	private HashSet<String> companies;
	private HashSet<String> macs;
	
	public Macs_companies() throws IOException
	{
		//Archivos son referentes al classpath
		String root = System.getProperty("java.class.path");
		file_companies = root + "/../mac_companies/companies.txt";
		file_macs = root + "/../mac_companies/macs.txt";
		
		companies = new HashSet<String>();
		macs = new HashSet<String>();
		
		//Leo las compañías
		List<String> comps = Folder_aux.read_file(file_companies);
		for(String c:comps)
			companies.add(c);
		
		//Leo las macs y guardo solo las con compañías útiles
		List<String> macs_comps = Folder_aux.read_file(file_macs);
		
		for(String s:macs_comps)
		{
			String[] aux = s.split("\t");
			String mac = aux[0].trim().toLowerCase();
			String comp = aux[1].split("#")[0].trim();
			if(companies.contains(comp))
				macs.add(mac);
		}
		
	}
	
	/***
	 * Indica si una mac pertenece a una compañía que produce celulares
	 * @param mac: mac determinada que se quiere analizar 
	 * @return true si se trata de un celular
	 */
	public boolean isPhone(String mac)
	{
		String[] aux = mac.split(":");
		return macs.contains(aux[0] + ":" + aux[1] + ":" + aux[2]);
	}
	
}
