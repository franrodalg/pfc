package dflibrary.samples.wallet;

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
public class WTView {

	/**
	 * 
	 */
	public WTView(){
		
		initModel();
		initGUI();
		start();
		
	}
	
	/**
	 * 
	 */
	private void initModel(){
		
		this.model = new WTModel(this);
		
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
	protected void execAppWorker(WTApp app){
		
		gui.execAppWorker(app);
		
	}
	
	/**
	 * 
	 * @param balance
	 */
	protected void setCurrentBalance(String balance){
		
		this.gui.lblCurrentBalance.setText(balance);
		
	}
	
	/**
	 * 
	 * @param balance
	 */
	protected void setCurrentBalance(int balance){
		
		setCurrentBalance(Integer.toString(balance));
		
	}
	
	/**
	 * 
	 * @param app
	 * @param events
	 * @param dates
	 */
	protected void fillComp(WTApp app, boolean events, boolean dates){
		
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
	protected WTModel getModel(){ return this.model; }
	
	
	/**
	 * 
	 * @param lock
	 */
	protected void setLock(boolean lock){this.lock = lock;}
	
	/**
	 * 
	 * @return
	 */
	protected boolean getLock(){ return this.lock; }
	
	/**
	 * 
	 * @return
	 */
	protected boolean getFRAllowed(){ return this.frAllowed; }
	
	/**
	 * 
	 * @return
	 */
	protected boolean getIBAllowed(){ return this.ibAllowed; }
	
	/**
	 * 
	 * @return
	 */
	protected boolean getRFAllowed(){ return this.rfAllowed; }
	
	/**
	 * 
	 * @return
	 */
	protected boolean getTDAllowed(){ return this.tdAllowed; }
	
	/**
	 * 
	 * @return
	 */
	protected boolean getTCAllowed(){ return this.tcAllowed; }
	
	
	private boolean lock;
	private boolean frAllowed;
	private boolean ibAllowed;
	private boolean rfAllowed;
	private boolean tdAllowed;
	private boolean tcAllowed;
	private GUI gui;
	private WTModel model;
	
	
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
			
			this.tdPanel = new WalletTicketingDemoPanel();
			
			this.setContentPane(tdPanel);
			this.pack();
			
			this.setTitle("Ticketing demo - " + WTApp.SELECT_READER.toString());
			this.setSize(DEFWIDTH, DEFHEIGHT);
			
		}
		
		/**
		 * 	
		 */
		protected void start(){
			
			
			if(startWorker != null) {
				if(startWorker.getState() == StateValue.STARTED) startWorker.cancel(true);
			}
						
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
		
		
		/**
		 * 
		 * @param app
		 */
		protected void execAppWorker(WTApp app){
			
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
		protected void fillComp(WTApp app, boolean events, boolean dates){
			
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
		protected void setPanel(WTApp app){
			
			this.setTitle("Ticketing demo - " + app.toString());
			
			this.tdPanel.changeAppPanel(app);
			
			this.setContentPane(tdPanel);
			this.pack();
			
			this.setSize(DEFWIDTH, DEFHEIGHT);
			
			
		}
		
		public static final int DEFWIDTH = 800;
		public static final int DEFHEIGHT = 400;
		
		//GUI COMPONENTS		
		
		private WalletTicketingDemoPanel tdPanel;
		private AppPanel appPanel;
		private MsgPanel msgPanel;
		private LogPanel logPanel;
		
		//App Panels
		
		private SelectReaderPanel srPanel;
		private SelectAppPanel saPanel;
		private FactoryResetPanel frPanel;
		private WalletAppLauncherPanel walPanel;
		private WalletConfigPanel wcfgPanel;
		private BalanceCheckPanel bcPanel;
		private RefillingPanel rPanel;
		private TicketingAppLauncherPanel talPanel;
		private TickConfigPanel tcfgPanel;
		private PurchasePanel buyPanel;
		private EntrancePanel entPanel;		
		
		//Log Panel
		
		private JTextArea ta;
		private JScrollPane sbrText;
		private JButton btnClearLog;
				
		//Select Reader Panel
		
		private JPanel srBtns;
		private JComboBox boxReaders;
		private JButton btnReScanReaders;
		private JButton btnOkReader;		
		
		//Select App Panel
		
		private JButton btnFactReset;
		private JButton btnWallet;
		private JButton btnTick;
		private JButton btnBackSA;
		
		//Factory Reset Panel
		
		private JComboBox boxBanksFR;
		private JButton btnBackFR;
		private JCheckBox checkFR;
		
		//Wallet App Launcher Panel
		
		private JButton btnWalletConfig;
		private JButton btnBalanceCheck;
		private JButton btnRefilling;
		private JButton btnBackWAL;
		
		//Wallet Configuration Panel
		
		private JComboBox boxBanksWalletCfg;
		private JTextArea txtInitialBalance;
		private JButton btnBackWalletCfg;
		
		//Balance Check Panel
		
		private JLabel lblCurrentBalanceTxt;
		private JLabel lblCurrentBalance;
		private JButton btnBackBalanceCheck;
		
		//Refilling Panel
		
		private JLabel lblCredit;
		private JTextArea txtCredit;
		private JButton btnBackRefilling;
		
		//Ticketing App Launcher Panel
		
		private JButton btnTickConfig;
		private JButton btnPurchase;
		private JButton btnEntrance;
		private JButton btnBackTAL;
		
		//Ticketing Configuration Panel
		
		private CfgOptPanel cfgOptPanel;
		private JPanel cfgBtnsPanel;
		private ButtonGroup groupCfgOpt;
		private JCheckBox checkTickDelete;
		private JCheckBox checkTickCreate;
		private JButton btnBackTickCfg;
		
		//ticketing Purchase Panel
		
		private PurchaseEventPanel buyEventPanel;
		private PurchaseTicketPanel buyTicketPanel;
		
		private JComboBox boxBuyEvents;
		private JComboBox boxBuyDate;
		
		private JLabel ticketLabel;
		private JButton btnChangeTicket;
		
		private JButton btnBackBuy;
		
		//Ticketing Entrance Panel
		
		private EntEventPanel entEventPanel;
		private JComboBox boxEntEvents;
		private JComboBox boxEntDate;
		private JButton btnBackEnt;
		
		//Workers
		
		private StartWorker startWorker;
		private AppWorker appWorker;
		
		//GUI CLASSES
		
		//PANELS
		
		//Wallet & Ticketing Demo Panel
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class WalletTicketingDemoPanel extends JPanel{
			
			/**
			 * 
			 */
			public WalletTicketingDemoPanel(){
				
				this.setLayout(new BorderLayout());
				this.setSize(DEFWIDTH, DEFHEIGHT);
				
				appPanel = new AppPanel();
				this.add(appPanel, BorderLayout.NORTH);
				
				changeAppPanel(WTApp.SELECT_READER);
				
				msgPanel = new MsgPanel("Establishing context");
				this.add(msgPanel, BorderLayout.CENTER);
				
				logPanel = new LogPanel();
				this.add(logPanel, BorderLayout.SOUTH);
				
				
			}
			
			/**
			 * 
			 * @param app
			 */
			protected void changeAppPanel(WTApp app){
				
				srPanel.setVisible(false);
				saPanel.setVisible(false);
				frPanel.setVisible(false);
				walPanel.setVisible(false);
				wcfgPanel.setVisible(false);
				bcPanel.setVisible(false);
				rPanel.setVisible(false);
				talPanel.setVisible(false);
				tcfgPanel.setVisible(false);
				buyPanel.setVisible(false);
				entPanel.setVisible(false);
				
				if (app == WTApp.SELECT_READER){
					srPanel.setVisible(true);
					if(msgPanel != null) msg("Select Reader and Press Ok");
				}
				else if (app == WTApp.SELECT_APP){
					saPanel.setVisible(true);
					msg("Select App or click Back button to change reader selection");
				}
				else if (app == WTApp.FACTORY_RESET){
					
					frPanel.setVisible(true);
					msg("Check the box to proceed with a Factory Reset and then insert Card");
				}
				else if (app == WTApp.SELECT_WALLET_APP){
					walPanel.setVisible(true);
					msg("Select Wallet App or click Back button to change app selection");
				}
				else if (app == WTApp.CONFIG_WALLET){
					wcfgPanel.setVisible(true);
					msg("Insert card to proceed with configuration");
				}
				else if (app == WTApp.BALANCE_CHECK){
					bcPanel.setVisible(true);
					msg("Insert card to check your current wallet balance");
				}
				else if (app == WTApp.REFILLING){
					rPanel.setVisible(true);
					msg("Insert card to refill your wallet balance");
				}
				else if (app == WTApp.SELECT_TICK_APP){
					talPanel.setVisible(true);
					msg("Select Ticketing App or click Back button to change app selection");
				}
				else if (app == WTApp.CONFIG_TICK){
					tcfgPanel.setVisible(true);
					msg("Insert card to proceed with configuration");
				}
				else if (app == WTApp.PURCHASE_TICKET){
					buyPanel.setVisible(true);
					msg("Select event and insert card to confirm purchase");
				}
				else if (app == WTApp.EVENT_ENTRANCE){
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
				
				saPanel = new SelectAppPanel();
				this.add(saPanel);
				
				frPanel = new FactoryResetPanel();
				this.add(frPanel);
				
				walPanel = new WalletAppLauncherPanel();
				this.add(walPanel);
				
				wcfgPanel = new WalletConfigPanel();
				this.add(wcfgPanel);
				
				bcPanel = new BalanceCheckPanel();
				this.add(bcPanel);
				
				rPanel = new RefillingPanel();
				this.add(rPanel);
				
				talPanel = new TicketingAppLauncherPanel();
				this.add(talPanel);
				
				tcfgPanel = new TickConfigPanel();
				this.add(tcfgPanel);
				
				buyPanel = new PurchasePanel();
				this.add(buyPanel);
				
				entPanel = new EntrancePanel();	
				this.add(entPanel);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
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
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class SelectAppPanel extends JPanel{
			
			/**
			 * 
			 */
			public SelectAppPanel(){
				
				btnFactReset = new JButton("Factory Reset");
				btnFactReset.addActionListener(new ChangeAppListener(WTApp.FACTORY_RESET));
				this.add(btnFactReset);
				
				btnWallet = new JButton("Wallet");
				btnWallet.addActionListener(new ChangeAppListener(WTApp.SELECT_WALLET_APP));
				this.add(btnWallet);
				
				btnTick = new JButton("Ticketing");
				btnTick.addActionListener(new ChangeAppListener(WTApp.SELECT_TICK_APP));
				this.add(btnTick);
				
				btnBackSA = new JButton("Back");
				btnBackSA.addActionListener(new BackListener(WTApp.SELECT_READER));
				this.add(btnBackSA);
				
			}
			
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class FactoryResetPanel extends JPanel{
			
			public FactoryResetPanel(){
				
				boxBanksFR = new JComboBox();
				this.add(boxBanksFR);
				
				checkFR = new JCheckBox("Confirm Factory Reset");
				checkFR.addActionListener(new CheckFRListener());
				this.add(checkFR);
				
				btnBackFR = new JButton("Back");
				btnBackFR.addActionListener(new BackListener(WTApp.SELECT_APP));
				this.add(btnBackFR);				
				
			}
			
		}
			
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class WalletAppLauncherPanel extends JPanel{
			
			public WalletAppLauncherPanel(){
				
				btnWalletConfig = new JButton("Configuration");
				btnWalletConfig.addActionListener(new ChangeAppListener(WTApp.CONFIG_WALLET));
				this.add(btnWalletConfig);
				
				btnBalanceCheck = new JButton("Balance Check");
				btnBalanceCheck.addActionListener(new ChangeAppListener(WTApp.BALANCE_CHECK));
				this.add(btnBalanceCheck);
				
				btnRefilling = new JButton("Refilling");
				btnRefilling.addActionListener(new ChangeAppListener(WTApp.REFILLING));
				this.add(btnRefilling);
				
				btnBackWAL = new JButton("Back");
				btnBackWAL.addActionListener(new BackListener(WTApp.SELECT_APP));
				this.add(btnBackWAL);
				
			}
			
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class WalletConfigPanel extends JPanel{
			
			public WalletConfigPanel(){
				
				this.add(new JLabel("Issuer Bank"));
				
				boxBanksWalletCfg = new JComboBox();
				this.add(boxBanksWalletCfg);

				this.add(new JLabel("Initial Balance"));
				
				txtInitialBalance = new JTextArea("", 1, 6);
				this.add(txtInitialBalance);
				
				btnBackWalletCfg = new JButton("Back");
				btnBackWalletCfg.addActionListener(new BackListener(WTApp.SELECT_WALLET_APP));
				this.add(btnBackWalletCfg);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BalanceCheckPanel extends JPanel{			
			
			public BalanceCheckPanel(){
				
				lblCurrentBalanceTxt = new JLabel("Current Balance: ");
				this.add(lblCurrentBalanceTxt);
				
				lblCurrentBalance = new JLabel("-");
				this.add(lblCurrentBalance);
				
				btnBackBalanceCheck = new JButton("Back");
				btnBackBalanceCheck.addActionListener(new BackListener(WTApp.SELECT_WALLET_APP));
				this.add(btnBackBalanceCheck);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class RefillingPanel extends JPanel{
			
			
			public RefillingPanel(){
				
				lblCredit = new JLabel("Refilling Amount:");
				add(lblCredit);
				
				txtCredit= new JTextArea("", 1, 6);				
				this.add(txtCredit);
				
				btnBackRefilling = new JButton("Back");
				btnBackRefilling.addActionListener(new BackListener(WTApp.SELECT_WALLET_APP));
				this.add(btnBackRefilling);
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class TicketingAppLauncherPanel extends JPanel{
			
			/**
			 * 
			 */
			public TicketingAppLauncherPanel(){
				
				btnTickConfig = new JButton("Configuration");
				btnTickConfig.addActionListener(new ChangeAppListener(WTApp.CONFIG_TICK));
				add(btnTickConfig);
				
				btnPurchase = new JButton("Purchase Tickets");
				btnPurchase.addActionListener(new ChangeAppListener(WTApp.PURCHASE_TICKET));
				add(btnPurchase);
				
				btnEntrance = new JButton("Event Entrance");
				btnEntrance.addActionListener(new ChangeAppListener(WTApp.EVENT_ENTRANCE));
				add(btnEntrance);
				
				btnBackTAL = new JButton("Back");
				btnBackTAL.addActionListener(new BackListener(WTApp.SELECT_APP));
				add(btnBackTAL);
				
			}			
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class TickConfigPanel extends JPanel{
			
			/**
			 * 
			 */
			public TickConfigPanel(){
				
				cfgOptPanel = new CfgOptPanel();
				this.add(cfgOptPanel);
				
				cfgBtnsPanel = new JPanel();			
				
				btnBackTickCfg = new JButton("Back");
				btnBackTickCfg.addActionListener(new BackListener(WTApp.SELECT_TICK_APP));
				cfgBtnsPanel.add(btnBackTickCfg);
				
				this.add(btnBackTickCfg);
				
				
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class PurchasePanel extends JPanel{
				
			/**
			 * 
			 */
			public PurchasePanel(){
				
				this.setLayout(new GridLayout(2,1));
				
				buyEventPanel = new PurchaseEventPanel();
				this.add(buyEventPanel);
				
				buyTicketPanel = new PurchaseTicketPanel();
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
				
				entEventPanel = new EntEventPanel();
				this.add(entEventPanel);
				
				btnBackEnt = new JButton("Back");
				btnBackEnt.addActionListener(new BackListener(WTApp.SELECT_TICK_APP));
				
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
				
				ta = new JTextArea("", 13, 30);
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
			/*
			public CfgOptPanel(){
				
				this.setLayout(new GridLayout(3,1));
				
				groupCfgOpt = new ButtonGroup();
				
				ConfigOption opt;
				
				opt = ConfigOption.FORMAT_AND_CONFIG;				
				addBtn(opt.toString(), opt, true);
				
				opt = ConfigOption.DELETE;				
				addBtn(opt.toString(), opt, false);
				
				opt = ConfigOption.KEEP_CONFIG;				
				addBtn(opt.toString(), opt, false);
				
				this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Configure Option"));
				
				
			}
			*/
			
			public CfgOptPanel(){
				
				this.setLayout(new GridLayout(2,1));
				
				checkTickDelete = new JCheckBox("Delete Ticketing app if already exists");
				checkTickDelete.addActionListener(new CheckTickDeleteListener());
				this.add(checkTickDelete);
				
				checkTickCreate = new JCheckBox("Create Ticketing app if it doesn't exist");
				checkTickCreate.addActionListener(new CheckTickCreateListener());
				this.add(checkTickCreate);
				
				this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Configure Options"));
				
				
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
		class PurchaseEventPanel extends JPanel{
			
			public PurchaseEventPanel(){
				
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
		class PurchaseTicketPanel extends JPanel{
			
			public PurchaseTicketPanel(){
				
				ticketLabel = new JLabel("No available tickets");
				this.add(ticketLabel);
				
				btnChangeTicket = new JButton("Change");
				btnChangeTicket.setEnabled(false);
				this.add(btnChangeTicket);

				btnBackBuy = new JButton("Back");
				btnBackBuy.addActionListener(new BackListener(WTApp.SELECT_TICK_APP));
				
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
				
				gui.setPanel(WTApp.SELECT_APP);
				selectReader();
				
			}
			
		}
		

		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class CheckFRListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				if(checkFR.isSelected()) msg("Insert card to proceed with the Factory Reset");
				else{
					msg("Check the box to proceed with a Factory Reset and then insert Card");
				}
				
				frAllowed = checkFR.isSelected();
			}
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class ChangeAppListener implements ActionListener{
			
			public ChangeAppListener(WTApp app){
				
				this.app = app;
				
			}
			
			public void actionPerformed(ActionEvent event){
				
				gui.setPanel(app);
				getModel().setApp(app);
				
			}
			
			private WTApp app;
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class BackListener implements ActionListener{
			
			public BackListener(WTApp app){
				
				this.app = app;
				
			}
			
			public void actionPerformed(ActionEvent event){
				
				if(appWorker != null) appWorker.cancel(true);
				
				gui.setPanel(app);
				getModel().setApp(app);
				
			}
			
			private WTApp app;
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class SelectBankListener implements ActionListener{
			
			/**
			 * 
			 * @param c
			 */
			public SelectBankListener(JComboBox c){ this.c = c; }
			
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent event){
				
				getModel().setBank((Bank) c.getSelectedItem());
				
			}
			
			private JComboBox c;
			
		}
	
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class InitialBalanceListener implements KeyListener{
			
			/**
			 * 
			 * @param e
			 */
			public void keyPressed(KeyEvent event){
				
			}
			
			/**
			 * 
			 * @param e
			 */
			public void keyTyped(KeyEvent event){
				
				
			}
			
			/**
			 * 
			 * @param e
			 */
			public void keyReleased(KeyEvent event){
				ibAllowed = false;
				String s = txtInitialBalance.getText();
				
				int i;
				
				try{
					i = Integer.parseInt(s);
				}catch(NumberFormatException e){
					msg("Invalid Initial Balance format. Please insert a number between 0 and 1000");
					return;
				}
				
				if((i < 0) || (i > 1000)) {
					msg("Invalid Initial Balance format. Please insert a number between 0 and 1000");
					return;
				}
				
				msg("Insert card to proceed with configuration");
				getModel().setCredit(i);
				ibAllowed = true;
			}
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class RefillingListener implements KeyListener{
			
			/**
			 * 
			 * @param e
			 */
			public void keyPressed(KeyEvent event){
				
			}
			
			/**
			 * 
			 * @param e
			 */
			public void keyTyped(KeyEvent event){
				
				
			}
			
			/**
			 * 
			 * @param e
			 */
			public void keyReleased(KeyEvent event){
				rfAllowed = false;
				String s = txtCredit.getText();
				
				int i;
				
				try{
					i = Integer.parseInt(s);
				}catch(NumberFormatException e){
					msg("Invalid Refilling amount format. Please insert a number between 0 and 1000");
					return;
				}
				
				if((i < 0) || (i > 1000)) {
					msg("Invalid Refilling amount format. Please insert a number between 0 and 1000");
					return;
				}
				
				msg("Insert card to proceed with refilling");
				getModel().setCredit(i);
				rfAllowed = true;
			}
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class CheckTickDeleteListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
				
				if(checkTickDelete.isSelected()){
					msg("Insert card to proceed with the Ticketing app configuration");
					log("Ticketing app deletion selected\n");
				}
				else{
					if(!checkTickCreate.isSelected())
						msg("Select at least one configuration option");
				}
				
				tdAllowed = checkTickDelete.isSelected();
				
			}
			
			
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class CheckTickCreateListener implements ActionListener{
			
			public void actionPerformed(ActionEvent event){
		
				if(checkTickCreate.isSelected()){
					msg("Insert card to proceed with the Ticketing app configuration");
					log("Ticketing app creation selected\n");
				}
				else{
					if(!checkTickDelete.isSelected())
						msg("Select at least one configuration option");
				}
				
				tcAllowed = checkTickCreate.isSelected();
				
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
			public void actionPerformed(ActionEvent event){
				
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
			public void actionPerformed(ActionEvent event){
				
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
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class AppWorker extends SwingWorker<Boolean, Void>{
			
			public AppWorker(WTApp app){
				this.app = app;
			}
			
			@Override
			public Boolean doInBackground(){
				
				/////////////////////////////////////////////
				
				log(app.toString());
				
				if((app == WTApp.PURCHASE_TICKET) || (app == WTApp.EVENT_ENTRANCE))		
					(new FillCompWorker(app)).execute();
				else (new FillCompWorker(app, false, false)).execute();
				
				while(app == getModel().getCurrApp()){
					getModel().work();
					getModel().getAppState().nextState(getModel());
				}
				
				return true;
			}
			
			private WTApp app;
		
		}
		
		/**
		 * 
		 * @author Francisco Rodríguez Algarra
		 *
		 */
		class FillCompWorker extends SwingWorker<Boolean, Void>{
			
			/**
			 * 
			 * @param app
			 */
			public FillCompWorker(WTApp app){
				
				this(app, true, true);
				
			}
			
			/**
			 * 
			 * @param app
			 * @param list_events
			 * @param list_dates
			 */
			public FillCompWorker(WTApp app, boolean list_events, boolean list_dates){
				
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
						if(app == WTApp.EVENT_ENTRANCE) setEventID();
						setLock(false);
						//System.out.println(getLock());
						return true;
					}
					
				}catch(Exception e){
					System.err.println(e.getMessage());
				}
				
				setLock(false);
				//System.out.println(getLock());
				return true;
			}
			
			/**
			 * 
			 * @param app
			 */
			private void prepareComponents(WTApp app){
				
				disableComponents(app);
				fillComponents(app);
				enableComponents(app);
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void disableComponents(WTApp app){
				
				disableListeners(app);
				
				if(app == WTApp.FACTORY_RESET){
					boxBanksFR.setEnabled(false);
					checkFR.setEnabled(false);
				}
				else if(app == WTApp.CONFIG_WALLET){
					boxBanksWalletCfg.setEnabled(false);
					txtInitialBalance.setEnabled(false);
					getModel().setCredit(0);
				}
				else if(app == WTApp.BALANCE_CHECK){
					setCurrentBalance("-");
				}
				else if(app == WTApp.REFILLING){
					txtCredit.setEnabled(false);
					getModel().setCredit(0);
				}
				else if(app == WTApp.CONFIG_TICK){

					checkTickDelete.setEnabled(false);
					checkTickCreate.setEnabled(false);
					
				}
				else if(app == WTApp.PURCHASE_TICKET){
					
					ticketLabel.setText("No available tickets yet");
					boxBuyEvents.setEnabled(false);
					boxBuyDate.setEnabled(false);
					btnChangeTicket.setEnabled(false);
					
				}
				else if(app == WTApp.EVENT_ENTRANCE){
					
					boxEntEvents.setEnabled(false);
					boxEntDate.setEnabled(false);
					
				}
				
			}
			
			private void disableListeners(WTApp app){
				
				if(app == WTApp.FACTORY_RESET){				
					removeListeners(boxBanksFR);				
				}
				else if(app == WTApp.CONFIG_WALLET){
					removeListeners(boxBanksWalletCfg);
				}
				else if(app == WTApp.BALANCE_CHECK){}
				else if(app == WTApp.REFILLING){}
				else if(app == WTApp.CONFIG_TICK){}
				else if(app == WTApp.PURCHASE_TICKET){					
					removeListeners(boxBuyEvents);
					removeListeners(boxBuyDate);										
				}
				else if(app == WTApp.EVENT_ENTRANCE){					
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
			private void fillComponents(WTApp app){
				
				JComboBox be;
				JComboBox bd;
				
				if(app == WTApp.FACTORY_RESET){
					fillBanks();
					frAllowed = false;
					checkFR.setSelected(false);
				}
				else if(app == WTApp.CONFIG_WALLET){
					fillBanks();
					txtInitialBalance.setText("0");
					msg("Insert card to proceed with configuration");
					getModel().setCredit(0);
					ibAllowed = true;
					
				}
				else if(app == WTApp.BALANCE_CHECK){}
				else if(app == WTApp.REFILLING){
					txtCredit.setText("0");
					msg("Insert card to proceed with refilling");
					getModel().setCredit(0);
					rfAllowed = true;
				}
				else if(app == WTApp.CONFIG_TICK){
					msg("Select at least one configuration option");
					tdAllowed = false;
					tcAllowed = false;
					checkTickDelete.setEnabled(false);
					checkTickDelete.setSelected(false);
					checkTickCreate.setEnabled(false);
					checkTickCreate.setSelected(false);
				}
				if(app == WTApp.PURCHASE_TICKET){
					be = boxBuyEvents;
					bd = boxBuyDate;
				}
				else if(app == WTApp.EVENT_ENTRANCE){
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

				
				if(app == WTApp.PURCHASE_TICKET) getTicket(app);	
				
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void enableComponents(WTApp app){
				
				enableListeners(app);
				
				if(app == WTApp.FACTORY_RESET){					
					setBank();					
					boxBanksFR.setEnabled(true);
					checkFR.setEnabled(true);
				}
				else if(app == WTApp.CONFIG_WALLET){
					setBank();					
					boxBanksWalletCfg.setEnabled(true);
					txtInitialBalance.setEnabled(true);				
				}
				else if(app == WTApp.BALANCE_CHECK){

				}
				else if(app == WTApp.REFILLING){
					txtCredit.setEnabled(true);
				}
				else if(app == WTApp.CONFIG_TICK){
					checkTickDelete.setEnabled(true);
					checkTickCreate.setEnabled(true);
				}
				if(app == WTApp.PURCHASE_TICKET){
					
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
				else if(app == WTApp.EVENT_ENTRANCE){
					
					boxEntEvents.setEnabled(true);
					boxEntDate.setEnabled(true);
					
					msg("Insert card to check ticket");
				}
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void enableListeners(WTApp app){
				
				if(app == WTApp.FACTORY_RESET){					
					boxBanksFR.addActionListener(new SelectBankListener(boxBanksFR));				
				}
				else if(app == WTApp.CONFIG_WALLET){
					boxBanksWalletCfg.addActionListener(new SelectBankListener(boxBanksWalletCfg));
					txtInitialBalance.addKeyListener(new InitialBalanceListener());
				}
				else if(app == WTApp.BALANCE_CHECK){}
				else if(app == WTApp.REFILLING){
					txtCredit.addKeyListener(new RefillingListener());
				}
				else if(app == WTApp.CONFIG_TICK){}
				else if(app == WTApp.PURCHASE_TICKET){
			
					boxBuyEvents.addActionListener(new SelectEventListener());
					boxBuyDate.addActionListener(new SelectDateListener());
					
				}
				else if(app == WTApp.EVENT_ENTRANCE){
					
					boxEntEvents.addActionListener(new SelectEventListener());
					boxEntDate.addActionListener(new SelectDateListener());
					
				}
				
			}
			
			/**
			 * 
			 */
			private void fillBanks(){
				
				banks = getModel().getBanks();
				
				if((banks == null) || (banks.length == 0))
					throw new RuntimeException("No banks found");
				
				JComboBox c;
				
				if(app == WTApp.FACTORY_RESET) c = boxBanksFR;
				else if(app == WTApp.CONFIG_WALLET) c = boxBanksWalletCfg;
				else return;
				
				c.removeAllItems();
				for(int i = 0; i < banks.length; i++) c.addItem(banks[i]);
				
			}
			
			/**
			 * 
			 */
			private void setBank(){
				
				JComboBox c;
				
				if(app == WTApp.FACTORY_RESET) c = boxBanksFR;
				else if(app == WTApp.CONFIG_WALLET) c = boxBanksWalletCfg;
				else return;
				
				getModel().setBank((Bank) c.getSelectedItem());
				
				
			}
			
			/**
			 * 
			 * @param app
			 */
			private void fillEvents(WTApp app){
				
				events = getModel().getEvents();
				
				if((events == null) || (events.length == 0))
					throw new RuntimeException("No events found");
				
				JComboBox c;
				
				if(app == WTApp.PURCHASE_TICKET) c = boxBuyEvents;
				else if(app == WTApp.EVENT_ENTRANCE) c = boxEntEvents;
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
			private void fillDates(WTApp app){
				
				JComboBox be;
				JComboBox bd;
				
				if(app == WTApp.PURCHASE_TICKET){
					be = boxBuyEvents;
					bd = boxBuyDate;
				}
				else if(app == WTApp.EVENT_ENTRANCE){
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
			private void getTicket(WTApp app){
				
				JComboBox be;
				JComboBox bd;
				
				if(app == WTApp.PURCHASE_TICKET){
					be = boxBuyEvents;
					bd = boxBuyDate;
				}
				else if(app == WTApp.EVENT_ENTRANCE){
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
			
			private WTApp app;
			
			private boolean list_events;
			private boolean list_dates;
			
			private Bank[] banks;
			
			private Ticket ticket;
			private Event[] events;
			private String[] dates;
			
			
		}
		
	}
	
}
