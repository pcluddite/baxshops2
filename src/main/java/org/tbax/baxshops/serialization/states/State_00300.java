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
package org.tbax.baxshops.serialization.states;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.tbax.baxshops.BaxShop;
import org.tbax.baxshops.BaxShopFlag;
import org.tbax.baxshops.ShopPlugin;
import org.tbax.baxshops.notification.*;
import org.tbax.baxshops.serialization.BaxConfig;
import org.tbax.baxshops.serialization.PlayerMap;
import org.tbax.baxshops.serialization.SafeMap;
import org.tbax.baxshops.serialization.StoredPlayer;

import java.util.*;

public class State_00300 extends LoaderWithNotes
{
    public static final double VERSION = 3.0;
    private Map<Long, UUID> legacyIds = new HashMap<>();
    private PlayerMap players = new PlayerMap();
    private ShopPlugin plugin;

    public State_00300(@NotNull ShopPlugin plugin)
    {
        this.plugin = plugin;
    }

    public static int flagMapToFlag(SafeMap args)
    {
        int flags = BaxShopFlag.NONE;
        if (args.containsKey("buyRequests")) {
            flags = BaxShopFlag.setFlag(flags, BaxShopFlag.BUY_REQUESTS, args.getBoolean("buyRequests", false));
        }
        if (args.containsKey("infinite")) {
            flags = BaxShopFlag.setFlag(flags, BaxShopFlag.INFINITE, args.getBoolean("infinite", false));
        }
        if (args.containsKey("sellRequests")) {
            flags = BaxShopFlag.setFlag(flags, BaxShopFlag.SELL_REQUESTS, args.getBoolean("sellRequests", true));
        }
        if (args.containsKey("sellToShop")) {
            flags = BaxShopFlag.setFlag(flags, BaxShopFlag.SELL_TO_SHOP, args.getBoolean("sellToShop", false));
        }
        return flags;
    }

    public UUID getShopId(long legacyId)
    {
        return legacyIds.get(legacyId);
    }

    public OfflinePlayer getPlayer(String playerName)
    {
        if (playerName == null)
            return StoredPlayer.ERROR;
        return players.get(playerName).get(0);
    }

    public UUID getPlayerId(String playerName)
    {
        if (playerName == null)
            return StoredPlayer.ERROR_UUID;
        return players.get(playerName).get(0).getUniqueId();
    }

    public Collection<StoredPlayer> getPlayers()
    {
        return players.values();
    }

    public void addLegacyShop(long legacyId, UUID id)
    {
        legacyIds.put(legacyId, id);
    }

    public void invalidateMaps()
    {
        legacyIds.clear();
        players.clear();
        legacyIds = null;
        players = null;
    }

    @Override
    public void loadConfig(@NotNull BaxConfig config)
    {
        config.setDeathTaxEnabled(config.getFileConfig().contains("DeathTax"));
        if (config.isDeathTaxEnabled()) {
            String goesTo = config.getFileConfig().getString("DeathTax.GoesTo");
            config.setDeathTaxGoesTo(getPlayerId(goesTo));
        }
        invalidateMaps();
    }

    @Override
    public @NotNull Collection<BaxShop> buildShops(@NotNull FileConfiguration state)
    {
        List<BaxShop> shops = new ArrayList<>();
        if (!state.isList("shops")) {
            return shops;
        }
        for (Object o : state.getList("shops")) {
            if (o instanceof BaxShop) {
                BaxShop shop = (BaxShop) o;
                addLegacyShop(shop.getLegacyId(), shop.getId());
                shop.setOwner(getPlayer(shop.getLegacyOwner()));
                shops.add(shop);
            } else {
                plugin.getLogger().warning("Could not load BaxShop of type " + o.getClass());
            }
        }
        return shops;
    }

    @Override
    public @NotNull Collection<StoredPlayer> buildPlayers(@NotNull FileConfiguration state)
    {
        return getPlayers();
    }

    @Override
    public @NotNull Collection<NoteSet> buildNotifications(@NotNull FileConfiguration state)
    {
        List<NoteSet> noteSets = new ArrayList<>();
        if (!state.isConfigurationSection("notes")) {
            return noteSets;
        }
        for (Map.Entry entry : state.getConfigurationSection("notes").getValues(false).entrySet()) {
            OfflinePlayer player = getPlayer(entry.getKey().toString());
            if (!(entry.getValue() instanceof List)) {
                plugin.getLogger().warning("Could not load notifications of type " + entry.getValue().getClass());
            }
            else {
                Deque<Notification> pending = new ArrayDeque<>(((List) entry.getValue()).size());
                for (Object o : (List)entry.getValue()) {
                    Notification n = null;
                    if (o instanceof Notification) {
                        n = (Notification) o;
                        pending.add(n);
                    }
                    else if (o instanceof DeprecatedNote) {
                        n = ((DeprecatedNote)o).getNewNote(this);
                        pending.add(n);
                    }
                    else {
                        plugin.getLogger().warning("Could not readFromDisk Notification of type " + entry.getValue().getClass());
                    }
                    if (n != null) {
                        convertLegacyPlayers(n);
                    }
                }
                if (StoredPlayer.DUMMY.equals(player)) {
                    Deque<Notification> errors = new ArrayDeque<>();
                    while (!pending.isEmpty()) {
                        Notification n = pending.remove();
                        if (n instanceof Claimable || n instanceof Request) {
                            errors.add(n);
                        }
                    }
                    if (!errors.isEmpty()) {
                        plugin.getLogger().warning("There is one or more claim or request notification assigned to the dummy player. " +
                            "These cannot be honored and will be assigned to an error user. The configuration file will need to be fixed manually.");
                        noteSets.add(new NoteSet(StoredPlayer.ERROR_UUID, errors));
                    }
                }
                else {
                    noteSets.add(new NoteSet(player.getUniqueId(), pending));
                }
            }
        }
        return noteSets;
    }

    private void convertLegacyPlayers(Notification n)
    {
        if (n instanceof StandardNote) {
            StandardNote standardNote = (StandardNote)n;
            standardNote.setBuyer(getPlayer(standardNote.getLegacyBuyer()));
            standardNote.setSeller(getPlayer(standardNote.getLegacySeller()));
            standardNote.setShop(getShopId(standardNote.getLegacyShopId()));
        }
        else if (n instanceof DeletedShopClaim) {
            DeletedShopClaim deletedShopClaim = (DeletedShopClaim)n;
            deletedShopClaim.setOwner(getPlayer(deletedShopClaim.getLegacyOwner()));
        }
        else if (n instanceof LollipopNotification) {
            LollipopNotification lollipopNotification = (LollipopNotification)n;
            lollipopNotification.setSender(getPlayer(lollipopNotification.getLegacySender()));
        }
    }

    @Override
    public @NotNull ShopPlugin getPlugin()
    {
        return plugin;
    }
}
