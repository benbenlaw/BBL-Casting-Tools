Casting Modifiers

Changes from Casting 1.21.1

- Now called Modifier instead of Equipment Modifier
- Old system completely removed
- Modifier are now their own classes and extend a base Modifier class
- This allows for much better creation of custom modifiers for other mods as well as helping various modifiers work together
- This system also allow for better compatibility by setting other modifiers as incompatible with each other if needed, example of this is Silk Touch and Fortune modifiers are incompatible with each other as they are in vanilla Minecraft
- All Equipment Modifier Items are removed
- JEI now shows the modifier effect and recipes inside a single category
- Searching for tools like pickaxes will now show the modifiers category and only show modifiers that are compatible with the item you searched
- All of that is also driven my the main Modifier class
- Basically adding modifier just requires a few steps now instead of bascically re making the systme for each modifier
- Tooltips significantly improved
- Leveling up to get additional modifier is now removed
- All modifiers application requires Experience now, as defined in the Modifier Class
- Modifiers can still use either a fluid, a sized ingredient or both this too is defined in the Modifier Class
- Modifiers can also be customized in the config, at the moment the required fluids/ items are hard coded but can be changed in the future if required
- Valid items for modifiers are now a list of Tags, as defined in the modifier class
- Valid items can also be a List of items as well, for example ignite can be applied to sticks and swords again defined in the modifier class
- Modifiers no longer work in ticks, its more like a crafting table now where you can see the effect added to the item before taking the item. When taking the item all required fluids and items are consumed.
- Due to that change outputs can no longer be automated out of the Modifier 
