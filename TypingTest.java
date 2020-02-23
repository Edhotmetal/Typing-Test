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

// imports for highlighting errors
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class TypingTest extends JFrame
{
    private GridLayout mainWindowLayout; // The overall layout of the window
    private Dimension relativeSize; // The relative size of the components in the layout
    //TODO: Finish all the components!

    // Top-left of GridLayout
    private JTextArea title; // Displays the title on the top left
    private Font titleFont; // The font for the title

    // Top-middle of GridLayout
    private JTextPane sampleTextPane; // Displays the sample text for the user
    private JPanel samplePanel; // Holds the sample label and text

    // Top-right of GridLayout
    private JLabel testStatusLabel; // displays the current status of the test
    private Font testStatusFont; // the font of the test status label

    // bottom-Middle of GridLayout
    private JTextPane inputTextPane; // receives input from the user
    private TextPrompt inputPrompt; // Provides placeholder text in the input box
    private JLabel inputLabel; // labels the input text field

    private JButton refreshButton; // Restarts the test when clicked
    private JButton helpButton; // Displays a help window when clicked
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
    private JLabel accuracy; // Displays the user's typing accuracy


    private String nextsampleTextPane; // stores the sample text to be displayed in the next text
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
	relativeSize = new Dimension(50,50);
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
	title.setPreferredSize(relativeSize);
	title.setBackground(sisal);
	add(title);

	sampleTextPane = new JTextPane();
	Border sampleTextPaneBorder = BorderFactory.createLineBorder(armadillo, 2);
	sampleTextPane.setBorder(BorderFactory.createCompoundBorder(sampleTextPaneBorder,
		    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

	sampleTextPane.setBackground(new Color(200, 197, 188));
	sampleTextPane.setAlignmentX(Component.CENTER_ALIGNMENT);
	sampleTextPane.setEditable(false);

	GridLayout sampleLayout = new GridLayout(1,1);
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
	add(new JLabel("PLACEHOLDER"));

	// setup the bottom-middle of the layout
	inputPanel = Box.createVerticalBox();

	inputTextPane = new JTextPane();
	inputTextPane.setMargin(new Insets(5, 5, 5, 5));
	inputPanel.add(inputTextPane);
	inputPrompt = new TextPrompt("Begin typing here!", inputTextPane);
	inputPrompt.setForeground(Color.GRAY);
	inputPrompt.changeAlpha(0.5f);
	
	// listen for the user's typing
	inputTextPane.addKeyListener(new KeyListener() {
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
	buttonPanel = new JPanel();
	buttonLayout = new GridLayout(1,2); // 1 row, 2 columns
	buttonLayout.setHgap(10);
	buttonPanel.setLayout(buttonLayout);
	// add buttons to button panel
	buttonPanel.add(refreshButton);
	buttonPanel.add(helpButton);
	buttonPanel.setBackground(sisal);
	refreshButton.setBackground(armadillo);
	refreshButton.setForeground(sisal);
	helpButton.setBackground(armadillo);
	helpButton.setForeground(sisal);

	inputPanel.add(buttonPanel);
	add(inputPanel);


	// setup the status panel at the bottom-right of the layout
	statusPanel = new JPanel();
	statusLayout = new GridLayout(3,2); // 3 rows, 2 columns
	statusPanel.setLayout(statusLayout);
	statusPanel.setBackground(sisal);
	statusLayout.setHgap(10);
	statusLayout.setVgap(0);
	statusFont = new Font(Font.SERIF, Font.BOLD, 18);

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

	accuracy = new JLabel();
	accuracy.setText("Accuracy: N/A");
	accuracy.setFont(statusFont);

	statusPanel.add(timerLabel);
	statusPanel.add(wpmLabel);
	statusPanel.add(timerDisplay);
	statusPanel.add(wpm);
	statusPanel.add(accuracy);
	statusPanel.add(wordsTyped);

	add(statusPanel);

	// set main window size
	setMinimumSize(new Dimension(800,300)); // Width: 800, Height: 300
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
	 * TODO It also checks the user's input and displays errors in real-time
	 */

	if(!testInProgress)
	{
	    if(!(inputTextPane.getText().length() == sampleTextPane.getText().length()))
		beginTest();
	}
	else
	{
	    wordsTyped.setText("Words Typed: " + inputTextPane.getText().split("\\s").length);
	    checkAccuracy(e.getKeyChar());
	    	}

	
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
	    inputTextPane.setEditable(false);
	    testStatusLabel.setText("Test completed");
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

    private void checkAccuracy(char c)
    {
	/** Every time the user types a character, this method checks for accuracy
	 * and displays errors in real-time by highlighting them in the
	 * input text pane.
	 */
	// The input text pane hasn't been updated with the user's latest input so
	// we must add it here
	String input = (inputTextPane.getText() + Character.toString(c));
	String sample = sampleTextPane.getText();
	boolean[] errors = new boolean[input.length()];

	// iterate through the input, checking it against the sample
	// and highlighting errors
	for(int i = 0; i < input.length(); i++)
	    if(input.charAt(i) != sample.charAt(i))
		errors[i] = true;

	highlight(errors, c);
    }

    private void highlight(boolean[] errors, char c)
    {
	/** Highlights the characters in the input text pane
	 * specified by the errors array
	 */

	String text = inputTextPane.getText() + Character.toString(c);
	inputTextPane.setText("");

	for(int i = 0; i < errors.length; i++)
	{
	    if(errors[i] == true)
		appendToInput(Character.toString(text.charAt(i)), Color.RED);
	    else
		appendToInput(Character.toString(text.charAt(i)), Color.BLACK);
	}
	
	// The process of validating input will have duplicated the input
	// so we need to remove the extra character
	inputTextPane.setText(
		inputTextPane.getText().substring(0,inputTextPane.getText().length() - 1));
    }

    private void appendToInput(String msg, Color c)
    {
	StyleContext sc = StyleContext.getDefaultStyleContext();
	AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

	aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
	aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

	int len = inputTextPane.getDocument().getLength();
	inputTextPane.setCaretPosition(len);
	inputTextPane.setCharacterAttributes(aset, false);
	inputTextPane.replaceSelection(msg);
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

	System.out.println("RESTARTING TEST");
	if(timer != null)
	    if(timer.isRunning())
		endTest();

	// Choose a new random sample string to test the user
	int randInt =  (int) (Math.random() * sampleTextPaneArray.length);
	setsampleTextPane(sampleTextPaneArray[randInt]);

	sampleTextPane.setText(nextsampleTextPane);
	timerDisplay.setText("0:0");
	inputTextPane.setText("");
	inputTextPane.setEditable(true);
	testStatusLabel.setText("<html><p>Start typing sample text to begin test</p></html>");
	wpm.setText("0");
	wordsTyped.setText("Words Typed: 0");
    }

    public void setsampleTextPane(String text)
    {
	/**Receives sample text and stores it in nextsampleTextPane
	 */
	nextsampleTextPane = text;
    }

    // An array of sample texts that the user must type!
    String[] sampleTextPaneArray = new String[]{"al sass lass as lass sass al",
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
