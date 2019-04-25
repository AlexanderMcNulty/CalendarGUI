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

	public class hourComponent extends JTextArea implements Observer {

		/**
		 * Default value, used to suppress warning
		 */
		private static final long serialVersionUID = 1L;
		public void viewNotify() {
			// TODO Auto-generated method stub
			this.setText(model.getText());
		}
		public hourComponent() {
			
		}
	}
	
	public CalendarGUI(String inputFile, String outputFile) throws Exception {
		
		model = new Calendar(inputFile);
		
		JFrame frame = new JFrame();
		JPanel currentDayFrame = new JPanel();
		
		hourComponent label1 = new hourComponent();	
		
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
		model.viewBy("D","G");
		label1.setText(model.getText());
		

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
		actionBar.add(createForm, BorderLayout.CENTER);

		newEventCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createForm.setVisible(!createForm.isVisible());
				actionBar.repaint();
			}
		});

		newEventSaveButton.addActionListener(action ->{ 
			model.getEvents().checkEvents(newEventDescription.getText(),
							   newEventDate.getText(),
							   newEventStart.getText(),
							   newEventEnd.getText());
			model.viewBy("D","G");
			label1.setText(model.getText());
			//currentDayFrame.repaint();
			label1.repaint();
		});
		
		frame.add(actionBar, BorderLayout.NORTH);
		frame.add(currentDayFrame, BorderLayout.EAST);

		
		//readline read line by line contrusting leaf shaps to build a composite shpe
		JTextArea label2 = new JTextArea();
		model.viewBy("M", "G");
		label2.setText(model.getText());
		frame.add(label2, BorderLayout.WEST);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(args -> {
			try {
				model.createOutputFile(outputFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.dispose();
		});
		
		frame.setPreferredSize(new Dimension(750, 350));
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.lightGray));
		frame.getContentPane().setBackground(Color.WHITE);
		//frame.add(data, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
}
