package net.dagonmomo.dagonindustry.common.capability;

import net.minecraft.world.chunk.Chunk;

public interface IChunkOilCap
{
    double getOil();
    void setOil(double oil);
    void setMaxOil(double maxOil);
    double getMaxOil();
    void addOil(double oil);
    long getTimeLastAccessed();
    void setTimeLastAccessed(long time);
    boolean isNew();
    Chunk getChunk();
    void setNew(boolean isNew);
}
