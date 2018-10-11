{\rtf1\ansi\ansicpg1252\cocoartf1504\cocoasubrtf830
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww10800\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 const https = require('https');\
\
exports.handler = function(context, event, callback) \{\
    question_body = event.question\
    from = event.from_number\
    str = question_body + "," + from\
    console.log(question_body)\
    console.log(from)\
    var myJSONObject = str;\
    var request = require('request');\
    \
    request(\{\
    url: "https://twilio-22115.firebaseio.com/questions.json",\
    method: "POST",\
    json: true,   \
    body: myJSONObject\
    \}, function (error, response, body)\{\
         console.log(response);\
         callback(null,"Success")\
    \});\
\};\
}