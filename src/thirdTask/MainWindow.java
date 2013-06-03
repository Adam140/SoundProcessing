package thirdTask;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import secondTask.Player;
import javax.swing.JCheckBox;

public class MainWindow {

	private JFrame frame;
	private CaptureThread captureThread;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnPlay;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textFieldThreshold;
	private JCheckBox checkBoxFile;
	private JCheckBox checkBoxRange;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					captureThread = new CaptureThread("output/temp.wav", 1, 0);
					captureThread.start();
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					btnPlay.setEnabled(false);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		});
		btnStart.setBounds(6, 60, 89, 23);
		frame.getContentPane().add(btnStart);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(captureThread != null)
				{
					captureThread.exit();
					btnPlay.setEnabled(true);
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);
				}
			}
		});
		btnStop.setEnabled(false);
		btnStop.setBounds(105, 60, 89, 23);
		frame.getContentPane().add(btnStop);

		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Player.play(new File("output/temp.wav"));
			}
		});
		btnPlay.setEnabled(false);
		btnPlay.setBounds(204, 60, 89, 23);
		frame.getContentPane().add(btnPlay);
		
		JRadioButton radioMode1 = new JRadioButton("Recording on button");
		radioMode1.setSelected(true);
		radioMode1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textFieldThreshold.setEnabled(false);
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				btnPlay.setEnabled(false);
			}
		});
		buttonGroup.add(radioMode1);
		radioMode1.setBounds(6, 7, 172, 23);
		frame.getContentPane().add(radioMode1);
		
		JRadioButton radioMode2 = new JRadioButton("Recording on voice");
		radioMode2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldThreshold.setEnabled(true);
				btnStart.setEnabled(false);
				btnStop.setEnabled(false);
				btnPlay.setEnabled(false);
				
				
				double threshold = 0d;
				try{
					threshold = Double.parseDouble(textFieldThreshold.getText());
					textFieldThreshold.setBackground(Color.white);
				}
				catch(Exception e1)
				{
				threshold = 0.05;
				textFieldThreshold.setBackground(Color.red);
				}
				
				try {
					captureThread = new CaptureThread("output/temp.wav", 2, threshold);
					captureThread.start();
					
					captureThread.join();
					System.out.println("koniec");
					btnPlay.setEnabled(true);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame,"Somethink goes wrong");
				}
			}
		});
		buttonGroup.add(radioMode2);
		radioMode2.setBounds(6, 33, 145, 23);
		frame.getContentPane().add(radioMode2);
		
		textFieldThreshold = new JTextField();
		textFieldThreshold.setEnabled(false);
		textFieldThreshold.setText("0.05");
		textFieldThreshold.setBounds(159, 34, 114, 20);
		frame.getContentPane().add(textFieldThreshold);
		textFieldThreshold.setColumns(10);
		
		JLabel lblAvgAmplitude = new JLabel("avg amplitude");
		lblAvgAmplitude.setBounds(284, 36, 89, 16);
		frame.getContentPane().add(lblAvgAmplitude);
		
		JButton btnShowDistanceGraph = new JButton("Show distance graph");
		btnShowDistanceGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame jframe = new JFrame("Graph");
				 
//				double[] s = {1d,1d,2d,3d,2d,0d};
//				double[] t = {0d,1d,1d,2d,3d,2d,1d};
//				
				String s = "trace0.csv";
				String t = "trace1.csv";
				
				DTW dtw = new DTW(t, s);
				dtw.calculateG();
				DistanceGraph distanceGraph = new DistanceGraph(dtw,checkBoxRange.isSelected());
				
		        jframe.getContentPane().add(distanceGraph);
		        
		        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        jframe.pack();
		        jframe.setVisible(true);
//		        dtw.printG();
//		        System.out.println(dtw);
		        if(checkBoxFile.isSelected())
		        	dtw.toFile("matrix.txt");
			}
		});
		btnShowDistanceGraph.setBounds(6, 224, 188, 26);
		frame.getContentPane().add(btnShowDistanceGraph);
		
		JButton btnShowPlot = new JButton("Show plot");
		btnShowPlot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				double[] s = {1d,1d,2d,3d,2d,0d};
//				double[] t = {0d,1d,1d,2d,3d,2d,1d};
//				
				String s = "trace0.csv";
				String t = "trace1.csv";
				
				DTW dtw = new DTW(t, s);
				dtw.calculateG();
				dtw.plotGraph();
				
			}
		});
		btnShowPlot.setBounds(6, 186, 188, 26);
		frame.getContentPane().add(btnShowPlot);
		
		checkBoxFile = new JCheckBox("Save g matrix to file");
		checkBoxFile.setBounds(6, 91, 145, 24);
		frame.getContentPane().add(checkBoxFile);
		
		checkBoxRange = new JCheckBox("Set range for graph");
		checkBoxRange.setBounds(6, 119, 143, 24);
		frame.getContentPane().add(checkBoxRange);
	}
}
