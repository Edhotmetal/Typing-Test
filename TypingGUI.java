import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class TypingGUI extends JFrame
{
    private JLabel sampleLabel; // Labels the sample text
    private JTextField sampleText; // displays the text the user should type
    private JLabel inputLabel; // labels the input text field
    private JTextField inputTextField; // receives input from the user
    private Box textPanel; // Holds the sample text and user input

    private JButton refreshButton; // Restarts the test when clicked
    private JButton helpButton; // Displays a help window when clicked
    private Box buttonPanel; // Holds the buttons at the bottom of the window

    // Create new form TypingGUI
    public TypingGUI()
    {
	System.out.println("Initializing GUI");
	initComponents();
	beginTest();
    }

    private void initComponents()
    {
	/** initComponents:
	* Called by the constructor to initialize the form
	**/
	setTitle("Typing Test");
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	// initialize the sample text field
	sampleText = new JTextField();
	sampleLabel = new JLabel();
	sampleLabel.setText("Sample");

	// Prevent the user from editing the sample text
	sampleText.setEditable(false);

	// initialize the input text field
	inputTextField = new JTextField();

	// Initialize the text panel
	textPanel = Box.createVerticalBox();

	// add text fields to box
	textPanel.add(sampleText);
	textPanel.add(inputTextField);

	// initialize the refresh button
	refreshButton = new JButton();
	refreshButton.setText("Refresh");

	// Set the refresh button to call its method when clicked
	refreshButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		refreshButtonClicked(e);
	    }
	});
	
	// initialize the help button
	helpButton = new JButton();
	helpButton.setText("HELP");

	// Set the help button to call its method when clicked
	helpButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		helpButtonClicked(e);
	    }
	});

	// Initialize the button panel
	buttonPanel = Box.createHorizontalBox();
	// add buttons to button panel
	buttonPanel.add(refreshButton);
	buttonPanel.add(helpButton);

	// set the main window's layout
	setLayout(new GridLayout(3,3));

	// add panels to the main window
	add(textPanel);
	add(buttonPanel);

	// set main window size
	setMinimumSize(new Dimension(800,500));
	setResizable(false);
	pack();
	// make sure the window is visible and focused
	toFront();

	setIconImage(getImage("images/keyboard.png"));

    }	

    public void refreshButtonClicked(ActionEvent e)
    {
	System.out.println("SOMEONE CLICKED THE REFRESH BUTTOHN!!!!!!");
	System.out.println("BEGINNING THE TEST ALL OVER AGAIN!!!");
	beginTest();
    }

    public void helpButtonClicked(ActionEvent e)
    {
	System.out.println("SOMEONE CLICKED THE HELP BUTTONN!!!!!");
    }

    private BufferedImage getImage(String imagePath)
    {
	/** Receives the image path and returns it as an BufferedImage
	 */
	BufferedImage icon = null;

	try
	{
	    icon = ImageIO.read(new File(imagePath));
	}
	catch(IOException e)
	{
	    System.out.println("Could not find \"" + imagePath + "\"!!");
	}

	return icon;
    }

    private void beginTest()
    {
	System.out.println("BEGINNING TEST@!!!!");
	sampleText.setText("This is some sample text");
    }
}
