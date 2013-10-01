// ZapposFamilyCoreValueResponse is a class that is used when processing the JSON response from the Zappos CoreValue API
// Gson converts the response into a ZapposFamilyCoreValueResponse object from which you can access the ZapposFamilyCoreValue object

public class ZapposFamilyCoreValueResponse {

	private String statusCode;
	private ZapposFamilyCoreValue[] values;

	public ZapposFamilyCoreValueResponse(String statusCode, ZapposFamilyCoreValue[] values) {
		this.statusCode = statusCode;
		this.values = values;
	}

	public String getStatusCode() {
		return statusCode;
	}

	// returns the random Core Value from the CoreValue API, if
	// for some reason there was an error, it provides a default one
	public ZapposFamilyCoreValue getValue() {
		if (values.length > 0)
			return values[0];
		else 
			return new ZapposFamilyCoreValue();
	}



}