import java.util.ArrayList;
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
		String[] cmd = {"tshark", "-r", path_in,"-o", "column.format:""Source","%s","Destination","%d","Protocol","%p""","-Ttext"};
		Process p = new ProcessBuilder(cmd).redirectError(Redirect.INHERIT).redirectOutput(Redirect.to(output)).start();
		p.waitFor();
		
		BufferedReader reader = new BufferedReader(new FileReader("out.txt"));
		String line = null;
		
		List<String> macAddress=new ArrayList<String>();

		int initialPos1=0;
		int finalPos1;
		int initialPos2;
		int finalPos2;
		String aux;
		while ((line = reader.readLine()) != null) {
		    if(!line.endsWith("802.11"))
		    {
		    	continue;
		    }
		    finalPos1=line.indexOf("->");
		    if(!line.substring(initialPos1,finalPos1-1).isEmpty()&&!macAddress.contains(line.substring(initialPos1,finalPos1-1))&&line.equalsIgnoreCase("            "))
		    {
		    	aux=line.substring(initialPos1,finalPos1-1);
		    	if(aux.endsWith(")")){
		    		aux=aux.substring(0, aux.indexOf("(")-1);
		    		if(macAddress.contains(aux)){
		    			continue;		    			
		    		}
		    	}
		    	
		    	macAddress.add(aux);
		    }
		    
		    initialPos2=line.indexOf('>')+2;
		    finalPos2=line.indexOf("802.11");

		    if(!macAddress.contains(line.substring(initialPos2,finalPos2-1)))
		    {
		    	aux=line.substring(initialPos2,finalPos2-1);
		    	if(aux.endsWith(")")){
		    		aux=aux.substring(0, aux.indexOf("(")-1);
		    		if(macAddress.contains(aux)){
		    			continue;		    			
		    		}
		    	}
		    	
		    	
		    	macAddress.add(aux);
		    	
		    }

		
		}

		reader.close();
		
		PrintWriter writer = new PrintWriter(args[1], "UTF-8");
		for(int i=0;i<macAddress.size();i++)
		{

			writer.write(macAddress.get(i)+"\n");

		}

		writer.close();
		
	}

}
