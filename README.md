# EAS Addon
An emergency alert system for Weather2-Remastered.
This mod was heavily inspired by @Mrbt0907 and myself. Thanks to him, I was able to create this mod and have it fully functioning.
Please report bugs to the Issues page using the issue template provided by GitHub.

# HOW TO
- Simply place the EAS Transmitter next to an advanced storm sensor and then right click it to enter a watch message, warning message, and set a frequency if desired (or use the preset default frequency).
- Set the EAS Receiver to the same frequency as your transmitter block to receive its EAS transmissions. You can also toggle the EAS sounds on and off via the EAS Receiver's UI.
- The block does not output anything at all by default. You must enter your own messages.
  
# SENSITIVITY
- You can also set the sensitivity (required redstone signal) for each message. Warning: Do not set the sensitivity to the same for both, watches will not work. The warning sensitivity must be a higher value than the watch sensitivity, as warnings take priority over watches.

- The default sensitivity values will display your watch message when the advanced storm sensor detects a supercell and will display a warning message when it detects a ef0 tornado. You can set the sensitivity of the warning message to 5 to detect ef1 tornadoes.

- The block will only alert if the sensitivity value for that particular message is met. It will issue a watch and then a warning, then the block will not output anything else until the detected signal is less than 2. This is to avoid spamming clients on the server. If you want it to alert for stronger storms, set your warning sensitivity higher.

# ADVANCED USES
- You may place more than one EAS Block on the same Frequency, each with their own sensitivity values if you'd like to alert for more severe storms.

For example: eas block #1 is on 162.465, and it uses the default sensitivity settings. It will display the configured message for Supercell and EF0 tornado stages.

Now, you introduce eas block #2, connected to the same advanced storm sensor. It is also set to 162.465 (thus matching eas block #1).
Set its sensitivity to issue a watch at 15 and a warning at 5. This block would not issue a watch at all and would instead issue a warning for ef1 tornadoes.

This allows you to issue a second warning if the storm has intensified.

# CRAFTING RECIPES

## EAS Receiver
![image](https://github.com/ProfessorFartsalot/EAS-Addon/assets/16886014/1e982681-8900-426c-be6a-7d4f8cc524dc)

## EAS Transmitter
![image](https://github.com/ProfessorFartsalot/EAS-Addon/assets/16886014/631ec4db-db50-41b4-957b-7f6781381aec)
