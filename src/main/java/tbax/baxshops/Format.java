/** +++====+++
 *  
 *  Copyright (c) Timothy Baxendale
 *
 *  +++====+++
**/

package tbax.baxshops;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Methods for formatting strings
 */
@SuppressWarnings("unused")
public final class Format
{   
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private Format()
    {
    }

    public static @NotNull String date(@NotNull Date dt)
    {
        return DATE_FORMAT.format(dt);
    }

    public static @NotNull String money(@NotNull String in)
    {
        return reset(ChatColor.GREEN + in);
    }
    
    public static @NotNull String money(double d)
    {
        return money(ShopPlugin.getEconomy().format(d));
    }
    
    public static @NotNull String money2(@NotNull String in)
    {
        return reset(ChatColor.DARK_GREEN + in);
    }
    
    public static @NotNull String money2(double d)
    {
        return money2(ShopPlugin.getEconomy().format(d));
    }
    
    public static @NotNull String number(@NotNull String n)
    {
        return reset(ChatColor.AQUA + n);
    }
    
    public static @NotNull String number(int n)
    {
        return number(Integer.toString(n));
    }
    
    public static @NotNull String error(@NotNull String err)
    {
        return reset(ChatColor.RED + err);
    }

    public static @NotNull String warning(@NotNull String msg)
    {
        return reset(ChatColor.GOLD + msg);
    }
    
    public static @NotNull String location(@NotNull String loc)
    {
        return reset(ChatColor.GOLD + loc);
    }
    
    public static @NotNull String location(@NotNull Location loc)
    {
        return location(String.format("(%d,%d,%d)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) + ChatColor.RESET;
    }
    
    public static @NotNull String itemName(int amount, @NotNull String name)
    {
        return itemName(String.format("%d %s", amount, name));
    }
    
    public static @NotNull String itemName(@NotNull String name)
    {
        return reset(ChatColor.GREEN + name);
    }
    
    public static @NotNull String username(@NotNull UUID uuid)
    {
        return username(ShopPlugin.getOfflinePlayer(uuid).getName());
    }

    public static @NotNull String username2(@NotNull UUID uuid)
    {
        return username2(ShopPlugin.getOfflinePlayer(uuid).getName());
    }

    public static @NotNull String username(@NotNull String name)
    {
        return reset(ChatColor.DARK_BLUE + name);
    }
    
    public static @NotNull String username2(@NotNull String name)
    {
        return reset(ChatColor.LIGHT_PURPLE + name);
    }
    
    public static @NotNull String command(@NotNull String cmd)
    {
        return reset(ChatColor.AQUA + cmd);
    }
    
    public static @NotNull String retailPrice(@NotNull String price)
    {
        return reset(ChatColor.DARK_GREEN + price);
    }
    
    public static @NotNull String retailPrice(double price)
    {
        return retailPrice(String.format("(%s)", ShopPlugin.getEconomy().format(price)));
    }
    
    public static @NotNull String refundPrice(@NotNull String price)
    {
        return reset(ChatColor.BLUE + price);
    }
    
    public static @NotNull String refundPrice(double price)
    {
        return refundPrice(String.format("(%s)", ShopPlugin.getEconomy().format(price)));
    }
    
    public static @NotNull String enchantments(@NotNull String enchant)
    {
        return reset(ChatColor.DARK_PURPLE + enchant);
    }
    
    public static @NotNull String bullet(@NotNull String b)
    {
        return reset(ChatColor.GRAY + b);
    }
    
    public static @NotNull String bullet(int b)
    {
        return bullet(Integer.toString(b));
    }
    
    public static @NotNull String listname(@NotNull String name)
    {
        return reset(ChatColor.WHITE + name);
    }
    
    public static @NotNull String flag(@NotNull String flag)
    {
        return reset(ChatColor.YELLOW + flag);
    }
    
    public static @NotNull String keyword(@NotNull String word)
    {
        return reset(ChatColor.GREEN + word);
    }
    
    private static @NotNull String reset(@NotNull String format)
    {
        return format + ChatColor.RESET;
    }
    
    /**
     * Converts a number 1-5 to a Roman numeral
     * @param n the number (1-5) to convert to a numeral
     * @return a string of the roman numeral
     */
    public static String toNumeral(int n)
    {
        assert n > 0 && n < 6;
        switch(n) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }
        return null;
    }
    
    public static String toFriendlyName(String name)
    {
        if (name == null || name.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean upper = true;
        for(int index = 0; index < name.length(); ++index) {
            char c = name.charAt(index);
            switch(c) {
                case '(': // end of name
                    return sb.toString();
                case '_': // make this char a space
                case ' ':
                    upper = true;
                    sb.append(' ');
                    break;
                default:
                    if (upper) {
                        sb.append(Character.toUpperCase(c));
                        upper = false;
                    }
                    else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }
        return sb.toString();
    }
    
    public static @NotNull String toAnsiColor(@NotNull String message) // obnoxious method to convert minecraft message colors to ansi colors
    {
        StringBuilder sb = new StringBuilder();
        boolean has_ansi = false;
        for(int index = 0; index < message.length(); ++index) {
            char c = message.charAt(index);
            if (c == ChatColor.COLOR_CHAR && ++index < message.length()) {
                c = Character.toLowerCase(message.charAt(index));
                sb.append((char)27);
                sb.append("[0;");
                switch(c) {
                    case '0': sb.append("30"); break;
                    case '1': sb.append("34"); break;
                    case '2': sb.append("32"); break;
                    case '3': sb.append("36"); break;
                    case '4': sb.append("31"); break;
                    case '5': sb.append("35"); break;
                    case '6': sb.append("33"); break;
                    case '7': sb.append("37"); break;
                    case '8': sb.append("37"); break;
                    case '9': sb.append("36"); break;
                    case 'a': sb.append("32"); break;
                    case 'b': sb.append("36"); break;
                    case 'c': sb.append("31"); break;
                    case 'd': sb.append("35"); break;
                    case 'e': sb.append("33"); break;
                    case 'f': sb.append("37"); break;
                    default:
                        sb.append("37"); break;
                }
                sb.append("m");
                if (!has_ansi) {
                    has_ansi = true;
                }
            }
            else {
                sb.append(c);
            }
        }
        if (has_ansi) {
            sb.append((char)27);
            sb.append("[0m"); // reset the color
        }
        return sb.toString();
    }

    /**
     * Generates a chat header with the given title
     * @param title the text in the header
     * @return the chat header
     */
    public static @NotNull String header(String title)
    {
        return ChatColor.GRAY.toString() +
            "------------ " +
            ChatColor.WHITE +
            title +
            ChatColor.GRAY +
            " ------------" +
            ChatColor.RESET;
    }

    public static @NotNull String stripColor(@NotNull String str)
    {
        StringBuilder sb = new StringBuilder(str.length());
        for (int idx = 0; idx < str.length(); ++idx) {
            char c = str.charAt(idx);
            if (c == ChatColor.COLOR_CHAR) {
                ++idx;
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
