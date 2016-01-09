import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;


public class PerkRun {
	
	public static ArrayList<String> vals;
	public static ArrayList<Device> devs=new ArrayList<Device>();
	public static Thread videoLoop=new Thread(new VideoResetLoop());;
	
	public static String perkPassword;
	public static int numDevices;
	public static int autoClearCacheCycles;
	public static boolean waitOnCacheClear;
	public static boolean rebootOnCacheClear;
	public static int resetTime;
	public static int resetExtraWait;
	public static int maxResetVar;
	public static int maxLogLength;
	public static int count=1;
	public static String lastAction="";
	public static boolean resetCyclesOnManualOverride;
	public static boolean deepModeActivated;
	public static boolean updateScreenshotsOnReset;

	public static String versionNumber="2.3.1";
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException{
		initVals();
		SysInt.log("Program started at "+SysInt.getCurrentDateTime());
		Scanner in=new Scanner(System.in);
		System.out.println("\n\nWelcome to PerkRun v"+versionNumber+"\n\n");
		System.out.println("Devices:");
		for (int p=0;p<numDevices;p++){
			System.out.println((p+1)+". "+devs.get(p).regName+" ("+devs.get(p).devName+")");
		}
		System.out.println("\nCommand Format:  [commmand letter] <device number|all>");
		while (true){
			System.out.println("\nCommands:\n[s]tart reset cycle\n[t]erminate reset cycle\n[p]ress Yes and continue\n[c]lear app data\n[r]eboot\n[i]s a cycle running?\n[u]pdate screenshots\n[k]ill Perk app\n[d]ata clear next reset");
			if (deepModeActivated){
				System.out.println("[DEEP] mode\n[q]uit\n");
			}else{
				System.out.println("[q]uit\n");
			}
			System.out.print("Enter command: ");
			String run=in.nextLine();
			String[] inputs=run.split(" ");
			System.out.println();
			
			if (inputs.length==1) {
				run=run+" all";
				inputs=run.split(" ");
			}
			
			if (inputs[0].equalsIgnoreCase("s")) {
				if (videoLoop.isAlive()){
					videoLoop.stop();
					SysInt.log("Video loop terminated at "+SysInt.getCurrentDateTime());
				}
				SysInt.log("New video loop started at "+SysInt.getCurrentDateTime());
				videoLoop=new Thread(new VideoResetLoop());
				videoLoop.start();
			} 
			else if (inputs[0].equalsIgnoreCase("t")) {
				if (videoLoop.isAlive()){
					videoLoop.stop();
					SysInt.log("Video loop terminated at "+SysInt.getCurrentDateTime());
				}
			} 
			else if (inputs[0].equalsIgnoreCase("p")) {
				if (inputs[1].equalsIgnoreCase("all")) {
					if (resetCyclesOnManualOverride){
						if (videoLoop.isAlive()){
							videoLoop.stop();
							SysInt.log("Video loop terminated at "+SysInt.getCurrentDateTime());
							SysInt.log("New video loop started at "+SysInt.getCurrentDateTime());
							videoLoop=new Thread(new VideoResetLoop());
							videoLoop.start();
						}
					}
					resetVideoCycle(-1);
					SysInt.log("All video cycles reset at "+SysInt.getCurrentDateTime());
				}
				else {
					resetVideoCycle(Integer.parseInt(inputs[1]));
				}
			} 
			else if (inputs[0].equalsIgnoreCase("c")) {
				if (inputs[1].equalsIgnoreCase("all")) {
					if (resetCyclesOnManualOverride){
						if (videoLoop.isAlive()){
							videoLoop.stop();
							SysInt.log("Video loop terminated at "+SysInt.getCurrentDateTime());
							SysInt.log("New video loop started at "+SysInt.getCurrentDateTime());
							videoLoop=new Thread(new VideoResetLoop());
							videoLoop.start();
						}
						count=1;
					}
					clearAppData(-1);
					if (waitOnCacheClear){
						System.out.print("Press Enter when all data cleared...");
						in.nextLine();
						waitOnCacheClear=false;
						Thread.sleep(7000);
						waitOnCacheClear=true;
						System.out.print("Press Enter when all set to low bandwidth...");
						in.nextLine();
						waitOnCacheClear=false;
						Thread.sleep(7000);
						waitOnCacheClear=true;
					}
					SysInt.log("All app data cleared at "+SysInt.getCurrentDateTime());
				}
				else {
					clearAppData(Integer.parseInt(inputs[1]));
					if (waitOnCacheClear){
						System.out.print("Press Enter when data cleared...");
						in.nextLine();
						waitOnCacheClear=false;
						Thread.sleep(7000);
						waitOnCacheClear=true;
						System.out.print("Press Enter when set to low bandwidth...");
						in.nextLine();
						waitOnCacheClear=false;
						Thread.sleep(7000);
						waitOnCacheClear=true;
					}
				}
			} 
			
			else if (inputs[0].equalsIgnoreCase("r")) {
				if (inputs[1].equalsIgnoreCase("all")) {
					reboot(-1);
				}
				else {
					reboot(Integer.parseInt(inputs[1]));
				}
			} 
			else if (inputs[0].equalsIgnoreCase("i")){
				if (videoLoop.isAlive()){
					System.out.println("\nA reset cycle is running:\n\n"+lastAction+"\n");
					System.out.println("It is currently "+SysInt.getCurrentDateTime()+"\n");
				}
				else {
					System.out.println("No reset cycle is running.");
				}
			}
			else if (inputs[0].equalsIgnoreCase("u")){
				updateScreenshots();
				SysInt.log("Updated screenshots at "+SysInt.getCurrentDateTime());
				System.out.println("Screenshots updated. Check PerkRun Web Console for more details.");
			}
			else if (inputs[0].equalsIgnoreCase("k")) {
				if (inputs[1].equalsIgnoreCase("all")) {
					killPerk(-1);
				}
				else {
					killPerk(Integer.parseInt(inputs[1]));
				}
			}

			else if (inputs[0].equalsIgnoreCase("d")) {
				count=autoClearCacheCycles;
				SysInt.log("Reset app data clear cycle at "+SysInt.getCurrentDateTime()+". Clearing app data next run.");
				System.out.println("Clearing app data next run.");
				lastAction=lastAction+"\nReset app data clear cycle at "+SysInt.getCurrentDateTime()+". Clearing app data next run.";
				//System.out.println(count);
			}
			else if (inputs[0].equalsIgnoreCase("q")) {
				System.out.print("Are you sure you want to quit? (y/n) ");
				String response=in.nextLine();
				if (response.equalsIgnoreCase("y")){
					System.out.println();
					break;
				}
			} 
			else if (inputs[0].equalsIgnoreCase("DEEP")) {
				DeepMode.deep();
			}
			else System.out.println(inputs[0] + " is not a valid command.");

			Thread.sleep(500);
		}
		in.close();
		SysInt.log("Program quit at "+SysInt.getCurrentDateTime());
		System.out.println("Goodbye.\n");
		videoLoop.stop();
		System.exit(0);
	}
	
	public static void initVals() throws IOException, URISyntaxException{
		SysInt.currentDir=(new File(PerkRun.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())).toString();
		if (SysInt.currentDir.contains("EclipseWorkspace")){
			SysInt.currentDir=SysInt.currentDir.substring(0, SysInt.currentDir.length()-4);
		}
		if (SysInt.currentDir.contains(".jar")){
			int slashIndex=SysInt.currentDir.length()-1;
			while (SysInt.currentDir.charAt(slashIndex)!='/' && !(SysInt.currentDir.charAt(slashIndex)+"").equals("\\")){
				slashIndex--;
			}
			SysInt.currentDir=SysInt.currentDir.substring(0, slashIndex);
		}
		SysInt.adb=SysInt.currentDir+"/adb -s ";
		vals=SysInt.readIn("config.perk");
		perkPassword=vals.get(0);
		numDevices=Integer.parseInt(vals.get(1));
		autoClearCacheCycles=Integer.parseInt(vals.get(2));
		if (autoClearCacheCycles>0 || autoClearCacheCycles==0){
			waitOnCacheClear=false;
		} else {
			if (vals.get(3).equalsIgnoreCase("true")){
				waitOnCacheClear=true;
			}
			else{
				waitOnCacheClear=false;
			}
		}
		if (vals.get(4).equalsIgnoreCase("true")){
			rebootOnCacheClear=true;
		}
		else {
			rebootOnCacheClear=false;
		}
		if (vals.get(5).equalsIgnoreCase("true")){
			resetCyclesOnManualOverride=true;
		}
		else {
			resetCyclesOnManualOverride=false;
		}
		if (vals.get(6).equalsIgnoreCase("true")){
			deepModeActivated=true;
		}
		else {
			deepModeActivated=false;
		}
		if (vals.get(7).equalsIgnoreCase("true")){
			updateScreenshotsOnReset=true;
		}
		else {
			updateScreenshotsOnReset=false;
		}
		resetTime=Integer.parseInt(vals.get(8));
		resetExtraWait=Integer.parseInt(vals.get(9));
		maxResetVar=Integer.parseInt(vals.get(10));
		maxLogLength=Integer.parseInt(vals.get(11));
		for (int k=0;k<numDevices;k++){
			int index=12+3*k;
			String nameIn=vals.get(index);
			String typeIn=vals.get(index+1);
			String serialIn=vals.get(index+2);
			devs.add(new Device(typeIn, serialIn, nameIn));
		}
		SysInt.startADB();
	}
	
	public static void resetVideoCycle(int deviceNum){
		if (deviceNum==-1){
			for (int p=0;p<devs.size();p++){
				devs.get(p).resetVideoCycle();
				SysInt.sleepTime(500, 500);
			}
		}
		else if (deviceNum<numDevices+1){
			devs.get(deviceNum-1).resetVideoCycle();
			SysInt.log("Reset video cycle of "+devs.get(deviceNum-1).regName+" at "+SysInt.getCurrentDateTime());
		} else {
			System.out.println("There is no device number "+deviceNum+" configured.");
		}
		Thread screenshotUpdate=new Thread(new ScreenshotUpdater());
		screenshotUpdate.start();
	}
	
	public static void reboot(int deviceNum) throws IOException{
		if (deviceNum==-1){
			for (int p=0;p<devs.size();p++){
				devs.get(p).reboot();
				SysInt.sleepTime(500, 500);
			}
			SysInt.log("All rebooted at "+SysInt.getCurrentDateTime());
		}
		else if (deviceNum<numDevices+1){
			devs.get(deviceNum-1).reboot();
			SysInt.log("Rebooted "+devs.get(deviceNum-1).regName+" at "+SysInt.getCurrentDateTime());
		} else {
			System.out.println("There is no device number "+deviceNum+" configured.");
		}
	}
	public static void clearAppData(int deviceNum){
		if (deviceNum==-1){
			for (int p=0;p<devs.size();p++){
				devs.get(p).clearAppData();
				SysInt.sleepTime(500, 500);
			}
		}
		else if (deviceNum<numDevices+1){
			devs.get(deviceNum-1).clearAppData();
			SysInt.log("Cleared app data of "+devs.get(deviceNum-1).regName+" at "+SysInt.getCurrentDateTime());
		} else {
			System.out.println("There is no device number "+deviceNum+" configured.");
		}
		Thread screenshotUpdate=new Thread(new ScreenshotUpdater());
		screenshotUpdate.start();
	}
	
	public static void killPerk(int deviceNum){
		if (deviceNum==-1){
			for (int p=0;p<devs.size();p++){
				devs.get(p).killPerk();
				SysInt.sleepTime(500, 500);
			}
		}
		else if (deviceNum<numDevices+1){
			devs.get(deviceNum-1).killPerk();
		} else {
			System.out.println("There is no device number "+deviceNum+" configured.");
		}
	}
	public static void updateScreenshots() throws IOException{
		for (int k=0;k<devs.size();k++){
			devs.get(k).takeScreenshot();
		}
		SysInt.writeWebConsole();
	}
}
