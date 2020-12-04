package com.github.elenterius.blightlings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IEntityUnveiler<T extends ArmorItem>
{
    /**
     * only called on client side
     */
    boolean canUnveilEntity(ItemStack stack, PlayerEntity player, @Nullable Entity invisibleEntity);

    static boolean canUnveilEntity(@Nullable PlayerEntity player, @Nullable Entity invisibleEntity) {
        if (player != null) {
            ItemStack stack = player.inventory.armorInventory.get(EquipmentSlotType.HEAD.getSlotIndex() - 1);
            if (!stack.isEmpty() && stack.getItem() instanceof IEntityUnveiler) {
                return ((IEntityUnveiler<?>) stack.getItem()).canUnveilEntity(stack, player, invisibleEntity);
            }
        }
        return false;
    }
}