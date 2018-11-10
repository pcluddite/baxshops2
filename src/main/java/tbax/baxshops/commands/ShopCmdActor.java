/** +++====+++
 *  
 *  Copyright (c) Timothy Baxendale
 *
 *  +++====+++
**/

package tbax.baxshops.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import tbax.baxshops.BaxShop;
import tbax.baxshops.Main;
import tbax.baxshops.ShopSelection;
import tbax.baxshops.serialization.StateFile;

import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author Timothy Baxendale (pcluddite@hotmail.com)
 */
public final class ShopCmdActor
{
    private final CommandSender sender;
    private final Command command;
    private final Main main;
    private Player pl;
    private String name;
    private String action;
    
    private String[] args;
    
    public ShopCmdActor(Main main, CommandSender sender, Command command, String[] args)
    {
        this.main = main;
        this.sender = sender;
        this.command = command;
        this.args = new String[args.length];
        System.arraycopy(args, 0, this.args, 0, args.length);
        this.name = command.getName();
        if (sender instanceof Player) {
            pl = (Player)sender;
        }
    }
    
    public CommandSender getSender()
    {
        return sender;
    }
    
    public Command getCommand()
    {
        return command;
    }
    
    public StateFile getState()
    {
        return Main.getState();
    }
    
    public Player getPlayer()
    {
        return pl;
    }
    
    public Main getMain()
    {
        return main;
    }
    
    public boolean isAdmin()
    {
        return sender.hasPermission("shops.admin");
    }

    public boolean isOwner()
    {
        return getShop() != null
                && pl != null
                && pl.getName().equalsIgnoreCase(getShop().getOwner());
    }

    public boolean hasPermission(String perm)
    {
        if (perm == null)
            return true;
        return sender.hasPermission(perm);
    }
    
    public boolean cmdIs(String... names)
    {
        for(int x = 0; x < names.length; ++x) {
            if (name.equalsIgnoreCase(names[x]))
                return true;
        }
        return false;
    }

    public ShopSelection getSelection()
    {
        return main.selectedShops.get(pl);
    }
    
    public int getNumArgs()
    {
        return args.length;
    }
    
    public String getArg(int index)
    {
        return args[index];
    }

    public int getArgInt(int index) throws PrematureAbortException
    {
        return getArgInt(index, String.format("Expecting argument %d to be a whole number", index));
    }

    public int getArgInt(int index, String errMsg) throws PrematureAbortException
    {
        try {
            return Integer.parseInt(args[index]);
        }
        catch(NumberFormatException e) {
            throw new CommandErrorException(e, errMsg);
        }
    }

    public boolean isArgInt(int index)
    {
        try {
            Integer.parseInt(args[index]);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    public boolean isArgDouble(int index)
    {
        try {
            Double.parseDouble(args[index]);
            return true;
        }
        catch(NumberFormatException e) {
            return false;
        }
    }

    public double getArgRoundedDouble(int index) throws PrematureAbortException
    {
        return Math.round(100d * getArgDouble(index)) / 100d;
    }

    public double getArgRoundedDouble(int index, String errMsg) throws PrematureAbortException
    {
        return Math.round(100d * getArgDouble(index, errMsg)) / 100d;
    }

    public double getArgDouble(int index) throws PrematureAbortException
    {
        return getArgDouble(index, String.format("Expecting argument %d to be a number", index));
    }

    public double getArgDouble(int index, String errMsg) throws  PrematureAbortException
    {
        try {
            return Double.parseDouble(args[index]);
        }
        catch (NumberFormatException e) {
            throw new CommandErrorException(e, errMsg);
        }
    }

    public boolean getArgBoolean(int index) throws PrematureAbortException
    {
        return getArgBoolean(index, String.format("Expecting argument %d to be yes/no", index));
    }

    public boolean getArgBoolean(int index, String errMsg) throws PrematureAbortException
    {
        if ("true".equalsIgnoreCase(args[index]) || "false".equalsIgnoreCase(args[index]))
            return "true".equalsIgnoreCase(args[index]);
        if ("yes".equalsIgnoreCase(args[index]) || "no".equalsIgnoreCase(args[index]))
            return "yes".equalsIgnoreCase(args[index]);
        if ("1".equalsIgnoreCase(args[index]) || "0".equalsIgnoreCase(args[index]))
            return "1".equalsIgnoreCase(args[index]);
        throw new CommandErrorException(errMsg);
    }

    public Logger getLogger()
    {
        return main.getLogger();
    }
    
    public BaxShop getShop()
    {
        if (getSelection() != null)
            return getSelection().getShop();
        return null;
    }
    
    public void setCmdName(String name)
    {
        this.name = name;
    }
    
    public String getCmdName()
    {
        return name;
    }
    
    /**
     * Gets the first argument (if present) in lower case
     * @return 
     */
    public String getAction()
    {
        if (action == null) { // lazy initialization
            action = args.length > 0 ? args[0].toLowerCase() : "";
        }
        return action;
    }
    
    /**
     * Inserts a new first argument in the argument list
     * @param action the new first argument
     */
    public void insertAction(String action)
    {
        String[] newArgs = new String[args.length + 1];
        System.arraycopy(args, 0, newArgs, 1, args.length);
        newArgs[0] = action;
        args = newArgs;
    }
    
    /**
     * Appends an argument to the end of the argument list
     * @param arg 
     */
    public void appendArg(Object arg)
    {
        appendArgs(arg);
    }

    public void appendArgs(Object... newArgs)
    {
        String[] allArgs = new String[args.length + newArgs.length];
        System.arraycopy(args, 0, allArgs, 0, args.length);
        for(int x = 0; x < newArgs.length; ++x) {
            allArgs[x + args.length] = newArgs[x].toString();
        }
        args = allArgs;
    }

    public void exitError(String format, Object... args) throws PrematureAbortException
    {
        throw new CommandErrorException(ChatColor.RED + String.format(format, args));
    }

    public void sendError(String format, Object... args)
    {
        getSender().sendMessage(ChatColor.RED + String.format(format, args));
    }

    public void sendWarning(String format, Object... args)
    {
        getSender().sendMessage(ChatColor.GOLD + String.format(format, args));
    }

    public void exitWarning(String format, Object... args) throws PrematureAbortException
    {
        throw new CommandErrorException(ChatColor.GOLD + String.format(format, args));
    }

    public void sendMessage(String format, Object... args)
    {
        getSender().sendMessage(String.format(format, args));
    }

    public void exitMessage(String format, Object... args) throws PrematureAbortException
    {
        throw new CommandErrorException(String.format(format, args));
    }

    public void logError(String format, Object... args)
    {
        getLogger().severe(String.format(format, args));
    }

    public void logWarning(String format, Object... args)
    {
        getLogger().warning(String.format(format, args));
    }

    public void logMessage(String format, Object... args)
    {
        getLogger().info(String.format(format, args));
    }

    public ItemStack getItemInHand()
    {
        return pl.getInventory().getItemInMainHand();
    }

    public List<ItemStack> takeArgFromInventory(ItemStack item, String arg) throws PrematureAbortException
    {
        List<ItemStack> ret = new ArrayList<>();
        int qty;
        if ("all".equalsIgnoreCase(arg)) {
            ItemStack clone = item.clone();
            qty = takeFromInventory(item, Integer.MAX_VALUE);
            clone.setAmount(qty);
            ret.add(clone);
        }
        else if ("most".equalsIgnoreCase(arg)) {

        }
        else if ("any".equalsIgnoreCase(arg)) {
            
        }
        else {
            int amt;
            try {
                amt = Integer.parseInt(arg);
            }
            catch (NumberFormatException e) {
                throw new CommandErrorException(e, String.format("%s is not a valid quantity", arg));
            }
            ItemStack clone = item.clone();
            qty = takeFromInventory(item, amt);
            clone.setAmount(qty);
            ret.add(clone);
        }
        return ret;
    }

    private List<ItemStack> takeAnyFromInventory()
    {
        // TODO: IMPLEMENT THIS METHOD
        ItemStack curr;
        PlayerInventory inv = pl.getInventory();
        return null;
    }

    public int takeFromInventory(ItemStack item, int amt)
    {
        PlayerInventory inv = pl.getInventory();
        ItemStack hand = getItemInHand();
        int qty = 0;
        if (hand != null && hand.isSimilar(item)) {
            qty += hand.getAmount();
            if (hand.getAmount() < amt) {
                hand.setAmount(hand.getAmount() - amt);
            }
            else {
                inv.setItemInMainHand(null);
            }
        }

        for(int x = 0; x < inv.getSize() && qty < amt; ++x) {
            ItemStack other = inv.getItem(x);
            if (other != null && other.isSimilar(item)) {
                qty += other.getAmount();
                if (other.getAmount() < amt) {
                    other.setAmount(other.getAmount() - amt);
                }
                else {
                    inv.setItem(x, null);
                }
            }
        }

        return qty;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(name);
        for (String s : args) {
            sb.append(" ");
            sb.append(s);
        }
        return sb.toString();
    }

    public void setArg(int index, Object value)
    {
        args[index] = value.toString();
    }
}
