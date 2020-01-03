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
package org.tbax.baxshops;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.tbax.baxshops.commands.BaxCommand;

@SuppressWarnings("unused")
public final class CommandHelp
{
    private BaxCommand cmd;
    private CommandHelpArgument[] args;
    private String longDescription;
    private String shortDescription;

    public CommandHelp(@NotNull BaxCommand cmd, @NotNull String shortDescription)
    {
        this.cmd = cmd;
        this.shortDescription = shortDescription;
    }

    public @NotNull String getName()
    {
        return cmd.getName();
    }

    public String getLongDescription()
    {
        return longDescription;
    }

    public void setLongDescription(String desc)
    {
        longDescription = desc;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public void setShortDescription(String desc)
    {
        shortDescription = desc;
    }

    public CommandHelpArgument[] getArgs()
    {
        return args;
    }

    public void setArgs(CommandHelpArgument... args)
    {
        this.args = args;
    }

    public @NotNull String getUsageString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.BLUE).append("Usage: ").append(ChatColor.GRAY).append("/shop ")
                .append(ChatColor.DARK_AQUA).append(cmd.getName()).append(ChatColor.GRAY);
        if (args != null) {
            for (CommandHelpArgument arg : args) {
                sb.append(" ").append(arg.getUsageString());
            }
        }
        return sb.toString();
    }

    public @NotNull String getAliasString()
    {
        StringBuilder sb = new StringBuilder();
        for(String alias : cmd.getAliases()) {
            sb.append(" ").append(alias);
        }
        return sb.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(Format.header(String.format("Help: /shop %s", cmd.getName()))).append('\n');
        sb.append(getUsageString());
        if (cmd.getAliases().length != 0) {
            sb.append('\n').append(ChatColor.BLUE).append("Aliases:").append(ChatColor.GRAY).append(getAliasString());
        }
        sb.append('\n');
        sb.append('\n');
        if (longDescription != null) {
            sb.append(Format.wordWrap(ChatColor.GRAY + longDescription)).append('\n');
        }
        else {
            sb.append(ChatColor.GRAY).append(shortDescription).append('\n');
        }

        if (cmd.requiresAdmin()) {
            sb.append(ChatColor.DARK_RED).append("This command is only available to admins").append('\n');
        }

        if (args != null) {
            for (CommandHelpArgument arg : args) {
                sb.append('\n').append(arg);
            }
        }
        return sb.toString();
    }
}
