# PerkRun

This utility runs PerkTV on Android fully autonomously.

## Features
- Automatic restarting of the video cycle after a preset interval
- Handling the clearing of the app cache after a preset interval, including a subsequent procedure of rebooting all devices, logging back in, reactivating "low bandwidth mode," and restarting the video cycle
- Maintaining a web console interface with device screenshots and a detailed event log
- Randomizing all timing and input coordinates to prevent detection of the use of automated input software

## adb
This program handles all communication with its connected devices through adb, and the used version is included in the repository for convenience. Feel free to replace this version in the running folder.

## config.perk
This file stores all basic configuration details, in self-descriptive variables. Lines starting with a "//" are ignored as comments. For all device configuration details, the following scheme applies:
- **name:** An arbitrary and unique name for each connected device. This is used for the web console and terminal application, and helps with device identification.
- **type:** The type of the device, which must match the pre-extension name of a .pro file (explained below).
- **serial:** The device's serial number, which is used to communicate input over adb.

## X.pro
Each .pro file represents an input profile, with a filename that indicates which device it represents. For example, if a profile file was named "X.pro", the corresponding device type in the config.perk file would simply be "X". The **name** variable represents the name of this device type, and the **delayMult** variable feature was never implemented, so it should be left as 1. 

Otherwise, all other variables represent tap locations in an "X Y" integer format. Those few variables which represent swipe or scroll locations (those with 4 numbers instead of 2) follow the format "StartX StartY EndX EndY". The **rebootSafe** boolean variable at the bottom of each profile represents whether the type of device can be rebooted without requiring any input sequences to return to the home screen after rebooting.

Several tap location variables have a "TopLeft" and a "BottomRight" component, and these are used to randomize the input location to prevent detection of the use of this automation program. They respectively represent the X-Y coordinates of the top left and the bottom right of the location that can be used for input, and the tap location will be randomized within that range on each input instance.

To build your own profiles for other devices besides the included ones, use the "Pointer location" option in Android's Developer Options to record the locations for each variable.

## runperk
This shell script is used to start the program, assuming that the code has been compiled into a runnable .jar program. The software can be run as desired from the Eclipse file structure. However, if you do wish to export the program and run it from a directory, move the following files to that directory, and ensure the the **runperk** file has the correct path to the exported .jar to start it:
- **PerkRun-vXX.jar**
- All **.pro** files
- One **config.perk** file
- **WebConsole.html**
- **adb**
- **captureScreen.sh**
- **log.perk**
- **runperk**

## Note
This project is now deprecated as PerkTV rates have dropped below a sustainable and worthwhile level, but was published for future research purposes. At its peak, this system would earn approximately $8.00 per day with 5 devices and without any intervention.
