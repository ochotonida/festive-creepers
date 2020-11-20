package festivecreepers.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FireworkExplosionPacket {

    private final CompoundNBT fireworks;
    private final double x;
    private final double y;
    private final double z;
    private final double motionX;
    private final double motionY;
    private final double motionZ;

    FireworkExplosionPacket(PacketBuffer buffer) {
        fireworks = buffer.readCompoundTag();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
        motionX = buffer.readDouble();
        motionY = buffer.readDouble();
        motionZ = buffer.readDouble();
    }

    public FireworkExplosionPacket(CompoundNBT fireworks, double x, double y, double z, Vector3d motion) {
        this.fireworks = fireworks;
        this.x = x;
        this.y = y;
        this.z = z;
        motionX = motion.x;
        motionY = motion.y;
        motionZ = motion.z;
    }

    void encode(PacketBuffer buffer) {
        buffer.writeCompoundTag(fireworks);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(motionX);
        buffer.writeDouble(motionY);
        buffer.writeDouble(motionZ);
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (Minecraft.getInstance().world != null) {
                Minecraft.getInstance().world.makeFireworks(x, y, z, motionX, motionY, motionZ, fireworks);
            }
        });
    }
}
