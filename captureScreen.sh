#!/bin/sh

/PerkRun/adb -s $1 shell screencap -p | /usr/bin/perl -pe 's/\x0D\x0A/\x0A/g'
