package thirdTask;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.math.plot.utils.Array;

import secondTask.Player;
import firstTask.WavFile;
import firstTask.WindowFunction;

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
	private double[] points; // all values of wave
	private double[][] dividedPoints; // all values of wave after sampling
	private File input;
	double[] co = null;
	private final ImageIcon iconReady = new ImageIcon("icon/on.png", "on");
	private final ImageIcon iconRec = new ImageIcon("icon/off.png", "off");
	private final ImageIcon iconStop = new ImageIcon("icon/stop.png", "stop");

	public File getInput() {
		return input;
	}

	public void setInput(File input) {
		this.input = input;
	}

	public int sampleRate;
	private JTextField tf_pattern;
	private JButton btnFindBest;
	JLabel found = new JLabel("");
	private String foundFile;
	JLabel lblMinPath = new JLabel("");
	private JComboBox<String> comboBox;
	private JCheckBox checkBoxResize;
	private JCheckBox checkBoxItakura;
	private JTextPane textPane;
	private JLabel icon;

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
		frame.setBounds(100, 100, 450, 364);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					captureThread = new CaptureThread("output/temp.wav", 1, 0, null);
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
				if (captureThread != null) {
					captureThread.exit();
					btnPlay.setEnabled(true);
					btnStart.setEnabled(true);
					btnStop.setEnabled(false);

					findBest();
					comboBox.setSelectedItem(foundFile);
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
				if (captureThread != null)
					captureThread.exit();
				textFieldThreshold.setEnabled(false);
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				btnPlay.setEnabled(false);
				setIconStop();
			}
		});
		buttonGroup.add(radioMode1);
		radioMode1.setBounds(6, 7, 172, 23);
		frame.getContentPane().add(radioMode1);

		JRadioButton radioMode2 = new JRadioButton("Recording on voice");
		radioMode2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (captureThread != null)
					captureThread.exit();
				textFieldThreshold.setEnabled(true);
				btnStart.setEnabled(false);
				btnStop.setEnabled(false);
				btnPlay.setEnabled(false);

				double threshold = 0d;
				try {
					threshold = Double.parseDouble(textFieldThreshold.getText());
					textFieldThreshold.setBackground(Color.white);
				} catch (Exception e1) {
					threshold = 0.05;
					textFieldThreshold.setBackground(Color.red);
				}

				try {
					captureThread = new CaptureThread("output/temp.wav", 2,	threshold, MainWindow.this);
					captureThread.start();
					btnPlay.setEnabled(true);
				} catch (Exception e1) {
					JOptionPane
							.showMessageDialog(frame, "Somethink goes wrong");
				}
//				findBest();
			}
		});
		buttonGroup.add(radioMode2);
		radioMode2.setBounds(6, 33, 145, 23);
		frame.getContentPane().add(radioMode2);

		textFieldThreshold = new JTextField();
		textFieldThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(captureThread != null)
				{
					double threshold = 0.05;
					try {
						threshold = Double.parseDouble(textFieldThreshold.getText());
						textFieldThreshold.setBackground(Color.white);
					} catch (Exception e1) {
						threshold = 0.05;
						textFieldThreshold.setBackground(Color.red);
					}
					
					captureThread.setThreshold(threshold);
				}
			}
		});
		textFieldThreshold.setEnabled(false);
		textFieldThreshold.setText("0.03");
		textFieldThreshold.setBounds(159, 34, 46, 20);
		frame.getContentPane().add(textFieldThreshold);
		textFieldThreshold.setColumns(10);

		JLabel lblAvgAmplitude = new JLabel("avg amplitude");
		lblAvgAmplitude.setBounds(215, 33, 89, 16);
		frame.getContentPane().add(lblAvgAmplitude);

		JButton btnShowDistanceGraph = new JButton("Show distance graph");
		btnShowDistanceGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame jframe = new JFrame("Graph");

//				 String s = "trace0.csv";
//				 String t = "trace1.csv";

				String t = "patterns/" + comboBox.getSelectedItem();
				String s = "current_trace.csv";

				System.out.println("Compare between " + s
						+ " and current sound");

				DTW dtw = new DTW(t, s, checkBoxItakura.isSelected());
				dtw.calculateG();
				dtw.plotGraph();
				DistanceGraph distanceGraph = new DistanceGraph(dtw,
						checkBoxRange.isSelected(), checkBoxResize.isSelected());

				jframe.getContentPane().add(distanceGraph);

				jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				jframe.pack();
				jframe.setVisible(true);
				// dtw.printG();
				// System.out.println(dtw);
				if (checkBoxFile.isSelected())
					dtw.toFile("matrix.txt");
			}
		});
		btnShowDistanceGraph.setBounds(6, 288, 188, 26);
		frame.getContentPane().add(btnShowDistanceGraph);

		checkBoxFile = new JCheckBox("Save g matrix to file");
		checkBoxFile.setBounds(6, 91, 145, 24);
		frame.getContentPane().add(checkBoxFile);

		checkBoxRange = new JCheckBox("Set level for grey map");
		checkBoxRange.setSelected(true);
		checkBoxRange.setBounds(6, 119, 188, 24);
		frame.getContentPane().add(checkBoxRange);

		tf_pattern = new JTextField();
		tf_pattern.setBounds(338, 291, 86, 20);
		frame.getContentPane().add(tf_pattern);
		tf_pattern.setColumns(10);

		JButton btnSaveAsPattern = new JButton("Save as pattern");
		btnSaveAsPattern.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				melCepstrum();
				String surfix = "(" + 0 + ")";
				try {
					// What ever the file path is.
					String fileName = "patterns/" + tf_pattern.getText();
					int index = 0;
					while (new File(fileName + surfix).isFile()) {
						index++;
						surfix = "(" + index + ")";
					}
					fileName += surfix;
					File statText = new File(fileName);
					FileOutputStream is = new FileOutputStream(statText);
					OutputStreamWriter osw = new OutputStreamWriter(is);
					Writer w = new BufferedWriter(osw);
					for (int i = 0; i < co.length; i++) {
						w.write(Double.toString(co[i]) + '\n');
					}
					w.close();
				} catch (IOException e1) {
					System.err.println("error");
				}

				comboBox.addItem(tf_pattern.getText() + surfix);
			}
		});
		btnSaveAsPattern.setBounds(204, 290, 124, 23);
		frame.getContentPane().add(btnSaveAsPattern);

		btnFindBest = new JButton("Find best");
		btnFindBest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				findBest();
			}
		});

		btnFindBest.setBounds(204, 256, 220, 23);
		frame.getContentPane().add(btnFindBest);
		found.setHorizontalTextPosition(SwingConstants.CENTER);
		found.setHorizontalAlignment(SwingConstants.CENTER);

		found.setFont(new Font("Tahoma", Font.PLAIN, 30));
		found.setBounds(252, 112, 172, 26);
		frame.getContentPane().add(found);

		lblMinPath.setBounds(252, 149, 170, 14);
		frame.getContentPane().add(lblMinPath);

		JLabel lblCompareWith = new JLabel("Compare with:");
		lblCompareWith.setBounds(6, 223, 130, 16);
		frame.getContentPane().add(lblCompareWith);

		File folder = new File("patterns/");
		String[] listOfFiles = folder.list();
		if (listOfFiles.length == 0) {
			listOfFiles = new String[1];
			listOfFiles[0] = "empty patterns folder";
		}
		comboBox = new JComboBox(listOfFiles);
		comboBox.setBounds(6, 251, 188, 25);
		frame.getContentPane().add(comboBox);

		checkBoxResize = new JCheckBox("Automatic resize");
		checkBoxResize.setSelected(true);
		checkBoxResize.setBounds(6, 147, 128, 24);
		frame.getContentPane().add(checkBoxResize);

		checkBoxItakura = new JCheckBox("Itakura parallelogram");
		checkBoxItakura.setBounds(6, 175, 172, 24);
		frame.getContentPane().add(checkBoxItakura);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(204, 175, 220, 69);
		frame.getContentPane().add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setEnabled(false);
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);
		
		JLabel lblGij = new JLabel("g(I,J) = ");
		lblGij.setBounds(204, 149, 46, 14);
		frame.getContentPane().add(lblGij);
		
		JLabel lblBestAnswer = new JLabel("Best match:");
		lblBestAnswer.setBounds(204, 96, 76, 14);
		frame.getContentPane().add(lblBestAnswer);
		
		icon = new JLabel("");
		icon.setBounds(309, 29, 31, 23);
		setIconStop();
		frame.getContentPane().add(icon);
	}

	public void convertMusicToPoint() {
		if (input != null && input.exists()) {
			try {
				WavFile wavFile = WavFile.openWavFile(input);

				this.sampleRate = (int) wavFile.getSampleRate();
				wavFile.display();
				int numChannels = wavFile.getNumChannels();
				int xLenght = (int) wavFile.getNumFrames();
				points = new double[xLenght];

				double[] buffer = new double[100 * numChannels];

				int framesRead;

				int i = 0;
				double max = 0;
				do {

					framesRead = wavFile.readFrames(buffer, 100);
					for (int s = 0; s < framesRead * numChannels; s++) {
						try {
							if (Math.abs(buffer[s]) > max)
								max = Math.abs(buffer[s]);
							points[i] = buffer[s];
							i++;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} while (framesRead != 0);

				wavFile.close();

				for (i = 0; i < points.length; i++)
					points[i] = points[i] * (1 / max);

			} catch (Exception e) {
				System.err.println(e);
			}
		}

		this.dividedPoints = WindowFunction.sampling(points, 2048);
	}

	public void melCepstrum() {
		setInput(new File("./output/temp.wav"));
		convertMusicToPoint();
		MelCepstrum m = new MelCepstrum();

		try {
			co = m.getMelCepstrum(dividedPoints, 2048, false, 44100, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// What ever the file path is.
			File statText = new File("current_trace.csv");
			FileOutputStream is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
			for (int i = 0; i < co.length; i++) {
				w.write(Double.toString(co[i]) + '\n');
			}
			w.close();
		} catch (IOException e) {
			System.err.println("error");
		}
		System.out.println(Array.toString(co));

	}

	public void findBest() {
		melCepstrum();

		File folder = new File("patterns/");
		File[] listOfFiles = folder.listFiles();
		String s = "current_trace.csv";
		double minimal = 999999;
		String best_match = "None(";
		HashMap<String, Double> map = new HashMap<>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				// System.out.println(file.getName());
				String t = "patterns/" + file.getName();
				DTW dtw = new DTW(t, s, checkBoxItakura.isSelected());
				dtw.calculateG();
				if (minimal > dtw.minimalPath2) {
					minimal = dtw.minimalPath2;
					best_match = file.getName();
				}
//				if (minimal > dtw.minimalPath) {
//					minimal = dtw.minimalPath;
//					best_match = file.getName();
//				}
				
				map.put(file.getName(), dtw.minimalPath2);
//				map.put(file.getName(), dtw.minimalPath);
			}
		}
		Map<String, Double> sorted_map = sortByValues(map);
		String tmp = sorted_map.toString();
		textPane.setText(tmp.substring(1, tmp.length() - 1));
		found.setText(best_match.split("\\(")[0]);
		foundFile = best_match;
		lblMinPath.setText(Double.toString(minimal));
	}

	public JCheckBox getCheckBoxItakura() {
		return checkBoxItakura;
	}
	
	public void setIconRec()
	{
		icon.setIcon(iconRec);
	}
	
	public void setIconReady()
	{
		icon.setIcon(iconReady);
	}
	
	public void setIconStop()
	{
		icon.setIcon(iconStop);
	}
	
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }

}


