/** +++====+++
 *
 *  Copyright (c) Timothy Baxendale
 *
 *  +++====+++
**/
package tbax.baxshops.notification;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import tbax.baxshops.BaxEntry;
import tbax.baxshops.Format;

import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public final class SaleNotification extends StandardNote
{
    public SaleNotification(UUID shopId, OfflinePlayer buyer, OfflinePlayer seller, BaxEntry entry)
    {
        super(shopId, buyer, seller, entry);
    }

    public SaleNotification(UUID shopId, UUID buyer, UUID seller, BaxEntry entry)
    {
        super(shopId, buyer, seller, entry);
    }

    public SaleNotification(Map<String, Object> args)
    {
        super(args);
    }

    @Override
    public @NotNull String getMessage(CommandSender sender)
    {
        if (getSeller().equals(sender)) {
            return String.format("%s accepted your request to sell %s for %s.",
                Format.username(buyer), entry.getFormattedName(), entry.getFormattedSellPrice()
            );
        }
        else {
            return getMessage();
        }
    }

    @Override
    public @NotNull String getMessage()
    {
        return String.format("%s accepted %s's request to sell %s for %s.",
            Format.username(buyer), Format.username2(seller), entry.getFormattedName(), entry.getFormattedSellPrice()
        );
    }

    public static SaleNotification deserialize(Map<String, Object> args)
    {
        return new SaleNotification(args);
    }

    public static SaleNotification valueOf(Map<String, Object> args)
    {
        return deserialize(args);
    }
}
