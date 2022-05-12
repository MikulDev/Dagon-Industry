package net.dagonmomo.dagonindustry.core.init;

import net.dagonmomo.dagonindustry.DagonIndustry;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit
{
    public static final ResourceLocation WATER_STILL_TEXTURE = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_TEXTURE = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_TEXTURE = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, DagonIndustry.MOD_ID);

    public static final RegistryObject<FlowingFluid> OIL_FLUID = FLUIDS.register("oil_fluid", () -> new ForgeFlowingFluid.Source(FluidInit.OIL_PROPERTIES));
    public static final RegistryObject<FlowingFluid> OIL_FLOWING = FLUIDS.register("oil_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.OIL_PROPERTIES));
    public static final RegistryObject<FlowingFluid> GASOLINE_FLUID = FLUIDS.register("gasoline_fluid", () -> new ForgeFlowingFluid.Source(FluidInit.GASOLINE_PROPERTIES));
    public static final RegistryObject<FlowingFluid> DIESEL_FLOWING = FLUIDS.register("gasoline_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.GASOLINE_PROPERTIES));

    public static final ForgeFlowingFluid.Properties OIL_PROPERTIES = new ForgeFlowingFluid.Properties(() -> OIL_FLUID.get(), () -> OIL_FLOWING.get(),
                                                       FluidAttributes.builder(WATER_STILL_TEXTURE, WATER_FLOWING_TEXTURE).overlay(WATER_OVERLAY_TEXTURE)
                                                       .density(15).viscosity(5).luminosity(0).sound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK).color(0xFF160C0F))
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(() -> BlockInit.OIL_BLOCK.get()).bucket(() -> ItemInit.OIL_BUCKET.get());

    public static final ForgeFlowingFluid.Properties GASOLINE_PROPERTIES = new ForgeFlowingFluid.Properties(() -> GASOLINE_FLUID.get(), () -> DIESEL_FLOWING.get(),
                                                                                                            FluidAttributes.builder(WATER_STILL_TEXTURE, WATER_FLOWING_TEXTURE).overlay(WATER_OVERLAY_TEXTURE)
                                                       .density(15).viscosity(5).luminosity(0).sound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK).color(0xFFB9BA28))
            .slopeFindDistance(3).levelDecreasePerBlock(2).block(() -> BlockInit.GASOLINE_BLOCK.get()).bucket(() -> ItemInit.GASOLINE_BUCKET.get());
}
