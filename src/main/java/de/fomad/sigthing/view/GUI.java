package de.fomad.sigthing.view;

import de.fomad.sigthing.controller.ConfigUtility;
import de.fomad.sigthing.controller.Controller;
import de.fomad.sigthing.model.ApplicationConfiguration;
import de.fomad.sigthing.model.CharacterInfo;
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
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jnativehook.NativeHookException;
import de.fomad.sigthing.model.Character;
import de.fomad.sigthing.model.Signature;
import de.fomad.sigthing.model.SolarSystem;
import de.fomad.sigthing.view.icons.IconCache;
import de.fomad.sigthing.view.sounds.SoundManager;
import java.awt.Component;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * @author binary gamura
 */
public class GUI extends JFrame implements Observer {

    private static final Logger LOGGER = LogManager.getLogger(GUI.class);

    private CardLayout cardLayout;

    private transient Controller controller;

    private InfoPanel infoPanel;

    private HistoryPanel historyPanel;

    private static final String TITLE = "Sig Thing";

    private transient final IconCache iconCache;

    private AboutDialog aboutDialog;
    
    private OptionDialog configurationDialog;
    
    private CommentDialog commentDialog;
    
    private SignatureTable table;
    
    private final SoundManager soundManager;
    
    private final ApplicationConfiguration configuration;
    
    private GUI() {
	super(TITLE);
	
	iconCache = new IconCache();
        configuration = new ApplicationConfiguration();
        soundManager = new SoundManager(configuration);
	setIconImage(iconCache.getIcon(IconCache.IconId.APP_ICON));
	GUI.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	GUI.this.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
                
                try
                {
                    ConfigUtility.writeConfig(configuration);
                }
                catch(IOException ex){
                    LOGGER.warn("unable to save configuration.", ex);
                }
                exitOperation();
	    }
	});
    }

    ApplicationConfiguration getConfig(){
        return configuration;
    }
    
    IconCache getIconCache() {
	return iconCache;
    }

    private void exitOperation() {
	int result = JOptionPane.showConfirmDialog(this, "do you really want to quit?", "quit?", JOptionPane.YES_NO_OPTION);
	if (result == JOptionPane.YES_OPTION) {
	    controller.shutDown();
	    dispose();
	}
    }

    private void showError(String title, String message) {
	JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createMainGui() {
	JPanel mainGui = new JPanel(new BorderLayout());
	
	infoPanel = new InfoPanel(this);

        table = new SignatureTable();
        table.getSelectionModel().addListSelectionListener((e) -> {
            int row = table.getSelectedRow();
            if(row >= 0){
                Signature selectedSignature = table.getSignatureAtRow(row);
                if(selectedSignature != null){
                    infoPanel.setCurrentSignature(selectedSignature);
                }
            }
        });
        
	JScrollPane centerPanel = new JScrollPane(table);
	centerPanel.setBorder(BorderFactory.createTitledBorder("current system"));

//	JButton helpButton = new JButton("help");
//	helpButton.setMnemonic('h');

	JButton deleteButton = new JButton("delete", iconCache.getImageIcon(IconCache.IconId.CANCEL_ICON));
        deleteButton.addActionListener(e -> {
            try {
                int selectedRow = table.getSelectedRow();
                if(selectedRow >= 0){
                    Signature selected = table.getSignatureAtRow(selectedRow);
                    if(selected != null){
                        int result = JOptionPane.showConfirmDialog(GUI.this, "do you really want to delete \""+selected.getSignature()+"\"?", "confirm", JOptionPane.YES_NO_OPTION);
                        if(result == JOptionPane.YES_OPTION){
                            controller.getDatabaseController().deleteSignature(selected);
                            table.setData(controller.getSignaturesForCurrentSystem());
                        }
                    }
                }
            }
            catch(SQLException ex){
                LOGGER.warn("error while deleting single signature.", ex);
                JOptionPane.showMessageDialog(GUI.this, ex.getMessage(), "error while deleting", JOptionPane.WARNING_MESSAGE);
            }
        });
	deleteButton.setMnemonic('d');

	JButton commentButton = new JButton("comment", iconCache.getImageIcon(IconCache.IconId.COMMENT));
        commentButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
                if(selectedRow >= 0){
                    Signature selected = table.getSignatureAtRow(selectedRow);
                    if(selected != null){
                        commentDialog.openCentered(selected);
                    }
                }
            
        });
	commentButton.setMnemonic('c');

	JButton optionsButton = new JButton("options", iconCache.getImageIcon(IconCache.IconId.CONFIGURATION));
        optionsButton.addActionListener(e -> {configurationDialog.openCentered(); initAlwaysOnTop();});
	optionsButton.setMnemonic('o');

	JButton aboutButton = new JButton("about", iconCache.getImageIcon(IconCache.IconId.INFO_ICON));
	aboutButton.addActionListener(e -> {
	    aboutDialog.openCentered();
	});
        aboutButton.setMnemonic('a');

	optionsButton.setMnemonic('o');
	JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
	buttonPanel.add(deleteButton);
	buttonPanel.add(commentButton);
	buttonPanel.add(optionsButton);
//	buttonPanel.add(helpButton);
	buttonPanel.add(aboutButton);

	mainGui.add(infoPanel, BorderLayout.NORTH);
	mainGui.add(centerPanel, BorderLayout.CENTER);
	mainGui.add(buttonPanel, BorderLayout.SOUTH);
	mainGui.add(historyPanel, BorderLayout.EAST);

	return mainGui;
    }

    Controller getController() {
        return controller;
    }

    private void initAndDisplay(Properties properties) throws IOException, URISyntaxException, NativeHookException, SQLException, ClassNotFoundException, PropertyVetoException {

	cardLayout = new CardLayout();
	getContentPane().setLayout(cardLayout);
        
        try{
            ConfigUtility.readConfig(configuration);
        }
        catch(IOException ex){
            LOGGER.warn("unable to read configuration. using defaults.", ex);
        }
	JButton authButton = new JButton(iconCache.getImageIcon(IconCache.IconId.LOGIN_ICON));
	authButton.addActionListener((e) -> {
	    try {
		String loginURL = properties.getProperty("auth_url") + "/authorize";
		String clientId = properties.getProperty("client_id");
		String callbackUrl = properties.getProperty("callback_url");

		URIBuilder builder = new URIBuilder(loginURL);
		builder.addParameter("response_type", "code");
		builder.addParameter("redirect_uri", callbackUrl);
		builder.addParameter("client_id", clientId);
		builder.addParameter("scope", "characterLocationRead characterNavigationWrite");

		URI target = builder.build();
		if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    showError("Unable to open browser! you cant use this application on your system. sorry...", "unable to open browser");
		    throw new Exception("open the browser is not supported on your operating system!");
		}
		controller.startLoginProcess();
		Desktop.getDesktop().browse(target);

	    }
	    catch (BindException ex) {
		showError("error", "unable to open local port. is the app already running or is your firewall blocking?");
		LOGGER.fatal("unable to open local server socket!", ex);
	    }
	    catch (URISyntaxException | IOException ex) {
		LOGGER.fatal("error while browsing to the login site!", ex);
	    }
	    catch (Exception ex) {
		LOGGER.fatal("unexpected error!", ex);
	    }
	});

	int callbackPort = new URI(properties.getProperty("callback_url")).getPort();
	String authUrl = properties.getProperty("auth_url");
	String apiUrl = properties.getProperty("api_url");
	String clientId = properties.getProperty("client_id");
	String clientSecret = properties.getProperty("client_secret");

	controller = new Controller(callbackPort, authUrl, apiUrl, clientId, clientSecret);
	controller.addObserver(this);
	controller.init();
	
	historyPanel = new HistoryPanel(controller.getModel());
        historyPanel.getList().addListSelectionListener(e -> {
            try {
                if(!e.getValueIsAdjusting()){
                    JList<HistoryPanel.HistoryNode> list = (JList<HistoryPanel.HistoryNode>) e.getSource();
                    HistoryPanel.HistoryNode selectedValue = list.getSelectedValue();
                    if(selectedValue != null){
                        SolarSystem solarSystem = selectedValue.getSolarSystem();
                        infoPanel.setCurrentSolarSystem(solarSystem);
                        table.setData(controller.getDatabaseController().querySignaturesFor(solarSystem.getId()));
                    }
                }
            }
            catch(SQLException ex){
                LOGGER.warn("error while getting data.", ex);
                JOptionPane.showMessageDialog(GUI.this, ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
            }
        });

	aboutDialog = new AboutDialog(this);
        configurationDialog = new OptionDialog(this);
        commentDialog = new CommentDialog(this);
	
	getContentPane().add(authButton, "openAuthButton");
	getContentPane().add(createMainGui(), "mainGui");

	cardLayout.show(getContentPane(), "openAuthButton");
//	cardLayout.show(getContentPane(),"mainGui");

        
        
	initAlwaysOnTop();
        toFront();

	setMinimumSize(new Dimension(640, 480));
        setPreferredSize(new Dimension(640, 480));
	validate();
	pack();
	setLocationRelativeTo(null);
	setVisible(true);
    }
    
    private void initAlwaysOnTop(){
        boolean alwaysOnTop = configuration.isAlwaysOnTop();
        setFocusable(!alwaysOnTop);
	setAlwaysOnTop(alwaysOnTop);
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	System.setProperty("awt.useSystemAAFontSettings", "on");
	System.setProperty("swing.aatext", "true");
	try {
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JDialog.setDefaultLookAndFeelDecorated(true);
	    UIManager.setLookAndFeel(new NimbusLookAndFeel());
	}
	catch (Exception ex) {
	    LOGGER.fatal("error while settings look and feel.", ex);
	}
	try (InputStream inputStream = args.length == 0 ? GUI.class.getClassLoader().getResourceAsStream("config.properties") : new FileInputStream(args[0])){

	    Properties properties = new Properties();
	    properties.load(inputStream);
	    
	    LOGGER.debug("starting GUI.");
	    new GUI().initAndDisplay(properties);
	}
	catch (ClassNotFoundException ex) {
	    LOGGER.fatal("unable to load database driver. please check your classpath!", ex);
	}
	catch (NativeHookException ex) {
	    LOGGER.fatal("unable to start key logger.", ex);
	}
	catch (FileNotFoundException ex) {
	    LOGGER.fatal("unable to load config file.", ex);
	}
	catch (IOException ex) {
	    LOGGER.fatal("IO error while initializing the application.", ex);
	}
	catch (URISyntaxException ex) {
	    LOGGER.fatal("unable to parse callback url from properties file!", ex);
	}
	catch (Exception ex) {
	    LOGGER.fatal("unexpected error.", ex);
	}
    }

    @Override
    public void update(Observable o, Object arg) {
	try {
	    if (o == controller) {
		ControllerEvent event = (ControllerEvent) arg;
		switch (event.getType()) {
		    case ERROR:
			Exception e = (Exception) event.getPayload();
                        if(isVisible()){
                            showError(e.getMessage(), "error");
                        }
			break;
		    case GOT_TOKEN:
			cardLayout.show(getContentPane(), "mainGui");
			LOGGER.info("got access token from auth api.");
			break;
		    case GOT_CHARACTER_INFO:
			CharacterInfo characterInfo = (CharacterInfo) event.getPayload();
			setTitle(TITLE + " (" + characterInfo.getName() + ")");
			LOGGER.info("got complete character info.");
			break;
		    case GOT_CHARACTER:
			Character character = (Character) event.getPayload();
			LOGGER.info("got complete character data.");
			infoPanel.setIcon(character);
			break;
		    case SOLAR_SYSTEM_CHANGE:
                        table.setData(controller.getSignaturesForCurrentSystem());
			infoPanel.setCurrentSolarSystem((SolarSystem) event.getPayload());
			historyPanel.repaintList();
			soundManager.playSound(SoundManager.SoundId.NOTIFICATION_SOLAR_SYSTEM_CHANGE);
			break;
                    case NEW_SIGNATURES:
                        table.setData(controller.getSignaturesForCurrentSystem());
                        soundManager.playSound(SoundManager.SoundId.NOTIFICATION_NEW_SIGNATURES);
                        break;
		}
	    }
	}
	catch (MalformedURLException | SQLException ex) {
	    LOGGER.error("error while handling event!", ex);
	}
    }

    public static class SolarSystemRenderer extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    SolarSystem solarSystem = (SolarSystem) value;
	    label.setText(solarSystem.getName());
	    return label;
	}
    }
}
