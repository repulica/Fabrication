package com.unascribed.fabrication.mixin.a_fixes.silverfish_step;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.fabrication.support.EligibleIf;
import com.unascribed.fabrication.support.MixinConfigPlugin;
import net.minecraft.entity.mob.SilverfishEntity;

@Mixin(SilverfishEntity.class)
@EligibleIf(configAvailable="*.silverfish_step")
public class MixinSilverfishEntity {

	// this method is poorly mapped. a better name is "hasStepSound"
	@Inject(at=@At("TAIL"), method="getMoveEffect()Lnet/minecraft/entity/Entity$MoveEffect;", cancellable=true)
	public void canClimb(CallbackInfoReturnable<Entity.MoveEffect> cir) {
		if (MixinConfigPlugin.isEnabled("*.silverfish_step")) cir.setReturnValue(Entity.MoveEffect.ALL);
	}

}
