/**
 * @author <a href="mailto:wegrowski.lukasz@gmail.com">Lukasz Wegrowski</a>
 * 
 * */

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


public class APKSigner {

	private JFrame frame;
	private final CardLayout cl = new CardLayout();;
	private final JPanel mainPanel = new JPanel();
	private final JPanel settingsPanel = new JPanel();
	final private JFileChooser fc = new JFileChooser();
	private Properties prop = new Properties();
	private InputStream fileInput;
	private OutputStream fileOutput;
	private boolean firstLaunch = false;
	
	//Signing
	private String jarSigner;
	private String zipAlign;
	private String keyStore;
	private String apkLocation;
	private char[] password; 
	private String outputFileName;
	private String alias;
	private String vFlag;
	
	//Main Board:
	private JTextField mTextField_jarsigner;
	private JTextField mTextField_zipalign;
	private JTextField mTextField_keystore;
	private JTextField mTextField_apklocation;
	private JPasswordField mPasswordField;
	private JTextField mTextField_outputFileName;
	
	private JButton mBtnSettings;
	private JLabel mLblJarsignerPath;
	private JLabel mLblZipalignPath;
	private JLabel mLblKeystore;
	private JLabel mLblApkLocation; 
	private JLabel mLblPassword;
	private JLabel mLblOutputFilename;
	private JLabel mLblOutput;
	
	private JButton mBtnBrowse_jarsigner;
	private JButton mBtnBrowse_zipalign;
	private JButton mBtnBrowse_keystore;
	private JButton mBtnBrowse_apkLocation;
	private JButton mBtnSign;
	private JTextArea mTextArea_console_output;
	
	//Settings Board:
	private JTextField sTextField_jarsigner;
	private JTextField sTextField_zipalign;
	private JTextField sTextField_keystore;
	private JPasswordField sPasswordField;
	
	private JLabel sLabelJarsignerPath;
	private JLabel sLabelZipalignPath;
	private JLabel sLblKeystore;
	private JLabel sLblPassword;
	
	private JButton sBtnBrowse_jarsigner;
	private JButton sBtnBrowse_zipalign;
	private JButton sBtnBrowse_keystore;
	private JButton sBtnSave;
	private JButton sBtnCancel;
	
	private JLabel sLblVFlag;
	private JTextField sTextField_vFlag;
	private JLabel sLblAlias;
	private JTextField sTextField_alias;
	
	private int labelPaddingLeft = 8;
	private int textViewPaddingLeft = 6;
	private int textViewWidth = 135;

	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					APKSigner window = new APKSigner();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void reloadValues(){
		jarSigner = mTextField_jarsigner.getText();
		zipAlign = mTextField_zipalign.getText();
		keyStore = mTextField_keystore.getText();
		password = mPasswordField.getPassword();
		apkLocation = mTextField_apklocation.getText();
		alias = sTextField_alias.getText();
		vFlag = sTextField_vFlag.getText();
		outputFileName = mTextField_outputFileName.getText();
	}
	
	public void sign(){
		int result;
		mTextArea_console_output.setText(null);
		reloadValues();
		
		if( ( jarSigner == null) || 
				(zipAlign == null)|| 
				(keyStore == null)|| 
				(password == null)|| 
				(apkLocation == null)|| 
				(alias == null)||  
				(vFlag == null)|| 
				(outputFileName == null) 
				){
			
			JOptionPane.showMessageDialog(null, "Fill all fields (also settings)");
			
		} else {
			
			result = Controller.jarsigner(jarSigner, keyStore, new String(password), apkLocation, alias);
			if(result == -1){
				mTextArea_console_output.append("Jarsigner: \nReturned -1 IOException\n");
			} else if(result == 0){
				mTextArea_console_output.append("Jarsigner OK...\n");
			} else {
				mTextArea_console_output.append("Something went wrong...\nJarsigner returned: " + result +"\nCheck Logs" );
			}
			
		//	System.out.println(result);
		//	mTextArea_console_output.append(result);
			result = Controller.zipAlign(zipAlign, Integer.valueOf( vFlag ), apkLocation, outputFileName);
			if(result == -1){
				mTextArea_console_output.append("Zipalign: \nReturned -1 IOException\n");
			} else if(result == 0){
				mTextArea_console_output.append("Zipalign OK...\n");
			} else {
				mTextArea_console_output.append("Something went wrong...\nZipalign returned: " + result +"\nCheck Logs\n");
				if(result == 1)
					mTextArea_console_output.append("Maybe file alread exists?\n");
			}
		//	mTextArea_console_output.append(result);
		//	System.out.println(result);
		//	System.out.println(">>" + password);
		//	System.out.println( new String(mPasswordField.getPassword()));
			
		}
			
			
		
	//	if(jarSigner != mTextField_jarsigner.getText())
			
		
	
		
	}
	
	public void saveProperties(){
		try {
			fileOutput = new FileOutputStream("config.properties");
			
			if(sTextField_jarsigner.getText() != null && !sTextField_jarsigner.getText().isEmpty()){
				jarSigner = sTextField_jarsigner.getText();
				prop.setProperty("jarsigner", jarSigner);
				//just to be sure
				mTextField_jarsigner.setText(jarSigner);
				sTextField_jarsigner.setText(jarSigner);
			}
			if(sTextField_zipalign.getText() != null && !sTextField_zipalign.getText().isEmpty()){
				zipAlign = sTextField_zipalign.getText();
				prop.setProperty("zipalign", zipAlign);
				mTextField_zipalign.setText(zipAlign);
				sTextField_zipalign.setText(zipAlign);
			}
			if(sTextField_keystore.getText() != null && !sTextField_keystore.getText().isEmpty()){
				keyStore = sTextField_keystore.getText();
				prop.setProperty("keystore",keyStore);
				mTextField_keystore.setText(keyStore);
				sTextField_keystore.setText(keyStore);
			}
			if(sTextField_alias.getText() != null && !sTextField_alias.getText().isEmpty()){
				alias = sTextField_alias.getText();
				prop.setProperty("alias", alias);
				sTextField_alias.setText(alias);
			}
			if(sTextField_vFlag.getText() != null && !sTextField_vFlag.getText().isEmpty()){
				vFlag = sTextField_vFlag.getText();
				prop.setProperty("vflag", vFlag);
				sTextField_vFlag.setText(vFlag);
			}
			if(sPasswordField.getPassword() != null && sPasswordField.getPassword().length != 0){
				password = sPasswordField.getPassword();
				prop.setProperty("password", new String(password));
				mPasswordField.setText(new String(password));
				sPasswordField.setText(new String(password));
			}
			
			try {
				prop.store(fileOutput, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		} finally {
			try {
				fileOutput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
		}
		
		
	}

	public void readProperties(){
		try {
			fileInput = new FileInputStream("config.properties");
			prop.load(fileInput);
			
			jarSigner = prop.getProperty("jarsigner");
			zipAlign = prop.getProperty("zipalign");
			keyStore = prop.getProperty("keystore");
			password = prop.getProperty("password").toCharArray(); 
			alias = prop.getProperty("alias");
			vFlag = prop.getProperty("vflag");
			
			if(jarSigner != null && !jarSigner.isEmpty()){
				mTextField_jarsigner.setText(jarSigner);
				sTextField_jarsigner.setText(jarSigner);
			}
			if(zipAlign != null && !zipAlign.isEmpty()){
				mTextField_zipalign.setText(zipAlign);
				sTextField_zipalign.setText(zipAlign);
			}
			if(keyStore != null && !keyStore.isEmpty()){
				mTextField_keystore.setText(keyStore);
				sTextField_keystore.setText(keyStore);
			}
			if(password != null && password.length != 0){
				mPasswordField.setText(new String(password));
				sPasswordField.setText(new String(password));
			}
			if(alias != null && !alias.isEmpty()){
				sTextField_alias.setText(alias);
			}
			if(vFlag != null && !vFlag.isEmpty()){
				sTextField_vFlag.setText(vFlag);
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			System.out.println("Properties not found.");
		//	firstLaunch = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not load properties");
	//		e.printStackTrace();
		} finally {
			try {
				if(fileInput != null)
					fileInput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
			}
		}
	}
	
	/**
	 * Create the application.
	 */
	public APKSigner() {
		
		File f = new File("config.properties");
		if(f.exists()) {
			firstLaunch = false;
		} else {
			firstLaunch = true;
		}
		
		initialize();
		readProperties();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().setLayout(cl);
		frame.getContentPane().add(mainPanel, "1");
		frame.getContentPane().add(settingsPanel, "2");
	
		mainPanel.setLayout(null);
		settingsPanel.setLayout(null);
	
		if(firstLaunch){
			JOptionPane.showMessageDialog(null, "Initial Launch, set settings!");
			settingsPanel.setVisible(true);
			mainPanel.setVisible(false);
		} else {
			mainPanel.setVisible(true);
			settingsPanel.setVisible(false);
		}
		
		//---------------------- Settings Panel
		sLabelJarsignerPath = new JLabel("Jarsigner Path");
		sLabelJarsignerPath.setBounds(8, 8, 135, 16);
		settingsPanel.add(sLabelJarsignerPath);
		
		sTextField_jarsigner = new JTextField();
		sTextField_jarsigner.setColumns(10);
		sTextField_jarsigner.setBounds(6, 23, 202, 28);
		settingsPanel.add(sTextField_jarsigner);
		
		sLabelZipalignPath = new JLabel("ZipAlign Path");
		sLabelZipalignPath.setBounds(8, 50, 124, 16);
		settingsPanel.add(sLabelZipalignPath);
		
		sTextField_zipalign = new JTextField();
		sTextField_zipalign.setColumns(10);
		sTextField_zipalign.setBounds(6, 65, 202, 28);
		settingsPanel.add(sTextField_zipalign);
		
		 sLblKeystore = new JLabel("Key Store");
		sLblKeystore.setBounds(8, 92, 124, 16);
		settingsPanel.add(sLblKeystore);
		
		sTextField_keystore = new JTextField();
		sTextField_keystore.setColumns(10);
		sTextField_keystore.setBounds(6, 107, 202, 28);
		settingsPanel.add(sTextField_keystore);
		
		sLblPassword = new JLabel("Password");
		sLblPassword.setBounds(8, 139, 124, 16);
		settingsPanel.add(sLblPassword);
		
		sPasswordField = new JPasswordField();
		sPasswordField.setBounds(6, 154, 202, 28);
		settingsPanel.add(sPasswordField);
		
		sBtnBrowse_jarsigner = new JButton("Browse");
		sBtnBrowse_jarsigner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 if (e.getSource() == sBtnBrowse_jarsigner) {
					  	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				        int returnVal = fc.showOpenDialog(sBtnBrowse_jarsigner);

				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				            File file = fc.getSelectedFile();
				            jarSigner = file.getAbsolutePath();
				            sTextField_jarsigner.setText(jarSigner);
				        } else {

				        }
				 } 
			}
		});
		sBtnBrowse_jarsigner.setBounds(220, 23, 73, 28);
		settingsPanel.add(sBtnBrowse_jarsigner);
		
		sBtnBrowse_zipalign = new JButton("Browse");
		sBtnBrowse_zipalign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (e.getSource() == sBtnBrowse_zipalign) {
				 	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			        int returnVal = fc.showOpenDialog(sBtnBrowse_zipalign);
	
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            zipAlign = file.getAbsolutePath();
			            sTextField_zipalign.setText(zipAlign);
			        } else {
	
			        }
				}
				
			}
		});
		sBtnBrowse_zipalign.setBounds(220, 65, 73, 28);
		settingsPanel.add(sBtnBrowse_zipalign);
		
		sBtnBrowse_keystore = new JButton("Browse");
		sBtnBrowse_keystore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 if (e.getSource() == sBtnBrowse_keystore) {
					 // 	fc.addChoosableFileFilter(jarFilter);
					 FileNameExtensionFilter filter = new FileNameExtensionFilter(
						        "Keystore File", "keystore");
					 fc.setFileFilter(filter);
					 fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					 int returnVal = fc.showOpenDialog(sBtnBrowse_keystore);

					 if (returnVal == JFileChooser.APPROVE_OPTION) {
						 File file = fc.getSelectedFile();
						 keyStore = file.getAbsolutePath();
						 sTextField_keystore.setText(keyStore);
					 } else {

					 }
				     fc.removeChoosableFileFilter(filter);
				   } 
				
			}
		});
		sBtnBrowse_keystore.setBounds(220, 107, 73, 28);
		settingsPanel.add(sBtnBrowse_keystore);
		
		sBtnSave = new JButton("SAVE");
		sBtnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				saveProperties();
				mainPanel.setVisible(true);
				settingsPanel.setVisible(false);
				
			}
		});
		sBtnSave.setBounds(327, 204, 117, 29);
		settingsPanel.add(sBtnSave);
		
		sBtnCancel = new JButton("Cancel");
		sBtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.setVisible(true);
				settingsPanel.setVisible(false);
			}
		});
		sBtnCancel.setBounds(327, 231, 117, 29);
		settingsPanel.add(sBtnCancel);
		
		sLblVFlag = new JLabel("\"-v\" flag");
		sLblVFlag.setBounds(305, 8, 61, 16);
		settingsPanel.add(sLblVFlag);
		
		sTextField_vFlag = new JTextField();
		sTextField_vFlag.setBounds(307, 23, 135, 28);
		settingsPanel.add(sTextField_vFlag);
		sTextField_vFlag.setColumns(10);
		
		sLblAlias = new JLabel("Alias");
		sLblAlias.setBounds(305, 50, 61, 16);
		settingsPanel.add(sLblAlias);
		
		sTextField_alias = new JTextField();
		sTextField_alias.setBounds(307, 65, 135, 28);
		settingsPanel.add(sTextField_alias);
		sTextField_alias.setColumns(10);
		
		
		//---------------------- Main Panel
		mBtnSettings = new JButton("Settings");
		mBtnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				settingsPanel.setVisible(true);
				mainPanel.setVisible(false);
			}
		});
		mBtnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				cl.show(settingsPanel, "2");
			}
		});
		mBtnSettings.setBounds(349, 6, 95, 29);
		mainPanel.add(mBtnSettings);
		
		mLblJarsignerPath = new JLabel("Jarsigner Path");
		mLblJarsignerPath.setBounds(labelPaddingLeft, 8, textViewWidth, 16);
		mainPanel.add(mLblJarsignerPath);
		
		mTextField_jarsigner = new JTextField();
		mTextField_jarsigner.setBounds(textViewPaddingLeft, 23, textViewWidth, 28);
		mainPanel.add(mTextField_jarsigner);
		mTextField_jarsigner.setColumns(10);
		
		mLblZipalignPath = new JLabel("ZipAlign Path");
		mLblZipalignPath.setBounds(labelPaddingLeft, 50, 124, 16);
		mainPanel.add(mLblZipalignPath);
		
		mTextField_zipalign = new JTextField();
		mTextField_zipalign.setBounds(textViewPaddingLeft, 65, textViewWidth, 28);
		mainPanel.add(mTextField_zipalign);
		mTextField_zipalign.setColumns(10);
		
		mLblKeystore = new JLabel("Key Store");
		mLblKeystore.setBounds(labelPaddingLeft, 92, 124, 16);
		mainPanel.add(mLblKeystore);
		
		mTextField_keystore = new JTextField();
		mTextField_keystore.setBounds(textViewPaddingLeft, 107, textViewWidth, 28);
		mainPanel.add(mTextField_keystore);
		mTextField_keystore.setColumns(10);
		
		mLblApkLocation = new JLabel("APK Location");
		mLblApkLocation.setBounds(labelPaddingLeft, 139, 124, 16);
		mainPanel.add(mLblApkLocation);
		
		mTextField_apklocation = new JTextField();
		mTextField_apklocation.setBounds(textViewPaddingLeft, 154, textViewWidth, 28);
		mainPanel.add(mTextField_apklocation);
		mTextField_apklocation.setColumns(10);
		
		mLblPassword = new JLabel("Password");
		mLblPassword.setBounds(labelPaddingLeft, 181, 61, 16);
		mainPanel.add(mLblPassword);
		
		mPasswordField = new JPasswordField();
		mPasswordField.setBounds(textViewPaddingLeft, 196, textViewWidth, 28);
		mainPanel.add(mPasswordField);
		
		
		mLblOutputFilename = new JLabel("Output Filename");
		mLblOutputFilename.setBounds(labelPaddingLeft, 223, 128, 16);
		mainPanel.add(mLblOutputFilename);
		
		mTextField_outputFileName = new JTextField();
		mTextField_outputFileName.setBounds(textViewPaddingLeft, 238, textViewWidth, 28);
		mainPanel.add(mTextField_outputFileName);
		mTextField_outputFileName.setColumns(10);
		
		mBtnBrowse_jarsigner = new JButton("Browse");
		mBtnBrowse_jarsigner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if (e.getSource() == mBtnBrowse_jarsigner) {
					  	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				        int returnVal = fc.showOpenDialog(mBtnBrowse_jarsigner);

				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				            File file = fc.getSelectedFile();
				            jarSigner = file.getAbsolutePath();
				            mTextField_jarsigner.setText(jarSigner);
				        } else {

				        }
				 } 
			}
		});
		mBtnBrowse_jarsigner.setBounds(144, 23, 73, 28);
		mainPanel.add(mBtnBrowse_jarsigner);
		
		mBtnBrowse_zipalign = new JButton("Browse");
		mBtnBrowse_zipalign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == mBtnBrowse_zipalign) {
				 	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			        int returnVal = fc.showOpenDialog(mBtnBrowse_zipalign);
	
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            zipAlign = file.getAbsolutePath();
			            mTextField_zipalign.setText(zipAlign);
			        } else {
	
			        }
				}
			}
		});
		mBtnBrowse_zipalign.setBounds(144, 65, 73, 28);
		mainPanel.add(mBtnBrowse_zipalign);
		
		mBtnBrowse_keystore = new JButton("Browse");
		mBtnBrowse_keystore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 if (e.getSource() == mBtnBrowse_keystore) {
					 // 	fc.addChoosableFileFilter(jarFilter);
					 FileNameExtensionFilter filter = new FileNameExtensionFilter(
						        "Keystore File", "keystore");
					 fc.setFileFilter(filter);
					 fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					 int returnVal = fc.showOpenDialog(mBtnBrowse_keystore);

					 if (returnVal == JFileChooser.APPROVE_OPTION) {
						 File file = fc.getSelectedFile();
						 keyStore = file.getAbsolutePath();
						 mTextField_keystore.setText(keyStore);
					 } else {

					 }
				     fc.removeChoosableFileFilter(filter);
				   } 
				
			}
		});
		mBtnBrowse_keystore.setBounds(144, 107, 73, 28);
		mainPanel.add(mBtnBrowse_keystore);
		
		mBtnBrowse_apkLocation = new JButton("Browse");
		mBtnBrowse_apkLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 if (e.getSource() == mBtnBrowse_apkLocation) {
					 // 	fc.addChoosableFileFilter(jarFilter);
					 FileNameExtensionFilter filter = new FileNameExtensionFilter(
						        "Android APK File", "apk");
					 fc.setFileFilter(filter);
					 fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					 int returnVal = fc.showOpenDialog(mBtnBrowse_apkLocation);

					 if (returnVal == JFileChooser.APPROVE_OPTION) {
						 File file = fc.getSelectedFile();
						 apkLocation = file.getAbsolutePath();
						 mTextField_apklocation.setText(apkLocation);
					 } else {

					 }
				     fc.removeChoosableFileFilter(filter);
				   } 
			}
		});
		mBtnBrowse_apkLocation.setBounds(142, 154, 73, 28);
		mainPanel.add(mBtnBrowse_apkLocation);
		
		mBtnSign = new JButton("SIGN!");
		mBtnSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sign();
			}
		});
		mBtnSign.setBounds(252, 43, 192, 44);
		mainPanel.add(mBtnSign);
		
		mTextArea_console_output = new JTextArea();
		mTextArea_console_output.setBounds(252, 127, 192, 144);
		mainPanel.add(mTextArea_console_output);
		
		mLblOutput = new JLabel("Output");
		mLblOutput.setBounds(249, 105, 61, 16);
		mainPanel.add(mLblOutput);
		
		
		
	}
}
