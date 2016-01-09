import java.io.IOException;


public class ScreenshotUpdater implements Runnable {

	@Override
	public void run() {
		if (PerkRun.updateScreenshotsOnReset){
			int resetExtraWait=PerkRun.resetExtraWait;
			resetExtraWait*=60000;
			SysInt.sleepTime(resetExtraWait, 1);
			try {
				PerkRun.updateScreenshots();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SysInt.log("Updated screenshots at "+SysInt.getCurrentDateTime());
		}
	}

}
