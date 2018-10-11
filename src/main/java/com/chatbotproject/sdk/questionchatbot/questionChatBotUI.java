package com.chatbotproject.sdk.questionchatbot;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.twilio.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

public class questionChatBotUI {

	private JFrame frame;
	private static JTextField textField;
	private static JList list;
	private JLabel lblNewLabel;
	private JList list_1;
	private JLabel lblNewLabel_1;
	private static JButton sendButton;
	static DefaultListModel listModel1 = new DefaultListModel();
	static DefaultListModel listModel2 = new DefaultListModel();
	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		//Initialize the SDK
		initializeSDK();
		final List<String> numbers = new ArrayList<String>();
		final List<String> numbers2 = new ArrayList<String>();
		
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
					questionChatBotUI window = new questionChatBotUI();
					window.frame.setVisible(true);
					
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
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	//Initialize the Database SDK
	public static void initializeSDK() throws IOException {
			FileInputStream serviceAccount = new FileInputStream("/Users/John/eclipse-workspace/twilio-22115-firebase-adminsdk-kcsl1-36bd268543.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccount))
			.setDatabaseUrl("https://twilio-22115.firebaseio.com/")
			.build();
				
			FirebaseApp.initializeApp(options);
	}
	
	//Twilio SMS Function
	public static void text(String text, String to_number) {
    	final String ACCOUNT_SID = "ACa4951f831c04f374481bfbdfe4a9d894";
        final String AUTH_TOKEN = "103abc9edfeeec2862c934ddcbf2167a";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        System.out.println( "Hello World!" );
        
        Message message = Message.creator(
        	    new PhoneNumber(to_number),  // To number
        	    new PhoneNumber("+19034146721"),  // From number
        	    text                    			// SMS body
        	).create();
    }
	
	//Create Application
	public questionChatBotUI() {
		initialize();
	}

	//Initialize Frame Contents
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 423);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		sendButton = new JButton("Send");
		
		list = new JList(listModel1);
		
		lblNewLabel = new JLabel("Incoming Questions");
		
		list_1 = new JList(listModel2);
		
		lblNewLabel_1 = new JLabel("Answered Questions");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(list_1, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
						.addComponent(list, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
						.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(list, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(sendButton))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
