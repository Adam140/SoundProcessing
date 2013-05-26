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
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import secondTask.Player;

public class MainWindow {

	private JFrame frame;
	private CaptureThread captureThread;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnPlay;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textFieldThreshold;

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
	}
}
