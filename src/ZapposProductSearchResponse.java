// ZapposProductSearchResponse is a class that is used when processing the JSON response from the Zappos Product API
// Gson converts the response into a ZapposProductSearchResponse object from which you can access the individual ZapposItem objects

public class ZapposProductSearchResponse {

	private String statusCode;
	private ZapposItem[] product;


	public ZapposProductSearchResponse(String statusCode, ZapposItem[] product) {
		this.statusCode = statusCode;
		this.product = product;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public ZapposItem getProduct() {
		// account for network error and
		// return a new ZapposItem if there aren't
		// any results from the product search
		if (product.length > 0)
			return product[0];
		else
			return new ZapposItem();
	}
}
