NSAndroid
=========

An experimental Android client for Neuralstar.

Right now, this is intended to use the phone's GPS capability to record position data and submit that data to a Neuralstar object. That object could then be rendered on a map.

Disclaimer
------------------------------

This project is currently in a sloppy state. I still need to complete the following:

 - [ ] Remove the dependency on the lat/long web client API that doesn't exist anymore
 - [ ] Create a separate web service that is completely independent of the web client (this would be copied to the same IIS server where Neuralstar is installed)
 - [ ] Slow down the lat/long polling so it doesn't kill the phone battery
 - [ ] Add a standard configuration screen
 - [ ] Make the lat/long polling interval a configurable item
 - [ ] Add ability to save your settings (don't just stomp on the values when I close and re-launch the app)
 - [ ] Add a "test" button to verify that the settings are good and everything can communicate correctly
 - [ ] Clean up the UI and add Neuralstar logo