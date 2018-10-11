# TwilioQuestionChatbot

## Introduction:
This is a **Twilio**-powered program that I created to help students in large classes get their questions answered.
One of my classes at UC Berkeley, CS61A (Structure and Interpretation of Computer Programs), had almost 1800 students present in the first lecture. With classes this large, it is impossible for everyone to get their questions answered in live lecture.

![alt text](https://imgur.com/OQk4ie8)
Im the one in the yellow circle

My solution, inspired by all the students on iMessage during lecture, is to use Twilio's API to enable students to text their questions directly to all the TAs sitting in lecture. This way, the TAs can quickly answer any questions that may be key to understanding the lecture, so no one gets left behind. Additionally, they can act as a filter to ensure that questions that are too basic or too advanced do not get answered by the instructor in class, so that the 1800 other people do not get their time wasted.

This solution is ideal because students do not need to worry about installing any software or creating any new accounts. All they have to do is text their questions to a number, and software handles the rest.

## What this program uses:
- Twilio -  *Cloud communications API that will handle the SMS requests* 
- Firebase - *Database for storing the students' questions*
- SL4J - *Logging framework*
- Java Swing - *GUI for the TAs' response software*

## Twilio:
Using Twilio Studio's chatbot function, a flow can be built to prompt the students for a question, and guide them through how to get a response from the instructors

![alt text](https://imgur.com/Jo0YqpM)
The flow used for this project

In the flow, you can see that the questions from the user are being sent to the Twilio Function, *Post_To_Database*:
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
This code takes the question and POSTS it to a Firebase database in JSON format.

## Firebase:
The Firebase Realtime Database API is used to store the questions that were sent to the Twilio Chatbot. Both the question and the phone number of the student is recorded in the database.

![alt text](https://imgur.com/YalyZJN)
Firebase database

This stores the question in the database for the TA's to view and respond to.

## Java Client:
The last part of this project was building a client to retrieve the questions from the database and allow the TA to send an SMS response back. For this part, Java Swing was used to build the GUI and Twilio's SDK was used to access the programmable SMS product, which allows the TA to respond to the student.

![alt text](https://imgur.com/Z17EIhO)
The client for the TA side

## Conclusion:
And that's it! Using the Twilio and Firebase APIs, I was able to create a pretty cool product that could potentially improve students' experiences in lecture. I did have to learn many things to actually carry out the project, including asychronous calls, JSON, Node.js, Maven, and Swing. The next thing I will work on adding to the project in the future is using some natural language processing on the existing database of questions to categorize and help the TAs formulate answers to similar questions.
