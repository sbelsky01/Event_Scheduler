package eventScheduler;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JFrame{
	private static final long serialVersionUID = 1L;

	private JPanel header;
	private JPanel body;
	private JButton add;
	private JButton remove;
	private JButton modify;
	private JButton display;

	public MainMenu() {
		//create the main window frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xStart = (int)(screenSize.getWidth()-600)/2;
		int yStart = (int)(screenSize.getHeight()-600)/2;
		setBounds(xStart, yStart, 600, 600);
		Container cp = getContentPane();
		JPanel frame = new JPanel();
		cp.add(frame);
		frame.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.setLayout(new GridLayout(2, 1, 0, 100));

		//create the header and body panels
		header = new JPanel(new GridLayout(1, 2, 40, 0));
		body = new JPanel(new GridLayout(0, 2, 10, 10));

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
		JLabel title = new JLabel("Main Menu", SwingConstants.LEFT);
		title.setVerticalAlignment(JLabel.BOTTOM);
		title.setFont(new Font("Calibri", Font.BOLD, 40));
		header.add(logo);
		header.add(title);

		//put the four buttons in the body
		frame.add(body);
		Font font = new Font("Calibri", Font.PLAIN, 20);
		add = new JButton("Add event");
		add.addActionListener(new openNewEventWindow());
		add.setFont(font);
		remove = new JButton("Remove event");
		remove.setFont(font);
		modify = new JButton("Edit event");
		modify.setFont(font);
		display = new JButton("View upcoming events");
		display.setFont(font);
		body.add(add);
		body.add(remove);
		body.add(modify);
		body.add(display);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Event Scheduler");
		setVisible(true);
	}

	public static void main(String[] args) {
		// Run GUI codes in Event-Dispatching thread for thread-safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainMenu();  // Let the constructor do the job
			}
		});
	}

	public class openNewEventWindow implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Run GUI codes in Event-Dispatching thread for thread-safety
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new AddEventWindow();  // Let the constructor do the job
				}
			});
			dispose();

		}

	}

}
