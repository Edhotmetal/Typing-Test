import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.time.Instant;
import java.time.Duration;
import java.lang.Math;

// imports for highlighting errors
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.BadLocationException;

public class TypingTest extends JFrame
{
    private GridLayout mainWindowLayout; // The overall layout of the window

    // Top-left of GridLayout
    private JTextArea title; // Displays the title on the top left
    private Font titleFont; // The font for the title

    // Top-middle of GridLayout
    private JTextPane sampleTextPane; // Displays the sample text for the user
    private JPanel samplePanel; // Holds the sample label and text
    private GridLayout sampleLayout; // arranges the sample components

    // Top-right of GridLayout
    private JLabel testStatusLabel; // displays the current status of the test
    private Font testStatusFont; // the font of the test status label

    // bottom-Middle of GridLayout
    private JTextPane inputTextPane; // receives input from the user
    private TextPrompt inputPrompt; // Provides placeholder text in the input box
    private JLabel inputLabel; // labels the input text field

    private JButton restartButton; // Restarts the test when clicked
    private JPanel buttonPanel; // Holds the buttons at the bottom of the window
    private GridLayout buttonLayout; // arranges the buttons on their panel
    private Box inputPanel; // Holds the input text field and buttons
    private GridLayout inputLayout; // holds the input text field and button panel

    // bottom-right of GridLayout
    private JPanel statusPanel; // holds the status components on the right
    private GridLayout statusLayout; // arranges the status components
    private Font statusFont; // the font of the status components
    private JLabel wpmLabel; // Labels the words-per-minute field
    private JLabel wpm; // Displays the user's words-per-minute
    private JLabel wordsTyped; // Displays the number of words the user has typed
    private JLabel timerLabel; // Labels the timer
    private JLabel timerDisplay; // Displays the timer
    private JLabel errorPercentage; // Displays the percentage of errors in the user's input

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
	restart();
    }

    private void initComponents()
    {
	/** initComponents:
	* Called by the constructor to initialize the form
	**/
	// COLORSCHEME
	Color armadillo = new Color(64,61,52);
	Color sisal = new Color(217,213,196);

	setTitle("Typing Test");
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	// set the main window's layout
	mainWindowLayout = new GridLayout(2,3); // 2 rows, 3 columns
	setLayout(mainWindowLayout);
	setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	mainWindowLayout.setVgap(10);
	mainWindowLayout.setHgap(10);
	// Set the main background color
	getContentPane().setBackground(sisal);

	// setup the top-left of the layout
	title = new JTextArea();
	title.setText("TYPING TEST");
	title.setEditable(false);
	title.setLineWrap(true);
	title.setWrapStyleWord(true);
	titleFont = new Font(Font.SERIF, Font.BOLD, 48);
	title.setFont(titleFont);
	title.setBackground(sisal);
	add(title);

	sampleTextPane = new JTextPane();
	Border sampleTextPaneBorder = BorderFactory.createLineBorder(armadillo, 2);
	sampleTextPane.setBorder(BorderFactory.createCompoundBorder(sampleTextPaneBorder,
		    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

	sampleTextPane.setBackground(new Color(200, 197, 188));
	sampleTextPane.setAlignmentX(Component.CENTER_ALIGNMENT);
	sampleTextPane.setEditable(false);

	sampleLayout = new GridLayout(1,1);
	sampleLayout.setVgap(3);
	samplePanel = new JPanel(sampleLayout);
	samplePanel.setBackground(sisal);
	samplePanel.add(sampleTextPane);
	add(samplePanel);

	// setup the top-right of the layout
	testStatusLabel = new JLabel();
	testStatusFont = new Font(Font.SERIF, Font.BOLD, 24);
	testStatusLabel.setFont(testStatusFont);
	add(testStatusLabel);

	// setup the bottom-left of the layout
	add(new JLabel("")); // Empty filler label

	// setup the bottom-middle of the layout
	inputPanel = Box.createVerticalBox();

	inputTextPane = new JTextPane();
	inputTextPane.setMargin(new Insets(10, 10, 10, 10));
	inputTextPane.setPreferredSize(new Dimension(100,100));
	inputPanel.add(inputTextPane);
	inputPrompt = new TextPrompt("Begin typing here!", inputTextPane);
	inputPrompt.setForeground(Color.GRAY);
	inputPrompt.changeAlpha(0.5f);
	
	// listen for the user's typing
	inputTextPane.addKeyListener(new KeyListener() {
	    @Override
	    public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_ENTER)
		    endTest();
		else
		    inputFieldUpdated(e);
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
	    }
	});
	
	// initialize the restart button
	restartButton = new JButton();
	restartButton.setText("Restart");

	// Set the restart button to call its method when clicked
	restartButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		restartButtonClicked();
	    }
	});

	// Initialize the button panel
	buttonPanel = new JPanel();
	buttonLayout = new GridLayout(1,2); // 1 row, 2 columns
	buttonLayout.setHgap(10);
	buttonPanel.setLayout(buttonLayout);
	// add buttons to button panel
	buttonPanel.add(restartButton);
	buttonPanel.setBackground(sisal);
	restartButton.setBackground(armadillo);
	restartButton.setForeground(sisal);

	inputPanel.add(buttonPanel);
	add(inputPanel);


	// setup the status panel at the bottom-right of the layout
	statusPanel = new JPanel();
	statusLayout = new GridLayout(3,2); // 3 rows, 2 columns
	statusPanel.setLayout(statusLayout);
	statusPanel.setBackground(sisal);
	statusLayout.setHgap(10);
	statusLayout.setVgap(0);
	statusFont = new Font(Font.SERIF, Font.BOLD, 16);

	timerLabel = new JLabel();
	timerLabel.setText("Time (s:ms)");
	timerLabel.setFont(statusFont);
	timerDisplay = new JLabel();
	timerDisplay.setFont(statusFont);

	wordsTyped = new JLabel();
	wordsTyped.setText("Words Typed: 0");
	wordsTyped.setFont(statusFont);

	wpmLabel = new JLabel();
	wpmLabel.setText("WPM");
	wpmLabel.setFont(statusFont);
	wpm = new JLabel();
	wpm.setText("0");
	wpm.setFont(statusFont);

	errorPercentage = new JLabel();
	errorPercentage.setFont(statusFont);

	statusPanel.add(timerLabel);
	statusPanel.add(wpmLabel);
	statusPanel.add(timerDisplay);
	statusPanel.add(wpm);
	statusPanel.add(errorPercentage);
	statusPanel.add(wordsTyped);

	Border statusPanelBorder = BorderFactory.createEtchedBorder(sisal, armadillo);
	statusPanel.setBorder(BorderFactory.createCompoundBorder(statusPanelBorder,
		    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

	add(statusPanel);

	// set main window size
	setMinimumSize(new Dimension(900,300)); // Width: 600, Height: 300
	setResizable(false);
	pack();
	// make sure the window is visible and focused
	toFront();

	// Set the window icon
	setIconImage(getImage("images/keyboard.png"));
    }

    private void restartButtonClicked()
    {
	/**This method is called when the user clicks the "Restart" button
	 * It calls restart() which sets up the user for the next test
	 */
	restart();
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
	    // If the test is not in progress and the test has not been completed
	    // begin the test
	    if(!testStatusLabel.getText().equals("Test completed"))
		beginTest();
	}
	else
	    wordsTyped.setText("Words Typed: " + inputTextPane.getText().split("\\s").length);

	
	if((inputTextPane.getText().length()) + 1 == sampleTextPane.getText().length())
	{
	    // When the test ends,
	    // Add the last character the user inputted
	    // Otherwise, it won't show up in the text area
	    inputTextPane.setText(inputTextPane.getText() + e.getKeyChar());
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
	testInProgress = true;
    }

    private void endTest()
    {
	/**Called when the user types enough characters
	 * or presses the enter key.
	 * It ends the test and stops the timer
	 */

	if(testInProgress)
	{
	    timer.stop();
	    calcWPM(Instant.now());
	    testInProgress = false;
	    inputTextPane.setEditable(false);
	    testStatusLabel.setText("Test completed");
	    calcErrorPercentage();
	    highlightErrors(checkErrors(inputTextPane.getText()));
	}
    }

    private void calcWPM(Instant end)
    {
	/**Calculates the user's typing speed at the end of the test.
	 * It counts the words in the input text field
	 * and divides it by the number of minutes that have passed
	 * since the test began
	 */

	// check if the test was completed or interrupted by the user clicking restart
	if(inputTextPane.getText().length() == sampleTextPane.getText().length())
	{
	    int numWords = inputTextPane.getText().split("\\s").length;
	    long millisElapsed = Duration.between(start,end).toMillis();
	    double minutesElapsed = millisElapsed / 60000.0;
	    wpm.setText(String.format("%3.2f", (numWords / minutesElapsed)));
	}
	else
	    wpm.setText("0");
    }

    private boolean[] checkErrors(String input)
    {
	/**At the end of the test, check the user's input for errorPercentage
	 * and return a boolean array indicating the indexes that contain
	 * errors
	 */

	String sample = sampleTextPane.getText();
	boolean[] errors = new boolean[input.length()];

	// iterate through the input, checking it against the sample
	// and highlighting errors
	for(int i = 0; i < input.length(); i++)
	    if(input.charAt(i) != sample.charAt(i))
		errors[i] = true;

	return errors;
    }

    private void highlightErrors(boolean[] errors)
    {
	/** Highlights the characters in the input text pane
	 * specified by the errors array
	 */

	// Initialize the red highlighter
	DefaultHighlighter.DefaultHighlightPainter highlightPainter = 
	    new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
	
	// Iterate through the errors and highlight them in the input
	for(int i = 0; i < errors.length; i++)
	{
	    if(errors[i])
	    {
		try
		{
		    inputTextPane.getHighlighter().addHighlight(i, i+1, highlightPainter);
		}
		catch (BadLocationException e)
		{
		    System.out.println("CANNOT HIGHLIGHT AT INDEX " + i);
		}
	    }
	}
	// dereference the highlighter so that it doesn't continue highlighting
	// during the next test in some cases
	highlightPainter = null;
    }

    private void calcErrorPercentage()
    {
	/** Calculates the user's error percentage and displays it
	 * in the status panel
	 */
	
	boolean[] errors = checkErrors(inputTextPane.getText());
	int numChars = errors.length;
	int numCorrect = 0;

	// add one to numCorrect for every error found in the errors array
	for(boolean error : errors)
	    if(error)
		numCorrect++;
	
	double error = ((double)numCorrect / (double)numChars);
	// Display the errorPercentage percentage to the nearest whole percent
	errorPercentage.setText(String.format("Error %%: %3.0f%%", error * 100));

    }


    private BufferedImage getImage(String imagePath)
    {
	/** Receives an image path
	 * If the image exists in the provided path, return it as a BufferedImage
	 * Otherwise, return null
	 */

	BufferedImage image = null;

	try
	{
	    image = ImageIO.read(new File(imagePath));
	}
	catch(IOException e)
	{
	    System.out.println("Could not find \"" + imagePath + "\"!!");
	}

	return image;
    }

    private void restart()
    {
	/** Resets the window to prepare for the next test
	 * This is called when the user clicks restart
	 * Not when the test ends
	 */

	System.out.println("RESTARTING TEST");
	if(timer != null)
	    if(timer.isRunning())
		endTest();

	// Choose a new random sample string to test the user
	int randInt =  (int) (Math.random() * sampleTextArray.length);
	setSampleText(sampleTextArray[randInt]);

	sampleTextPane.setText(nextSampleText);
	timerDisplay.setText("0:0");
	inputTextPane.setText("");
	inputTextPane.setEditable(true);
	// Wrap the label text in html and paragraph tags so that it will wrap
	// around to the next line
	testStatusLabel.setText("<html><p>Start typing sample text to begin test</p></html>");
	wpm.setText("0");
	wordsTyped.setText("Words Typed: 0");
	errorPercentage.setText("Error %: N/A");
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
