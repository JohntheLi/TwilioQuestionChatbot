package com.chatbotproject.sdk.questionchatbot;
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
import com.google.gson.Gson;

import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

class TestApp
{
	public static void main( String[] args ) throws IOException
    {
  		//final List<Question> questions = new ArrayList<Question>();
  		final List<String> questions = new ArrayList<String>();
		
		//Initialize the SDK
    		FileInputStream serviceAccount = new FileInputStream("/Users/John/eclipse-workspace/twilio-22115-firebase-adminsdk-kcsl1-36bd268543.json");

    		FirebaseOptions options = new FirebaseOptions.Builder()
    	    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    	    .setDatabaseUrl("https://twilio-22115.firebaseio.com/")
    	    .build();
    		
    		FirebaseApp.initializeApp(options);
  
    		
    		//Start GUI
    		JFrame.setDefaultLookAndFeelDecorated(true);
    		JFrame f=new JFrame("Question Chatbot");  
    	    final JTextField tf=new JTextField();  
    	    tf.setBounds(50,50, 150,20);  
    	    JButton b=new JButton("Click Here");  
    	    b.setBounds(50,100,95,30);
    	    b.addActionListener(new ActionListener(){  
    	    	public void actionPerformed(ActionEvent e){  
    	    	            tf.setText("Nice Meme.");
    	    	            System.out.println("Sending Question");
    	    	            try {
    	    					addToDataBase();
    	    				} catch (IOException ee) {
    	    					ee.printStackTrace();
    	    				}
    	    	            System.out.println(questions);
    	    	        }  
    	    	});  
    	    f.setLayout(new GridLayout(2, 1));	
    	    f.add(b);
    	    	f.add(tf);  
    	    	f.setSize(500,500);  
    	    	//f.setLayout(null); 
    	    	f.pack();
    	    	f.setVisible(true);   
    	    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void addToDataBase() throws IOException {
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL("https://twilio-22115.firebaseio.com/questions.json").openConnection()));
		httpcon.setDoOutput(true);
		httpcon.setRequestProperty("Content-Type", "application/json");
		httpcon.setRequestProperty("Accept", "application/json");
		httpcon.setRequestMethod("POST");
		httpcon.connect();

		final String str = "why doesnt this work?,+19728328986";
		Gson g = new Gson();
		String toWriteOut = g.toJson(str);
		final OutputStreamWriter osw = new OutputStreamWriter(httpcon.getOutputStream());
		osw.write(toWriteOut);
		osw.close();
		System.out.println("success" + httpcon.getResponseCode());
	}
}
