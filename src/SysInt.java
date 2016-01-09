import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class SysInt {
	
	public static String currentDir;
	public static String adb;
	
	
	public static void log(String text){
		
			String filename=currentDir+"/log.perk";
			FileWriter fw;
			try {
				ArrayList<String> lines=new ArrayList<String>();
				BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.equals("")){
						
					}
					else {
						lines.add(line);
					}
				}
				br.close();
				boolean append;
				if (lines.size()>PerkRun.maxLogLength-1){
					append=false;
				}
				else append=true;
				
				fw = new FileWriter(filename, append);
				fw.write(text+"\n");
				fw.close();
			} catch (IOException e) { e.printStackTrace(); } 
			 
	}
	public static String getCurrentDateTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	public static int sleepTime(int millis, int maxVar){
		Random rand=new Random();
		int sleep=rand.nextInt(maxVar)+millis;
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sleep;
	}
	public static int waitTime(int resetTime, int resetExtraWait, int maxResetVar){
		Random rand=new Random();
		maxResetVar*=60000;
		resetExtraWait*=60000;
		resetTime*=3600000;
		int sleep=rand.nextInt(maxResetVar)+resetTime+resetExtraWait;
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		String currentTime=dateFormat.format(cal.getTime());
		//System.out.println(sleep);
		cal.add(Calendar.MILLISECOND, sleep);
		String futureTime=dateFormat.format(cal.getTime());
		String nextAction="next reset";
		if (PerkRun.autoClearCacheCycles==PerkRun.count){
			nextAction="clearing app data";
		}
		log("All reset at "+currentTime+", "+nextAction+" at "+futureTime);
		PerkRun.lastAction="All reset at "+currentTime+", "+nextAction+" at "+futureTime;
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sleep;
	}
	private static int[] parseCoords(String coords){
		String[] strings=coords.split(" ");
		int[] coordarr=new int[strings.length];
		for (int k=0;k<strings.length;k++){
			coordarr[k]=Integer.parseInt(strings[k]);
		}
		return coordarr;
	}
	private static String randomizeLoc(String coordTL, String coordBR){
		int[] topleft=parseCoords(coordTL);
		int[] botright=parseCoords(coordBR);
		int xmin=topleft[0];
		int ymin=topleft[1];
		int xmax=botright[0];
		int ymax=botright[1];
		Random rand=new Random();
		int randx=rand.nextInt(xmax-xmin)+xmin;
		int randy=rand.nextInt(ymax-ymin)+ymin;
		return randx+" "+randy;
	}
	public static void tap(String serial, String coordTL, String coordBR) throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" shell input tap ";
		String coords=randomizeLoc(coordTL, coordBR);
		String run=adb+serial+command+coords;
		r.exec(run);
	}
	public static void tap(String serial, String coords) throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" shell input tap ";
		String run=adb+serial+command+coords;
		r.exec(run);
	}
	public static void swipe(String serial, String swipeCoords) throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" shell input swipe ";
		String run=adb+serial+command+swipeCoords;
		r.exec(run);
	}
	private static void keyevent(String serial, String eventCode) throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" shell input keyevent ";
		String run=adb+serial+command+eventCode;
		r.exec(run);
	}
	public static void home(String serial) throws IOException{	keyevent(serial, "3");	}
	public static void back(String serial) throws IOException{	keyevent(serial, "4");	}
	public static void enter(String serial) throws IOException{	keyevent(serial, "66");	}
	public static void reboot(String serial) throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" reboot";
		String run=adb+serial+command;
		r.exec(run);
	}
	public static void killPackage(String serial, String packageName) throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" shell am force-stop ";
		String run=adb+serial+command+packageName;
		r.exec(run);
	}
	public static void text(String serial, String text) throws IOException, InterruptedException{
		Runtime r = Runtime.getRuntime();
		String command=" shell input text ";
		text=text.replace(" ", "%s");
		String run=adb+serial+command+text;
		r.exec(run);
	}
	public static void startADB() throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" devices";
		String run=currentDir+"/adb"+command;
		r.exec(run);
	}
	public static void takeScreenshot(String serial, String regName) throws IOException{
		ProcessBuilder builder = new ProcessBuilder("sh", "captureScreen.sh", serial);
		builder.redirectOutput(new File(currentDir+"/screenshots/"+regName+"screen.png"));
		builder.start();
	}
	public static void writeWebConsole() throws IOException{
		String htmlLoc=currentDir+"/WebConsole.html";
		FileWriter fw=new FileWriter(htmlLoc, false);
		String time=getCurrentDateTime();
		fw.write("<html><head><style>\n.rotate270 {\n-webkit-transform: rotate(270deg);\n-moz-transform: rotate(270deg);\n-o-transform: rotate(270deg);\n-ms-transform: rotate(270deg);\ntransform: rotate(270deg);\n}\n</style><title>PerkRun Web Console</title></head><body><p><br>\n<h2>PerkRun Web Console</h2><br>\n<h3><u>Last Updated</u>: "+time+"</h3><br>\n<h4><a href=\"log.perk\" target=\"_blank\">Log</a></h4><br><br><br><center>\n");
		for (int p=0;p<PerkRun.devs.size();p++){
			String name=PerkRun.devs.get(p).regName;
			String type=PerkRun.devs.get(p).devName;
			fw.write("<b>"+(p+1)+". "+name+" ("+type+")</b><br><br>\n<img src=\"screenshots/"+name+"screen.png\" class=\"rotate270\" /><br><br>\n\n");
		}
		fw.write("</center></p></body></html>");
		fw.close();
	}
	public static String getRunningPackages(String serial) throws IOException{
		Runtime r = Runtime.getRuntime();
		String command=" shell ps | grep u0";
		String run=adb+serial+command;
		Process exec=r.exec(run);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
		String output="";
		String s = null;
		while ((s = stdInput.readLine()) != null) {
		    output=output+"\n"+s;
		}
		return output;
	}
	public static ArrayList<String> readIn(String fileLoc) throws IOException{
		fileLoc=currentDir+"/"+fileLoc;
		ArrayList<String> lines=new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(fileLoc)));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.equals("") || line.startsWith("//")){
				
			}
			else {
				line=line.split("=")[1];
				lines.add(line);
			}
		}
		br.close();
		
		return lines;
	}
}
