package com.ngames.nclient.mixin;

import static com.ngames.nclient.NClient.MC;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ngames.nclient.module.Modules;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

@Mixin(ModifiableAttributeInstance.class)
public class MixinModifiableAttributeInstance {

    @Shadow
    private double baseValue;

    @Inject(method = "applyModifier", at = @At("TAIL"), cancellable = true)
    private void applyModifier(AttributeModifier modifier, CallbackInfo info) {
        if (Modules.strafe.isEnabled()) {
            IAttributeInstance iatt = MC.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (!iatt.hasModifier(Modules.strafe.strafe))
                iatt.applyModifier(Modules.strafe.strafe);
            iatt.removeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
        }
    }

    @Inject(method = "removeModifier(Ljava/util/UUID;)V", at = @At("TAIL"), cancellable = true)
    private void removeModifier(UUID p_188479_1_, CallbackInfo info) {
        if (Modules.strafe.isEnabled()) {
            IAttributeInstance iatt = MC.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (!iatt.hasModifier(Modules.strafe.strafe))
                iatt.applyModifier(Modules.strafe.strafe);
            iatt.removeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
        }
    }

    @Inject(method = "removeModifier(Lnet/minecraft/entity/ai/attributes/AttributeModifier;)V", at = @At("TAIL"), cancellable = true)
    private void removeModifier(AttributeModifier modifier, CallbackInfo info) {
        if (Modules.strafe.isEnabled()) {
            IAttributeInstance iatt = MC.player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (!iatt.hasModifier(Modules.strafe.strafe))
                iatt.applyModifier(Modules.strafe.strafe);
            iatt.removeModifier(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
        }
    }
}
