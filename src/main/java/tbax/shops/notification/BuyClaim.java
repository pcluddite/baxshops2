/*
 * Copyright (C) Timothy Baxendale
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package tbax.shops.notification;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.tbax.baxshops.serialization.StateLoader;
import org.tbax.baxshops.serialization.states.StateLoader_00100;
import org.tbax.baxshops.serialization.states.StateLoader_00200;
import org.tbax.baxshops.serialization.states.StateLoader_00210;
import tbax.shops.ShopEntry;

@Deprecated
public class BuyClaim implements Notification
{
    public ShopEntry entry;
    public int shopId;
    public String buyer;
    public static final String JSON_TYPE_ID = "BuyClaim";

    public BuyClaim(StateLoader_00100 state00200, JsonObject o)
    {
        buyer = o.get("buyer").getAsString();
        shopId = o.get("shop").getAsInt();
        if (state00200 instanceof StateLoader_00210) {
            entry = new ShopEntry((StateLoader_00210)state00200, o.get("entry").getAsJsonObject());
        }
        else if (state00200 instanceof StateLoader_00200) {
            entry = new ShopEntry((StateLoader_00200)state00200, o.get("entry").getAsJsonObject());
        }
        else {
            entry = new ShopEntry(state00200, o.get("entry").getAsJsonObject());
        }
    }

    @Override
    public @NotNull org.tbax.bukkit.notification.Notification getNewNote(StateLoader stateLoader)
    {
        return new org.tbax.baxshops.notification.BuyClaim(
                stateLoader.getShop(null, shopId).getId(),
                stateLoader.getPlayerSafe(null, buyer),
                stateLoader.getPlayerSafe(null, ((StateLoader_00100)stateLoader).getShopOwner(shopId)),
                entry.update((StateLoader_00100)stateLoader)
        );
    }

    @Override
    public @NotNull Class<? extends org.tbax.bukkit.notification.Notification> getNewNoteClass()
    {
        return org.tbax.baxshops.notification.BuyClaim.class;
    }
}
