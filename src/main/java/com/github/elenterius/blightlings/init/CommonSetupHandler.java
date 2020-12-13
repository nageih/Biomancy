package com.github.elenterius.blightlings.init;

import com.github.elenterius.blightlings.BlightlingsMod;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = BlightlingsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class CommonSetupHandler
{
    @SubscribeEvent
    public static void onSetup(final FMLCommonSetupEvent event) {
        // do stuff after common setup event on single thread
        event.enqueueWork(() -> {
            ModEntityTypes.onPostSetup();
            ModFeatures.injectCarvableBlocks();
            ModBiomes.onPostSetupBiomes();
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegisterEntityType(final RegistryEvent.Register<EntityType<?>> event) {
        DefaultDispenseItemBehavior behavior = new DefaultDispenseItemBehavior()
        {
            @Override
            protected ItemStack dispenseStack(IBlockSource iBlockSource, ItemStack stack) {
                EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                Direction direction = iBlockSource.getBlockState().get(DispenserBlock.FACING);
                entityType.spawn(iBlockSource.getWorld(), stack, null, iBlockSource.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };

        //hacky fix for spawn eggs and deferred entity types
        BlightlingsMod.LOGGER.info("Injecting EntityType into SpawnEggs...");
        final Map<EntityType<?>, SpawnEggItem> EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "field_195987_b");
        Objects.requireNonNull(EGGS);
        for (RegistryObject<Item> entry : ModItems.ITEMS.getEntries()) {
            if (entry.get() instanceof SpawnEggItem) {
                SpawnEggItem item = (SpawnEggItem) entry.get();
                EGGS.put(item.getType(null), item);
                DispenserBlock.registerDispenseBehavior(item, behavior);
            }
        }
    }
}
