# TwilioQuestionChatbot

## Introduction
This is a **Twilio**-powered program that I created to help students in large classes get their questions answered.
One of my classes at UC Berkeley, CS61A (Structure and Interpretation of Computer Programs), had almost 1800 students present in the first lecture. With classes this large, it is impossible for everyone to get their questions answered in live lecture.

![alt text](https://imgur.com/OQk4ie8.jpg)
Im the one in the yellow circle

My solution, inspired by all the students on iMessage during lecture, is to use Twilio's API to enable students to text their questions directly to all the Graduate Student Instructors (GSIs or TAs) sitting in lecture. This way, the TAs can quickly answer any questions that may be key to understanding the lecture, so no one gets left behind. Additionally, they can act as a filter to ensure that questions that are too basic or too advanced do not get answered by the instructor in class, so that the 1800 other people do not have to waste lecture time waiting for answers that don't apply to them.

This solution is ideal because students do not need to worry about installing any software or creating any new accounts. All they have to do is text their questions to a number, and this software handles the rest.

## What this program uses
- Twilio -  *Cloud communications API that will handle the SMS requests* 
- Firebase - *Database for storing the students' questions*
- SL4J - *Logging framework*
- Java Swing - *GUI for the TAs' response software*
- UiPath - *Added 10/13/18, Creates a robot to automate the GSI's response*
- Microsoft Text Analysis - *Added 10/13/18, Uses NLP to formulate answer*

## Program Flow
![alt text](https://imgur.com/k7Palol.png)
1. The user sends their question via SMS to Twilio, which then sends both the question and the user phone number to the Firebase. 
2. The UiPath robot TA retrieves the question from the database, then retrieves information from reading PDF notes.
3. The information is sent to Microsoft's Text Analysis API for Natural Language Processing, returning key terms from the question and any relevant matching information from the PDF notes.
4. The robot inputs this information into a Java client. The human TA can edit and the formulated answer before sending it to the user.

## Twilio
Using Twilio Studio's chatbot function, a flow can be built to prompt the students for a question, and guide them through how to get a response from the instructors.

![alt text](https://imgur.com/Jo0YqpM.jpg)

In the flow for this project, you can see that the questions from the user are being sent to the Twilio Function, *Post_To_Database*:
```
const https = require('https');

exports.handler = function(context, event, callback) {
    question_body = event.question
    from = event.from_number
    str = question_body + "," + from
    console.log(question_body)
    console.log(from)
    var myJSONObject = str;
    var request = require('request');
    
    request({
    url: "https://twilio-22115.firebaseio.com/questions.json",
    method: "POST",
    json: true,   
    body: myJSONObject
    }, function (error, response, body){
         console.log(response);
         callback(null,"Success")
    });
};
```
This function takes the user question and POSTS it to a Firebase database in JSON format. With the Twilio Studio widgets, it becomes very easy to build a flow for the question to be retreived and sent to the database.

## Firebase
The Firebase Realtime Database API is used to store the questions that were sent to the Twilio Chatbot. Both the question and the phone number of the student is recorded in the database.

![alt text](https://imgur.com/jKO9ouF.png)

This stores the question in the database for the TAs to view and respond to.

## UiPath Robot
Using UiPath, a robot is run on the TA's computer which automatically retrieves questions from Firebase and reads any PDF notes or Lecture transcripts fed to it. It then uses Microsoft Text Analysis to discover any key terms in the question and return relevant information from the PDF information. 

![alt text](https://imgur.com/XFrkxgM.png)

## Java Client
The last part of this project was building a client that takes in the information returned from the NLP and formulates an answer as a response. Before the answer is sent back to the user, a human TA has the opportunity to edit the answer, in case their are grammatical errors or irrelevant information. The answered and unanswered questions are kept in seperate tables, and recurring keywords in questions are also displayed so that the instructors can determine what topics need to be emphasized. For this part, Java Swing was used to build the GUI and Twilio's SDK was used to access the programmable SMS product, which allows the TA to respond to the student.

![alt text](https://imgur.com/1Ij8RER.jpg)

## Conclusion
And that's it! Using the Twilio and Firebase APIs, I was able to create a pretty cool product that could potentially improve students' experiences in lecture. I did have to learn many things to actually carry out the project, including asychronous calls, RPA, JSON, Node.js, and NLP.
