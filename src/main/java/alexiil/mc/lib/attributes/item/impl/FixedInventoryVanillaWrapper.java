package alexiil.mc.lib.attributes.item.impl;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.IFixedItemInv;

/** An {@link IFixedItemInv} that wraps a vanilla {@link Inventory}. */
public final class FixedInventoryVanillaWrapper extends FixedInventoryViewVanillaWrapper implements IFixedItemInv {

    public FixedInventoryVanillaWrapper(Inventory inv) {
        super(inv);
    }

    @Override
    public boolean setInvStack(int slot, ItemStack to, Simulation simulation) {
        if (isItemValidForSlot(slot, to)) {
            if (simulation == Simulation.ACTION) {
                inv.setInvStack(slot, to);
            }
            return true;
        }
        return false;
    }
}