import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CalendarGUI {
	
	public Calendar model;

	public class DayComponent extends JPanel implements Observer {
		private static final long serialVersionUID = 1L;
		String hours;
		public DayComponent() {
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			model.viewBy("D","G");
			String[] events = model.getText().split("-");
			int count = 1;
			for(int i = 0; i < 24; i++) {
//				if(i == Integer.parseInt(events[1].split(":")[1])) {
//					this.add(new JLabel(i + ":00 - " + i+1 + ":00  -  " +
//										events[count++]
//									));
//				} else {
				add(new HourComponent(i + ":00 - " + (i+1) + ":00"));
//				}
			}
		}
		public void viewNotify() {
			model.viewBy("D","G");
			String[] events = model.getText().split("-");
			int count = 1;
			for(int i = 0; i < 24; i++) {
//				if(i == Integer.parseInt(events[1].split(":")[1])) {
//					this.add(new JLabel(i + ":00 - " + i+1 + ":00  -  " +
//										events[count++]
//									));
//				} else {
					add(new HourComponent(i + ":00 - " + (i+1) + ":00"));
//				}
			}
		}
	}
	
	public class HourComponent extends JPanel {
		public HourComponent(String timeSlot){
			add(new JLabel(timeSlot), BorderLayout.WEST);
			add(new JLabel("Potential Event"), BorderLayout.CENTER);
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
	
	public class CreateDateComponent extends JTextField implements Observer {
		private static final long serialVersionUID = 1L;
		public void viewNotify() {
			model.viewBy("D","G");
			this.setText(model.getSelectedDay().getMonth().getValue() + "/"
					  + model.getSelectedDay().getDayOfMonth() + "/"
					  + model.getSelectedDay().getYear());
		}
		public CreateDateComponent() {
			super(model.getSelectedDay().getMonth().getValue() + "/"
					  + model.getSelectedDay().getDayOfMonth() + "/"
					  + model.getSelectedDay().getYear(), 6);
		}
	}
	
	public CalendarGUI(String inputFile, String outputFile) throws Exception {
		
		model = new Calendar(inputFile);
		
		JFrame frame = new JFrame();
		
		createCurrentDayFrame(frame);
		createActionBar(frame, outputFile);

		MonthComponent months = new MonthComponent();
		model.attach(months);
		frame.add(months, BorderLayout.WEST);

		
		frame.setPreferredSize(new Dimension(850, 450));
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.lightGray));
		frame.getContentPane().setBackground(Color.WHITE);
		//frame.add(data, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void createActionBar(JFrame frame, String outputFile) {
		// TODO Auto-generated method stub
		JPanel actionBar = new JPanel(new BorderLayout());
		actionBar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 8));
		//actionBar.setLayout();
		JPanel createForm = new JPanel();
		JTextField newEventDescription = new JTextField("Description Here", 30);
		CreateDateComponent newEventDate = new CreateDateComponent();
		model.attach(newEventDate);
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
		
	}

	private void createCurrentDayFrame(JFrame frame) {
		JPanel currentDayFrame = new JPanel();
		currentDayFrame.setLayout(new BorderLayout());
		DayComponent label1 = new DayComponent();
		JScrollPane scrollPane = new JScrollPane(label1);
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
		JPanel changeDay = new JPanel();
		changeDay.add(previousDayButton, BorderLayout.EAST);
		changeDay.add(nextDayButton, BorderLayout.WEST);
		currentDayFrame.add(changeDay, BorderLayout.NORTH);
		currentDayFrame.add(scrollPane, BorderLayout.CENTER);
		frame.add(currentDayFrame, BorderLayout.EAST);
	}
	
}
