package studio.dreamys.util.delayedEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import studio.dreamys.util.interfaces.IDelayedEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DelayedEventManager {
    private static final Map<Integer, EventEntry> eventEntryMap = new HashMap<>();
    private static final Map<String, IDelayedEvent> delayedEvents = new HashMap<>();

    static {
        delayedEvents.put("creeper", new DelayedEventCreeper());
        delayedEvents.put("thunder", new DelayedEventThunder());
    }

    public static void addDelayedEvent(int entityId, IDelayedEvent.SmashEntry smashEntry, String eventType) {
        IDelayedEvent event = delayedEvents.get(eventType);
        if (event != null) {
            eventEntryMap.put(entityId, new EventEntry(smashEntry, event));
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.world.isRemote) {
            return;
        }

        World world = event.world;
        Iterator<Map.Entry<Integer, EventEntry>> iterator = eventEntryMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, EventEntry> entry = iterator.next();
            EventEntry eventEntry = entry.getValue();
            Entity entity = world.getEntityByID(entry.getKey());

            if (entity == null || entity.isDead) {
                iterator.remove();
                continue;
            }

            boolean isFlyingType = entity instanceof EntityFlying || entity instanceof EntityBat ||
                    entity instanceof EntityBlaze || entity instanceof EntityParrot;

            if (isFlyingType) {
                if (eventEntry.ticksAirborne > 10 && Math.abs(entity.motionY) < 0.1) {
                    eventEntry.delayedEvent.onEvent(eventEntry.smashEntry, entity);
                    iterator.remove();
                } else {
                    eventEntry.ticksAirborne++;
                }
                continue;
            }

            if (!eventEntry.smashEntry.wasAirborne && entity.motionY > 0.1) {
                eventEntry.smashEntry.wasAirborne = true;
                eventEntry.smashEntry.lastMotionY = entity.motionY;
            } else if (eventEntry.smashEntry.wasAirborne) {
                if (entity.motionY < eventEntry.smashEntry.lastMotionY && entity.motionY < 0) {
                    eventEntry.delayedEvent.onEvent(eventEntry.smashEntry, entity);
                    iterator.remove();
                    continue;
                }
                eventEntry.smashEntry.lastMotionY = entity.motionY;
            }

            eventEntry.ticksAirborne++;
            if (eventEntry.ticksAirborne > 200) {
                iterator.remove();
            }
        }
    }

    private static class EventEntry {
        public final IDelayedEvent.SmashEntry smashEntry;
        public final IDelayedEvent delayedEvent;
        public int ticksAirborne;

        public EventEntry(IDelayedEvent.SmashEntry smashEntry, IDelayedEvent delayedEvent) {
            this.smashEntry = smashEntry;
            this.delayedEvent = delayedEvent;
            this.ticksAirborne = 0;
        }
    }
}