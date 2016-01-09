
public class VideoResetLoop implements Runnable{
	
	public void run() {
		while (true){
			SysInt.waitTime(PerkRun.resetTime, PerkRun.resetExtraWait, PerkRun.maxResetVar);
			if (PerkRun.autoClearCacheCycles==PerkRun.count){
				PerkRun.count=1;
				PerkRun.clearAppData(-1);
				SysInt.log("All app data cleared at "+SysInt.getCurrentDateTime());
			}
			else {
				PerkRun.resetVideoCycle(-1);
				if (PerkRun.autoClearCacheCycles>0 || PerkRun.autoClearCacheCycles==0) PerkRun.count++;
			}
		}
	}

}
