import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

import static javax.swing.ScrollPaneConstants.*;


public class CalendarGUI {
	
	public Calendar model;
	JFrame frame;
	
	public class DayComponent extends JPanel implements Observer {
		private static final long serialVersionUID = 1L;
		String hours;
		public DayComponent() {
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			this.setSize(500,400);
			model.viewBy("D","G");
			String[] events = model.getText().split("-");
			int count = 1;
			for(int i = 0; i < 24; i++) {
//				if(i == Integer.parseInt(events[1].split(":")[1])) {
//					this.add(new JLabel(i + ":00 - " + i+1 + ":00  -  " +
//										events[count++]
//									));
//				} else {
				add(new HourComponent(i + ":00 - " + (i+1) + ":00" + events[1]));
//				}
			}
		}
		public void viewNotify() {
			this.removeAll();
			model.viewBy("D","G");
			String[] events = model.getText().split("-");
			int count = 1;
			for(int i = 0; i < 24; i++) {
//				if(i == Integer.parseInt(events[1].split(":")[1])) {
//					this.add(new JLabel(i + ":00 - " + i+1 + ":00  -  " +
//										events[count++]
//									));
//				} else {
					add(new HourComponent(i + ":00 - " + (i+1) + ":00" + events[1]));
//				}
			}
			revalidate();
		}
	}
	
	public class HourComponent extends JPanel {
		public HourComponent(String timeSlot){
			add(new JLabel(timeSlot), BorderLayout.WEST);
			add(new JLabel("Potential Event"), BorderLayout.CENTER);
			this.setSize(500,400);

		}
		
	}
	

	public class MonthComponent extends JPanel implements Observer {
		private static final long serialVersionUID = 1L;
		public void viewNotify() {
			this.removeAll();
			model.viewBy("M","G");
			String[] components = model.getText().split("\n"); 
			
			this.add(new JLabel(components[0]), BorderLayout.NORTH);
			
			JPanel weeks = new JPanel(new GridLayout(0,7,2,2));
			String[] dayNames = components[1].split(" ");
			for(int i = 0; i < dayNames.length; i++) {
				JLabel dayName = new JLabel(dayNames[i]);
				weeks.add(dayName);
			}
			for(int i = 0; i < 7*5; i++) {
				String cur = components[2+(i/7)].split(" ")[i%7];
				if(cur.contains("|")) {
					weeks.add(new JLabel());
				} else {
					weeks.add(new JButton(cur));
				}
			}
			this.add(weeks, BorderLayout.CENTER);
			revalidate();
			
		}
		public MonthComponent() {
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(500, 400));

			model.viewBy("M","G");
			String[] components = model.getText().split("\n"); 
			
			this.add(new JLabel(components[0]), BorderLayout.NORTH);
			
			JPanel weeks = new JPanel(new GridLayout(0,7,2,2));
			String[] dayNames = components[1].split(" ");
			for(int i = 0; i < dayNames.length; i++) {
				JLabel dayName = new JLabel(dayNames[i]);
				weeks.add(dayName);
			}
			for(int i = 0; i < 7*5; i++) {
				String cur = components[2+(i/7)].split(" ")[i%7];
				if(cur.contains("|")) {
					weeks.add(new JLabel());
				} else {
					weeks.add(new JButton(cur));
				}
			}
			
			this.add(weeks, BorderLayout.CENTER);
			
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
		frame = new JFrame();
		
		createDayPanel();
		createActionBar(outputFile);
		createMonthPanel();
		
		frame.setPreferredSize(new Dimension(850, 450));
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.lightGray));
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private void createActionBar(String outputFile) {
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

	private void createDayPanel() {
		JPanel currentDayFrame = new JPanel();
		currentDayFrame.setBorder(BorderFactory.createLineBorder(Color.WHITE, 8));
		currentDayFrame.setLayout(new BorderLayout());
		DayComponent label1 = new DayComponent();
		JScrollPane scrollPane = new JScrollPane(label1);
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
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
		frame.add(currentDayFrame, BorderLayout.CENTER);
	}
	
	private void createMonthPanel() {
		MonthComponent month = new MonthComponent();
		month.setBorder(BorderFactory.createLineBorder(Color.WHITE, 8));
		model.attach(month);
		frame.add(month, BorderLayout.WEST);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
