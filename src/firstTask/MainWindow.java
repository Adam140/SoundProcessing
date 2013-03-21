package firstTask;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextField textFieldInputFile;
	private Diagram diagram;
	private JFileChooser fc = new JFileChooser();
	private JButton btnGenerateWave;
	private JLabel lblMainFrequency;
	private JTextField textFieldMainFreq;
	private JButton btnPlay;
	private JLabel lblOutputFile;
	private JTextField textFieldOutputFile;
	private JButton btnSave;
	private WaveGenerator wave;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 472, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 424, 0 };
		gbl_contentPane.rowHeights = new int[] { 200, 109, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		diagram = new Diagram(100, getWidth());
		diagram.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int notches = e.getWheelRotation();
				int h = diagram.getHeight();
				if (notches > 0) {
					// diagram.hstep+=9.5;
					diagram.setSize(diagram.getWidth(), h + 100);
					diagram.setPreferredSize(new Dimension(diagram.getWidth(),
							h));
					diagram.revalidate();
					diagram.once = false;
					diagram.repaint();
					scrollPane.revalidate();

				} else if (notches < 0) {
					diagram.setSize(diagram.getWidth(), h - 100);
					// diagram.hstep-=h*0.95;
					diagram.setPreferredSize(new Dimension(diagram.getWidth(),
							h));
					diagram.revalidate();
					diagram.once = false;
					diagram.repaint();
					scrollPane.revalidate();
				}
			}
		});
		diagram.setAutoscrolls(true);
		diagram.setPreferredSize(new Dimension(3000, 100));
		diagram.setBackground(Color.pink);

		scrollPane = new JScrollPane(diagram);
		scrollPane.setPreferredSize(new Dimension(3002, 100));
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		scrollPane.revalidate();
		contentPane.add(scrollPane, gbc_scrollPane);

		JPanel panelBottom = new JPanel();
		panelBottom.setPreferredSize(new Dimension(10, 100));
		panelBottom.setSize(new Dimension(0, 100));
		panelBottom.setMinimumSize(new Dimension(10, 200));
		panelBottom.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_panelBottom = new GridBagConstraints();
		gbc_panelBottom.fill = GridBagConstraints.BOTH;
		gbc_panelBottom.gridx = 0;
		gbc_panelBottom.gridy = 1;
		contentPane.add(panelBottom, gbc_panelBottom);
		GridBagLayout gbl_panelBottom = new GridBagLayout();
		gbl_panelBottom.columnWidths = new int[] { 96, 232, 0, 0 };
		gbl_panelBottom.rowHeights = new int[] { 20, 0, 0, 0 };
		gbl_panelBottom.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelBottom.rowWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panelBottom.setLayout(gbl_panelBottom);

		JLabel labelInputFile = new JLabel("Input file:");
		GridBagConstraints gbc_labelInputFile = new GridBagConstraints();
		gbc_labelInputFile.anchor = GridBagConstraints.EAST;
		gbc_labelInputFile.insets = new Insets(0, 0, 5, 5);
		gbc_labelInputFile.gridx = 0;
		gbc_labelInputFile.gridy = 0;
		panelBottom.add(labelInputFile, gbc_labelInputFile);

		textFieldInputFile = new JTextField();
		textFieldInputFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int returnVal = fc.showOpenDialog(MainWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					diagram.file = file;
					textFieldInputFile.setText(file.getAbsolutePath());
					diagram.once = false;
				} else
					textFieldInputFile.setText("");
			}
		});
		textFieldInputFile.setSize(new Dimension(100, 0));
		textFieldInputFile.setMinimumSize(new Dimension(60, 20));
		textFieldInputFile.setPreferredSize(new Dimension(60, 20));
		textFieldInputFile.setEnabled(false);
		textFieldInputFile.setEditable(false);
		GridBagConstraints gbc_textFieldInputFile = new GridBagConstraints();
		gbc_textFieldInputFile.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldInputFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldInputFile.anchor = GridBagConstraints.NORTH;
		gbc_textFieldInputFile.gridx = 1;
		gbc_textFieldInputFile.gridy = 0;
		panelBottom.add(textFieldInputFile, gbc_textFieldInputFile);
		textFieldInputFile.setColumns(10);

		btnGenerateWave = new JButton("GENERATE WAVE");
		btnGenerateWave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				diagram.repaint();
			}
		});
		GridBagConstraints gbc_btnGenerateWave = new GridBagConstraints();
		gbc_btnGenerateWave.anchor = GridBagConstraints.WEST;
		gbc_btnGenerateWave.insets = new Insets(0, 0, 5, 0);
		gbc_btnGenerateWave.gridx = 2;
		gbc_btnGenerateWave.gridy = 0;
		panelBottom.add(btnGenerateWave, gbc_btnGenerateWave);

		lblMainFrequency = new JLabel("Main frequency:");
		GridBagConstraints gbc_lblMainFrequency = new GridBagConstraints();
		gbc_lblMainFrequency.anchor = GridBagConstraints.EAST;
		gbc_lblMainFrequency.insets = new Insets(0, 0, 5, 5);
		gbc_lblMainFrequency.gridx = 0;
		gbc_lblMainFrequency.gridy = 1;
		panelBottom.add(lblMainFrequency, gbc_lblMainFrequency);

		textFieldMainFreq = new JTextField();
		GridBagConstraints gbc_textFieldMainFreq = new GridBagConstraints();
		gbc_textFieldMainFreq.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldMainFreq.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldMainFreq.gridx = 1;
		gbc_textFieldMainFreq.gridy = 1;
		panelBottom.add(textFieldMainFreq, gbc_textFieldMainFreq);
		textFieldMainFreq.setColumns(10);

		btnPlay = new JButton("PLAY");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ("Stop".equalsIgnoreCase(btnPlay.getText())) {
					btnPlay.setText("Play");
					wave.exit();
				}
				else if(!textFieldMainFreq.getText().isEmpty())
				{
					try{
						Double freq = Double.parseDouble(textFieldMainFreq.getText());
						btnPlay.setText("Stop");
						wave = new WaveGenerator(freq);
						wave.start();
					}
					catch(Exception e1)
					{
						textFieldMainFreq.setText("ERROR");
						System.out.println(e1);
					}
					
				}
			}
		});
		GridBagConstraints gbc_btnPlay = new GridBagConstraints();
		gbc_btnPlay.anchor = GridBagConstraints.WEST;
		gbc_btnPlay.insets = new Insets(0, 0, 5, 0);
		gbc_btnPlay.gridx = 2;
		gbc_btnPlay.gridy = 1;
		panelBottom.add(btnPlay, gbc_btnPlay);

		lblOutputFile = new JLabel("Output file:");
		GridBagConstraints gbc_lblOutputFile = new GridBagConstraints();
		gbc_lblOutputFile.anchor = GridBagConstraints.EAST;
		gbc_lblOutputFile.insets = new Insets(0, 0, 0, 5);
		gbc_lblOutputFile.gridx = 0;
		gbc_lblOutputFile.gridy = 2;
		panelBottom.add(lblOutputFile, gbc_lblOutputFile);

		textFieldOutputFile = new JTextField();
		GridBagConstraints gbc_textFieldOutputFile = new GridBagConstraints();
		gbc_textFieldOutputFile.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldOutputFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldOutputFile.gridx = 1;
		gbc_textFieldOutputFile.gridy = 2;
		panelBottom.add(textFieldOutputFile, gbc_textFieldOutputFile);
		textFieldOutputFile.setColumns(10);

		btnSave = new JButton("Save");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.anchor = GridBagConstraints.WEST;
		gbc_btnSave.gridx = 2;
		gbc_btnSave.gridy = 2;
		panelBottom.add(btnSave, gbc_btnSave);

		panel = new JPanel();
		panel.setMaximumSize(new Dimension(32767, 50));
		panel.setPreferredSize(new Dimension(1000, 50));
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setMinimumSize(new Dimension(1000, 50));
		panel.setBounds(new Rectangle(0, 0, 1000, 50));
		// scrollPane.setRowHeaderView(panel);
		// scrollPane.add(panel);
		panel.setLayout(null);

		// scrollPane.add(diagram);
		// contentPane.add(diagram);
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}
}
