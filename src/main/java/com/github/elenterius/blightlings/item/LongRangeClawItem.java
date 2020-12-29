package com.github.elenterius.blightlings.item;

import com.github.elenterius.blightlings.init.ModAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import java.util.UUID;

public class LongRangeClawItem extends ClawWeaponItem {

	public static AttributeModifier RETRACTED_CLAW_REACH_MODIFIER = new AttributeModifier(UUID.fromString("d76adb08-2bb3-4e88-997d-766a919f0f6b"), "attack_reach_modifier", 0.5f, AttributeModifier.Operation.ADDITION);
	public static AttributeModifier EXTENDED_CLAW_REACH_MODIFIER = new AttributeModifier(UUID.fromString("29ace568-4e32-4809-840c-3c9a0e1ebcd4"), "attack_reach_modifier", 1.5f, AttributeModifier.Operation.ADDITION);

	private final Lazy<Multimap<Attribute, AttributeModifier>> lazyAttributeModifiersV2;

	public LongRangeClawItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
		super(tier, attackDamageIn, attackSpeedIn, builderIn);
		lazyAttributeModifiersV2 = Lazy.of(this::createAttributeModifiersV2);
	}

	protected Multimap<Attribute, AttributeModifier> createAttributeModifiersV2() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		Multimap<Attribute, AttributeModifier> clawAttributes = lazyAttributeModifiers.get();
		clawAttributes.forEach((attribute, attributeModifier) -> {
			if (attributeModifier != RETRACTED_CLAW_REACH_MODIFIER) {
				builder.put(attribute, attributeModifier);
			}
		});
		builder.put(ModAttributes.getAttackReachDistance(), EXTENDED_CLAW_REACH_MODIFIER);
		return builder.build();
	}

	@Override
	protected void addAdditionalAttributeModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
		super.addAdditionalAttributeModifiers(builder);
		builder.put(ModAttributes.getAttackReachDistance(), RETRACTED_CLAW_REACH_MODIFIER);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		return slot == EquipmentSlotType.MAINHAND && stack.getOrCreateTag().getInt("LongClawTimeLeft") > 0 ? lazyAttributeModifiersV2.get() : super.getAttributeModifiers(slot, stack);
	}

	public void onCriticalHitEntity(ItemStack stack, LivingEntity attacker, LivingEntity target) {
		super.onCriticalHitEntity(stack, attacker, target);
		if (!attacker.world.isRemote()) {
			stack.getOrCreateTag().putInt("LongClawTimeLeft", 60);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote() && worldIn.getGameTime() % 20L == 0L) {
			CompoundNBT nbt = stack.getOrCreateTag();
			int timeLeft = nbt.getInt("LongClawTimeLeft");
			if (timeLeft > 0) {
				nbt.putInt("LongClawTimeLeft", timeLeft - 1);
			}
		}
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
}