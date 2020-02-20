import java.awt.event.ActionListener;

public class TypingTest
{
    public static void main(String[] args)
    {
	String sampleText = "al sass lass lass sass al";
	TypingGUI gui = new TypingGUI(sampleText);
	gui.setVisible(true);
    }
}
