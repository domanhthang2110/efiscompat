Epic Fight x Iron's Spells Animation Fix

If you updated to 2.1.8 from an older version please delete the mod config file (efiscompat.toml) so it can regenerate the correct config

This mod fixes the issue where Iron's Spells casting animations don't play when using Epic Fight. It adds custom spellcasting animations through Epic Fight's system, with special variants for staves. It also act as a compatibility mod that fix some problems with EF and ISS when used together

Features:
- Multiple casting animations, with unique staff variants when holding staff.
- Configurable options for defining staff items and hiding held items during animations.
- Support spell cancelling when performing various Epic Fight's skill like guarding, rolling,...
- Fully configurabled for almost all of the features
  
Turn off the "Filter Animation" setting in Epic Fight for the best experience
Also turn off both Iron's Spell client config as shown below

irons_spellbooks-client.toml
```
[Animations]
    #What to render in first person while casting.
    showFirstPersonArms = false
    showFirstPersonItems = false
```
