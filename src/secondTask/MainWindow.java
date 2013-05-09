package secondTask;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import firstTask.ConsoleUtil;
import firstTask.WavFileGenerator;

public class MainWindow implements ActionListener, KeyListener {

	private JFrame frame;
	private JTextField jtfCustomHz;
	private JTextField jtfDuration;
	private JTextArea console;
	private JButton btnPlay;
	private final File staticFile = new File("./output/player.wav"); // from
																		// this
																		// we
																		// will
																		// play
																		// sound
	private JComboBox comboWave;
	private JTextField tf_sin_hz;
	private JTextField tf_tri_hz;
	private JTextField tf_saw_hz;
	private JTextField tf_rec_hz;
	private JTextField tf_red_hz;
	private JTextField tf_white_hz;
	private JButton btnSinusoidal;
	private JButton btnWhiteNoise;
	private JButton btnTriangular;
	private JButton btnRectangular;
	private JLabel iconRedN;
	private JLabel iconSaw;
	private JLabel iconWhiteN;
	private JLabel iconSin;
	private JLabel iconRect;
	private JLabel iconTrian;
	private ImageIcon iconOn = new ImageIcon("icon/on.png", "on");
	private ImageIcon iconOff = new ImageIcon("icon/off.png", "off");
	private Properties waves = new Properties();
	private RealTimePlayerFacade realTimePlayer = new RealTimePlayerFacade(waves);
	public static JTextField tf_fc;
	public static JTextField tf_Q;
	public static JTextField tf_amplifier;
	public static JCheckBox chckbxLowpassFilter;

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
		frame.setBounds(100, 100, 654, 378);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		console = new JTextArea();
		console.setBounds(10, 27, 154, 210);
		frame.getContentPane().add(console);

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
				if (Util.isNumber(jtfDuration.getText())) {
					String temp = "";
					temp += "100.0,";
					temp += jtfDuration.getText() + ";\n";
					console.setText(console.getText() + temp);
				} else {
					JOptionPane.showMessageDialog(frame, "Enter correct duration");
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
		btnCustomHz.setBounds(190, 277, 112, 23);
		frame.getContentPane().add(btnCustomHz);

		jtfDuration = new JTextField();
		jtfDuration.setHorizontalAlignment(SwingConstants.CENTER);
		jtfDuration.setText("200");
		jtfDuration.setBounds(190, 217, 62, 20);
		frame.getContentPane().add(jtfDuration);
		jtfDuration.setColumns(10);

		JLabel lblDuration = new JLabel("Duration");
		lblDuration.setBounds(190, 200, 71, 16);
		frame.getContentPane().add(lblDuration);

		JLabel lblNewLabel = new JLabel("ms");
		lblNewLabel.setBounds(259, 223, 36, 16);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblHz = new JLabel("Hz");
		lblHz.setBounds(288, 254, 24, 16);
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

		chckbxLowpassFilter = new JCheckBox("Low-pass filter");
		chckbxLowpassFilter.setBounds(390, 214, 122, 23);
		frame.getContentPane().add(chckbxLowpassFilter);

		tf_fc = new JTextField();
		tf_fc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( RealTimePlayer.filter != null)
				{
					RealTimePlayer.filter.updateDate(Double.valueOf(tf_fc.getText()), Double.valueOf(tf_Q.getText()));
					RealTimePlayer.filter.setParametersForLPF();
				}
			}
		});
		tf_fc.setText("100");
		tf_fc.setBounds(426, 251, 86, 20);
		frame.getContentPane().add(tf_fc);
		tf_fc.setColumns(10);

		tf_Q = new JTextField();
		tf_Q.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( RealTimePlayer.filter != null)
				{
					RealTimePlayer.filter.updateDate(Double.valueOf(tf_fc.getText()), Double.valueOf(tf_Q.getText()));
					RealTimePlayer.filter.setParametersForLPF();
				}
			}
		});
		tf_Q.setText("1");
		tf_Q.setBounds(426, 278, 86, 20);
		frame.getContentPane().add(tf_Q);
		tf_Q.setColumns(10);

		JLabel lblAmplifier = new JLabel("Amplifier");
		lblAmplifier.setBounds(321, 311, 46, 14);
		frame.getContentPane().add(lblAmplifier);

		tf_amplifier = new JTextField();
		tf_amplifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( RealTimePlayer.filter != null)
				{
					RealTimePlayer.filter.setAmplifiler(Double.valueOf(tf_amplifier.getText()));
				}
			}
		});
		tf_amplifier.setText("1");
		tf_amplifier.setColumns(10);
		tf_amplifier.setBounds(426, 309, 86, 20);
		frame.getContentPane().add(tf_amplifier);

		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!console.getText().equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
					Date date = new Date();
					String file = "./output/" + dateFormat.format(date) + ".wav";
					WavFileGenerator output = new WavFileGenerator(new File(file), ConsoleUtil.convertText(console.getText()), comboWave.getSelectedIndex());
					if (chckbxLowpassFilter.isSelected()) {
						Filter f = new Filter(44100, Double.valueOf(tf_fc.getText()), Double.valueOf(tf_Q.getText()));
						f.setParametersForLPF();
						output.filter = f;
						f.setAmplifiler(Double.valueOf(tf_amplifier.getText()));

						output.setUse_filter(true);
					} else {
						output.setUse_filter(false);
					}

					output.write();
					JOptionPane.showMessageDialog(frame, "Save in " + file);
				}
			}
		});
		btnSave.setBounds(176, 56, 121, 23);
		frame.getContentPane().add(btnSave);

		comboWave = new JComboBox();
		comboWave.setModel(new DefaultComboBoxModel(new String[] { "sinusoidal wave", "triangular wave", "sawtooth wave", "rectangular wave", "red noise", "white noise" }));
		comboWave.setBounds(10, 311, 175, 20);
		frame.getContentPane().add(comboWave);

		btnSinusoidal = new JButton("Sinusoidal");
		btnSinusoidal.setActionCommand("sinusoidal_ON");
		btnSinusoidal.setName(WaveType.SINUSOIDAL.toString());
		btnSinusoidal.setBounds(390, 24, 110, 23);
		btnSinusoidal.addActionListener(this);
		frame.getContentPane().add(btnSinusoidal);

		JLabel lblGenerators = new JLabel("Generators");
		lblGenerators.setBounds(324, 12, 104, 16);
		frame.getContentPane().add(lblGenerators);

		tf_sin_hz = new JTextField();
		tf_sin_hz.setName(WaveType.SINUSOIDAL.toString());
		tf_sin_hz.addKeyListener(this);
		tf_sin_hz.setBounds(512, 25, 54, 20);
		frame.getContentPane().add(tf_sin_hz);
		tf_sin_hz.setColumns(10);

		btnTriangular = new JButton("Triangular");
		btnTriangular.setActionCommand("triangular_ON");
		btnTriangular.setName(WaveType.TRIANGULAR.toString());
		btnTriangular.setBounds(392, 56, 108, 23);
		btnTriangular.addActionListener(this);
		frame.getContentPane().add(btnTriangular);

		tf_tri_hz = new JTextField();
		tf_tri_hz.setName(WaveType.TRIANGULAR.toString());
		tf_tri_hz.setColumns(10);
		tf_tri_hz.setBounds(512, 57, 54, 20);
		tf_tri_hz.addKeyListener(this);
		frame.getContentPane().add(tf_tri_hz);

		JButton btnSawtooth = new JButton("Sawtooth");
		btnSawtooth.setName(WaveType.SAWTOOTH.toString());
		btnSawtooth.setActionCommand("sawtooth_ON");
		btnSawtooth.setBounds(390, 91, 110, 23);
		btnSawtooth.addActionListener(this);
		frame.getContentPane().add(btnSawtooth);

		tf_saw_hz = new JTextField();
		tf_saw_hz.setName(WaveType.SAWTOOTH.toString());
		tf_saw_hz.setColumns(10);
		tf_saw_hz.setBounds(512, 92, 54, 20);
		tf_saw_hz.addKeyListener(this);
		frame.getContentPane().add(tf_saw_hz);

		btnRectangular = new JButton("Rectangular");
		btnRectangular.setActionCommand("rectangular_ON");
		btnRectangular.setName(WaveType.RECTANGULAR.toString());
		btnRectangular.setBounds(390, 119, 110, 23);
		btnRectangular.addActionListener(this);
		frame.getContentPane().add(btnRectangular);

		tf_rec_hz = new JTextField();
		tf_rec_hz.setName(WaveType.RECTANGULAR.toString());
		tf_rec_hz.setColumns(10);
		tf_rec_hz.setBounds(512, 120, 54, 20);
		tf_rec_hz.addKeyListener(this);
		frame.getContentPane().add(tf_rec_hz);

		JButton btnRedNoise = new JButton("Red noise");
		btnRedNoise.setActionCommand("redNoise_ON");
		btnRedNoise.setName(WaveType.RED_NOISE.toString());
		btnRedNoise.setBounds(390, 153, 110, 23);
		btnRedNoise.addActionListener(this);
		frame.getContentPane().add(btnRedNoise);

		tf_red_hz = new JTextField();
		tf_red_hz.setEnabled(false);
		tf_red_hz.setEditable(false);
		tf_red_hz.setColumns(10);
		tf_red_hz.setBounds(512, 154, 54, 20);
		frame.getContentPane().add(tf_red_hz);

		btnWhiteNoise = new JButton("White noise");
		btnWhiteNoise.setActionCommand("whiteNoise_ON");
		btnWhiteNoise.setName(WaveType.WHITE_NOISE.toString());
		btnWhiteNoise.setBounds(390, 185, 110, 23);
		btnWhiteNoise.addActionListener(this);
		frame.getContentPane().add(btnWhiteNoise);

		tf_white_hz = new JTextField();
		tf_white_hz.setEnabled(false);
		tf_white_hz.setEditable(false);
		tf_white_hz.setColumns(10);
		tf_white_hz.setBounds(512, 186, 54, 20);
		frame.getContentPane().add(tf_white_hz);

		JLabel lblSmoothness = new JLabel("Cut-off frequency");
		lblSmoothness.setBounds(324, 253, 110, 16);
		frame.getContentPane().add(lblSmoothness);

		JLabel lblResonance = new JLabel("Resonance");
		lblResonance.setBounds(322, 281, 93, 16);
		frame.getContentPane().add(lblResonance);

		ImageIcon iconOff = new ImageIcon("icon/off.png", "off");

		iconSin = new JLabel(iconOff);
		iconSin.setBounds(569, 27, 16, 16);
		frame.getContentPane().add(iconSin);

		iconTrian = new JLabel("", iconOff, JLabel.CENTER);
		iconTrian.setBounds(569, 60, 16, 16);
		frame.getContentPane().add(iconTrian);

		iconSaw = new JLabel("", iconOff, JLabel.CENTER);
		iconSaw.setBounds(569, 95, 16, 16);
		frame.getContentPane().add(iconSaw);

		iconRect = new JLabel("", iconOff, JLabel.CENTER);
		iconRect.setBounds(569, 123, 16, 16);
		frame.getContentPane().add(iconRect);

		iconRedN = new JLabel("", iconOff, JLabel.CENTER);
		iconRedN.setBounds(569, 157, 16, 16);
		frame.getContentPane().add(iconRedN);

		iconWhiteN = new JLabel("", iconOff, JLabel.CENTER);
		iconWhiteN.setBounds(569, 189, 16, 16);
		frame.getContentPane().add(iconWhiteN);

		final JButton btnPlayRealTime = new JButton("Play");
		btnPlayRealTime.setActionCommand("PLAY");
		btnPlayRealTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if("PLAY".equals(btnPlayRealTime.getActionCommand()))
				{
					btnPlayRealTime.setActionCommand("STOP");
					btnPlayRealTime.setText("Stop");
					realTimePlayer.play();
				}
				else if("STOP".equals(btnPlayRealTime.getActionCommand()))
				{
					btnPlayRealTime.setActionCommand("PLAY");
					btnPlayRealTime.setText("Play");
					realTimePlayer.stop();
				}
			}
		});
		btnPlayRealTime.setBounds(522, 211, 98, 26);
		frame.getContentPane().add(btnPlayRealTime);
		
		JLabel lblTo = new JLabel("to");
		lblTo.setBounds(512, 254, 16, 14);
		frame.getContentPane().add(lblTo);

	}

	public JButton getBtnSinusoidal() {
		return btnSinusoidal;
	}

	public JButton getBtnWhiteNoise() {
		return btnWhiteNoise;
	}

	public JButton getBtnTriangular() {
		return btnTriangular;
	}

	public JButton getBtnRectangular() {
		return btnRectangular;
	}

	public JLabel getIconRedN() {
		return iconRedN;
	}

	public JLabel getIconSaw() {
		return iconSaw;
	}

	public JLabel getIconWhiteN() {
		return iconWhiteN;
	}

	public JLabel getIconSin() {
		return iconSin;
	}

	public JLabel getIconRect() {
		return iconRect;
	}

	public JLabel getIconTrian() {
		return iconTrian;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		String command = event.getActionCommand();

		if (command.startsWith("sinusoidal")) {
			if (command.endsWith("_ON")) {
				getIconSin().setIcon(iconOn);
				((JButton) event.getSource()).setActionCommand("sinusoidal_OFF");
			} else {
				tf_sin_hz.setText("0.0");
				waves.put(WaveType.SINUSOIDAL, 0.0);
				getIconSin().setIcon(iconOff);
				((JButton) event.getSource()).setActionCommand("sinusoidal_ON");
			}

		} else if (command.startsWith("triangular")) {
			if (command.endsWith("_ON")) {
				getIconTrian().setIcon(iconOn);
				((JButton) event.getSource()).setActionCommand("triangular_OFF");
			} else {
				tf_tri_hz.setText("0.0");
				waves.put(WaveType.TRIANGULAR, 0.0);
				getIconTrian().setIcon(iconOff);
				((JButton) event.getSource()).setActionCommand("triangular_ON");
			}

		} else if (command.startsWith("sawtooth")) {
			if (command.endsWith("_ON")) {
				getIconSaw().setIcon(iconOn);
				((JButton) event.getSource()).setActionCommand("sawtooth_OFF");
			} else {
				tf_saw_hz.setText("0.0");
				waves.put(WaveType.SAWTOOTH, 0.0);
				getIconSaw().setIcon(iconOff);
				((JButton) event.getSource()).setActionCommand("sawtooth_ON");
			}

		} else if (command.startsWith("rectangular")) {
			if (command.endsWith("_ON")) {
				getIconRect().setIcon(iconOn);
				((JButton) event.getSource()).setActionCommand("rectangular_OFF");
			} else {
				tf_rec_hz.setText("0.0");
				waves.put(WaveType.RECTANGULAR, 0.0);
				getIconRect().setIcon(iconOff);
				((JButton) event.getSource()).setActionCommand("rectangular_ON");
			}

		} else if (command.startsWith("redNoise")) {
			if (command.endsWith("_ON")) {
				tf_red_hz.setText("1");
				getIconRedN().setIcon(iconOn);
				((JButton) event.getSource()).setActionCommand("redNoise_OFF");
				waves.put(WaveType.RED_NOISE, 1.0);
			} else {
				tf_red_hz.setText("0");
				getIconRedN().setIcon(iconOff);
				((JButton) event.getSource()).setActionCommand("redNoise_ON");
				waves.put(WaveType.RED_NOISE, 0.0);
			}

		} else if (command.startsWith("whiteNoise")) {
			if (command.endsWith("_ON")) {
				tf_white_hz.setText("1");
				getIconWhiteN().setIcon(iconOn);
				((JButton) event.getSource()).setActionCommand("whiteNoise_OFF");
				waves.put(WaveType.WHITE_NOISE, 1.0);
			} else {
				tf_white_hz.setText("0");
				getIconWhiteN().setIcon(iconOff);
				((JButton) event.getSource()).setActionCommand("whiteNoise_ON");
				waves.put(WaveType.WHITE_NOISE, 0.0);
			}

		}
		realTimePlayer.changeParams();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		WaveType wt = WaveType.valueOf(((JTextField) arg0.getSource()).getName());
		Component[] components = frame.getContentPane().getComponents();
		
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof JButton && wt.toString().equals(components[i].getName())) {
				if (((JButton) components[i]).getActionCommand().endsWith("_ON"))
					((JButton) components[i]).doClick();
			}
		}

		try {
			double freq = Double.parseDouble(((JTextField) arg0.getSource()).getText());
			switch (wt) {
			case SINUSOIDAL:
				waves.put(WaveType.SINUSOIDAL, freq);
				break;
			case TRIANGULAR:
				waves.put(WaveType.TRIANGULAR, freq);
				break;
			case SAWTOOTH:
				waves.put(WaveType.SAWTOOTH, freq);
				break;
			case RECTANGULAR:
				waves.put(WaveType.RECTANGULAR, freq);
				break;
			default:
				break;
			}
			((JTextField) arg0.getSource()).setBackground(Color.white);
			realTimePlayer.changeParams();
		} catch (Exception e) {
			((JTextField) arg0.getSource()).setBackground(Color.red);
			switch (wt) {
			case SINUSOIDAL:
				waves.put(WaveType.SINUSOIDAL, 0.0);
				break;
			case TRIANGULAR:
				waves.put(WaveType.TRIANGULAR, 0.0);
				break;
			case SAWTOOTH:
				waves.put(WaveType.SAWTOOTH, 0.0);
				break;
			case RECTANGULAR:
				waves.put(WaveType.RECTANGULAR, 0.0);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}
}
