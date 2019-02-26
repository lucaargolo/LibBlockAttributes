package alexiil.mc.lib.attributes.item.impl;

import net.minecraft.item.ItemStack;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.IFixedItemInv;
import alexiil.mc.lib.attributes.item.IFixedItemInvView;
import alexiil.mc.lib.attributes.item.IInvSlotChangeListener;
import alexiil.mc.lib.attributes.item.IItemExtractable;
import alexiil.mc.lib.attributes.item.IItemInsertable;
import alexiil.mc.lib.attributes.item.IItemInvStats;
import alexiil.mc.lib.attributes.item.filter.IItemFilter;

/** An {@link IFixedItemInv} with no slots. Because this inventory is unmodifiable this also doubles as the empty
 * implementation for {@link IFixedItemInvView}. */
public enum EmptyFixedItemInv implements IFixedItemInv {
    INSTANCE;

    private static IllegalArgumentException throwInvalidSlotException() {
        throw new IllegalArgumentException("There are no valid slots in this empty inventory!");
    }

    @Override
    public int getInvSize() {
        return 0;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        throw throwInvalidSlotException();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        throw throwInvalidSlotException();
    }

    @Override
    public IItemFilter getFilterForSlot(int slot) {
        throw throwInvalidSlotException();
    }

    @Override
    public int getMaxAmount(int slot, ItemStack stack) {
        throw throwInvalidSlotException();
    }

    @Override
    public IItemInvStats getStatistics() {
        return EmptyItemInvStats.INSTANCE;
    }

    @Override
    public IListenerToken addListener(IInvSlotChangeListener listener) {
        // We don't need to keep track of the listener because this empty inventory never changes.
        return () -> {};
    }

    @Override
    public boolean setInvStack(int slot, ItemStack to, Simulation simulation) {
        throw throwInvalidSlotException();
    }

    @Override
    public IFixedItemInvView getView() {
        return this;
    }

    @Override
    public IItemInsertable getInsertable() {
        return RejectingItemInsertable.NULL_INSERTABLE;
    }

    @Override
    public IItemInsertable getInsertable(int[] slots) {
        if (slots.length == 0) {
            return RejectingItemInsertable.NULL_INSERTABLE;
        }
        throw throwInvalidSlotException();
    }

    @Override
    public IItemExtractable getExtractable() {
        return EmptyItemExtractable.NULL_EXTRACTABLE;
    }

    @Override
    public IItemExtractable getExtractable(int[] slots) {
        if (slots.length == 0) {
            return EmptyItemExtractable.NULL_EXTRACTABLE;
        }
        throw throwInvalidSlotException();
    }
}