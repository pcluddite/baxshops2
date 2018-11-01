/** +++====+++
 *  
 *  Copyright (c) Timothy Baxendale
 *  Copyright (c) 2012 Nathan Dinsmore and Sam Lazarus
 *
 *  +++====+++
**/

package tbax.baxshops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tbax.baxshops.commands.PrematureAbortException;
import tbax.baxshops.serialization.ItemNames;

/**
 *
 * @author Timothy Baxendale (pcluddite@hotmail.com)
 */
public final class BaxShop implements ConfigurationSerializable
{
    public static final int ITEMS_PER_PAGE = 7;
    
    private long id = -1;
    private String owner;
    private final ArrayList<Location> locations = new ArrayList<>();
    private final ArrayList<BaxEntry> inventory = new ArrayList<>();

    private long flags = BaxShopFlag.NOTIFY | BaxShopFlag.SELL_REQUESTS;

    public BaxShop()
    {
    }
    
    public BaxShop(Map<String, Object> args)
    {
        id = (long)args.get("id");
        owner = (String)args.get("owner");
        flags = (long)args.get("flags");
        inventory.addAll((ArrayList)args.get("inventory"));
        locations.addAll((ArrayList)args.get("locations"));
        if (hasFlagInfinite()) {
            for(BaxEntry entry : inventory) {
                entry.setInfinite(hasFlagInfinite());
            }
        }
    }

    public long getId()
    {
        return id;
    }

    public void setId(long newId)
    {
        id = newId;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String newOwner)
    {
        owner = newOwner;
    }
    
    public int getIndexOfEntry(BaxEntry entry)
    {
        for(int index = 0; index < inventory.size(); index++) {
            if (inventory.get(index).equals(entry)) {
                return index;
            }
        }
        return -1; // not found
    }
    
    public List<Location> getLocations()
    {
        return locations;
    }
    
    private static boolean compareLoc(Location a, Location b)
    {
        return a.getBlockX() == b.getBlockX() &&
               a.getBlockY() == b.getBlockY() &&
               a.getBlockZ() == b.getBlockZ() &&
               a.getWorld().equals(b.getWorld());
    }
    
    public boolean hasLocation(Location loc)
    {
        for(Location l : locations) {
            if (compareLoc(l, loc)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean removeLocation(Location loc)
    {
        for(int i = 0; i < locations.size(); ++i) {
            if (compareLoc(locations.get(i), loc)) {
                locations.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public void addLocation(Location loc)
    {
        locations.add(loc);
    }
    
    private int ceil(double x) {
        return (int) Math.ceil(x);
    }

    /**
     * Gets the number of pages in this shop's inventory.
     *
     * @return the number of pages
     */
    public int getPages()
    {
        return ceil((double) inventory.size() / ITEMS_PER_PAGE);
    }

    /**
     * Gets the number of items in this shop's inventory.
     *
     * @return the number of items
     */
    public int getInventorySize()
    {
        return inventory.size();
    }

    public BaxEntry getEntry(String arg) throws PrematureAbortException
    {
        try {
            return getEntryAt(Integer.parseInt(arg) - 1);
        }
        catch (NumberFormatException e) {
            return ItemNames.getItemFromAlias(arg, this);
        }
        catch (IndexOutOfBoundsException e) {
            throw new PrematureAbortException(e, Resources.NOT_FOUND_SHOPITEM);
        }
    }

    /**
     * Gets the entry at the given index in this shop's inventory.
     *
     * @param index
     * @return the shop entry
     */
    public BaxEntry getEntryAt(int index)
    {
        return inventory.get(index);
    }

    /**
     * Add an item to this shop's inventory.
     * @param entry
     */
    public void addEntry(BaxEntry entry)
    {
        inventory.add(entry);
    }

    /**
     * Checks if this shop's inventory contains an item.
     *
     * @param stack the item to check for
     * @return whether the shop contains the item
     */
    public boolean containsItem(ItemStack stack)
    {
        return findEntry(stack) != null;
    }

    /**
     * Find an entry for an item in this shop's inventory.
     *
     * @param stack the item to find
     * @return the item's entry, or null
     */
    public BaxEntry findEntry(ItemStack stack)
    {
        for (BaxEntry e : inventory) {
            if (e.isItemEqual(stack)) {
                return e;
            }
        }
        return null;
    }
    
    public ItemStack toItem(List<String> sign)
    {
        ItemStack item = new ItemStack(Material.SIGN, 1);
        ArrayList<String> lore = new ArrayList<>();
        for(String line : sign) {
            lore.add(ChatColor.BLUE + line);
        }
        lore.add(ChatColor.GRAY + "ID: " + id);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + owner + "'s shop");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Determines whether an item is a shop
     * @param item
     * @return 
     */
    public static boolean isShop(ItemStack item)
    {
        return item.getType() == Material.SIGN &&
               item.hasItemMeta() &&
               item.getItemMeta().hasLore() &&
               item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).startsWith(ChatColor.GRAY + "ID: ");
    }
    
    /**
     * Converts an item to a BaxShop
     * Note: This should only be used after calling isShop()
     * @param item
     * @return 
     */
    public static BaxShop fromItem(ItemStack item)
    {
        long uid = Long.parseLong(item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 1).substring((ChatColor.GRAY + "ID: ").length()));
        return Main.getState().getShop(uid);
    }
    
    /**
     * Extracts the sign text from the lore of a shop item
     * Note: This should only be used after calling isShop()
     * @param item
     * @return 
     */
    public static String[] extractSignText(ItemStack item)
    {
        List<String> lore = item.getItemMeta().getLore().subList(0, item.getItemMeta().getLore().size() - 1);
        String[] lines = new String[lore.size()];
        for(int i = 0; i < lines.length; ++i) {
            lines[i] = ChatColor.stripColor(lore.get(i));
        }
        return lines;
    }

    public void setFlagBuyRequests(boolean value)
    {
        flags = BaxShopFlag.setFlag(flags, BaxShopFlag.BUY_REQUESTS, value);
    }

    public boolean hasFlagBuyRequests()
    {
        return BaxShopFlag.hasFlag(flags, BaxShopFlag.BUY_REQUESTS);
    }

    public void setFlagSellRequests(boolean value)
    {
        flags = BaxShopFlag.setFlag(flags, BaxShopFlag.SELL_REQUESTS, value);
    }

    public boolean hasFlagSellRequests()
    {
        return BaxShopFlag.hasFlag(flags, BaxShopFlag.SELL_REQUESTS);
    }

    public void setFlagNotify(boolean value)
    {
        flags = BaxShopFlag.setFlag(flags, BaxShopFlag.NOTIFY, value);
    }

    public boolean hasFlagNotify()
    {
        return BaxShopFlag.hasFlag(flags, BaxShopFlag.NOTIFY);
    }

    public void setFlagSellToShop(boolean value)
    {
        flags = BaxShopFlag.setFlag(flags, BaxShopFlag.SELL_TO_SHOP, value);
    }

    public boolean hasFlagSellToShop()
    {
        return BaxShopFlag.hasFlag(flags, BaxShopFlag.SELL_TO_SHOP);
    }

    public void setFlagInfinite(boolean value)
    {
        flags = BaxShopFlag.setFlag(flags, BaxShopFlag.INFINITE, value);
    }

    public boolean hasFlagInfinite()
    {
        return BaxShopFlag.hasFlag(flags, BaxShopFlag.INFINITE);
    }

    @Override
    public Map<String, Object> serialize()
    {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("owner", owner);
        map.put("flag", flags);
        map.put("inventory", inventory);
        map.put("locations", locations);
        return map;
    }
    
    public static BaxShop deserialize(Map<String, Object> args)
    {
        return new BaxShop(args);
    }
    
    public static BaxShop valueOf(Map<String, Object> args)
    {
        return deserialize(args);
    }
}
