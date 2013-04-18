package secondTask;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainWindow {

	private JFrame frame;
	private JTextField jtfCustomHz;
	private JTextField jtfDuration;

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
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setBounds(100, 100, 299, 351);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea console = new JTextArea();
		console.setBounds(10, 27, 154, 210);
		frame.getContentPane().add(console);
		
		JLabel lblMenu = new JLabel("Menu");
		lblMenu.setBounds(203, 11, 46, 14);
		frame.getContentPane().add(lblMenu);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(174, 27, 102, 23);
		frame.getContentPane().add(btnPlay);
		
		JButton btnGenerateOcean = new JButton("Ocean effect");
		btnGenerateOcean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGenerateOcean.setBounds(174, 61, 102, 23);
		frame.getContentPane().add(btnGenerateOcean);
		
		JButton btn100Hz = new JButton("100 Hz");
		btn100Hz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn100Hz.setBounds(10, 248, 70, 23);
		frame.getContentPane().add(btn100Hz);
		
		JButton btn200Hz = new JButton("200 Hz");
		btn200Hz.setBounds(10, 277, 70, 23);
		frame.getContentPane().add(btn200Hz);
		
		JButton btn300Hz = new JButton("300 Hz");
		btn300Hz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn300Hz.setBounds(90, 248, 70, 23);
		frame.getContentPane().add(btn300Hz);
		
		JButton btn400Hz = new JButton("400 Hz");
		btn400Hz.setBounds(90, 277, 70, 23);
		frame.getContentPane().add(btn400Hz);
		
		jtfCustomHz = new JTextField();
		jtfCustomHz.setText("500");
		jtfCustomHz.setBounds(174, 249, 86, 20);
		frame.getContentPane().add(jtfCustomHz);
		jtfCustomHz.setColumns(10);
		
		JButton btnCustomHz = new JButton("Custom Hz");
		btnCustomHz.setBounds(171, 277, 89, 23);
		frame.getContentPane().add(btnCustomHz);
		
		jtfDuration = new JTextField();
		jtfDuration.setHorizontalAlignment(SwingConstants.CENTER);
		jtfDuration.setText("200");
		jtfDuration.setBounds(174, 217, 62, 20);
		frame.getContentPane().add(jtfDuration);
		jtfDuration.setColumns(10);
		
		JLabel lblDuration = new JLabel("Duration");
		lblDuration.setBounds(178, 200, 46, 14);
		frame.getContentPane().add(lblDuration);
		
		JLabel lblNewLabel = new JLabel("ms");
		lblNewLabel.setBounds(247, 220, 36, 14);
		frame.getContentPane().add(lblNewLabel);
	}
}
