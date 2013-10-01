// ZappifyFrame is a class that creates a frame and a panel to display information about a specific item from 
// Zappos.com.  It allows the user to search for an item, and input their email address to be notified about discounts on 
// that item. Multiple items can be added.

import java.awt.*;
import java.text.*;
import java.net.URL;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;


@SuppressWarnings("serial")
public class ZappifyFrame extends JFrame {

	ZapposDetailsPanel panel;

	public ZappifyFrame() {
		super("Default");
	}

	public ZappifyFrame(String s) {
		super(s);

		panel = new ZapposDetailsPanel(null);

		setContentPane(panel);

		setSize(500, 500);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);

	}

	// custom panel for the frame
	private class ZapposDetailsPanel extends JPanel{

		private ZapposItem item;
		private BufferedImage image;
		private JTextField searchField, emailAddressField;
		private JButton linkButton, searchButton, emailMeButton;

		private XLabel xMark; // to show that an email address is required before beginning to monitor an item
		private CheckLabel checkMark; // to confirm when an item has been saved with an email address


		public ZapposDetailsPanel(URL url) {

			try {                
				if (image != null)
					image = ImageIO.read(url);
			} catch (IOException ex) {
				System.out.println("EXCEPTION " + ex);
			}

			setLayout(null);
			setBackground(Color.WHITE);

			// setup the check mark
			checkMark = new CheckLabel();
			checkMark.setVisible(false);

			// setup the 'x' mark
			xMark = new XLabel();
			xMark.setVisible(false);

			// initialize the ZapposItem
			item = new ZapposItem();
			item.setPrice("-1");

			// setup the link button that will take the user to the item on zappos.com
			linkButton = new JButton("See it on Zappos.com");
			linkButton.setBounds(260, 196, linkButton.getWidth(), linkButton.getHeight());
			linkButton.setVisible(false);


			linkButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					goToZapposDotCom();
				}
			});

			// setup the search field
			searchField = new JTextField();
			searchField.setVisible(true);
			searchField.setPreferredSize(new Dimension(200, 30));

			// add action for 'Enter' key
			searchField.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					searchForItemAndUpdatePanel(searchField.getText());
					repaint();
				}
			}); 


			// setup the email address field
			emailAddressField = new JTextField();
			emailAddressField.setVisible(false);
			emailAddressField.setPreferredSize(new Dimension(200, 30));

			// add action for 'Enter' key
			emailAddressField.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					startMonitoringItem();
				}
			}); 


			// setup the search button
			searchButton = new JButton("Search Zappos");
			searchButton.setVisible(true);

			//Add action listener to button
			searchButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					searchForItemAndUpdatePanel(searchField.getText());
					repaint();
				}
			});    


			// setup the email me button to start monitoring an item
			emailMeButton = new JButton("Email me when this is at least 20% off!");
			emailMeButton.setVisible(false);

			//Add action listener to button
			emailMeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					startMonitoringItem();
				}
			});    

			// add all the components to the panel
			this.add(xMark);
			this.add(checkMark);
			this.add(linkButton);
			this.add(searchField);
			this.add(searchButton);
			this.add(emailMeButton);
			this.add(emailAddressField);

		}

		@Override
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);

			int x = 260;
			int y = 148;
			// if it is the initial time to paint, display a message to search
			if (item.getPrice().equals("-1")) {

				g.drawString("Search for an item from Zappos.com", 135, 240);

			} else if (!item.getProductName().equals("")) {

				if (image != null)
					x = image.getWidth() + 20;

				// reposition the image based on it's height
				if (image.getHeight() == 320)
					g.drawImage(image, 10, 50, null);
				else
					g.drawImage(image, 10, 80, null);

				// draw the brand name
				g.drawString(item.getBrandName(), x, y);


				// draw the product name, if it's too long, draw a substring of it plus '...'
				if (item.getProductName().length() > 32) 
					g.drawString(item.getProductName().substring(0, 32) + " ...", x, y + 24);
				else
					g.drawString(item.getProductName(), x, y + 24);

				// if the item is discounted currently, draw the original price with a strikethrough
				if (!item.getPrice().equals(item.getOriginalPrice())) {
					AttributedString origPrice = new AttributedString(item.getOriginalPrice());

					origPrice.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON,
							0, item.getPrice().length());

					g.drawString(origPrice.getIterator(), x, y + 48);

					g.setColor(Color.RED);
					g.drawString(item.getPrice(), x + 60, y + 48);
					g.setColor(Color.BLACK);

					// if there is a price draw it in red
				} else if (!item.getPrice().equals("")) {
					g.setColor(Color.RED);
					g.drawString(item.getPrice(), x, y + 48);
					g.setColor(Color.BLACK);
				}

				// if there is currently a discount, draw the percent off, if not, skip it
				// draw the link button to the item's URL if it's available
				if (!item.getPercentOff().equals("0%")) {

					g.drawString(item.getPercentOff() + " off", x, y + 72);
					linkButton.setVisible(true);
					linkButton.setBounds(x - 5, y + 81, linkButton.getWidth(), linkButton.getHeight());

				} else if (!item.getProductUrl().equals("")) {

					linkButton.setVisible(true);
					linkButton.setBounds(x - 6, y + 58, linkButton.getWidth(), linkButton.getHeight());
				} 

				// If the email address field is visible, draw a label in front of it
				if (emailAddressField.isVisible()) 
					g.drawString("Email Address:", 102, 399);

				// position the email address field and button
				emailAddressField.setBounds(202, 380, emailAddressField.getWidth(), emailAddressField.getHeight());
				emailMeButton.setBounds(107, 420, emailMeButton.getWidth(), emailMeButton.getHeight());

			} else {

				xMark.setVisible(false);
				checkMark.setVisible(false);
				linkButton.setVisible(false);
				emailMeButton.setVisible(false);
				emailAddressField.setVisible(false);

				g.drawString("No results for \'" + searchField.getText() + "\'", 150, 240);
			}

			// position the other components properly on the panel
			xMark.setBounds(405, 378, 30, 30);
			checkMark.setBounds(410, 378, 30, 30);
			searchField.setBounds(80, 5, searchField.getWidth(), searchField.getHeight());
			searchButton.setBounds(285, 5, searchButton.getWidth(), searchButton.getHeight());
		}


		private void searchForItemAndUpdatePanel(String searchTerm) {

			item = Zappify.getZapposItemForSearchTerm(searchTerm);

			//reset the email address field and hide any indicators
			emailAddressField.setText("");
			checkMark.setVisible(false);
			xMark.setVisible(false);

			// load the image 
			try {

				URL url = new URL(item.getDefaultImageUrl());
				image = ImageIO.read(url);

			} catch (IOException ex) {

				System.out.println(ex.getMessage());

			}

			emailAddressField.setVisible(true);
			emailMeButton.setVisible(true);
		} 

		private void startMonitoringItem() {

			// if the email address field isn't empty and if it follows the general email address pattern, 
			// start monitoring the item and show the confirmation check mark
			// If it is, show an 'x' mark and don't start monitoring the item yet
			if (!emailAddressField.getText().equals("") && emailAddressField.getText().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {

				item.setEmailAddress(emailAddressField.getText());
				Zappify.addItemToItemsToWatch(item);
				checkMark.setVisible(true);
				xMark.setVisible(false);

			} else {

				xMark.setVisible(true);
				checkMark.setVisible(false);
			}
		}

		private void goToZapposDotCom() {
			try {
				Desktop.getDesktop().browse(new URL(item.getProductUrl()).toURI());

			} catch (Exception e) {

				System.out.println("Exception: " + e.getMessage());
			}
		}
	}

	// a class to show off my awesome drawing skills...
	// actually a template I found online and edited a little to
	// show a nice little green check mark when an item has been added to the list
	// to be monitored
	private class CheckLabel extends JLabel {
		private final Polygon polygon;

		public CheckLabel() {

			setPreferredSize(new Dimension(30, 30));

			int[] xPoints = new int[]{0, 6, 28, 7, 0, 0};
			int[] yPoints = new int[]{10, 25, 3, 20, 10, 10};

			polygon = new Polygon(xPoints, yPoints, xPoints.length);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;

			// set the color to be dark green
			g2.setColor(new Color(37, 111, 39));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.fillPolygon(polygon);
		}
	}

	// same as above, but it draws a red 'x' to show that the email address field must contain
	// a valid email address
	private class XLabel extends JLabel {
		private final Polygon leftX, rightX;

		public XLabel() {

			setPreferredSize(new Dimension(30, 30));

			int[] lXPoints = new int[]{6, 9, 24, 21};
			int[] lYPoints = new int[]{9, 6, 21, 24};

			int[] rXPoints = new int[]{6, 9, 24, 21};
			int[] rYPoints = new int[]{21, 24, 9, 6};

			leftX = new Polygon(lXPoints, lYPoints, lXPoints.length);
			rightX = new Polygon(rXPoints, rYPoints, rXPoints.length);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;

			g2.setColor(new Color(159, 0, 4));
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.fillPolygon(leftX);
			g2.fillPolygon(rightX);

		}
	}
}