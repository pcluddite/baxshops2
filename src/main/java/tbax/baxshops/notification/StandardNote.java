/*
 * Copyright (C) 2013-2019 Timothy Baxendale
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
package tbax.baxshops.notification;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tbax.baxshops.BaxEntry;
import tbax.baxshops.BaxShop;
import tbax.baxshops.ShopPlugin;
import tbax.baxshops.serialization.UpgradeableSerialization;
import tbax.baxshops.serialization.SafeMap;
import tbax.baxshops.serialization.StoredPlayer;
import tbax.baxshops.serialization.UpgradeableSerializable;
import tbax.baxshops.serialization.annotations.SerializeMethod;
import tbax.baxshops.serialization.states.State_00300;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public abstract class StandardNote implements Notification, UpgradeableSerializable
{
    protected BaxEntry entry;
    protected Date date;

    @SerializeMethod(getter = "getBuyer")
    protected UUID buyer;

    @SerializeMethod(getter = "getSeller")
    protected UUID seller;

    @SerializeMethod(getter = "getShopId")
    protected UUID shopId;

    public StandardNote(UUID shopId, OfflinePlayer buyer, OfflinePlayer seller, BaxEntry entry)
    {
        this(shopId, buyer.getUniqueId(), seller.getUniqueId(), entry);
    }

    public StandardNote(UUID shopId, UUID buyer, UUID seller, BaxEntry entry)
    {
        this.shopId = shopId;
        this.buyer = buyer;
        this.seller = seller;
        this.entry = new BaxEntry(entry);
        this.date = new Date();
    }

    public StandardNote(Map<String, Object> args)
    {
        UpgradeableSerialization.upgrade(this, args);
    }

    @Deprecated
    public void upgrade00300(@NotNull SafeMap map)
    {
        buyer = State_00300.getPlayerId(map.getString("buyer", StoredPlayer.DUMMY_NAME));
        seller = State_00300.getPlayerId(map.getString("seller", StoredPlayer.DUMMY_NAME));
        shopId = State_00300.getShopId(map.getLong("shopId"));
        entry = map.getBaxEntry("entry");
    }

    @Deprecated
    public void upgrade00400(@NotNull SafeMap map)
    {
        entry = map.getBaxEntry("entry");
        buyer = map.getUUID("buyer");
        seller = map.getUUID("seller");
        shopId = map.getUUID("shopId");
        date = map.getDate("date");
    }

    @Override
    public @Nullable Date getSentDate()
    {
        return date;
    }

    public @NotNull OfflinePlayer getBuyer()
    {
        return ShopPlugin.getOfflinePlayer(buyer);
    }

    public @NotNull OfflinePlayer getSeller()
    {
        return ShopPlugin.getOfflinePlayer(seller);
    }

    public @NotNull BaxEntry getEntry()
    {
        return entry;
    }

    public @NotNull UUID getShopId()
    {
        if (shopId == null)
            return shopId = BaxShop.DUMMY_UUID;
        return shopId;
    }
}
