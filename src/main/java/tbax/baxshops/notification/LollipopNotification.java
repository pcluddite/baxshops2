/** +++====+++
 *  
 *  Copyright (c) Timothy Baxendale
 *  Copyright (c) 2012 Nathan Dinsmore and Sam Lazarus
 *
 *  +++====+++
**/

package tbax.baxshops.notification;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

/**
 * A LollipopNotification notifies a player that someone sent him/her a
 * lollipop.
 */
public final class LollipopNotification implements ConfigurationSerializable, Notification
{
    public static final double DEFAULT_TASTINESS = 40;
    public static final Map<Double, String> adjectives = new LinkedHashMap<>();
    static {
        adjectives.put(0.0, "a disgusting");
        adjectives.put(10.0, "a bad");
        adjectives.put(20.0, "an icky");
        adjectives.put(30.0, "a bland");
        adjectives.put(40.0, "a");
        adjectives.put(50.0, "an OK");
        adjectives.put(55.0, "a better-than-average");
        adjectives.put(60.0, "a good");
        adjectives.put(70.0, "a great");
        adjectives.put(80.0, "a tasty");
        adjectives.put(90.0, "a delicious");
        adjectives.put(99.0, "a wonderful");
    }

    public String sender;
    public double tastiness;
    
    public LollipopNotification(Map<String, Object> args)
    {
        this.sender = (String)args.get("sender");
        this.tastiness = (double)args.get("tastiness");
    }

    public LollipopNotification(String sender, double tastiness)
    {
        this.sender = sender;
        this.tastiness = tastiness < 0 ? 0 : tastiness > 100 ? 100 : tastiness;
    }

    @Override
    public String getMessage(Player player)
    {
        String adjective = null;
        for (Entry<Double, String> entry : adjectives.entrySet()) {
                if (tastiness >= entry.getKey()) {
                        adjective = entry.getValue();
                }
        }
        return sender + " sent you " + adjective + " lollipop";
    }
    
    public Map<String, Object> serialize()
    {
        Map<String, Object> args = new HashMap<>();
        args.put("sender", sender);
        args.put("tastiness", tastiness);
        return args;
    }
    
    public static LollipopNotification deserialize(Map<String, Object> args)
    {
        return new LollipopNotification(args);
    }
    
    public static LollipopNotification valueOf(Map<String, Object> args)
    {
        return deserialize(args);
    }

    @Override
    public boolean checkIntegrity()
    {
        return true;
    }
}