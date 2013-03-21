package firstTask;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import java.awt.Rectangle;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private JScrollPane scrollPane;


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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		panel = new JPanel();
		panel.setMaximumSize(new Dimension(32767, 50));
		panel.setPreferredSize(new Dimension(1000, 50));
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setMinimumSize(new Dimension(1000, 50));
		panel.setBounds(new Rectangle(0, 0, 1000, 50));
//		scrollPane.setRowHeaderView(panel);
//		scrollPane.add(panel);
		panel.setLayout(null);
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
