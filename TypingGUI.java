import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.time.Instant;
import java.time.Duration;

public class TypingGUI extends JFrame
{
    private GridLayout mainWindowLayout; // The overall layout of the window
    private Dimension relativeSize; // The relative size of the components in the layout
    //TODO: Finish all the components!

    // Top-left of GridLayout
    private JLabel imageLabel; // Displays an image on the top left corner

    // Top-middle of GridLayout
    private JLabel sampleLabel; // Labels the sample text
    private JLabel sampleText; // Displays the sample text for the user
    private Box sampleBox; // Holds the sample text and label

    // Top-right of GridLayout
    //TODO: PUT SOMETHING ON THE TOP-RIGHT

    //TODO: Middle-left of GridLayout

    // Middle of GridLayout
    private JTextField inputTextField; // receives input from the user
    private TextPrompt inputPrompt; // Provides placeholder text in the input box
    private JLabel inputLabel; // labels the input text field

    // Middle-right of GridLayout
    private JLabel wpmLabel; // Labels the words-per-minute field
    private Box statusBox; // Holds the status components on the right side
    private Box wpmBox; // Horizontal box that goes on the top of the status pane
    private JLabel wpm; // Displays the user's words-per-minute
    private JLabel timerLabel; // Labels the timer
    private JLabel timerDisplay; // Displays the timer
    private Box timerBox; // Horizontal box that holds the timer at the bottom of the status pane

    //TODO: Bottom-left of GridLayout

    // Bottom-middle of GridLayout
    private JButton refreshButton; // Restarts the test when clicked
    private JButton helpButton; // Displays a help window when clicked
    private Box buttonPanel; // Holds the buttons at the bottom of the window

    //TODO: Bottom-right of GridLayout

    private String nextSampleText; // stores the sample text to be displayed in the next text
    private int sampleLength; // stores the length of the sample text
    private boolean testInProgress; // stores the state of the test
    private Timer timer; // Times the test
    private ActionListener timerListener; // Listens to the timer's actions
    private Instant start; // The time the test started

    public TypingGUI(String nextSampleText)
    {
	/** Create new form TypingGUI
	 */
	System.out.println("Initializing GUI");
	initComponents();
	this.nextSampleText = nextSampleText;
	refresh();
    }

    private void initComponents()
    {
	/** initComponents:
	* Called by the constructor to initialize the form
	**/
	setTitle("Typing Test");
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	// set the main window's layout
	mainWindowLayout = new GridLayout(3,3);
	setLayout(mainWindowLayout);
	setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	//TODO: REVISIT THIS mainWindowLayout.setVgap(50);
	relativeSize = new Dimension(100,100);

	// setup the top-left of the layout
	//imageLabel = new JLabel(new ImageIcon(getImage("images/mario_typing.jpg")));
	imageLabel = new JLabel("PLACEHOLDER");
	imageLabel.setPreferredSize(relativeSize);
	add(imageLabel);

	// setup the top-middle of the layout
	sampleText = new JLabel();
	sampleLabel = new JLabel();
	sampleLabel.setText("Sample");
	sampleBox = Box.createVerticalBox();
	sampleBox.add(sampleLabel);
	sampleBox.add(sampleText);
	sampleBox.setPreferredSize(relativeSize);
	add(sampleBox);

	// setup the top-right of the layout
	add(new JLabel("PLACEHOLDER"));

	// setup the middle-left of the layout
	add(new JLabel("PLACEHOLDER"));

	// setup the middle of the layout
	inputTextField = new JTextField();
	add(inputTextField);
	inputPrompt = new TextPrompt("Begin typing here!", inputTextField);
	inputPrompt.setForeground(Color.GRAY);
	inputPrompt.changeAlpha(0.5f);

	// listen for the user's typing
	inputTextField.addKeyListener(new KeyListener() {
	    @Override
	    public void keyTyped(KeyEvent e) {
		inputFieldUpdated();
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
	    }
	});

	// setup the middle-right of the layout
	wpmBox = Box.createHorizontalBox();
	wpmLabel = new JLabel();
	wpmLabel.setText("WPM");
	wpmBox.add(wpmLabel);
	wpm = new JLabel();
	wpm.setText("0");
	wpmBox.add(wpm);

	statusBox = Box.createVerticalBox();
	statusBox.add(wpmBox);

	timerBox = Box.createHorizontalBox();
	timerLabel = new JLabel();
	timerLabel.setText("Time");
	timerDisplay = new JLabel();
	timerDisplay.setText("Start typing sample text to begin test");
	timerBox.add(timerLabel);
	timerBox.add(timerDisplay);
	statusBox.add(timerBox);

	add(statusBox);

	// setup the bottom-left of the layout
	add(new JLabel("PLACEHOLDER"));

	// initialize the refresh button
	refreshButton = new JButton();
	refreshButton.setText("refresh");

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

	// setup the bottom-middle of the layout
	add(buttonPanel);

	// setup the bottom-right of the layout
	add(new JLabel("PLACEHOLDER"));

	// set main window size
	setMinimumSize(new Dimension(800,500));
	setResizable(false);
	pack();
	// make sure the window is visible and focused
	toFront();

	// Set the window icon
	setIconImage(getImage("images/keyboard.png"));
    }	

    private void refreshButtonClicked(ActionEvent e)
    {
	System.out.println("SOMEONE CLICKED THE refresh BUTTOHN!!!!!!");
	System.out.println("BEGINNING THE TEST ALL OVER AGAIN!!!");
	refresh();
    }

    private void helpButtonClicked(ActionEvent e)
    {
	System.out.println("SOMEONE CLICKED THE HELP BUTTONN!!!!!");
    }

    private void inputFieldUpdated()
    {
	if(!testInProgress)
	    beginTest();

	if(inputTextField.getText().length() == sampleText.getText().length())
	    endTest();
    }

    private void beginTest()
    {
	timerListener = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		timerDisplay.setText(Duration.between(start, Instant.now()).getSeconds()
			+ ":" + Duration.between(start, Instant.now()).getNano() * 1000000);
	    }
	};
	timer = new Timer(1, timerListener);
	timer.start();
	testInProgress = true;
    }

    private void endTest()
    {
	timer.stop();
	testInProgress = false;
    }

    private BufferedImage getImage(String imagePath)
    {
	/** Receives an image path
	 * If the image exists in the provided path, return it as a BufferedImage
	 * Otherwise, return null
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

    private void refresh()
    {
	System.out.println("RESTARTING TEST@!!!!");
	sampleText.setText(nextSampleText);
	sampleLength = nextSampleText.length();
	timerDisplay.setText("Start typing sample text to begin test");
	inputTextField.setText("");
	//TODO: TypingTest will serve another sample string for the user
    }

    public void setSampleText(String text)
    {
	/**Receives sample text and stores it in nextSampleText
	 */
	nextSampleText = text;
    }
}
