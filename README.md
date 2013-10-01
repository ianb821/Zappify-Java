Zappify - Java
============

Zappify - A sample application using the Zappos API


About
============

Zappify is a sample app that I wrote as a challenge for Zappos.  It makes use of their Search, Product, and CoreValue APIs.  You are able to search for items from Zappos.com and have the displayed along with important information about it.  You can then enter you email address to be notified when the item is at least 20% off.  It will check every hour for updated pricing.

Zappify makes use of the [Gson Library](http://code.google.com/p/google-gson/) to convert the JSON results from the Zappos API into Java Objects.

[SendGrid](www.sendgrid.com) is used to send the email notifications.

###Note
To actually use the project:
 
	* Link the Gson library to your application
 	* Put your Zappos API Key in the constant defined in Zappify.java.
	* Put Your SendGrid username and password in the constants defined in Zappify.java


Images
============


####Standard View:
![Standard View](https://raw.github.com/ianb821/Zappify-Java/master/Zappify%20Images/ZapposItem.jpg)

####Bad Email Address View:
![Standard View](https://raw.github.com/ianb821/Zappify-Java/master/Zappify%20Images/BadEmail.jpg)

####Confirmation View:
![Standard View](https://raw.github.com/ianb821/Zappify-Java/master/Zappify%20Images/Confirmation.jpg)

####No Search Results View:
![Standard View](https://raw.github.com/ianb821/Zappify-Java/master/Zappify%20Images/NoSearchResults.jpg)

