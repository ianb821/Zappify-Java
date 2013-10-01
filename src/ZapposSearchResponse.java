// ZapposSearchResponse is a class that is used when processing the JSON response from the Zappos Search API
// Gson converts the response into a ZapposSearchResponse object from which you can access the individual ZapposItem objects

public class ZapposSearchResponse {

	private String statusCode;
	private ZapposItem[] results;

	public ZapposSearchResponse(String statusCode, ZapposItem[] results) {
		this.statusCode = statusCode;
		this.results = results;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public ZapposItem getResults() {
		// account for network error and
		// return a new ZapposItem if there aren't
		// any results from the search
		if (results.length > 0)
			return results[0];
		else
			return new ZapposItem();
	}
}