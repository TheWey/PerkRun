import java.util.ArrayList;


public class ResetVideoCycle implements Runnable{
	public ArrayList<String> codes;
	//basic info
	public String serial;
	public String regName;
	public String devName;
	public String devType;
	public int delayMult;
	
	//general and reset info
	public String PerkAppIcon;
	
	public String trailersBubbleTopLeft;
	public String trailersBubbleBotRight;
	
	public String firstVidTopLeft;
	public String firstVidBotRight;
	
	public String watchTopLeft;
	public String watchBotRight;
	
	public String stillWatchingTopLeft;
	public String stillWatchingBotRight;
	
	//clear app data info
	public String settingsIcon;
	public String settingsScrollUp;
	public String appsMenu;
	public String perkSettingsIcon;
	public String clearData;
	public String dialogOK;
	
	public String loginButtonTopLeft;
	public String loginButtonBotRight;
	
	public String passwordFieldTopLeft;
	public String passwordFieldBotRight;
	
	public String bankedPointsXTopLeft;
	public String bankedPointsXBotRight;
	
	public String screenTapTopLeft;
	public String screenTapBotRight;
	
	public String slideMenuTopLeft;
	public String slideMenuBotRight;
	
	public String slideMenuSwipeUp;
	public String lowBandwidthCheckTopLeft;
	public String lowBandwidthCheckBotRight;
	
	public String appTrailersSidebarTopLeft;
	public String appTrailersSidebarBotRight;
	
	public boolean rebootSafe;
	
	public ResetVideoCycle(String serialIn, ArrayList<String> codesIn){
		serial=serialIn;
		codes=codesIn;
		devName=codes.get(0);
		delayMult=Integer.parseInt(codes.get(1));
		
		PerkAppIcon=codes.get(2);
		
		trailersBubbleTopLeft=codes.get(3);
		trailersBubbleBotRight=codes.get(4);
		
		firstVidTopLeft=codes.get(5);
		firstVidBotRight=codes.get(6);
		
		watchTopLeft=codes.get(7);
		watchBotRight=codes.get(8);
		
		stillWatchingTopLeft=codes.get(9);
		stillWatchingBotRight=codes.get(10);
		
		settingsIcon=codes.get(11);
		settingsScrollUp=codes.get(12);
		appsMenu=codes.get(13);
		perkSettingsIcon=codes.get(14);
		clearData=codes.get(15);
		dialogOK=codes.get(16);
		
		loginButtonTopLeft=codes.get(17);
		loginButtonBotRight=codes.get(18);
		
		passwordFieldTopLeft=codes.get(19);
		passwordFieldBotRight=codes.get(20);
		
		bankedPointsXTopLeft=codes.get(21);
		bankedPointsXBotRight=codes.get(22);
		
		screenTapTopLeft=codes.get(23);
		screenTapBotRight=codes.get(24);
		
		slideMenuTopLeft=codes.get(25);
		slideMenuBotRight=codes.get(26);
		
		slideMenuSwipeUp=codes.get(27);
		lowBandwidthCheckTopLeft=codes.get(28);
		lowBandwidthCheckBotRight=codes.get(29);
		
		appTrailersSidebarTopLeft=codes.get(30);
		appTrailersSidebarBotRight=codes.get(31);
		
		if (codes.get(32).equalsIgnoreCase("true")){
			rebootSafe=true;
		}
		else {
			rebootSafe=false;
		}
	}

	
	public void run() {
		try {
			
			int sleeptime=5000*delayMult;
			int sleepvar=1000;
			
			SysInt.home(serial);
			SysInt.sleepTime(7000, 3000);
			SysInt.tap(serial, PerkAppIcon);
			SysInt.sleepTime(7000, 3000);
			SysInt.tap(serial, stillWatchingTopLeft, stillWatchingBotRight);
			SysInt.sleepTime(sleeptime, sleepvar);
			SysInt.back(serial);
			SysInt.sleepTime(15000*delayMult, 5000);
			SysInt.tap(serial, slideMenuTopLeft, slideMenuBotRight);
			SysInt.sleepTime(sleeptime, sleepvar);
			SysInt.tap(serial, appTrailersSidebarTopLeft, appTrailersSidebarBotRight);
			SysInt.sleepTime(20000, 5000);
			SysInt.tap(serial, firstVidTopLeft, firstVidBotRight);
			SysInt.sleepTime(sleeptime, sleepvar);
			SysInt.tap(serial, watchTopLeft, watchBotRight);
			
		} catch (Exception e) {	e.printStackTrace(); }
	}
}
