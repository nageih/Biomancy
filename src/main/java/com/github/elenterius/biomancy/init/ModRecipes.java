package com.github.elenterius.biomancy.init;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.recipe.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModRecipes {

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BiomancyMod.MOD_ID);

	public static final RegistryObject<SpecialRecipeSerializer<PotionBeetleRecipe>> CRAFTING_SPECIAL_POTION_BEETLE = RECIPE_SERIALIZERS.register("crafting_special_potion_beetle", () -> new SpecialRecipeSerializer<>(PotionBeetleRecipe::new));
	public static final RegistryObject<SpecialRecipeSerializer<MasonBeetleRecipe>> CRAFTING_SPECIAL_MASON_BEETLE = RECIPE_SERIALIZERS.register("crafting_special_mason_beetle", () -> new SpecialRecipeSerializer<>(MasonBeetleRecipe::new));
	public static final RegistryObject<SpecialRecipeSerializer<SewingKitRepairRecipe>> REPAIR_SPECIAL_SEWING_KIT = RECIPE_SERIALIZERS.register("repair_special_sewing_kit", () -> new SpecialRecipeSerializer<>(SewingKitRepairRecipe::new));
	public static final RegistryObject<SpecialRecipeSerializer<AddUserToAccessKeyRecipe>> CRAFTING_SPECIAL_ADD_USER_TO_KEY = RECIPE_SERIALIZERS.register("crafting_special_add_user_to_key", () -> new SpecialRecipeSerializer<>(AddUserToAccessKeyRecipe::new));

	public static final RegistryObject<IRecipeSerializer<ChewerRecipe>> CHEWER_SERIALIZER = RECIPE_SERIALIZERS.register("chewing", ChewerRecipe.Serializer::new);
	public static final RegistryObject<IRecipeSerializer<DigesterRecipe>> DIGESTER_SERIALIZER = RECIPE_SERIALIZERS.register("digesting", DigesterRecipe.Serializer::new);
	public static final RegistryObject<IRecipeSerializer<DecomposerRecipe>> DECOMPOSING_SERIALIZER = RECIPE_SERIALIZERS.register("decomposing", DecomposerRecipe.Serializer::new);
	public static final RegistryObject<IRecipeSerializer<EvolutionPoolRecipe>> EVOLUTION_POOL_SERIALIZER = RECIPE_SERIALIZERS.register("evolution_pool", EvolutionPoolRecipe.Serializer::new);

	public static final IRecipeType<ChewerRecipe> CHEWER_RECIPE_TYPE = createRecipeType("chewing");
	public static final IRecipeType<DigesterRecipe> DIGESTER_RECIPE_TYPE = createRecipeType("digesting");
	public static final IRecipeType<DecomposerRecipe> DECOMPOSING_RECIPE_TYPE = createRecipeType("decomposing");
	public static final IRecipeType<EvolutionPoolRecipe> EVOLUTION_POOL_RECIPE_TYPE = createRecipeType("evolution_pool");

	public static final ItemPredicate ANY_MEATLESS_FOOD_ITEM_PREDICATE = new ItemPredicate() {
		@Override
		public boolean test(ItemStack stack) {
			Item item = stack.getItem();
			return item.isFood() && item.getFood() != null && !item.getFood().isMeat();
		}

		@Override
		public JsonElement serialize() {
			return JsonNull.INSTANCE;
		}
	};

	private ModRecipes() {}

	public static void registerComposterRecipes() {
		ComposterBlock.CHANCES.putIfAbsent(ModItems.DIGESTATE.get(), 0.7f);
	}

	public static void registerCustomItemPredicates() {
		ItemPredicate.register(BiomancyMod.createRL("any_meatless_food"), jsonObject -> ANY_MEATLESS_FOOD_ITEM_PREDICATE);
	}

	public static void registerRecipeTypes() {
		Registry.register(Registry.RECIPE_TYPE, BiomancyMod.createRL("chewing"), CHEWER_RECIPE_TYPE);
		Registry.register(Registry.RECIPE_TYPE, BiomancyMod.createRL("digesting"), DIGESTER_RECIPE_TYPE);
		Registry.register(Registry.RECIPE_TYPE, BiomancyMod.createRL("decomposing"), DECOMPOSING_RECIPE_TYPE);
		Registry.register(Registry.RECIPE_TYPE, BiomancyMod.createRL("evolution_pool"), EVOLUTION_POOL_RECIPE_TYPE);
	}

	private static <T extends IRecipe<?>> IRecipeType<T> createRecipeType(String name) {
		return new IRecipeType<T>() {
			@Override
			public String toString() {
				return name;
			}
		};
	}

}