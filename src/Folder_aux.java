import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static java.nio.file.StandardCopyOption.*;

/***
 * Fachada para manejo del sistema de archivos
 * @author rodrigo
 *
 */
public class Folder_aux {
	
	/***
	 * Elimina un directorio
	 * @param path
	 */
	public static void delete_directory(String path)
    {
    	File theDir = new File(path);
    	if (theDir.exists())
    		theDir.delete();
    }
	
	/***
	 * Mueve un archivo a otra carpeta
	 * @param file
	 * @param destiny
	 * @throws IOException
	 */
	public static void move_file(String file, String destiny) throws IOException
	{
		Path source = (new File(file)).toPath();
		destiny += source.getFileName().toString(); 
		Path target = (new File(destiny)).toPath();
		Files.move(source, target, REPLACE_EXISTING);
	}
	
	/***
	 * Indica si existe un directorio
	 * @param path
	 * @return
	 */
	public static boolean exists_directory(String path)
    {
    	return (new File(path)).exists();
    }
	
	/***
	 * Elimina todos los archivos presentes en una carpeta
	 * @param path
	 */
	public static void clean_directory(String path)
    {
    	File folder = new File(path);
    	if(folder.exists())
    	{
    		File[] files = folder.listFiles();
    	    if(files!=null) { //some JVMs return null for empty dirs
    	        for(File f: files)
    	            f.delete();
    	    }
    	}
    }
	
	/***
	 * Obtiene todos los subdirectorios de la carpeta
	 * @param folder
	 * @return
	 */
	public static String[] getSubDirectories(String folder)
	{
		File file = new File(folder);
		String[] directories = file.list(new FilenameFilter() {
		  @Override
		  public boolean accept(File dir, String name) {
		    return new File(dir, name).isDirectory();
		  }
		});
		return directories;
	}
	
	/***
	 * Obtiene los archivos dentro de la carpeta
	 * @param folder: Ruta hasta la carpeta 
	 * @param extension: Tipo de archivo, ejemplo: ".*\\.txt"
	 * @return
	 */
	public static String[] getFiles(String folder, String extension)
	{
		File path_f = new File(folder);
		return path_f.list(new DirFilter(extension));
		
	}
	
	/***
	 * Crea una nueva carpeta
	 * @param path
	 */
	public static void create_directory(String path)
    {
    	File theDir = new File(path);
    	// if the directory does not exist, create it
    	if (!theDir.exists())
    		theDir.mkdir();
    }
	
	/***
	 * Lee un archivo
	 * @param path: Ruta hasta al archivo que se quiere leer
	 * @return Lista con cada una de las líneas que contenía el archivo
	 * @throws IOException
	 */
	public static List<String> read_file(String path) throws IOException
	{
		List<String> ret = new ArrayList<String>();
		
		BufferedReader in = new BufferedReader(new FileReader(path));
		String l;
		while((l = in.readLine())!= null)
			ret.add(l);
		
		in.close();
		
		return ret;
	}
}
