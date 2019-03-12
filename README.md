# ProximityContent (Beacon Finder)
[![Build Status](https://travis-ci.com/sierleunam/ProximityContent.svg?branch=master)](https://travis-ci.com/sierleunam/ProximityContent)

## Overview
Helper application that can perform various operations and is controlled through specific files created or deleted in the android Downloads folder.

### Operating mode
when called it wil start a foreground service, and can be stopped from the notifications panel.

#### Beacons detection
To start the scanning process you need to create a file called: 'start.scan'.
To stop the the scanning you delete the file 'start.scan'

While the scanning is in progress, a file named 'beacon.json' e created/updated with the nearest beacon detected.

The data is presented in json format.
##### example:
	{
		"timestamp":"201903124015",
		"name":"verde",
		"uuid":"<uuid>",
		"macaddress":"[<MAC Address>]",
		"rssi":-92,
		"distance":17.00011174830764
	}
Also, another file called 'beacons.json' is create with a JSON array containing all the beacons in range.
##### example:
	{ 
		"list" :[
				{
					"timestamp":"201903124015",
					"name":"verde",
					"uuid":"<uuid>",
					"macaddress":"[<MAC Address>]",
					"rssi":-92,
					"distance":17.00011174830764
				},
			 ...
			 ]
	}


#### Audio playback
To start the audio playback you need to create/modify a file called 'audio.play' with the audio filename in its contens.
To stop just delete the file.
