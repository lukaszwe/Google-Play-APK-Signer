/**
 * @author <a href="mailto:wegrowski.lukasz@gmail.com">Lukasz Wegrowski</a>
 * 
 * */

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class FileTypeFilter extends FileFilter {

	private String extension;
    private String description;
 
    public FileTypeFilter(String extension, String description) {
    	this.extension = extension;
        this.description = description;
    }
	
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
            return true;
        }
        return file.getName().endsWith(extension);
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description + String.format(" (*%s)", extension);
	}

	

}
