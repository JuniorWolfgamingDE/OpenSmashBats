package tsuteto.smashbat.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 */
public class PacketPlayerSmashing extends AbstractPacket
{
    private double vecX;
    private double vecY;
    private double vecZ;

    public PacketPlayerSmashing()
    {
    }

    public PacketPlayerSmashing(double vecX, double vecY, double vecZ)
    {
        this.vecX = vecX;
        this.vecY = vecY;
        this.vecZ = vecZ;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeDouble(vecX);
        buffer.writeDouble(vecY);
        buffer.writeDouble(vecZ);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        vecX = buffer.readDouble();
        vecY = buffer.readDouble();
        vecZ = buffer.readDouble();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        player.addVelocity(vecX, vecY, vecZ);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
    }
}
