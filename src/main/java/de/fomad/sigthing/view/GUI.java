package de.fomad.sigthing.view;

import de.fomad.sigthing.controller.Controller;
import de.fomad.sigthing.model.ControllerEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jnativehook.NativeHookException;

/**
 * @author binary gamura
 */
public class GUI extends JFrame implements Observer
{
    private static final Logger LOGGER = LogManager.getLogger(GUI.class);
    
    private CardLayout cardLayout;
    
    private Properties properties;
    
    private Controller controller;
    
    private InfoPanel infoPanel;
    
    private GUI()
    {
	super("SigThing");
	
	GUI.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	GUI.this.addWindowListener(new WindowAdapter()
	{
	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		exitOperation();
	    }
	});
    }
    
    private void exitOperation(){
	int result = JOptionPane.showConfirmDialog(this, "do you really want to quit?", "quit?", JOptionPane.YES_NO_OPTION);
	if(result == JOptionPane.YES_OPTION){
	    controller.shutDown();
	    dispose();
	}
    }
    
    private void showError(String title, String message){
	JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    private JPanel createMainGui()
    {
	JPanel mainGui = new JPanel(new BorderLayout());
        
        infoPanel = new InfoPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("selected item"));
        
        JScrollPane centerPanel = new JScrollPane();
        
        JButton deleteButton = new JButton("delete");
        JButton commentButton = new JButton("comment");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
        buttonPanel.add(deleteButton);
        buttonPanel.add(commentButton);
	
        mainGui.add(infoPanel, BorderLayout.NORTH);
        mainGui.add(centerPanel, BorderLayout.CENTER);
        mainGui.add(buttonPanel, BorderLayout.SOUTH);
        
	return mainGui;
    }
    
    private void initAndDisplay(String pathToConfig) throws IOException, URISyntaxException, NativeHookException, SQLException, ClassNotFoundException{
		
	cardLayout = new CardLayout();
	getContentPane().setLayout(cardLayout);
        
	
	JButton openAuthButton = new JButton("login with eve");
	openAuthButton.addActionListener((e) -> {
	    try
	    {
		String loginURL = properties.getProperty("login_url");
		String clientId = properties.getProperty("client_id");
		String callbackUrl = properties.getProperty("callback_url");
		
		URIBuilder builder = new URIBuilder(loginURL);
		builder.addParameter("response_type", "code");
		builder.addParameter("redirect_uri", callbackUrl);
		builder.addParameter("client_id", clientId);
		builder.addParameter("scope", "characterLocationRead");
		
		URI target = builder.build();
		if(!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
		    showError("Unable to open browser! you cant use this application on your system. sorry...", "unable to open browser");
		    throw new Exception("open the browser is not supported on your operating system!");
		}
		controller.startLoginProcess();
		Desktop.getDesktop().browse(target);
		
	    }
	    catch(BindException ex){
		showError("error", "unable to open local port. is the app already running or is your firewall blocking?");
		LOGGER.fatal("unable to open local server socket!", ex);
	    }
	    catch(URISyntaxException | IOException ex){
		LOGGER.fatal("error while browsing to the login site!", ex);
	    }
	    catch(Exception ex){
		LOGGER.fatal("unexpected error!", ex);
	    }
	});
	
	
	getContentPane().add(openAuthButton, "openAuthButton");
	getContentPane().add(createMainGui(),"mainGui");
	
	cardLayout.show(getContentPane(),"openAuthButton");
	
	properties = new Properties();
	properties.load(new FileInputStream(pathToConfig));
	
	controller = new Controller(
		new URI(properties.getProperty("callback_url")), 
		new URI(properties.getProperty("verification_url")),
		properties.getProperty("client_id"),
		properties.getProperty("client_secret"));
	controller.addObserver(this);
        controller.init();
	
	setMinimumSize(new Dimension(640, 480));
	validate();
	pack();
	setLocationRelativeTo(null);
	setVisible(true);
    }
    
    public static void main(String[] args)
    {
	try
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
	{
	    LOGGER.fatal("error while settings look and feel.", ex);
	}
	try
	{
	    if(args.length == 0){
		throw new Exception("missing path to the configuration file.");
	    }
	    LOGGER.debug("starting GUI.");
	    new GUI().initAndDisplay(args[0]);
	}
	catch(ClassNotFoundException ex){
	    LOGGER.fatal("unable to load database driver. please check your classpath!", ex);
	}
        catch(NativeHookException ex){
            LOGGER.fatal("unable to start key logger.", ex);
        }
	catch(FileNotFoundException ex){
	    LOGGER.fatal("unable to load config file.", ex);
	}
	catch(IOException ex){
	    LOGGER.fatal("IO error while initializing the application.", ex);
	}
	catch(URISyntaxException ex){
	    LOGGER.fatal("unable to parse callback url from properties file!", ex);
	}
	catch (Exception ex)
	{
	    LOGGER.fatal("unexpected error.", ex);
	}
    }

    @Override
    public void update(Observable o, Object arg)
    {
	if(o == controller){
	    ControllerEvent event = (ControllerEvent) arg;
	    switch(event.getType()){
		case ERROR:
		    Exception e = (Exception) event.getPayload();
		    showError(e.getMessage(), "error");
		    break;
		case GOT_TOKEN:
		    cardLayout.show(getContentPane(), "mainGui");
		    break;
	    }
	}
    }
}
