/* 
 * The MIT License
 *
 * Copyright © 2013-2015 Timothy Baxendale (pcluddite@hotmail.com) and 
 * Copyright © 2012 Nathan Dinsmore and Sam Lazarus.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tbax.baxshops.executer;

import tbax.baxshops.BaxEntry;
import tbax.baxshops.BaxShop;
import tbax.baxshops.Format;
import tbax.baxshops.help.Help;
import tbax.baxshops.Main;
import tbax.baxshops.serialization.Clipboard;

/**
 *
 * @author Timothy Baxendale (pcluddite@hotmail.com)
 */
public final class FlagCmdExecuter
{
    
    public static boolean execute(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasArgs(2, Help.FLAG);
        
        if (!req.isValid()) return true;
        
        if (!cmd.getAction().equals("flag")) {
            Main.sendError(cmd.getSender(), Help.FLAG.toUsageString());
            return true;
        }
        
        switch(cmd.getArg(1).toLowerCase()) {
            case "selltoshop":
            case "sell_to_shop":
                return sellToShop(cmd);
            case "infinite":
            case "isinfinite":
                return infinite(cmd);
            case "sellrequest":
            case "sellrequests":
            case "sell_request":
            case "sell_requests":
                return sellRequests(cmd);
            case "buyrequest":
            case "buyrequests":
            case "buy_request":
            case "buy_requests":
                return buyRequests(cmd);
            case "owner":
                return owner(cmd);
            case "list":
                return list(cmd);
            case "notify":
            case "notifications":
            case "notes":
                return shopNotify(cmd);
            default:
                Main.sendError(cmd.getPlayer(), "Invalid shop flag '" + cmd.getArg(1) + "'");
                return false;
        }
    }
    
    public static boolean sellToShop(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasSelection();
        req.hasOwnership();
        req.hasArgs(3, Help.FLAG);
        
        if (!req.isValid()) {
            return true;
        }
        
        BaxShop shop = cmd.getShop();
        
        if (!Clipboard.isBoolean(cmd.getArg(2))) {
            Main.sendError(cmd.getSender(), "Usage:\n/shop flag sell_to_shop [true|false]");
            return true;
        }
        shop.sellToShop = Clipboard.parseBoolean(cmd.getArg(2));
        cmd.getPlayer().sendMessage(Format.flag("Sell to Shop") + " is " + Format.keyword(shop.sellToShop ? "enabled" : "disabled"));
        return true;
    }
    
    public static boolean shopNotify(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasSelection();
        req.hasOwnership();
        req.hasArgs(3, Help.FLAG);
        
        if (!req.isValid())
            return true;
        
        BaxShop shop = cmd.getShop();
        
        if (!Clipboard.isBoolean(cmd.getArg(2))) {
            Main.sendError(cmd.getSender(), "Usage:\n/shop flag notify [true|false]");
            return true;
        }
        shop.notify = Clipboard.parseBoolean(cmd.getArg(2));
        cmd.getPlayer().sendMessage(Format.flag("Notifications ") + " for this shop are " + Format.keyword(shop.sellToShop ? "enabled" : "disabled"));
        return true;
    }
    
    public static boolean infinite(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasSelection();
        req.hasOwnership();
        req.hasArgs(3, Help.FLAG);
        
        if (!req.isValid())
            return true;
       
        BaxShop shop = cmd.getShop();
        
        if (!Clipboard.isBoolean(cmd.getArg(2))) {
            Main.sendError(cmd.getSender(), "Usage:\n/shop flag infinite [true|false]");
            return true;
        }
        shop.infinite = Clipboard.parseBoolean(cmd.getArg(2));
        for(BaxEntry e : shop.inventory) {
            if (e.infinite = shop.infinite) { // this is on purpose. It's not an error.
                e.setAmount(1);
            }
        }
        cmd.getPlayer().sendMessage(Format.flag("Infinite items") + " for this shop are " + Format.keyword(shop.infinite ? "enabled" : "disabled"));
        return true;
    }
    
    public static boolean sellRequests(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasSelection();
        req.hasOwnership();
        req.hasArgs(3, Help.FLAG);
        
        if (!req.isValid())
            return true;
        
        BaxShop shop = cmd.getShop();
        
        if (!Clipboard.isBoolean(cmd.getArg(2))) {
            Main.sendError(cmd.getSender(), "Usage:\n/shop flag SELL_REQUESTS [true|false]");
            return true;
        }
        shop.sellRequests = Clipboard.parseBoolean(cmd.getArg(2));
        cmd.getPlayer().sendMessage(Format.flag("Sell requests") + " for this shop are " + Format.keyword(shop.sellRequests ? "enabled" : "disabled"));
        return true;
    }
    
    public static boolean buyRequests(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasSelection();
        req.hasOwnership();
        req.hasArgs(3, Help.FLAG);
        
        if (!req.isValid())
            return true;
        
        BaxShop shop = cmd.getShop();
        
        if (!Clipboard.isBoolean(cmd.getArg(2))) {
            Main.sendError(cmd.getSender(), "Usage:\n/shop flag BUY_REQUESTS [true|false]");
            return true;
        }
        shop.buyRequests = Clipboard.parseBoolean(cmd.getArg(2));
        cmd.getPlayer().sendMessage(Format.flag("Buy requests") + " for this shop are " + Format.keyword(shop.buyRequests ? "enabled" : "disabled"));
        return true;
    }
    
    public static boolean owner(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasSelection();
        req.hasOwnership();
        req.hasArgs(3, Help.FLAG);
        
        if (!req.isValid())
            return true;
        
        
        BaxShop shop = cmd.getShop();
        
        shop.owner = cmd.getArg(2);
        cmd.getPlayer().sendMessage(Format.username(shop.owner) + " is now the owner!" + 
                (cmd.getSelection().isOwner ? "\nYou will still be able to edit this shop until you leave or reselect it." : ""));
        return true;
    }
    
    public static boolean list(ShopCmd cmd)
    {
        CmdRequisite req = cmd.getRequirements();
        
        req.hasSelection();
        req.hasOwnership();
        
        if (!req.isValid())
            return true;
        
        BaxShop shop = cmd.getShop();
        
        cmd.getPlayer().sendMessage("\nFlags currently aplied to this shop:");
        cmd.getPlayer().sendMessage(String.format("%s: %s", Format.flag("INFINITE"), Format.keyword(shop.infinite ? "On" : "Off")));
        cmd.getPlayer().sendMessage(String.format("%s: %s", Format.flag("NOTIFY"), Format.keyword(shop.notify ? "On" : "Off")));
        cmd.getPlayer().sendMessage(String.format("%s: %s", Format.flag("SELL_TO_SHOP"), Format.keyword(shop.sellToShop ? "On" : "Off")));
        cmd.getPlayer().sendMessage(String.format("%s: %s", Format.flag("SELL_REQUESTS"), Format.keyword(shop.sellRequests ? "On" : "Off")));
        cmd.getPlayer().sendMessage(String.format("%s: %s", Format.flag("BUY_REQUESTS"), Format.keyword(shop.buyRequests ? "On" : "Off")));
        return true;
    }
}
