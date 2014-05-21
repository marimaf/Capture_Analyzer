import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;


class DirFilter implements FilenameFilter {
	private Pattern pattern;
	
	public DirFilter(String regex) {
		pattern = Pattern.compile(regex);
	}
	
	public boolean accept(File dir, String name) {
		// Strip path information, search for regex:
		return pattern.matcher(new File(name).getName()).matches();
	}
} 