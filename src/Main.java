import java.io.File;
import java.io.IOException;
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
		// TODO Auto-generated method stub

		//El primer parámetro debe ser la ruta al archivo .cap
		/*
		String path_in = args[0];
		String path_out = args[1];
		*/
		String path_in = "./android.cap";
		String path_out = "./out.txt";
		
		//Llamo a tshark
		File output = new File(path_out);
		String[] cmd = {"tshark", "-r", path_in};
		Process p = new ProcessBuilder(cmd).redirectError(Redirect.INHERIT).redirectOutput(Redirect.to(output)).start();
		p.waitFor();
		
	}

}
