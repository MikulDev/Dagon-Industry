package net.dagonmomo.dagonindustry.core.util.registries;

import net.dagonmomo.dagonindustry.common.te.*;
import net.dagonmomo.dagonindustry.core.init.TileEntityInit;
import net.minecraft.tileentity.TileEntityType;

/**
 * Utility class that holds some commonly used TEs. Feel free to add the rest (I was lazy for this part)
 */
public class ModTileEntities
{
    public static TileEntityType<SteamhammerTileEntity> STEAMHAMMER = TileEntityInit.STEAMHAMMER.get();
    public static TileEntityType<CarbonGeneratorTileEntity> CARBON_GENERATOR = TileEntityInit.CARBON_GENERATOR.get();
    public static TileEntityType<FermenterTileEntity> FERMENTER = TileEntityInit.FERMENTER.get();
    public static TileEntityType<AlloyFurnaceTileEntity> ALLOY_FURNACE = TileEntityInit.ALLOY_FURNACE.get();
    public static TileEntityType<ArcFurnaceTileEntity> ARC_FURNACE = TileEntityInit.ARC_FURNACE.get();
    public static TileEntityType<BioRefineryTileEntity> BIO_REFINERY = TileEntityInit.BIO_REFINERY.get();
    public static TileEntityType<CrusherTileEntity> CRUSHER = TileEntityInit.CRUSHER.get();
    public static TileEntityType<MultiBlockPieceTileEntity> MULTIBLOCK = TileEntityInit.MULTIBLOCK_PIECE.get();
}
