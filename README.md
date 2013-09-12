twilio-sms-google-app-engine
============================

Demo app that runs on Google App Engine which sends a SMS via Twilio.

I took the sample Google App Engine app that comes with Eclipse and added the linkage to Twilio.

If you want to use it yourself, customize the phone number, sid, and auto token in
com.lcddave.gae.twilio.sms.server.GreetingServiceImpl. (That's where all the Twilio "meat" is too.)

Built using JDK 1.7u40 with Google App Engine 1.8.4. Added a Apache Commons Codec jar for Base64.
