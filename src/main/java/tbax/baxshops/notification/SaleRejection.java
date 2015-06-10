package tbax.baxshops.notification;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tbax.baxshops.BaxEntry;
import tbax.baxshops.BaxShop;
import tbax.baxshops.Main;
import tbax.baxshops.Resources;
import tbax.baxshops.serialization.ItemNames;

/**
 * A SaleRejection notifies a seller that his/her offer was rejected.
 */
public class SaleRejection implements Claimable {
    private static final long serialVersionUID = 1L;
    /**
     * An entry for the offered item
     */
    public BaxEntry entry;
    /**
     * The shop to which the item is being sold
     */
    public BaxShop shop;
    /**
     * The seller of the item
     */
    public String seller;

    /**
     * Constructs a new notification.
     * @param shop the shop to which the seller was selling
     * @param entry an entry for the item (note: not the one in the shop)
     * @param seller the seller of the item
     */
    public SaleRejection(BaxShop shop, BaxEntry entry, String seller) {
        this.shop = shop;
        this.entry = entry;
        this.seller = seller;
    }

    @Override
    public String getMessage(Player player) {
        return player == null || !player.getName().equals(seller) ?
                String.format("%s rejected %s's request to sell %d %s for $%.2f",
                                shop.owner, seller, entry.getAmount(), ItemNames.getItemName(entry),
                                entry.refundPrice * entry.getAmount()) :
                String.format("§1%s §frejected your request to sell §e%d %s§F for §a$%.2f§F",
                                shop.owner, entry.getAmount(), ItemNames.getItemName(entry),
                                entry.refundPrice * entry.getAmount());
    }

    @Override
    public boolean claim(Player player) {
        ItemStack item = entry.toItemStack();
        if (Main.inventoryFitsItem(player, item)){
            player.getInventory().addItem(item);
            return true;
        }
        else {
            Main.sendError(player, Resources.NO_ROOM);
            return false;
        }
    }

    public static final String TYPE_ID = "SaleReject";
    
    @Override
    public JsonElement toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("type", "SaleReject");
        o.addProperty("seller", seller);
        o.addProperty("shop", shop.uid);
        o.add("entry", entry.toJson());
        return o;
    }
    
    public SaleRejection() {
    }
    
    public static SaleRejection fromJson(JsonObject o) {
        SaleRejection claim = new SaleRejection();
        claim.seller = o.get("seller").getAsString();
        claim.shop = Main.instance.state.getShop(o.get("shop").getAsInt());
        claim.entry = new BaxEntry(o.get("entry").getAsJsonObject());
        return claim;
    }
}
