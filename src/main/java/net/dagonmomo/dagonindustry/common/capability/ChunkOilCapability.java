package net.dagonmomo.dagonindustry.common.capability;

import net.dagonmomo.dagonindustry.core.util.DIMath;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class ChunkOilCapability implements IChunkOilCap
{
    @CapabilityInject(IChunkOilCap.class)
    public static Capability<IChunkOilCap> CHUNK_OIL = null;

    private double oil;
    private double maxOil;
    private long lastAccess;
    final Chunk chunk;
    boolean isNew = true;

    public ChunkOilCapability(Chunk chunk)
    {
        this.lastAccess = chunk.getWorld().getGameTime();
        this.chunk = chunk;
    }

    public ChunkOilCapability()
    {
        this(null);
    }

    @Override
    public double getOil()
    {
        initialize();
        setOil(DIMath.clamp(oil + (chunk.getWorld().getGameTime() - lastAccess) / 100.0, oil, maxOil));
        lastAccess = chunk.getWorld().getGameTime();
        return oil;
    }

    @Override
    public void setOil(double oil)
    {
        initialize();
        this.oil = oil;
    }

    @Override
    public void setMaxOil(double maxOil)
    {
        this.maxOil = maxOil;
    }

    @Override
    public double getMaxOil()
    {
        return maxOil;
    }

    @Override
    public void addOil(double oil)
    {
        setOil(this.oil + oil);
    }

    @Override
    public long getTimeLastAccessed()
    {
        return lastAccess;
    }

    @Override
    public void setTimeLastAccessed(long time)
    {
        this.lastAccess = time;
    }

    @Override
    public Chunk getChunk()
    {
        return chunk;
    }

    @Override
    public boolean isNew()
    {
        return isNew;
    }

    @Override
    public void setNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    void initialize()
    {
        if (isNew)
        {
            isNew = false;
            setMaxOil(Math.random() < 0.05 ? 600 + (int) (Math.random() * 3600): 0);
            setOil(maxOil);
        }
    }
}
