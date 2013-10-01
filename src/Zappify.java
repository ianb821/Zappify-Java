/*
 * Zappify is a simple app that lets a user search for a product by name/product id and gives them the ability to enter their email
 * address to receive an email when the chosen product is at least 20% off its normal price.  The app checks immediately, and then
 * every hour for updated pricing.  When the item is at least 20% off, the user is emailed a notification letting them know that the item 
 * is on sale, provides a link to it on Zappos.com, and shares a Zappos Family Core Value with them.
 */

import java.io.*;
import java.net.*;
import java.util.*;

// used for converting JSON to ZapposItems
import com.google.gson.Gson;

public class Zappify {

	//keep track of all the items that the user wants to be alerted about
	private static LinkedList<ZapposItem> itemsToWatch = new LinkedList<ZapposItem>();
	private static Timer timer;

	// constants used for searching with the Zappos API
	private static final String PRODUCT_SEARCH = "http://api.zappos.com/Product?id=";
	private static final String SEARCH = "http://api.zappos.com/Search/term/";
	private static final String LIMIT = "?limit=1";
    
    // add your Zappos API Key here to use Zappify
	private static final String API_KEY = "&key=PUT_YOUR_KEY_HERE";
    
    // add your SendGrid Username and Password here
    private static final String SG_USERNAME = "YOUR_USERNAME_HERE";
    private static final String SG_PASSWORD = "YOUR_PASSWORD_HERE";

	// main method for the program
	public static void main(String[] args) {

		new ZappifyFrame("Zappify");
	}



	// method used by to add an item to the list of items to be watched
	// also starts the timer if this is the first item
	public static void addItemToItemsToWatch(ZapposItem item) {
		itemsToWatch.add(item);
		checkForItemsThatTheUserShouldBeNotifiedAbout();
		if (timer == null)
			startCheckingForItemsToNotifyAbout();
	}

	// starts the repeating timer to check for updated pricing
	private static void startCheckingForItemsToNotifyAbout() {

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				checkForItemsThatTheUserShouldBeNotifiedAbout();
			}
		}, 0, (1000 * 60 * 60)); // repeat every hour

	}

	//loops through all of the items in the list and if they're discounted at least
	// 20%, it emails the user at the desired email address and then marks the item so that
	// it doesn't notify the user more than once about the same item
	private static void checkForItemsThatTheUserShouldBeNotifiedAbout() {

		for (ZapposItem item : itemsToWatch) {
			if (Double.parseDouble(item.getPercentOff().substring(0, item.getPercentOff().length() - 1)) >= 20) // now.thats.a.lot.of.method.calls
				if (!item.getHasBeenNotified()) {
					sendEmailAboutItem(item);
					item.setHasBeenNotified(true);
				}
		}
	}



	// method used to make all URL requests returns the JSON string to 
	// the caller to be interpreted there
	private static String httpGet(String urlStr) throws IOException {

		URL url = new URL(urlStr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		if (connection.getResponseCode() != 200) {
			throw new IOException(connection.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder responseString = new StringBuilder();

		String line;
		while ((line = rd.readLine()) != null) 
			responseString.append(line);
		rd.close();

		connection.disconnect();
		return responseString.toString();
	}


	// method to alert the user that an item they were watching has been discounted at
	// least 20%.  Uses SendGrid API, and sends the email from 'noreply@zappos.com'
	private static void sendEmailAboutItem(ZapposItem item) {

		String subjectForEmail = item.getProductName();

		// format the email body with the item link and current discount
		String textBodyForEmail = "It's your lucky day, " + item.getProductName() + " is now " + item.getPercentOff() + " off! Click the following link and head on over to Zappos to purchase it before it's too late!\n" + item.getProductUrl();



		// get a random Zappos Family Core Value to include at the bottom of the notification email
		try {

			String coreValueResponse = httpGet("http://api.zappos.com/CoreValue/random");

			Gson gson = new Gson();
			ZapposFamilyCoreValueResponse coreValueRequest = gson.fromJson(coreValueResponse, ZapposFamilyCoreValueResponse.class);
			ZapposFamilyCoreValue coreValue = coreValueRequest.getValue();

			String coreValueForEmail = "\n\nZappos Family Core Value #" + coreValue.getId() + ":\n" + coreValue.getSummary();


			// create the call to the SendGrid API to send an email notification
			String urlString = "https://sendgrid.com/api/mail.send.json?api_user=" + SG_USERNAME + "&api_key=" + SG_PASSWORD + "&to=" + URLEncoder.encode(item.getEmailAddress(), "UTF-8") +
					"&subject=" + URLEncoder.encode(subjectForEmail, "UTF-8") + "&text=" + URLEncoder.encode(textBodyForEmail, "UTF-8") + 
					URLEncoder.encode(coreValueForEmail, "UTF-8") + "&from=noreply@zappos.com";

			// send the email
			httpGet(urlString);

		} catch (IOException ex) {
			System.out.println(ex.toString());
		}

	}

	// method to search the Zappos Search (and Product) API for the term
	// that the user entered.  Returns the first resulting item from the search term.
	public static ZapposItem getZapposItemForSearchTerm(String searchTerm) {

		String URLRequest = SEARCH + searchTerm + LIMIT + API_KEY;

		Gson gson = new Gson();
		ZapposItem item = new ZapposItem();

		try {

			String response = httpGet(URLRequest);

			ZapposSearchResponse theResponse = gson.fromJson(response, ZapposSearchResponse.class);
			item = theResponse.getResults();

			String productId = item.getProductId();
			String productResponse = httpGet(PRODUCT_SEARCH + productId + API_KEY);

			ZapposProductSearchResponse product = gson.fromJson(productResponse, ZapposProductSearchResponse.class);
			item.setDefaultImageUrl(product.getProduct().getDefaultImageUrl());

		} catch (IOException ex) {
			System.out.println(ex.toString());
		}

		return item;
	} 
}
