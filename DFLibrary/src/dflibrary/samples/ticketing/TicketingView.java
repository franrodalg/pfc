package dflibrary.samples.ticketing;

import javax.swing.*;
import javax.swing.SwingWorker.StateValue;
import java.awt.*;
import java.awt.event.*;

import dflibrary.library.DFResponse;

/**
 * 
 * @author Francisco Rodríguez Algarra
 *
 */
public class TicketingView {

	/**
	 * 
	 */
	public TicketingView(){
		
		initModel();
		initGUI();
		start();
		
	}
	
	/**
	 * 
	 */
	private void initModel(){
		
		this.model = new TicketingModel(this);
		
	}
	
	/**
	 * 
	 */
	private void initGUI(){
		
		this.gui = new GUI();
		this.gui.setVisible(true);
		
	}
	
	/**
	 * 
	 */
	private void start(){
		
		log("Starting Ticketing Demo\n");
		gui.start();
		
	}
	
	/**
	 * 
	 */
	private void close(){
		
		log("Closing Ticketing Demo\n");
		
	}
	
	/**
	 * 
	 */
	protected void scanComplete(){
		
		log("Context scan complete\n");
		
	}
	
	/**
	 * 
	 * @param readers
	 */
	protected void listReaders(String[] readers){
		
		gui.listReaders(readers);
	}
	
	/**
	 * 
	 */
	protected void selectReader(){
		
		String reader = gui.getSelectedReader();
		
		getModel().selectReader(reader);
		
	}
	
	/**
	 * 
	 * @param reader
	 */
	protected void readerConnected(String reader){
		
		log("Selected reader: " + reader + "\n");
		
	}
	
	/**
	 * 
	 * @param app
	 */
	protected void execAppWorker(TicketingApp app){
		
		gui.execAppWorker(app);
		
	}
	
	/**
	 * 
	 * @param app
	 * @param events
	 * @param dates
	 */
	protected void fillComp(TicketingApp app, boolean events, boolean dates){
		
		gui.fillComp(app, events, dates);
		
	}
	
	//Outputs
	
	/**
	 * 
	 * @param s
	 */
	protected void log(String s){
		
		gui.log(s);
		
	}
	
	/**
	 * 
	 * @param res
	 */
	protected void log(DFResponse res){
		
		log(res.toString());
		
	}
	/**
	 * 
	 * @param s
	 */
	protected void msg(String s){
		
		gui.msg(s);
		
	}
	
	/**
	 * 
	 * @param s
	 */
	protected void error(String s){
		
		JOptionPane.showMessageDialog(gui, s, "", JOptionPane.ERROR_MESSAGE);
		System.err.println(s);
		
	}
	
	/**
	 * 
	 * @param s
	 */
	protected void show(String s){
		
		JOptionPane.showMessageDialog(gui, s, "", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected GUI getGUI(){ return this.gui; }
	
	/**
	 * 
	 * @return
	 */
	protected TicketingModel getModel(){ return this.model; }
	
	/**
	 * 
	 * @return
	 */
	protected boolean getLock(){return this.lock;}
	
	/**
	 * 
	 * @param lock
	 */
	protected void setLock(boolean lock){this.lock = lock;}
	
	private boolean lock;
	private GUI gui;
	private TicketingModel model;
	
	
	/**
	 * 
	 * @author Francisco Rodríguez Algarra
	 *
	 */
	class GUI extends JFrame{
		
		/**
		 * 
		 */
		public GUI(){
			
			try{			
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				SwingUtilities.updateComponentTreeUI(this);
			}catch(Exception e){}			
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			this.tdPanel = new TicketingDemoPanel();
			
			this.setContentPane(tdPanel);
			this.pack();
			
			this.setTitle("Ticketing demo - " + TicketingApp.SELECT_READER.toString());
			this.setSize(DEFWIDTH, DEFHEIGHT);
			
		}
		
		/**
		 * 	
		 */
		protected void start(){
			
			
			if(startWorker != null) {	if(startWorker.getState() == StateValue.STARTED) startWorker.cancel(true);}
						
			startWorker = new StartWorker();
			startWorker.execute();
			
		}
		
		/**
		 * 
		 * @param readers
		 */
		protected void listReaders(String[] readers){
			
			if((readers == null) || (readers.length ==0)) {
				
				log("No readers connected\n");
				msg("No readers connected. Please connect at least one and press Re-Scan");								
				return;
			}
			
			log("Connected Readers:");
			for(int i = 0; i<readers.length; i++){
				log("" + i + ".- " + readers[i]);
				boxReaders.addItem(readers[i]);
				
			}
			log("");
			boxReaders.setEnabled(true);
			btnOkReader.setEnabled(true);
			msg("Select Reader and press OK");
			
		}
		
		/**
		 * 
		 * @return
		 */
		protected String getSelectedReader(){
			
			return (String)boxReaders.getSelectedItem();
			
		}
		
		protected void execAppWorker(TicketingApp app){
			
			if(appWorker != null){
				if(appWorker.getState() == StateValue.STARTED) appWorker.cancel(true);
			}
			
			appWorker = new AppWorker(app);
			appWorker.execute();
		}
		
		/**
		 * 
		 * @param app
		 * @param events
		 * @param dates
		 */
		protected void fillComp(TicketingApp app, boolean events, boolean dates){
			
			(new FillCompWorker(app, events, dates)).execute();
			
		}
		
		/**
		 * 
		 * @param s
		 */
		protected void log(String s){
			
			if(logPanel != null) logPanel.addText(s);
			
		}
		
		/**
		 * 
		 * @param s
		 */
		protected void msg(String s){
			
			msgPanel.setMsg(s);
			
		}
		
		
		/**
		 * 
		 * @param app
		 */
		protected void setPanel(TicketingApp app){
			
			this.setTitle("Ticketing demo - " + app.toString());
			
			this.tdPanel.changeAppPanel(app);
			
			this.setContentPane(tdPanel);
			this.pack();
			
			this.setSize(DEFWIDTH, DEFHEIGHT);
			
			
		}
		
		public static final int DEFWIDTH = 600;
		public static final int DEFHEIGHT = 400;
		
		//GUI COMPONENTS		
		
		private TicketingDemoPanel tdPanel;
		private AppPanel appPanel;
		private JPanel msgInfo;
		private MsgPanel msgPanel;
		private MsgPanel infoPanel;
		private LogPanel logPanel;
		
		//App Panels
		
		private SelectReaderPanel srPanel;
		private AppLauncherPanel alPanel;
		private ConfigPanel cfgPanel;
		private BuyPanel buyPanel;
		private EntrancePanel entPanel;
		
		//Select Reader Panel
		
		private JPanel srBtns;
		private JComboBox boxReaders;
		private JButton btnReScanReaders;
		private JButton btnOkReader;
		
		//LogPanel
		
		private JTextArea ta;
		private JScrollPane sbrText;
		private JButton btnClearLog;
		
		//App Launcher Panel
		
		private JButton btnConfig;
		private JButton btnBuy;
		private JButton btnEnt;
		private JButton btnBackAl;
		
		//Cfg Panel
		
		private CfgOptPanel cfgOptPanel;
		private JPanel cfgBtnsPanel;
		private ButtonGroup groupCfgOpt;
		private BtnBackApp btnBackCfg;
		
		//Buy Panel
		
		private BuyEventPanel buyEventPanel;
		private BuyTicketPanel buyTicketPanel;
		
		private JComboBox boxBuyEvents;
		private JComboBox boxBuyDate;
		
		private JLabel ticketLabel;
		private JButton btnChangeTicket;
		
		private BtnBackApp btnBackBuy;
		
		//Entrance Panel
		
		private EntEventPanel entEventPanel;
		private JComboBox boxEntEvents;
		private JComboBox boxEntDate;
		private BtnBackApp btnBackEnt;
		
		//Workers
		
		private StartWorker startWorker;
		private AppWorker appWorker;
		/*
		
		private ConfigWorker cfgWorker;
		private BuyWorker buyWorker;
		private EntWorker entWorker;
		*/
		
		//GUI CLASSES
		
		//PANELS
		
		//Ticketing Demo Panel
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class TicketingDemoPanel extends JPanel{
			
			/**
			 * 
			 */
			public TicketingDemoPanel(){
				this.setLayout(new BorderLayout());
				this.setSize(DEFWIDTH, DEFHEIGHT);
				
				appPanel = new AppPanel();
				this.add(appPanel, BorderLayout.NORTH);
				
				changeAppPanel(TicketingApp.SELECT_READER);
				
				msgInfo = new JPanel();
				msgInfo.setLayout(new GridLayout(2,1,3,3));
				
				msgPanel = new MsgPanel("Establishing context");
				msgInfo.add(msgPanel);
				
				infoPanel = new MsgPanel("");
				msgInfo.add(infoPanel);
				
				this.add(msgInfo, BorderLayout.CENTER);
				
				logPanel = new LogPanel();
				this.add(logPanel, BorderLayout.SOUTH);
				
				
			}
			
			/**
			 * 
			 * @param app
			 */
			protected void changeAppPanel(TicketingApp app){
				
				srPanel.setVisible(false);
				alPanel.setVisible(false);
				cfgPanel.setVisible(false);
				buyPanel.setVisible(false);
				entPanel.setVisible(false);
				
				if (app == TicketingApp.SELECT_READER){
					srPanel.setVisible(true);
					if(msgPanel != null) msg("Select Reader and Press Ok");
				}
				else if (app == TicketingApp.APP_LAUNCHER){
					alPanel.setVisible(true);
					msg("Select App or click Back button to change reader selection");
				}
				else if (app == TicketingApp.CONFIG_PICC){
					cfgPanel.setVisible(true);
					msg("Insert card to proceed with configuration");
				}
				else if (app == TicketingApp.BUY_TICKET){
					buyPanel.setVisible(true);
					msg("Select event and insert card to confirm purchase");
				}
				else if (app == TicketingApp.EVENT_ENTRANCE){
					entPanel.setVisible(true);
					msg("Select event and insert card to enter");
				}
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class AppPanel extends JPanel{
			
			public AppPanel(){
				
				srPanel = new SelectReaderPanel();
				this.add(srPanel);
				
				alPanel = new AppLauncherPanel();
				this.add(alPanel);
				
				cfgPanel = new ConfigPanel();
				this.add(cfgPanel);
				
				buyPanel = new BuyPanel();
				this.add(buyPanel);
				
				entPanel = new EntrancePanel();	
				this.add(entPanel);
				
			}
			
		}
		
		/**
		 * 
		 * @author frankie
		 *
		 */
		class SelectReaderPanel extends JPanel{
			
			public SelectReaderPanel(){
				
				
				boxReaders = new JComboBox();
				boxReaders.setEnabled(false);
				this.add(boxReaders);
				
				srBtns = new JPanel();
				
				btnReScanReaders = new JButton("Re-Scan");
				btnReScanReaders.addActionListener(new ReScanReadersListener());
				btnReScanReaders.setEnabled(true);
				srBtns.add(btnReScanReaders);
				
				btnOkReader = new JButton("Ok");
				btnOkReader.addActionListener(new OkReaderListener());
				btnOkReader.setEnabled(false);
				srBtns.add(btnOkReader);
				
				this.add(srBtns);
			}
			
			
		}
		
		/**
		 * 
		 * @author frankie
		 *
		 */
		class AppLauncherPanel extends JPanel{
			
			/**
			 * 
			 */
			public AppLauncherPanel(){
				
				btnConfig = new JButton("Configure Card");
				btnConfig.addActionListener(new ConfigListener());
				add(btnConfig);
				
				btnBuy = new JButton("Buy Tickets");
				btnBuy.addActionListener(new BuyListener());
				add(btnBuy);
				
				btnEnt = new JButton("Event Entrance");
				btnEnt.addActionListener(new EntListener());
				add(btnEnt);
				
				btnBackAl = new JButton("Back");
				btnBackAl.addActionListener(new BackAlListener());
				add(btnBackAl);
				
			}
			
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class ConfigPanel extends JPanel{
			
			/**
			 * 
			 */
			public ConfigPanel(){
				
				this.setLayout(new GridLayout(1,2,30,0));
				
				cfgOptPanel = new CfgOptPanel();
				this.add(cfgOptPanel);
				
				cfgBtnsPanel = new JPanel();
				
				btnBackCfg = new BtnBackApp();
				cfgBtnsPanel.add(btnBackCfg);
				
				this.add(cfgBtnsPanel);
				
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BuyPanel extends JPanel{
				
			/**
			 * 
			 */
			public BuyPanel(){
				
				this.setLayout(new GridLayout(2,1));
				
				buyEventPanel = new BuyEventPanel();
				this.add(buyEventPanel);
				
				buyTicketPanel = new BuyTicketPanel();
				this.add(buyTicketPanel);
							
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class EntrancePanel extends JPanel{
		
			/**
			 * 
			 */
			public EntrancePanel(){
				
				this.setLayout(new GridLayout(2,1));
				
				entEventPanel = new EntEventPanel();
				this.add(entEventPanel);
				
				btnBackEnt = new BtnBackApp();
				this.add(btnBackEnt);

			}
		
		}

		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class MsgPanel extends JPanel{
			
			/**
			 * 
			 */
			public MsgPanel(){ this(""); }
			
			/**
			 * 
			 * @param msg
			 */
			public MsgPanel(String msg){
				msgLabel = new JLabel(msg);
				add(msgLabel);			
			}
		
			/**
			 * 
			 * @return
			 */
			public String getMsg(){
				return msgLabel.getText();
			}
			
			/**
			 * 
			 * @param msg
			 */
			public void setMsg(String msg){
			
				msgLabel.setText(msg);
			
			}
			
			private JLabel msgLabel;
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class LogPanel extends JPanel{
			

			/**
			 * 
			 */
			public LogPanel(){
				
				this.setLayout(new BorderLayout());
				
				ta = new JTextArea("", 10, 30);
				ta.setLineWrap(true);
				ta.setEditable(false);
				sbrText = new JScrollPane(ta);
				sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				add(sbrText, BorderLayout.NORTH);
				
				btnClearLog = new JButton("Clear Log");
				btnClearLog.addActionListener(new ClearLogListener());
				add(btnClearLog, BorderLayout.SOUTH);
				
			}
			
			/**
			 * 
			 * @param s
			 */
			public void addText(String s){
				ta.setText(ta.getText() + "\n" + s);
			}
			
			/**
			 * 
			 */
			public void clear(){
				ta.setText("");
			}
						
		}
		
		//Otros componentes
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class CfgOptPanel extends JPanel{
			
			/**
			 * 
			 */
			public CfgOptPanel(){
				
				this.setLayout(new GridLayout(3,1));
				
				groupCfgOpt = new ButtonGroup();
				
				ConfigOption opt;
				
				opt = ConfigOption.FORMAT_AND_CONFIG;				
				addBtn(opt.toString(), opt, true);
				
				opt = ConfigOption.FORMAT;				
				addBtn(opt.toString(), opt, false);
				
				opt = ConfigOption.KEEP_CONFIG;				
				addBtn(opt.toString(), opt, false);
				
				this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Configure Option"));
				
				
			}
			
			/**
			 * 
			 * @param name
			 * @param opt
			 * @param def
			 */
			private void addBtn(String name, ConfigOption opt, boolean def){
				
				JRadioButton rb = new JRadioButton(name);
				final ConfigOption opt2 = opt;
				rb.addActionListener( new ActionListener(){
					public void actionPerformed(ActionEvent event){
						getModel().setConfigOption(opt2);
						log("Configure Option selected: " + getModel().getConfigOption().toString());
					}
				}
				);
				groupCfgOpt.add(rb);
				rb.setSelected(def);
				this.add(rb);
				
			}
			
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BuyEventPanel extends JPanel{
			
			public BuyEventPanel(){
				
				boxBuyEvents = new JComboBox();
				this.add(boxBuyEvents);
				
				boxBuyDate = new JComboBox();
				this.add(boxBuyDate);
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BuyTicketPanel extends JPanel{
			
			public BuyTicketPanel(){
				
				ticketLabel = new JLabel("No available tickets");
				this.add(ticketLabel);
				
				btnChangeTicket = new JButton("Change");
				btnChangeTicket.setEnabled(false);
				this.add(btnChangeTicket);

				btnBackBuy = new BtnBackApp();
				this.add(btnBackBuy);
				
			}
			
		}
		

		
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class EntEventPanel extends JPanel{
		
			public EntEventPanel(){
				
				boxEntEvents = new JComboBox();
				this.add(boxEntEvents);
				
				boxEntDate = new JComboBox();
				this.add(boxEntDate);
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BtnBackApp extends JButton{
			
			/**
			 * 
			 */
			public BtnBackApp(){
				
				super("Back");
				this.addActionListener(new BackAppListener());
				
			}
					
		}

		
		
		//LISTENERS
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class ClearLogListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){ logPanel.clear(); }
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class ReScanReadersListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){ 
				
				log("Re-scanning connected readers\n");
				boxReaders.setEnabled(false);
				btnOkReader.setEnabled(false);
				boxReaders.removeAllItems();
				gui.start();
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class OkReaderListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				gui.setPanel(TicketingApp.APP_LAUNCHER);
				selectReader();
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class ConfigListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				TicketingApp app = TicketingApp.CONFIG_PICC;
				
				gui.setPanel(app);
				getModel().setApp(app);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BuyListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				TicketingApp app = TicketingApp.BUY_TICKET;
				
				gui.setPanel(app);
				getModel().setApp(app);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class EntListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				TicketingApp app = TicketingApp.EVENT_ENTRANCE;
				
				gui.setPanel(app);
				getModel().setApp(app);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BackAlListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				TicketingApp app = TicketingApp.SELECT_READER;
				
				gui.setPanel(app);
				getModel().setApp(app);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BackAppListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				if(appWorker != null) appWorker.cancel(true);
				
				TicketingApp app = TicketingApp.APP_LAUNCHER;	
				gui.setPanel(app);
				getModel().setApp(app);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class SelectEventListener implements ActionListener{
			
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent evet){
				
				(new FillCompWorker(getModel().getCurrApp(), false, true)).execute();			
				
			}
					
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class SelectDateListener implements ActionListener{
			
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent evet){
				
				(new FillCompWorker(getModel().getCurrApp(), false, false)).execute();			
				
			}
					
		}
		
		//WORKERS
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class StartWorker extends SwingWorker<Boolean, Void>{
			
			@Override
			public Boolean doInBackground(){
				getModel().start();
				return true;
			}
					
		}
		
		class AppWorker extends SwingWorker<Boolean, Void>{
			
			public AppWorker(TicketingApp app){
				this.app = app;
			}
			
			@Override
			public Boolean doInBackground(){
				log(app.toString());
				(new FillCompWorker(app)).execute();
				
				while(app == getModel().getCurrApp()){
					getModel().work();
					getModel().getAppState().nextState(getModel());
				}
				
				return true;
			}
			
			private TicketingApp app;
		
		}
		
		class FillCompWorker extends SwingWorker<Boolean, Void>{
			
			/**
			 * 
			 * @param app
			 */
			public FillCompWorker(TicketingApp app){
				
				this(app, true, true);
				
			}
			
			/**
			 * 
			 * @param app
			 * @param list_events
			 * @param list_dates
			 */
			public FillCompWorker(TicketingApp app, boolean list_events, boolean list_dates){
				
				this.app = app;
				setLock(true);
				this.list_events = list_events;
				this.list_dates = list_dates;
				
			}
			
			@Override
			public Boolean doInBackground(){
				
				try{
					if(getModel().getCurrApp() == app){
						
						prepareComponents(app);
						if(app == TicketingApp.EVENT_ENTRANCE) setEventID();
						setLock(false);
						return true;
					}
					
				}catch(Exception e){
					System.err.println(e.getMessage());
				}
				
				setLock(false);
				return true;
			}
			
			/**
			 * 
			 * @param app
			 */
			private void prepareComponents(TicketingApp app){
				
				disableComponents(app);
				fillComponents(app);
				enableComponents(app);
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void disableComponents(TicketingApp app){
				
				disableListeners(app);
				
				if(app == TicketingApp.BUY_TICKET){
					
					ticketLabel.setText("No available tickets yet");
					boxBuyEvents.setEnabled(false);
					boxBuyDate.setEnabled(false);
					btnChangeTicket.setEnabled(false);
					
				}
				else if(app == TicketingApp.EVENT_ENTRANCE){
					
					boxEntEvents.setEnabled(false);
					boxEntDate.setEnabled(false);
					
				}
				
			}
			
			private void disableListeners(TicketingApp app){
				

				if(app == TicketingApp.BUY_TICKET){
					
					removeListeners(boxBuyEvents);
					removeListeners(boxBuyDate);
										
				}
				else if(app == TicketingApp.EVENT_ENTRANCE){
					
					removeListeners(boxEntEvents);
					removeListeners(boxEntDate);
					
				}
				
				
			}
			
			/**
			 * 
			 * @param c
			 */
			private void removeListeners(JComboBox c){
				
				for(int i = c.getActionListeners().length; i > 0; i--){
					c.removeActionListener(c.getActionListeners()[i-1]);
				}
				
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void fillComponents(TicketingApp app){
				
				JComboBox be;
				JComboBox bd;
				
				if(app == TicketingApp.BUY_TICKET){
					be = boxBuyEvents;
					bd = boxBuyDate;
				}
				else if(app == TicketingApp.EVENT_ENTRANCE){
					be = boxEntEvents;
					bd = boxEntDate;
				}
				else return;
					
				if(list_dates){
						
						if(list_events){
							be.removeAllItems();
							fillEvents(app);
						}
						bd.removeAllItems();
						fillDates(app);
				}

				
				if(app == TicketingApp.BUY_TICKET) getTicket(app);	
				
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void enableComponents(TicketingApp app){
				
				enableListeners(app);
				
				if(app == TicketingApp.BUY_TICKET){
					
					boxBuyEvents.setEnabled(true);
					boxBuyDate.setEnabled(true);
					
					if(ticket == null){
						
						btnChangeTicket.setEnabled(false);
						msg("Select another event or click Back to cancel purchase process");
						
					}
					else{
						
						btnChangeTicket.setEnabled(false);
						msg("Insert card to confirm purchase");
						
					}
					
				}
				else if(app == TicketingApp.EVENT_ENTRANCE){
					
					boxEntEvents.setEnabled(true);
					boxEntDate.setEnabled(true);
					
					msg("Insert card to check ticket");
				}
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void enableListeners(TicketingApp app){
				
				if(app == TicketingApp.BUY_TICKET){
			
					boxBuyEvents.addActionListener(new SelectEventListener());
					boxBuyDate.addActionListener(new SelectDateListener());
					
				}
				else if(app == TicketingApp.EVENT_ENTRANCE){
					
					boxEntEvents.addActionListener(new SelectEventListener());
					boxEntDate.addActionListener(new SelectDateListener());
					
				}
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void fillEvents(TicketingApp app){
				
				events = getModel().getEvents();
				
				if((events == null) || (events.length == 0))
					throw new RuntimeException("No events found");
				
				JComboBox c;
				
				if(app == TicketingApp.BUY_TICKET) c = boxBuyEvents;
				else if(app == TicketingApp.EVENT_ENTRANCE) c = boxEntEvents;
				else return;
				
				c.addItem(events[0].getDescription());
				
				int i = 1;
				while(i < events.length){
					if(notRepeated(i)) c.addItem(events[i].getDescription());
					i++;
				}
						
			}
			
			/**
			 * 
			 * @param pos
			 * @return
			 */
			private boolean notRepeated(int pos){
				
				for(int i =0; i < pos; i++){
					if(events[pos].getDescription().equals(events[i].getDescription())) return false;
				}
				
				return true;
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void fillDates(TicketingApp app){
				
				JComboBox be;
				JComboBox bd;
				
				if(app == TicketingApp.BUY_TICKET){
					be = boxBuyEvents;
					bd = boxBuyDate;
				}
				else if(app == TicketingApp.EVENT_ENTRANCE){
					be = boxEntEvents;
					bd = boxEntDate;
				}
				else return;
				
				String event = (String)be.getSelectedItem();
				
				dates = getModel().getDates(event);
				
				for(int i = 0; i < dates.length; i++){
					bd.addItem(dates[i]);
				}
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void getTicket(TicketingApp app){
				
				JComboBox be;
				JComboBox bd;
				
				if(app == TicketingApp.BUY_TICKET){
					be = boxBuyEvents;
					bd = boxBuyDate;
				}
				else if(app == TicketingApp.EVENT_ENTRANCE){
					be = boxEntEvents;
					bd = boxEntDate;
				}
				else return;
				
				String event = (String)be.getSelectedItem();
				String date = (String)bd.getSelectedItem();
				
				try{
					ticket = getModel().getAvTicket(event, date);
					Ticket.Ubication ub = ticket.getUbication();
					
					ticketLabel.setText(ub.toString());
				
				}catch(Exception e){
					ticket = null;
					ticketLabel.setText("No available tickets");
				}
				
				getModel().setTicket(ticket);
				
			}
			
			/**
			 * 
			 */
			private void setEventID(){
				
				String description = (String)boxEntEvents.getSelectedItem();
				String date = (String)boxEntDate.getSelectedItem();
				
				int event_id = getModel().getEventID(description, date);
				getModel().setEventID(event_id);
				
			}
			
			/**
			 * 
			 * @param b
			 */
			public void setListEvents(boolean b){
				
				if(b == true){ list_dates = true; }
				list_events = b;
				
			}
			
			/**
			 * 
			 * @param b
			 */
			public void setListDates(boolean b){
				
				if(b == false){ list_events = false; }
				list_dates = b;
				
			}
			
			private TicketingApp app;
			private boolean list_events;
			private boolean list_dates;
			
			private Ticket ticket;
			private Event[] events;
			private String[] dates;
			
			
		}
		
	}
	
}
