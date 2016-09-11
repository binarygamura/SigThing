package de.fomad.sigthing.view;

import de.fomad.sigthing.view.icons.IconCache;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author binary gamura
 */
public final class AboutDialog extends JDialog {
    
    private final GUI parent;
    
    private static final String markup = "<html>\n" +
	"<h1>SigThing</h1><p>SigThing is a small tool for players of the awesome mmorpg EVE-Online which allows them to track their path<br>"
      + "through the universe and discoveries they make during their journey. It is the successor of a small tool by the name of \"SigTrack\"<br>"
      + "I created before. It took use of the ingame browser (IGB) but lacked in terms of integration. Since CCP, the creator<br>"
      + "of EVE-Online, annouced the end of lifetime for the IGB, I had to create something new. And here it is! I hope you find<br>"
      + "this program as useful as I do, if not, just dont use it.</p><h2>Features</h2>\n" +
	"<ul>\n" +
	"  <li>authentification via OAuth2.</li>\n" +
	"  <li>keylogger to allow you to add your exploration data from within<br>the game without any forms or buttons to press.</li>\n" +
	"  <li>local in-memory database to persist your data.</li>\n" +
	"  <li>nice looking user interface.</li>\n" +
	"</ul><h2>Limitations</h2>\n" +
	"<ul>\n" +
	"  <li>since this app uses the same keylogger as the famous KOSChecker,<br>you actually can't run both programs at the same time.</li>\n" +
	"  <li>some things are still untested. test coverage will increase over<br>he time.</li>\n" +
	"  <li>the application tries to stay on top of all other applications,<br>but sadly this doesnt work if you start the game in fullscreen mode.</li>\n" +
	"</ul><h2>Contact</h2><p>Feel free to write an email to frederik.priede@gmail.com<br>if you have any concerns about this awesome piece of software.</p><h2>Important</h2><p>Please note that this software is work in progress and might not run on your operating system or hardware.</p><h2>Licence</h2><p>This Free software! Dont blame me, if this app kills your hardware, software, your cat or even your familiy. I warned you! I use media and stuff from other people:</p>\n" +
	"<ul>\n" +
	"  <li>Text Message Alert 5 Sound from Daniel Simion, taken from<br><a href=\"http://soundbible.com/2158-Text-Message-Alert-5.html\">soundbible</a></li>\n" +
	"  <li>Awesome Free Icon Set from<br><a href=\"http://www.fatcow.com/free-icons\">fatcow</a></li>\n" +
	"</ul>\n" +
	"</html>";
    
    public AboutDialog(GUI parent){
	super(parent, "About");
	setIconImage(parent.getIconCache().getIcon(IconCache.IconId.INFO_ICON));
	this.parent = parent;
	init();
    }
    
    private void init(){
        setModal(true);
        setResizable(false);
	setLayout(new BorderLayout());
	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	
	JButton closeButton = new JButton("close", parent.getIconCache().getImageIcon(IconCache.IconId.CANCEL_ICON));	
	closeButton.addActionListener(e -> {setVisible(false);});
        closeButton.setMnemonic('c');
	
	JLabel textLabel = new JLabel(markup);
        textLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	
	add(textLabel, BorderLayout.CENTER);
	add(closeButton, BorderLayout.SOUTH);
	
	validate();
	pack();
	
    }
    
    public void openCentered(){
	setLocationRelativeTo(parent);
	setVisible(true);
    }
}
