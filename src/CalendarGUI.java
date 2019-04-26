import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CalendarGUI {
	
	public Calendar model;

	public class HourComponent extends JTextArea implements Observer {
		private static final long serialVersionUID = 1L;
		public void viewNotify() {
			model.viewBy("D","G");
			this.setText(model.getText());
		}
		public HourComponent() {
			model.viewBy("D","G");
			this.setText(model.getText());
		}
	}
	
	public class MonthComponent extends JTextArea implements Observer {
		private static final long serialVersionUID = 1L;
		public void viewNotify() {
			model.viewBy("M","G");
			this.setText(model.getText());
		}
		public MonthComponent() {
			model.viewBy("M","G");
			this.setText(model.getText());
		}
	}
	
	public CalendarGUI(String inputFile, String outputFile) throws Exception {
		
		model = new Calendar(inputFile);
		
		JFrame frame = new JFrame();
		JPanel currentDayFrame = new JPanel();
		
		HourComponent label1 = new HourComponent();	
		
		model.attach(label1);

		JButton previousDayButton  = new JButton(); 
		previousDayButton.setText("<");
		previousDayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.viewBy("D","P");
			}
		});
		JButton nextDayButton = new JButton();
		nextDayButton.setText(">");
		nextDayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.viewBy("D","N");
			}
		});
		
		currentDayFrame.add(previousDayButton, BorderLayout.WEST);
		currentDayFrame.add(label1, BorderLayout.CENTER);
		currentDayFrame.add(nextDayButton, BorderLayout.EAST);
		JPanel actionBar = new JPanel(new BorderLayout());
		actionBar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 8));
		//actionBar.setLayout();
		JPanel createForm = new JPanel();
		JTextField newEventDescription = new JTextField("Description Here", 30);
		JTextField newEventDate = new JTextField(model.getSelectedDay().getMonth().getValue() + "/"
												  + model.getSelectedDay().getDayOfMonth() + "/"
												  + model.getSelectedDay().getYear(), 6);
		JTextField newEventStart = new JTextField("21:00", 4);
		
		JTextField newEventEnd = new JTextField("23:00", 4);
		JButton newEventSaveButton = new JButton("Save");
		
		JButton newEventCreateButton = new JButton("Create");
		actionBar.add(newEventCreateButton, BorderLayout.WEST);
		
		createForm.add(newEventDescription);
		createForm.add(newEventDate);
		createForm.add(newEventStart);
		createForm.add(new JLabel("to "));
		createForm.add(newEventEnd);
		createForm.add(newEventSaveButton);
		createForm.setVisible(false);
		JButton exitButton = new JButton("Exit");
		actionBar.add(exitButton, BorderLayout.EAST);
		actionBar.add(createForm, BorderLayout.CENTER);

		newEventCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createForm.setVisible(!createForm.isVisible());
				actionBar.repaint();
			}
		});

		newEventSaveButton.addActionListener(action ->{ 
			model.createEvent(newEventDescription.getText(),
							   newEventDate.getText(),
							   newEventStart.getText(),
							   newEventEnd.getText());
		});
		
		exitButton.addActionListener(args -> {
			try {
				model.createOutputFile(outputFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.dispose();
		});
		
		frame.add(actionBar, BorderLayout.NORTH);
		frame.add(currentDayFrame, BorderLayout.EAST);
		
		MonthComponent months = new MonthComponent();
		model.attach(months);
		frame.add(months, BorderLayout.WEST);

		
		frame.setPreferredSize(new Dimension(750, 350));
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.lightGray));
		frame.getContentPane().setBackground(Color.WHITE);
		//frame.add(data, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
}
