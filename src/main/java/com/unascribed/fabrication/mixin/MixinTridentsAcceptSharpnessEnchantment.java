package com.unascribed.fabrication.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.fabrication.support.EligibleIf;
import com.unascribed.fabrication.support.MixinConfigPlugin.RuntimeChecks;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ImpalingEnchantment;
import net.minecraft.enchantment.PowerEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Mixin(DamageEnchantment.class)
@EligibleIf(configEnabled="*.tridents_accept_sharpness")
public class MixinTridentsAcceptSharpnessEnchantment extends Enchantment {

	protected MixinTridentsAcceptSharpnessEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}

	@Inject(at=@At("HEAD"), method="canAccept(Lnet/minecraft/enchantment/Enchantment;)Z", cancellable=true)
	public void canAccept(Enchantment other, CallbackInfoReturnable<Boolean> ci) {
		if (!RuntimeChecks.check("*.tridents_accept_sharpness")) return;
		if (other instanceof PowerEnchantment || other instanceof ImpalingEnchantment) {
			ci.setReturnValue(false);
		}
	}
	
	@Inject(at=@At("HEAD"), method="isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z", cancellable=true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (RuntimeChecks.check("*.tridents_accept_sharpness") && this == Enchantments.SHARPNESS && stack.getItem() == Items.TRIDENT) {
			ci.setReturnValue(true);
		}
	}
	
}
