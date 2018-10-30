/** +++====+++
 *  
 *  Copyright (c) Timothy Baxendale
 *
 *  +++====+++
**/

package tbax.baxshops;

import org.bukkit.ChatColor;

/**
 *
 * @author Timothy Baxendale (pcluddite@hotmail.com)
 */
public final class Resources
{
    // TODO: Timed notifications
    /**
     * The block ID for a signpost
     */
    public static final int SIGN = 63;

    /**
     * The distance from the sign in any direction which the player can go
     * before they leave the shop
     */
    public static final int SHOP_RANGE = 4 * 4;
    
    public static final int EXPIRE_TIME_DAYS = 5;
    
    public static final String NOT_FOUND_SELECTED = "You do not have any shop selected!\nYou must select a shop to perform this action!";
    public static final String[] SIGN_CLOSED = {"This shop has", "been closed by", "%s"};
    // Errors
    public static final String NO_ROOM = "Your inventory is full.";
    public static final String NO_PERMISSION = "You do not have permission to use this command.";
    public static final String NOT_FOUND_SHOPITEM = "That item has not been added to this shop.\nUse /shop add to add a new item";
    public static final String INVALID_DECIMAL = "The number entered for the %s is invalid.";
    public static final String NO_MONEY = "You do not have enough money.";
    public static final String ERROR_INLINE = ChatColor.RED + "<ERROR>";
    public static final String NOT_FOUND_HELDITEM = "You need to be holding an item to perform this action.";
    public static final String INVALID_SHOP_ACTION = "'/shop %s' is not a valid action";
    public static final String ERROR_GENERIC = "The action could not be completed due to the following error:\n%s";
    // Info
    public static final String CURRENT_BALANCE = "Your current balance is %s.";
    public static final String SOME_ROOM = "Only " + ChatColor.RED + "%d %s" + ChatColor.RESET + " could fit in your inventory.";
    public static final String CHARGED_MSG = "You were charged %s.";
    public static final String NO_SUPPLIES = "There is not enough of this item in the shop.";
    public static final String NOT_FOUND_ALIAS = "The item alias could not be found.";
    public static final String NOT_FOUND_SIGN = "%s's shop is missing its sign.";
    public static final String NOT_FOUND_NOTE = "You have no notifications for this action.";
    public static final String ITEM_ADDED = "The item(s) have been added to your inventory.";
}
