/** +++====+++
 *
 *  Copyright (c) Timothy Baxendale
 *
 *  +++====+++
 **/

package tbax.baxshops.commands.flags;

import tbax.baxshops.BaxEntry;
import tbax.baxshops.BaxShop;
import tbax.baxshops.Format;
import tbax.baxshops.commands.ShopCmdActor;
import tbax.baxshops.errors.PrematureAbortException;

public class FlagCmdInfinite extends FlagCmd
{
    @Override
    public String[] getAliases() { return new String[]{"infinite", "isinfinite","inf"}; }

    @Override
    public void onCommand(ShopCmdActor actor) throws PrematureAbortException
    {
        BaxShop shop = actor.getShop();
        boolean value = actor.getArgBoolean(2, "Usage:\n/shop flag infinite [true|false]");
        shop.setFlagInfinite(value);
        for(BaxEntry e : shop)
        {
            e.setInfinite(value);
        }

        actor.sendMessage(Format.flag("Infinite items") + " for this shop are " + Format.keyword(value ? "enabled" : "disabled"));
    }
}