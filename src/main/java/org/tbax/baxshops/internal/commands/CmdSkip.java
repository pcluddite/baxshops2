/*
 * Copyright (C) Timothy Baxendale
 * Portions derived from Shops Copyright (c) 2012 Nathan Dinsmore and Sam Lazarus.
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
package org.tbax.baxshops.internal.commands;

import org.jetbrains.annotations.NotNull;
import org.tbax.baxshops.CommandHelp;
import org.tbax.baxshops.commands.CmdActor;
import org.tbax.baxshops.errors.PrematureAbortException;
import org.tbax.baxshops.internal.Permissions;
import org.tbax.baxshops.internal.Resources;
import org.tbax.baxshops.internal.ShopPlugin;
import org.tbax.baxshops.serialization.StoredPlayer;

public final class CmdSkip extends ShopCommand
{
    @Override
    public @NotNull String getName()
    {
        return "skip";
    }

    @Override
    public @NotNull String[] getAliases()
    {
        return new String[] { "sk" };
    }

    @Override
    public String getPermission()
    {
        return Permissions.SHOP_TRADER;
    }

    @Override
    public CommandHelp getHelp(@NotNull CmdActor actor)
    {
        CommandHelp help = new CommandHelp(this, "skip latest notification");
        help.setLongDescription("Skip your most recent notification and add it to the back of your queue");
        return help;
    }

    @Override
    public boolean hasValidArgCount(@NotNull CmdActor actor)
    {
        return false;
    }

    @Override
    public boolean requiresSelection(@NotNull ShopCmdActor actor)
    {
        return false;
    }

    @Override
    public boolean requiresOwner(@NotNull ShopCmdActor actor)
    {
        return false;
    }

    @Override
    public boolean requiresPlayer(@NotNull CmdActor actor)
    {
        return true;
    }

    @Override
    public boolean requiresItemInHand(@NotNull ShopCmdActor actor)
    {
        return false;
    }

    @Override
    public void onShopCommand(@NotNull ShopCmdActor actor) throws PrematureAbortException
    {
        StoredPlayer player = actor.getStoredPlayer();
        if (!player.hasNotes()) {
            actor.exitError(Resources.NOT_FOUND_NOTE);
        }
        else {
            player.queueNote(player.dequeueNote());
            ShopPlugin.showNotification(actor.getPlayer());
        }
    }
}