/*
 * Copyright (c) 2019 AlexIIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package alexiil.mc.lib.attributes.item.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.ActionResult;

import alexiil.mc.lib.attributes.item.mixin.HopperHooks;

/**
 * Mixin for the hopper block entity to add support for LBA insertables and extractables.
 * This will also add LBA extraction support to the hopper minecart.
 */
@SuppressWarnings("ConstantConditions")
@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @Inject(method = "insert", at = @At("HEAD"), cancellable = true, require = 1, allow = 1)
    private void onInsert(CallbackInfoReturnable<Boolean> cri) {
        HopperBlockEntity self = (HopperBlockEntity) (Object) this;
        ActionResult result = HopperHooks.tryInsert(self);
        if (result != ActionResult.PASS) {
            cri.setReturnValue(result.isAccepted());
        }
    }

    @Inject(method = "extract", at = @At("HEAD"), cancellable = true, require = 1, allow = 1)
    private static void onExtract(Hopper hopper, CallbackInfoReturnable<Boolean> cri) {
        ActionResult result = HopperHooks.tryExtract(hopper);
        if (result != ActionResult.PASS) {
            cri.setReturnValue(result.isAccepted());
        }
    }

}
