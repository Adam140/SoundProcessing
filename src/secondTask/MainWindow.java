package secondTask;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import firstTask.ConsoleUtil;
import firstTask.WavFileGenerator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JSeparator;

public class MainWindow {

	private JFrame frame;
	private JTextField jtfCustomHz;
	private JTextField jtfDuration;
	private JTextArea console;
	private JButton btnPlay;
	private final File staticFile = new File("./output/player.wav");	// from this we will play sound
	private JComboBox comboWave;
	private JTextField tf_sin_hz;
	private JTextField tf_tri_hz;
	private JTextField tf_saw_hz;
	private JTextField tf_rec_hz;
	private JTextField tf_red_hz;
	private JTextField tf_white_hz;

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
		frame.setBounds(100, 100, 562, 378);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		console = new JTextArea();
		console.setBounds(10, 27, 154, 210);
		frame.getContentPane().add(console);
		
		JLabel lblMenu = new JLabel("Menu");
		lblMenu.setBounds(190, 13, 46, 14);
		frame.getContentPane().add(lblMenu);
		
		btnPlay = new JButton("Play");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				WavFileGenerator output = new WavFileGenerator(staticFile, ConsoleUtil.convertText(console.getText()), comboWave.getSelectedIndex());
				output.write();
				
				Player.play(staticFile);
			}
		});
		btnPlay.setBounds(174, 27, 121, 23);
		frame.getContentPane().add(btnPlay);
		
		JButton btnGenerateOcean = new JButton("Ocean effect");
		btnGenerateOcean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGenerateOcean.setBounds(174, 91, 121, 23);
		frame.getContentPane().add(btnGenerateOcean);
		
		JButton btn100Hz = new JButton("100 Hz");
		btn100Hz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(Util.isNumber(jtfDuration.getText()))
				{
				String temp = "";
				temp += "100.0,";
				temp += jtfDuration.getText() + ";\n";
				console.setText(console.getText() + temp);
				}
				else
				{
					JOptionPane.showMessageDialog(frame,"Enter correct duration");
				}
			}
		});
		btn100Hz.setBounds(10, 250, 85, 23);
		frame.getContentPane().add(btn100Hz);
		
		JButton btn200Hz = new JButton("200 Hz");
		btn200Hz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		btn200Hz.setBounds(10, 277, 85, 23);
		frame.getContentPane().add(btn200Hz);
		
		JButton btn300Hz = new JButton("300 Hz");
		btn300Hz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn300Hz.setBounds(100, 250, 85, 23);
		frame.getContentPane().add(btn300Hz);
		
		JButton btn400Hz = new JButton("400 Hz");
		btn400Hz.setBounds(100, 277, 85, 23);
		frame.getContentPane().add(btn400Hz);
		
		jtfCustomHz = new JTextField();
		jtfCustomHz.setText("500");
		jtfCustomHz.setBounds(190, 249, 86, 20);
		frame.getContentPane().add(jtfCustomHz);
		jtfCustomHz.setColumns(10);
		
		JButton btnCustomHz = new JButton("Custom Hz");
		btnCustomHz.setBounds(190, 277, 122, 23);
		frame.getContentPane().add(btnCustomHz);
		
		jtfDuration = new JTextField();
		jtfDuration.setHorizontalAlignment(SwingConstants.CENTER);
		jtfDuration.setText("200");
		jtfDuration.setBounds(190, 217, 62, 20);
		frame.getContentPane().add(jtfDuration);
		jtfDuration.setColumns(10);
		
		JLabel lblDuration = new JLabel("Duration");
		lblDuration.setBounds(190, 200, 71, 14);
		frame.getContentPane().add(lblDuration);
		
		JLabel lblNewLabel = new JLabel("ms");
		lblNewLabel.setBounds(259, 223, 36, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblHz = new JLabel("Hz");
		lblHz.setBounds(288, 254, 24, 14);
		frame.getContentPane().add(lblHz);
		
		JButton btnClearConsole = new JButton("Clear console");
		btnClearConsole.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				console.setText("");
			}
		});
		btnClearConsole.setBounds(190, 311, 121, 23);
		frame.getContentPane().add(btnClearConsole);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!console.getText().equals(""))
				{
					DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
					Date date = new Date();
					String file = "./output/" + dateFormat.format(date) + ".wav";
					WavFileGenerator output = new WavFileGenerator(new File(file), ConsoleUtil.convertText(console.getText()), comboWave.getSelectedIndex());
					output.write();
					JOptionPane.showMessageDialog(frame, "Save in " + file);
				}
			}
		});
		btnSave.setBounds(176, 56, 121, 23);
		frame.getContentPane().add(btnSave);
		
		comboWave = new JComboBox();
		comboWave.setModel(new DefaultComboBoxModel(new String[] {"sinusoidal wave", "triangular wave", "sawtooth wave", "rectangular wave", "red noise", "white noise"}));
		comboWave.setBounds(10, 311, 175, 20);
		frame.getContentPane().add(comboWave);
		
		JButton btnSinusoidal = new JButton("Sinusoidal");
		btnSinusoidal.setBounds(326, 27, 89, 23);
		frame.getContentPane().add(btnSinusoidal);
		
		JLabel lblGenerators = new JLabel("Generators");
		lblGenerators.setBounds(372, 13, 62, 14);
		frame.getContentPane().add(lblGenerators);
		
		tf_sin_hz = new JTextField();
		tf_sin_hz.setBounds(420, 28, 46, 20);
		frame.getContentPane().add(tf_sin_hz);
		tf_sin_hz.setColumns(10);
		
		JButton btnTriangular = new JButton("Triangular");
		btnTriangular.setBounds(326, 56, 89, 23);
		frame.getContentPane().add(btnTriangular);
		
		tf_tri_hz = new JTextField();
		tf_tri_hz.setColumns(10);
		tf_tri_hz.setBounds(420, 57, 46, 20);
		frame.getContentPane().add(tf_tri_hz);
		
		JButton btnSawtooth = new JButton("Sawtooth ");
		btnSawtooth.setBounds(326, 88, 89, 23);
		frame.getContentPane().add(btnSawtooth);
		
		tf_saw_hz = new JTextField();
		tf_saw_hz.setColumns(10);
		tf_saw_hz.setBounds(420, 89, 46, 20);
		frame.getContentPane().add(tf_saw_hz);
		
		JButton btnRectangular = new JButton("Rectangular");
		btnRectangular.setBounds(326, 119, 91, 23);
		frame.getContentPane().add(btnRectangular);
		
		tf_rec_hz = new JTextField();
		tf_rec_hz.setColumns(10);
		tf_rec_hz.setBounds(420, 120, 46, 20);
		frame.getContentPane().add(tf_rec_hz);
		
		JButton btnRedNoise = new JButton("Red noise");
		btnRedNoise.setBounds(326, 153, 89, 23);
		frame.getContentPane().add(btnRedNoise);
		
		tf_red_hz = new JTextField();
		tf_red_hz.setEnabled(false);
		tf_red_hz.setEditable(false);
		tf_red_hz.setColumns(10);
		tf_red_hz.setBounds(420, 154, 46, 20);
		frame.getContentPane().add(tf_red_hz);
		
		JButton btnWhiteNoise = new JButton("White noise");
		btnWhiteNoise.setBounds(326, 185, 89, 23);
		frame.getContentPane().add(btnWhiteNoise);
		
		tf_white_hz = new JTextField();
		tf_white_hz.setEnabled(false);
		tf_white_hz.setEditable(false);
		tf_white_hz.setColumns(10);
		tf_white_hz.setBounds(420, 186, 46, 20);
		frame.getContentPane().add(tf_white_hz);
		
		JCheckBox chckbxLowpassFilter = new JCheckBox("Low-pass filter");
		chckbxLowpassFilter.setBounds(326, 216, 97, 23);
		frame.getContentPane().add(chckbxLowpassFilter);
		
		JSlider slider = new JSlider();
		slider.setBounds(420, 250, 110, 23);
		frame.getContentPane().add(slider);
		
		JLabel lblSmoothness = new JLabel("Cut-off frequency");
		lblSmoothness.setBounds(322, 254, 93, 14);
		frame.getContentPane().add(lblSmoothness);
		
		JLabel lblResonance = new JLabel("Resonance");
		lblResonance.setBounds(322, 281, 93, 14);
		frame.getContentPane().add(lblResonance);
		
		JSlider slider_1 = new JSlider();
		slider_1.setBounds(420, 277, 110, 23);
		frame.getContentPane().add(slider_1);
	}
}
