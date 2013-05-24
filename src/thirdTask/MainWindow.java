package thirdTask;

import java.awt.EventQueue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

import secondTask.Player;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainWindow {

	private JFrame frame;
	private CaptureThread captureThread;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnPlay;

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
					captureThread = new CaptureThread("output/temp.wav");
					captureThread.start();
					btnStart.setEnabled(false);
					btnStop.setEnabled(true);
					btnPlay.setEnabled(false);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		});
		btnStart.setBounds(10, 11, 89, 23);
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
		btnStop.setBounds(109, 11, 89, 23);
		frame.getContentPane().add(btnStop);

		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Player.play(new File("output/temp.wav"));
			}
		});
		btnPlay.setEnabled(false);
		btnPlay.setBounds(208, 11, 89, 23);
		frame.getContentPane().add(btnPlay);
	}

}
