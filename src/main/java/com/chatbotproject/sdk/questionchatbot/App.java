package roboGSI.robo;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.twilio.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class App {

	private JFrame frmRobogsi;
	private static JTextArea textField;
	private static JList list;
	private JLabel lblNewLabel;
	private JList list_1;
	private JLabel lblNewLabel_1;
	private static JButton sendButton;
	private static JButton generateButton;
	static DefaultListModel listModel1 = new DefaultListModel();
	static DefaultListModel listModel2 = new DefaultListModel();
	private static JTextField textField_1;
	private static JTextPane textPane;
	private JLabel lblCommonlyAskedAbout;
	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	public static void main(String[] args) throws IOException, EncryptedDocumentException, InvalidFormatException 
	{
		//Initialize the SDK
		initializeSDK();
		final List<String> numbers = new ArrayList<String>();
		final List<String> numbers2 = new ArrayList<String>();
		final Map<String, Integer> common = new HashMap<String, Integer>();
		
		// Attach a listener to read the data at our questions reference
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference("/questions/");
		System.out.println("ref " + ref);
		ref.addChildEventListener(new ChildEventListener() 
		{
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) 
			{
				System.out.println("Child added");
				if(dataSnapshot != null) 
				{
						listModel1.addElement(dataSnapshot.getValue(String.class).split(",")[0]);
		    			numbers.add(dataSnapshot.getValue(String.class).split(",")[1]);
		    		    FileOutputStream fileOut;
						try {
							XSSFWorkbook workbook = new XSSFWorkbook();
							XSSFSheet sheet = workbook.createSheet("Sheet 1");
							Row row = sheet.createRow(1);
							Cell cell = row.createCell(1);
							cell.setCellType(CellType.STRING);
			    		    cell.setCellValue(dataSnapshot.getValue(String.class).split(",")[0]);
							fileOut = new FileOutputStream("C:/Users/John/eclipse-workspace/RoboGSI/Meme2.xlsx");
							workbook.write(fileOut);
			    		    fileOut.close();
			    		    workbook.close();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Error in updating local excel file");
							e.printStackTrace();
						}
				}
			}
			public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
			public void onChildRemoved(DataSnapshot snapshot) {}
			public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
			public void onCancelled(DatabaseError error) {}
		});
		
		//GUI 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frmRobogsi.setVisible(true);
					
					list.addListSelectionListener(new ListSelectionListener(){

						public void valueChanged(ListSelectionEvent e) {
							// TODO Auto-generated method stub
							if (e.getValueIsAdjusting() == false) {

						        if (list.getSelectedIndex() == -1) {
						        //No selection, disable send button.
						        		sendButton.setEnabled(false);

						        } else {
						        //Selection, enable the send button.
						        		sendButton.setEnabled(true);
						        }
						    }
						}
						
					});
					
					sendButton.addActionListener(new ActionListener(){  
						
						public void actionPerformed(ActionEvent e){  
				    	            System.out.println("Sending Question");
				    	            int index = list.getSelectedIndex();
				    	            
				    	            //Send answer to number 
				    	            text(textField.getText(), numbers.get(index));
				    	            textField.requestFocusInWindow();
				    	            textField.setText("");
				    	            
				    	            //Add answered question to second list
				    	            listModel2.addElement(listModel1.get(index));
				    	            numbers2.add(numbers.get(index));
				    	    			
				    	            //Remove answered question from first list
				    	            listModel1.remove(index);
				    	    			numbers.remove(index);
				    	    			
				    	    			int size = listModel1.size();
				    	    			
				    	    			if(size == 0) {
				    	    				sendButton.setEnabled(false);
				    	    			}else {
				    	    				if(index == listModel1.getSize()) {
				    	    					index--;
				    	    				}
				    	    				list.setSelectedIndex(index);
				    	    				list.ensureIndexIsVisible(index);
				    	    			}
				    	    			
				    	        }  
				    	});
						generateButton.addActionListener(new ActionListener(){  
							public void actionPerformed(ActionEvent e){  
				    	            System.out.println("Generating Answer...");
				    	            
				    	            //String[] key_phrases0 = {"inverse square of diagonalization"};
				    	            String[] key_phrases0 = textField_1.getText().split("&");

				    	    		//, "inverse matrix", "diagonalization work", "types of matrices","matrix division commutative
				    	    		String pdf_file = "";
				    	    		//Scanner in = new Scanner(new File("pdf_input2.txt"));

				    	    		try {
				    	    			BufferedReader in = new BufferedReader(new FileReader("pdf_input2.txt"));
				    	    			String line;
				    	    			while((line = in.readLine()) != null/*in.hasNextLine()*/)
											pdf_file += line + "\n";
				    	    			Map<String, List<String>> result = parses(key_phrases0, pdf_file);
					    	    		String outputt = "";
				    	    			for(String phrase: key_phrases0)
					    	    		{
					    	    			System.out.println("Phrase: " + phrase);
					    	    			System.out.println("result: " + result.get(phrase));
					    	    			for(String s: result.get(phrase))
					    	    				outputt += s;
					    	    		}
				    	    			textField.setText(outputt);
					    	    		in.close();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										System.out.println("Error in generating answer!");
										e1.printStackTrace();
									}//in.nextLine() + "\n";
				    	    		
				    	    		for(String term: textField_1.getText().split(" ")){
				    	    			int value = 1;
				    	    			if(common.containsKey(term))
				    	    				value = common.get(term)+1;
				    	    			common.put(term, value);
				    	    		}
				    	            textField_1.setText("");
				    	            Map<String,Integer> shm = new TreeMap<String,Integer>();
				    	            shm = sortHashMapByValues(common);
				    	            List<String> a = new ArrayList<String>(shm.keySet());
				    	            String commonTerms = "";
				    	            for(int i=a.size()-1; i>=0;i--){
				    	            	if (i==a.size()-1)
				    	            		commonTerms = a.get(i);
				    	            	else	
				    	            		commonTerms = commonTerms + ", " + a.get(i);
				    	            	System.out.println(a);
				    	            }
				    	            textPane.setText(commonTerms);
				    	        }  
				    	});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	//sort hashmap
	public static LinkedHashMap<String, Integer> sortHashMapByValues(
	        Map<String, Integer> passedMap) {
	    List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
	    List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    LinkedHashMap<String, Integer> sortedMap =
	        new LinkedHashMap<String, Integer>();

	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        int val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            String key = keyIt.next();
	            int comp1 = passedMap.get(key);
	            int comp2 = val;

	            if (comp1 == comp2) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	
	//Initialize the Database SDK
	public static void initializeSDK() throws IOException {
			FileInputStream serviceAccount = new FileInputStream("C:/Users/John/eclipse-workspace/RoboGSI/twilio-22115-firebase-adminsdk-kcsl1-36bd268543.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.setDatabaseUrl("https://twilio-22115.firebaseio.com/")
			.build();
				
			FirebaseApp.initializeApp(options);
	}
	
	
	public static Map<String, List<String>> parses(String[] key_phrases, String pdf_file)
	{
		Set<String> all_words = new HashSet<String>();
		Map<String, List<String>> word_map = new HashMap<String, List<String>>();
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		String[] lines = pdf_file.split("\n");
		for(String phrase: key_phrases)
		{
			String[] words = phrase.split(" ");
			for(String word: words)
				all_words.add(word);
		}
		System.out.println("all words: " + all_words);
		for(String word: all_words)
		{
			List<String> lst = new ArrayList<String>();
			for(String line: lines)
			{
				if(line.contains(word))
				{
					lst.add(line);
				}
			}
			if(lst.size() < 25)
				word_map.put(word, lst);
		}
		for(String word: word_map.keySet())
			System.out.println("word: " + word + " lines: " + word_map.get(word));
		for(String phrase: key_phrases)
		{
			List<String> lst = new ArrayList<String>();
			String[] words_orig = phrase.split(" ");
			Set<String> words = new HashSet<String>();
			for(String word: words_orig)
				words.add(word);
			for(String word: words)
				if(word_map.containsKey(word)) {
					for(String line: word_map.get(word)) {
						Scanner s = new Scanner(line);
						String first = s.next();
						if(first != "Since" && first != "We")
							lst.add(line);
						s.close();
					}
				}
			result.put(phrase, lst);
		}
		return result;
	}
	
	
	//Twilio SMS Function
	public static void text(String text, String to_number) {
    	final String ACCOUNT_SID = "";
        final String AUTH_TOKEN = "";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        System.out.println( "Hello World!" );
        
        Message message = Message.creator(
        	    new PhoneNumber(to_number),  // To number
        	    new PhoneNumber("+1"),  // From number
        	    text                    			// SMS body
        	).create();
    }
	
	//Create Application
	public App() {
		initialize();
	}

	//Initialize Frame Contents
	private void initialize() {
		frmRobogsi = new JFrame();
		frmRobogsi.setTitle("RoboGSI");
		frmRobogsi.setBounds(100, 100, 450, 675);
		frmRobogsi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField = new JTextArea();
		textField.setColumns(10);
		textField.setWrapStyleWord(true);
		textField.setLineWrap(true);
		
		sendButton = new JButton("Send");
		
		list = new JList(listModel1);
		
		lblNewLabel = new JLabel("Incoming Questions:");
		
		list_1 = new JList(listModel2);
		
		lblNewLabel_1 = new JLabel("Answered Questions:");
		
		JLabel lblNewLabel_2 = new JLabel("Answer:");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JLabel lblKeywords = new JLabel("Keywords:");
		
		generateButton = new JButton("Generate");
		
		textPane = 	new JTextPane();
		
		lblCommonlyAskedAbout = new JLabel("Frequently asked subjects:");
		GroupLayout groupLayout = new GroupLayout(frmRobogsi.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(list, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
								.addComponent(lblNewLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
								.addComponent(lblNewLabel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
								.addComponent(list_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(textPane, GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblCommonlyAskedAbout, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblKeywords, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(textField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(generateButton)))
							.addGap(4)))
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
					.addGap(191))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(338, Short.MAX_VALUE)
					.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(list, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(lblCommonlyAskedAbout)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addGap(22)
					.addComponent(lblKeywords)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(generateButton))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(sendButton)
					.addGap(89))
		);
		frmRobogsi.getContentPane().setLayout(groupLayout);
	}
}
