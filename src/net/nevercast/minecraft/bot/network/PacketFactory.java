package net.nevercast.minecraft.bot.network;

import net.nevercast.minecraft.bot.network.packets.*;
import java.util.HashMap;

import com.esotericsoftware.minlog.Log;

/**
 * Handles supported and unsupported packets.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class PacketFactory {
	
	private static final String LOG_PREFIX = PacketFactory.class.getSimpleName();

    private static HashMap<Byte, Class<? extends Packet>> supportedPackets = new HashMap<Byte, Class<? extends Packet>>();
    private static HashMap<Byte, Integer> unsupportedPackets = new HashMap<Byte, Integer>();

    static{
    	// For unsupported packets, format is (id, length - 1)
    	
        supportedPackets.put((byte)0x00, Packet00KeepAlive.class);
        supportedPackets.put((byte)0x01, Packet01LoginRequest.class);
        supportedPackets.put((byte)0x02, Packet02Handshake.class);
        supportedPackets.put((byte)0x03, Packet03ChatMessage.class);
        supportedPackets.put((byte)0x04, Packet04TimeUpdate.class);
        supportedPackets.put((byte)0x05, Packet05EntityEquipment.class);
        supportedPackets.put((byte)0x06, Packet06SpawnLocation.class);
        unsupportedPackets.put((byte)0x07, 9);										// Use Entity
        supportedPackets.put((byte)0x08, Packet08UpdateHealth.class);
        supportedPackets.put((byte)0x09, Packet09Respawn.class);
        supportedPackets.put((byte)0x0A, Packet0APlayer.class);
        supportedPackets.put((byte)0x0B, Packet0BPlayerPosition.class);
        supportedPackets.put((byte)0x0C, Packet0CPlayerLook.class);
        supportedPackets.put((byte)0x0D, Packet0DPlayerPositionAndLook.class);
        unsupportedPackets.put((byte)0x0E, 11); 									// Digging
        supportedPackets.put((byte)0x0F, Packet0FPlayerBlockPlacement.class);
        unsupportedPackets.put((byte)0x10, 2); 										// Held Item Change
        unsupportedPackets.put((byte)0x11, 14); 									// Use Bed
        unsupportedPackets.put((byte)0x12, 5); 										// Animation
        unsupportedPackets.put((byte)0x13, 5); 										// Entity Action
        supportedPackets.put((byte)0x14, Packet14NamedEntitySpawn.class);
        supportedPackets.put((byte)0x15, Packet15ItemSpawned.class);
        unsupportedPackets.put((byte)0x16, 8); 										// Collect item
        supportedPackets.put((byte)0x17, Packet17SpawnObjectVehicle.class);
        supportedPackets.put((byte)0x18, Packet18MobSpawned.class);
        supportedPackets.put((byte)0x19, Packet19SpawnPainting.class);
        unsupportedPackets.put((byte)0x1A, 18);										// Spawn Experience Orb
        unsupportedPackets.put((byte)0x1C, 10); 									// Entity velocity
        supportedPackets.put((byte)0x1D, Packet1DEntityDestroyed.class);
        unsupportedPackets.put((byte)0x1E, 4); 										// Entity
        unsupportedPackets.put((byte)0x1F, 7); 										// Entity Relative Move
        unsupportedPackets.put((byte)0x20, 6); 										// Entity Look
        unsupportedPackets.put((byte)0x21, 9); 										// Entity Look/Move
        unsupportedPackets.put((byte)0x22, 18); 									// Entity Teleport
        unsupportedPackets.put((byte)0x23, 5);	 									// Entity Head Look
        unsupportedPackets.put((byte)0x26, 5); 										// Entity state
        unsupportedPackets.put((byte)0x27, 8); 										// Attach Entity
        supportedPackets.put((byte)0x28, Packet28EntityMetadata.class);
        unsupportedPackets.put((byte)0x29, 8); 										// Entity MetaData
        unsupportedPackets.put((byte)0x2A, 5); 										// Remove Entity Effect
        unsupportedPackets.put((byte)0x2B, 8); 										// Set Experience
        unsupportedPackets.put((byte)0x32, 9);										// Pre-Chunk (Map Column Allocation)
        supportedPackets.put((byte)0x33, Packet33MapChunk.class);
        supportedPackets.put((byte)0x34, Packet34MultiBlockChange.class);
        supportedPackets.put((byte)0x35, Packet35BlockChange.class);
        unsupportedPackets.put((byte)0x36, 12); 									// Block Action
        supportedPackets.put((byte)0x3C, Packet3CExplosion.class);
        unsupportedPackets.put((byte)0x3D, 17); 									// Effect
        supportedPackets.put((byte)0x46, Packet46StateInfo.class);
        unsupportedPackets.put((byte)0x47, 17); 									// Thunderbolt
        supportedPackets.put((byte)0x64, Packet64OpenWindow.class);
        unsupportedPackets.put((byte)0x65, 1); 										// Close window
        supportedPackets.put((byte)0x66, Packet66ClickWindow.class);
        supportedPackets.put((byte)0x67, Packet67SetSlot.class);
        supportedPackets.put((byte)0x68, Packet68WindowItems.class);
        unsupportedPackets.put((byte)0x69, 5); 										// Update Window Property
        unsupportedPackets.put((byte)0x6A, 4); 										// Confirm Transaction
        supportedPackets.put((byte)0x6B, Packet6BCreativeInventoryAction.class);
        unsupportedPackets.put((byte)0x6C, 2); 										// Enchant Item
        supportedPackets.put((byte)0x82, Packet82UpdateSign.class);
        supportedPackets.put((byte)0x83, Packet83ItemData.class);
        unsupportedPackets.put((byte)0x84, 23); 									// Update Tile Entity
        unsupportedPackets.put((byte)0xC8, 5); 										// Increment Statistic
        supportedPackets.put((byte)0xC9, PacketC9PlayerListItem.class);
        unsupportedPackets.put((byte)0xCA, 4);										// Player Abilities
        supportedPackets.put((byte)0xFA, PacketFAPluginMessage.class);
        supportedPackets.put((byte)0xFD, PacketFDEncryptionKeyRequest.class);
        supportedPackets.put((byte)0xFF, PacketFFDisconnect.class);
    }

    public static boolean getSupportsPacketId(byte id){
        return supportedPackets.containsKey(id);
    }

    public static boolean getCanEatPacket(byte id){
        return unsupportedPackets.containsKey(id);
    }

    public static int getPacketLength(byte id){
        return unsupportedPackets.get(id);
    }

    public static Packet getPacket(byte id){
        Class<? extends Packet> clazz = supportedPackets.get(id);
        try{
            Object o = clazz.newInstance();
            if(!(o instanceof Packet)){
                Log.warn(LOG_PREFIX, "Invalid packet type: " + o);
                return null;
            }else{
                Packet packet = (Packet)o;
                return packet;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            Log.warn(LOG_PREFIX, "Error handling packet:", e);
            return null;
        }
    }
}
