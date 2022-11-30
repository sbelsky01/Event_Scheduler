package eventScheduler;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;

import javax.swing.*;



public class AddEventWindow extends JFrame{

	private JPanel header;
	private JPanel body;
	private JButton submit;

	public AddEventWindow() {
		//create the main window frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xStart = (int)(screenSize.getWidth()-600)/2;
		int yStart = (int)(screenSize.getHeight()-600)/2;
		setBounds(xStart, yStart, 600, 600);
		Container cp = getContentPane();
		JPanel frame = new JPanel();
		cp.add(frame);
		frame.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));

		//button to return to main menu
		JPanel top = new JPanel();
		top.setLayout(new FlowLayout(SwingConstants.LEFT));
		JButton menuReturn = new JButton("Return to Main Menu");
		menuReturn.addActionListener(new MainMenuReturn());
		frame.add(top);
		top.add(menuReturn);top.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

		//create the header and body panels
		header = new JPanel(new GridLayout(1, 2, 20, 0));header.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
		body = new JPanel(new GridLayout(0, 1, 10, 3));body.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

		//put the logo and page title on in the header
		frame.add(header);
		JLabel logo = new JLabel("", SwingConstants.RIGHT);
		logo.setVerticalAlignment(JLabel.BOTTOM);

		//resize the image
		ImageIcon image = new ImageIcon("MyCal.png");
		Image scale = image.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
		ImageIcon scaledImage = new ImageIcon(scale);
		logo.setIcon(scaledImage);

		//set the title styling
		JLabel title = new JLabel("Add Event:", SwingConstants.LEFT);
		title.setVerticalAlignment(JLabel.BOTTOM);
		title.setFont(new Font("Calibri", Font.BOLD, 40));

		//add elements to header
		header.add(logo);
		header.add(title);

		//set window attributes
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Event Scheduler");
		setVisible(true);

		//create inputs
		frame.add(body);

		//event title
		JPanel nameP = new JPanel(new FlowLayout(SwingConstants.LEADING));
		JLabel nameL = new JLabel("Event title:");
		JTextField name = new JTextField(30);
		nameP.add(nameL);
		nameP.add(name);
		body.add(nameP);
		nameP.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

		//date
		JPanel dateP = new JPanel(new FlowLayout(SwingConstants.LEADING));
		JLabel dateL = new JLabel("Date:");
		JTextField date = new JTextField(30);
		dateP.add(dateL);
		dateP.add(date);
		body.add(dateP);
		dateP.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

		//time
		JPanel timeP = new JPanel(new FlowLayout(SwingConstants.LEADING));
		JLabel timeL = new JLabel("Time:");
		JTextField time = new JTextField(30);
		time.setSize(500, 100);
		timeP.add(timeL);
		timeP.add(time);
		body.add(timeP);
		timeP.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

		//address
		JPanel addressP = new JPanel(new BorderLayout());
		JLabel addressL = new JLabel("Address:");
		JTextField address = new JTextField(30);
		addressP.add(addressL);
		//addressP.add(address);
		body.add(addressP);
		addressP.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));


		/*
		 * //date picker Date today = new Date(); UtilDateModel model = new
		 * UtilDateModel(); model.setDate(today.getYear(), today.getMonth(),
		 * today.getDate()); model.setSelected(true); JDatePanelImpl datePanel = new
		 * JDatePanelImpl(model, null); JDatePickerImpl datePicker = new
		 * JDatePickerImpl(datePanel, null);
		 * 
		 * frame.add(datePicker);
		 */

	}

	public static void main(String[] args) {
		// Run GUI codes in Event-Dispatching thread for thread-safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AddEventWindow();  // Let the constructor do the job
			}
		});
	}

	public class MainMenuReturn implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Run GUI codes in Event-Dispatching thread for thread-safety
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new MainMenu();  // Let the constructor do the job	
				}
			});
			dispose();
		}


	}
}
