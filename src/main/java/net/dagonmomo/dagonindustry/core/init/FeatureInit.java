package net.dagonmomo.dagonindustry.core.init;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FeatureInit
{
    @SubscribeEvent
    public static void addAllOres(BiomeLoadingEvent event)
    {
        addOre(event, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlockInit.COBALT_ORE.get().getDefaultState(), 6, 0, 16, 4);
        addOre(event, OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlockInit.SULFUR_ORE.get().getDefaultState(), 12, 16, 56, 12);
    }

    public static void addOre(BiomeLoadingEvent event, RuleTest rule, BlockState state, int veinSize, int minHeight, int maxHeight, int count)
    {
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                                          Feature.ORE.withConfiguration(new OreFeatureConfig(rule, state, veinSize)).withPlacement(
                                          Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight))).square().count(count));
    }
}
