import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.time.Instant;
import java.time.Duration;
import java.lang.Math;

public class TypingTest extends JFrame
{
    private GridLayout mainWindowLayout; // The overall layout of the window
    private Dimension relativeSize; // The relative size of the components in the layout
    //TODO: Finish all the components!

    // Top-left of GridLayout
    private JLabel imageLabel; // Displays an image on the top left corner

    // Top-middle of GridLayout
    private JLabel sampleLabel; // Labels the sample text
    private JTextArea sampleText; // Displays the sample text for the user
    private JPanel samplePanel; // Holds the sample label and text

    // Top-right of GridLayout
    //TODO: PUT SOMETHING ON THE TOP-RIGHT

    //TODO: Middle-left of GridLayout

    // Middle of GridLayout
    private JTextArea inputTextArea; // receives input from the user
    private TextPrompt inputPrompt; // Provides placeholder text in the input box
    private JLabel inputLabel; // labels the input text field

    // Middle-right of GridLayout
    private JLabel wpmLabel; // Labels the words-per-minute field
    private Box statusBox; // Holds the status components on the right side
    private JLabel wpm; // Displays the user's words-per-minute
    private JLabel wordsTyped; // Displays the number of words the user has typed
    private JLabel timerLabel; // Labels the timer
    private JLabel timerDisplay; // Displays the timer

    //TODO: Bottom-left of GridLayout

    // Bottom-middle of GridLayout
    private JButton refreshButton; // Restarts the test when clicked
    private JButton helpButton; // Displays a help window when clicked
    private Box buttonPanel; // Holds the buttons at the bottom of the window

    //TODO: Bottom-right of GridLayout

    private String nextSampleText; // stores the sample text to be displayed in the next text
    private boolean testInProgress; // stores the state of the test
    private Timer timer; // Times the test
    private ActionListener timerListener; // Listens to the timer's actions
    private Instant start; // The time the test started

    public TypingTest()
    {
	/** Create new form TypingTest
	 */
	System.out.println("Initializing GUI");
	initComponents();
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
	mainWindowLayout.setVgap(10);
	relativeSize = new Dimension(50,50);
	// Set the main background color
	getContentPane().setBackground(new Color(217, 213, 196)); // Sisal

	// setup the top-left of the layout
	//imageLabel = new JLabel(new ImageIcon(getImage("images/mario_typing.jpg")));
	imageLabel = new JLabel("PLACEHOLDER");
	imageLabel.setPreferredSize(relativeSize);
	add(imageLabel);

	// setup the top-middle of the layout
	sampleLabel = new JLabel();
	sampleLabel.setText("Sample Text");
	sampleLabel.setMaximumSize(relativeSize);
	sampleLabel.setHorizontalAlignment(SwingConstants.LEFT);

	sampleText = new JTextArea();
	Border sampleTextBorder = BorderFactory.createLineBorder(new Color(64, 61, 52), 2); // Armadillo
	sampleText.setBorder(BorderFactory.createCompoundBorder(sampleTextBorder,
		    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

	sampleText.setBackground(new Color(200, 197, 188)); // Taupe Gray
	sampleText.setLineWrap(true);
	sampleText.setWrapStyleWord(true);
	sampleText.setRows(3);
	sampleText.setAlignmentX(Component.CENTER_ALIGNMENT);
	sampleText.setEditable(false);

	GridLayout sampleLayout = new GridLayout(2,1);
	sampleLayout.setVgap(0);
	samplePanel = new JPanel(sampleLayout);
	samplePanel.add(sampleLabel);
	samplePanel.add(sampleText);
	add(samplePanel);

	// setup the top-right of the layout
	add(new JLabel("PLACEHOLDER"));

	// setup the middle-left of the layout
	add(new JLabel("PLACEHOLDER"));

	// setup the middle of the layout
	inputTextArea = new JTextArea();
	inputTextArea.setPreferredSize(relativeSize);
	inputTextArea.setLineWrap(true);
	inputTextArea.setWrapStyleWord(true);
	add(inputTextArea);
	inputPrompt = new TextPrompt("Begin typing here!", inputTextArea);
	inputPrompt.setForeground(Color.GRAY);
	inputPrompt.changeAlpha(0.5f);

	// listen for the user's typing
	inputTextArea.addKeyListener(new KeyListener() {
	    @Override
	    public void keyTyped(KeyEvent e) {
		inputFieldUpdated(e);
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
	    }
	});

	// setup the status panel at the middle-right of the layout
	statusBox = Box.createVerticalBox();
	timerLabel = new JLabel();
	timerDisplay = new JLabel();
	timerDisplay.setText("Start typing sample text to begin test");
	Box topOfStatus = Box.createHorizontalBox();
	topOfStatus.add(timerLabel);
	topOfStatus.add(Box.createHorizontalStrut(10));

	statusBox.add(topOfStatus);
	statusBox.add(timerDisplay);

	wordsTyped = new JLabel();
	wordsTyped.setText("Words Typed: 0");

	wpmLabel = new JLabel();
	wpmLabel.setText("WPM");
	wpm = new JLabel();
	wpm.setText("0");
	statusBox.add(wpmLabel);
	statusBox.add(wpm);
	statusBox.add(wordsTyped);

	add(statusBox);

	// setup the bottom-left of the layout
	add(new JLabel("PLACEHOLDER"));

	// initialize the refresh button
	refreshButton = new JButton();
	refreshButton.setText("Refresh");

	// Set the refresh button to call its method when clicked
	refreshButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		refreshButtonClicked(e);
	    }
	});

	// initialize the help button
	helpButton = new JButton();
	helpButton.setText("HELP");

	// Set the help button to call its method when clicked
	helpButton.addActionListener(new ActionListener() {
	    @Override
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
	setMinimumSize(new Dimension(450,350));
	setResizable(false);
	pack();
	// make sure the window is visible and focused
	toFront();

	// Set the window icon
	setIconImage(getImage("images/keyboard.png"));
    }

    private void refreshButtonClicked(ActionEvent e)
    {
	/**This method is called when the user clicks the "Refresh" button
	 * It calls refresh() which sets up the user for the next test
	 */

	System.out.println("SOMEONE CLICKED THE refresh BUTTOHN!!!!!!");
	System.out.println("BEGINNING THE TEST ALL OVER AGAIN!!!");
	refresh();
    }

    private void helpButtonClicked(ActionEvent e)
    {
	/**This method is called when the user clicks the "HELP" button
	 * It displays a new window with a help message
	 */

	System.out.println("SOMEONE CLICKED THE HELP BUTTONN!!!!!");
    }

    private void inputFieldUpdated(KeyEvent e)
    {
	/**This method is called whenever the user types a key in the input
	 * text field.
	 * If the test hasn't started yet, it starts the test.
	 * If the test is in progress, it checks if the user has typed enough characters
	 * to end the test
	 */

	if(!testInProgress)
	{
	    if(!(inputTextArea.getText().length() == sampleText.getText().length()))
		beginTest();
	}
	else
	    wordsTyped.setText("Words Typed: " + inputTextArea.getText().split("\\s").length);

	if((inputTextArea.getText().length()) + 1 == sampleText.getText().length())
	{
	    // Add the last character the user inputted
	    // Otherwise, it won't show up in the text area
	    inputTextArea.setText(inputTextArea.getText() + e.getKeyChar());
	    endTest();
	}
    }

    private void beginTest()
    {
	/**Called when the user starts typing.
	 * It records the current time and begins the timer
	 */

	start = Instant.now();
	timerListener = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		timerDisplay.setText(Duration.between(start, Instant.now()).getSeconds()
			+ ":" + Duration.between(start, Instant.now()).getNano() / 10000000);
	    }
	};
	timer = new Timer(1, timerListener);
	timer.start();
	timerLabel.setText("Time");
	testInProgress = true;
    }

    private void endTest()
    {
	/**Called when the user types enough characters
	 * It ends the test and stops the timer
	 * TODO: Calculate accuracy
	 */

	if(testInProgress)
	{
	    timer.stop();
	    calcWPM(Instant.now());
	    testInProgress = false;
	    inputTextArea.setEditable(false);
	}
    }

    private void calcWPM(Instant end)
    {
	/**Calculates the user's typing speed at the end of the test
	 * It counts the words in the input text field
	 * and divides it by the number of minutes that have passed
	 * since the test began
	 */

	// check if the test was completed or interrupted by the user clicking refresh
	if(inputTextArea.getText().length() == sampleText.getText().length())
	{
	    int numWords = inputTextArea.getText().split("\\s").length;
	    long millisElapsed = Duration.between(start,end).toMillis();
	    double minutesElapsed = millisElapsed / 60000.0;
	    wpm.setText(String.format("%3.2f", (numWords / minutesElapsed)));
	}
	else
	    wpm.setText("0");
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
	/** Resets the window to prepare for the next test
	 * This is called when the user clicks refresh
	 * Not when the test ends
	 */

	System.out.println("RESTARTING TEST@!!!!");
	if(timer != null)
	    if(timer.isRunning())
		endTest();

	// Choose a new random sample string to test the user
	int randInt =  (int) (Math.random() * sampleTextArray.length);
	setSampleText(sampleTextArray[randInt]);

	sampleText.setText(nextSampleText);
	timerDisplay.setText("Start typing sample text to begin test");
	inputTextArea.setText("");
	timerLabel.setText("");
	inputTextArea.setEditable(true);
    }

    public void setSampleText(String text)
    {
	/**Receives sample text and stores it in nextSampleText
	 */
	nextSampleText = text;
    }

    // An array of sample texts that the user must type!
    String[] sampleTextArray = new String[]{"al sass lass as lass sass al",
			";a as la s; ;; ll ss a ss a",
			"The quick brown fox jumps over the lazy dog",
			"The baker needs to knead the dough with pizzaz",
			"Awkward letter patterns are formidable foes to the average typist",
			"ls awk grep chmod top apt cd rm cp mv dd cat pwd touch sudo ifconfig man",
			"Real men don't define acronyms. They understand them genetically."};

    public static void main(String[] args)
    {
	TypingTest test = new TypingTest();
	test.setVisible(true);
    }
}
