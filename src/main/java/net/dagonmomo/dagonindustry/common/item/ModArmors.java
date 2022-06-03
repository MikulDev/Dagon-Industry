package net.dagonmomo.dagonindustry.common.item;

import net.dagonmomo.dagonindustry.core.util.registries.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;

public enum ModArmors implements IArmorMaterial
{
    STEEL_ARMOR("steel", 1, new int[] {4, 7, 9, 4}, 17, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 4f, 0.2f,
                () -> Ingredient.fromItems(ModItems.STEEL_PLATE, ModItems.COBALT_PLATE));

    private static final int[] baseDurability = {500, 600, 700, 500};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] armorValues;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResist;
    private final Ingredient repairMaterial;

    ModArmors(String name, int durabilityMultiplier, int[] armorValues, int enchantability,
              SoundEvent equipSound, float toughness, float knockbackResist, Supplier<Ingredient> repairMaterial)
    {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.armorValues = armorValues;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResist = knockbackResist;
        this.repairMaterial = repairMaterial.get();
    }

    @Override
    public int getDurability(EquipmentSlotType slot)
    {
        return baseDurability[slot.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slot)
    {
        return this.armorValues[slot.getIndex()];
    }

    @Override
    public int getEnchantability()
    {
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent()
    {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairMaterial()
    {
        return this.repairMaterial;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public float getToughness()
    {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance()
    {
        return this.knockbackResist;
    }
}
