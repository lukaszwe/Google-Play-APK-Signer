/**
 * @author <a href="mailto:wegrowski.lukasz@gmail.com">Lukasz Wegrowski</a>
 * 
 * */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Controller {

	static PrintWriter writer;
	
	public static int jarsigner(String jarsignerPath, String keyStorePath, String storePass, String unsignedApkPath, String aliasName){
		
		Process p;
		ProcessBuilder pb = new ProcessBuilder(
												jarsignerPath,"-verbose",
												"-keystore",keyStorePath,"-storepass",
												storePass,unsignedApkPath,aliasName
												);
		
		try {
			writer = new PrintWriter("Logs.txt","UTF-8");//new FileWriter("Logs.txt",true));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			p = pb.start();	
			BufferedReader reader = 
	                new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ( (line = reader.readLine()) != null) {
			   builder.append(line);
			   builder.append(System.getProperty("line.separator"));
			} 
			String result = builder.toString();
			
			//SAVE TO LOG
			System.out.println(result);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
			
			writer.append("\n>>>>>>>> " + dateFormat.format(date) + " <<<<<<<<\n");
			writer.append("--------JARSIGNER-------\n");
			writer.append(result);
			writer.append("\n------------------------\n");
			writer.close();
			//System.out.println("Process exists with value: " + p.exitValue());
			
			return p.exitValue();//result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public static int zipAlign(String zipAlignPath, int vFlag, String unalignedApkPath, String outputApkPath){
		Process p;
		ProcessBuilder pb = new ProcessBuilder(
												zipAlignPath,
												"-v",String.valueOf(vFlag),
												unalignedApkPath,
												outputApkPath
												);
		
		try {
			writer = new PrintWriter(new FileWriter("Logs.txt",true));//"Logs.txt", "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			p = pb.start();
			BufferedReader reader = 
	                new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ( (line = reader.readLine()) != null) {
			   builder.append(line);
			   builder.append(System.getProperty("line.separator"));
			} 
			
			//SAVE TO LOG
			System.out.println(builder.toString());
			writer.append("\n--------ZIPALIGN--------\n");
			writer.append(builder.toString());
			writer.append("\n------------------------\n");
			writer.close();
		
			return p.exitValue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	
}
