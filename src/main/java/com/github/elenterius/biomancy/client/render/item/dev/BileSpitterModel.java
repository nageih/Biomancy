package com.github.elenterius.biomancy.client.render.item.dev;

import com.github.elenterius.biomancy.BiomancyMod;
import com.github.elenterius.biomancy.world.item.weapon.BileSpitterItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BileSpitterModel extends AnimatedGeoModel<BileSpitterItem> {

	@Override
	public ResourceLocation getModelResource(BileSpitterItem item) {
		return BiomancyMod.createRL("geo/item/bile_spitter.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BileSpitterItem item) {
		return BiomancyMod.createRL("textures/item/weapon/bile_spitter.png");
	}

	@Override
	public ResourceLocation getAnimationResource(BileSpitterItem item) {
		return BiomancyMod.createRL("animations/item/bile_spitter.animation.json");
	}

}