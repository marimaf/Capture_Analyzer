/**
 * Clase que contiene los datos importantes de la detección de una mac
 * @author rodrigo
 *
 */
public class Capture 
{
	private String mac;
	private String t_first_detection;
	private String t_last_detection;
	private boolean isChekedByWeakFiler;
	private boolean isChekedByStrongFiler;
	
	public String get_mac()
	{
		return mac;
	}
	
	/**
	 * Podría darse que una mac pasa el strong filter en una captura y en otra no. Por eso permito actualizar su valor.
	 * @param b: Indica si pasó el strong filter en esta captura
	 */
	public void updateStrongFilter(boolean b)
	{
		isChekedByStrongFiler = isChekedByStrongFiler | b;
	}
	
	public void set_t_last_detection(String t_last_detection) {
		this.t_last_detection = t_last_detection;
	}

	public Capture(String data)
	{
		String[] aux = data.split("\t");
		this.mac = aux[0];
		this.t_first_detection = aux[1];
		this.t_last_detection = aux[2];
		this.isChekedByWeakFiler = aux[3].compareTo("true") == 0;
		this.isChekedByStrongFiler = aux[4].compareTo("true") == 0;
	}
	
	public Capture(String mac, String t_first_detection, String t_last_detection, boolean isChekedByWeakFiler, boolean isChekedByStrongFiler)
	{
		this.mac = mac;
		this.t_first_detection = t_first_detection;
		this.t_last_detection = t_last_detection;
		this.isChekedByWeakFiler = isChekedByWeakFiler;
		this.isChekedByStrongFiler = isChekedByStrongFiler;
	}
	
	@Override
	public String toString()
	{
		return mac + "\t" + t_first_detection + "\t" + t_last_detection + "\t" 
							+ isChekedByWeakFiler  + "\t" + isChekedByStrongFiler;
	}
}
