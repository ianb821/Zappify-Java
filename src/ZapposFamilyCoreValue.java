// The ZapposFamilyCoreValue class represents a Zappos Family Core Value, it is used to provide the summary
// of one of the Core Values in every notification email that a customer receives.

public class ZapposFamilyCoreValue {

	private String name, id, summary, description;

	public ZapposFamilyCoreValue(String name, String id, String summary, String description) {
		this.name = name;
		this.id = id;
		this.summary = summary;
		this.description = description;
	}

	public ZapposFamilyCoreValue() {
		this.id = "9";
		this.summary = "Passion is the fuel that drives us and our company forward.";
		this.name = "Be Passionate and Determined";
		this.description = "We value passion, determination, perseverance, and the sense of urgency. We are inspired because we believe in what we are doing and where we are going. We don't take 'no' or 'that'll never work' for an answer because if we had, then Zappos would have never started in the first place. Passion and determination are contagious. We believe in having a positive and optimistic (but realistic) attitude about everything we do because we realize that this inspires others to have the same attitude. There is excitement in knowing that everyone you work with has a tremendous impact on a larger dream and vision, and you can see that impact day in and day out.";
	}

	public String getId() {
		return id;
	}

	public String getSummary() {
		return summary;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}
}