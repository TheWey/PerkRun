import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DeepMode {


	public static boolean updateScreenshots;
	public static boolean firstTime=true;
	public static String[] tapCommands={"PerkAppIcon" ,"trailersBubble" ,"firstVid" ,"watch" ,"stillWatching" ,"settingsIcon" ,"appsMenu" ,"perkSettingsIcon" ,"clearData" ,"dialogOK" ,"loginButton" ,"passwordField" ,"bankedPointsX" ,"screenTap" ,"slideMenu" ,"lowBandwidthCheck" ,"appTrailersSidebar"};
	public static String[] swipeCommands={"settingsScrollUp", "slideMenuSwipeUp"};
	public static Scanner in=new Scanner(System.in);

	public static void deep() throws NumberFormatException, IOException, InterruptedException{
		initDeep();
		
		boolean inDeep=true;
		while(inDeep){
			
			System.out.println("\nUpdating screenshots: "+updateScreenshots+"\n");
			System.out.println("Commands:\n\n1. tap\n2. swipe\n3. tapLocation\n4. swipeLocation\n5. back\n6. enter\n7. home\n8. sendText\n9. killPackage\n10. updateScreenshotsPref\n11. exitDeep\n");
			
			System.out.print("Enter DEEP MODE command: ");
			String run=in.nextLine();
			
			String[] inputs=run.split(" ");
			System.out.println();

			while (inputs.length==1 && !run.equalsIgnoreCase("exitDeep") && !run.equalsIgnoreCase("updateScreenshotsPref") && !run.equalsIgnoreCase("10") && !run.equalsIgnoreCase("11")) {
				System.out.print("Enter device number: ");
				String devNum=in.nextLine();
				run=run+" "+devNum;
				inputs=run.split(" ");
				System.out.println();
			}

			
			if (inputs[0].equalsIgnoreCase("tap") || inputs[0].equalsIgnoreCase("1")) {
				for (int n=0;n<tapCommands.length;n++){
					System.out.println((n+1)+". "+tapCommands[n]);
				}
				System.out.println();
				boolean gettingLocation=true;
				ArrayList<String> locations = new ArrayList<String>();
				while (gettingLocation){
					System.out.print("Enter tap location (name code or number): ");
					String commandInput=in.nextLine();
					System.out.println();
					locations=interpretTaps(commandInput, Integer.parseInt(inputs[1])-1);
					if (!locations.get(0).equals("-1")){
						gettingLocation=false;
					}
				}
				if (locations.size()==1){
					SysInt.tap(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial, locations.get(0));
				}
				else if (locations.size()==2){
					SysInt.tap(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial, locations.get(0), locations.get(1));
				}
			} 
			
			else if (inputs[0].equalsIgnoreCase("swipe") || inputs[0].equalsIgnoreCase("2")) {
				for (int n=0;n<swipeCommands.length;n++){
					System.out.println((n+1)+". "+swipeCommands[n]);
				}
				System.out.println();
				boolean gettingLocation=true;
				String locations="";
				while (gettingLocation){
					System.out.print("Enter swipe location (name code or number): ");
					String commandInput=in.nextLine();
					System.out.println();
					locations=interpretSwipes(commandInput, Integer.parseInt(inputs[1])-1);
					if (!locations.equals("-1")){
						gettingLocation=false;
					}
				}
				SysInt.swipe(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial, locations);

			} 
			
			else if (inputs[0].equalsIgnoreCase("tapLocation") || inputs[0].equalsIgnoreCase("3")) {
				boolean gettingLocation=true;
				String location="";
				while (gettingLocation){
					System.out.print("Enter tap coordinates (x y): ");
					location=in.nextLine();
					System.out.println();
					String[] splitLocation=location.split(" ");
					if (splitLocation.length==2){
						gettingLocation=false;
					}
				}
				SysInt.tap(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial, location);
			} 
			
			else if (inputs[0].equalsIgnoreCase("swipeLocation") || inputs[0].equalsIgnoreCase("4")) {
				boolean gettingLocation=true;
				String location="";
				while (gettingLocation){
					System.out.print("Enter swipe coordinates (x1 y1 x2 y2): ");
					location=in.nextLine();
					System.out.println();
					String[] splitLocation=location.split(" ");
					if (splitLocation.length==4){
						gettingLocation=false;
					}
				}
				SysInt.swipe(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial, location);
			}
			else if (inputs[0].equalsIgnoreCase("back") || inputs[0].equalsIgnoreCase("5")) {
				SysInt.back(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial);
			}
			else if (inputs[0].equalsIgnoreCase("enter") || inputs[0].equalsIgnoreCase("6")) {
				SysInt.enter(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial);
			}
			else if (inputs[0].equalsIgnoreCase("home") || inputs[0].equalsIgnoreCase("7")) {
				SysInt.home(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial);
			}
			else if (inputs[0].equalsIgnoreCase("sendText") || inputs[0].equalsIgnoreCase("8")) {
				System.out.print("Enter text string to send to device: ");
				String text=in.nextLine();
				SysInt.text(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial, text);
			}
			else if (inputs[0].equalsIgnoreCase("killPackage") || inputs[0].equalsIgnoreCase("9")) {
				System.out.println("Running packages: ");
				String runningPackages=SysInt.getRunningPackages(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial);
				System.out.println(runningPackages);
				boolean gettingPackage=true;
				String packageName="";
				while (gettingPackage){
					System.out.print("Enter package to kill: ");
					packageName=in.nextLine();
					if (runningPackages.contains(packageName)){
						gettingPackage=false;
					}
					else {
						System.out.println("Package not found running. Please enter a running package to kill.");
					}
				}
				SysInt.killPackage(PerkRun.devs.get(Integer.parseInt(inputs[1])-1).serial, packageName);
			}
			else if (inputs[0].equalsIgnoreCase("updateScreenshotsPref") || inputs[0].equalsIgnoreCase("10")) {
				screenshotPrefs();
			} 

			else if (inputs[0].equalsIgnoreCase("exitDeep") || inputs[0].equalsIgnoreCase("11")) {
				inDeep=false;
			} 
			else System.out.println(inputs[0]+" is not a valid command.");


			if (updateScreenshots && inDeep){
				Thread.sleep(3000);
				PerkRun.updateScreenshots();
			}
			
		}
	}
	public static void screenshotPrefs(){
		boolean responded=false;
		while (!responded){
			System.out.print("Would you like to update the Web Console screenshots after each action? (y/n) ");
			String response=in.nextLine();
			System.out.println();
			if (response.equalsIgnoreCase("y")){
				updateScreenshots=true;
				responded=true;
			} else if (response.equalsIgnoreCase("n")){
				updateScreenshots=false;
				responded=true;
			} else {
				System.out.println("Please enter a valid response.\n");
			}
		}
	}
	public static void initDeep(){
		System.out.println("\n\n\n\nWelcome to...DEEP MODE\n\nWARNING: NOTHING IN DEEP MODE IS LOGGED\n\n");
		if (firstTime){
			screenshotPrefs();
		}
		firstTime=false;
		
		System.out.println("\nCommand Format:  [commmand] <device number>\n");
		
	}
	public static String interpretSwipes(String swipeCommand, int devNumber){
		String pos;
		if (swipeCommand.equalsIgnoreCase("settingsScrollUp") || swipeCommand.equalsIgnoreCase("1")) {
			pos=PerkRun.devs.get(devNumber).settingsScrollUp;
		}
		else if (swipeCommand.equalsIgnoreCase("slideMenuSwipeUp") || swipeCommand.equalsIgnoreCase("2")) {
			pos=PerkRun.devs.get(devNumber).slideMenuSwipeUp;
		}
		else pos="-1";
		return pos;
	}
	public static ArrayList<String> interpretTaps(String tapCommand, int devNumber){
		ArrayList<String> pos=new ArrayList<String>(); 

		if (tapCommand.equalsIgnoreCase("PerkAppIcon") || tapCommand.equalsIgnoreCase("1")) {
			pos.add(PerkRun.devs.get(devNumber).PerkAppIcon);
		}
		else if (tapCommand.equalsIgnoreCase("trailersBubble") || tapCommand.equalsIgnoreCase("2")) {
			pos.add(PerkRun.devs.get(devNumber).trailersBubbleTopLeft);
			pos.add(PerkRun.devs.get(devNumber).trailersBubbleBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("firstVid") || tapCommand.equalsIgnoreCase("3")) {
			pos.add(PerkRun.devs.get(devNumber).firstVidTopLeft);
			pos.add(PerkRun.devs.get(devNumber).firstVidBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("watch") || tapCommand.equalsIgnoreCase("4")) {
			pos.add(PerkRun.devs.get(devNumber).watchTopLeft);
			pos.add(PerkRun.devs.get(devNumber).watchBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("stillWatching") || tapCommand.equalsIgnoreCase("5")) {
			pos.add(PerkRun.devs.get(devNumber).stillWatchingTopLeft);
			pos.add(PerkRun.devs.get(devNumber).stillWatchingBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("settingsIcon") || tapCommand.equalsIgnoreCase("6")) {
			pos.add(PerkRun.devs.get(devNumber).settingsIcon);
		}
		else if (tapCommand.equalsIgnoreCase("appsMenu") || tapCommand.equalsIgnoreCase("7")) {
			pos.add(PerkRun.devs.get(devNumber).appsMenu);
		}
		else if (tapCommand.equalsIgnoreCase("perkSettingsIcon") || tapCommand.equalsIgnoreCase("8")) {
			pos.add(PerkRun.devs.get(devNumber).perkSettingsIcon);
		}
		else if (tapCommand.equalsIgnoreCase("clearData") || tapCommand.equalsIgnoreCase("9")) {
			pos.add(PerkRun.devs.get(devNumber).clearData);
		}
		else if (tapCommand.equalsIgnoreCase("dialogOK") || tapCommand.equalsIgnoreCase("10")) {
			pos.add(PerkRun.devs.get(devNumber).dialogOK);
		}
		else if (tapCommand.equalsIgnoreCase("loginButton") || tapCommand.equalsIgnoreCase("11")) {
			pos.add(PerkRun.devs.get(devNumber).loginButtonTopLeft);
			pos.add(PerkRun.devs.get(devNumber).loginButtonBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("passwordField") || tapCommand.equalsIgnoreCase("12")) {
			pos.add(PerkRun.devs.get(devNumber).passwordFieldTopLeft);
			pos.add(PerkRun.devs.get(devNumber).passwordFieldBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("bankedPointsX") || tapCommand.equalsIgnoreCase("13")) {
			pos.add(PerkRun.devs.get(devNumber).bankedPointsXTopLeft);
			pos.add(PerkRun.devs.get(devNumber).bankedPointsXBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("screenTap") || tapCommand.equalsIgnoreCase("14")) {
			pos.add(PerkRun.devs.get(devNumber).screenTapTopLeft);
			pos.add(PerkRun.devs.get(devNumber).screenTapBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("slideMenu") || tapCommand.equalsIgnoreCase("15")) {
			pos.add(PerkRun.devs.get(devNumber).slideMenuTopLeft);
			pos.add(PerkRun.devs.get(devNumber).slideMenuBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("lowBandwidthCheck") || tapCommand.equalsIgnoreCase("16")) {
			pos.add(PerkRun.devs.get(devNumber).lowBandwidthCheckTopLeft);
			pos.add(PerkRun.devs.get(devNumber).lowBandwidthCheckBotRight);
		}
		else if (tapCommand.equalsIgnoreCase("appTrailersSidebar") || tapCommand.equalsIgnoreCase("17")) {
			pos.add(PerkRun.devs.get(devNumber).appTrailersSidebarTopLeft);
			pos.add(PerkRun.devs.get(devNumber).appTrailersSidebarBotRight);
		}
		else pos.add("-1");

		return pos;
	}
}
