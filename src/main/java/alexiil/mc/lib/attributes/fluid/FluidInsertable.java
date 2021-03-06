/*
 * Copyright (c) 2019 AlexIIL
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package alexiil.mc.lib.attributes.fluid;

import javax.annotation.Nullable;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.ConstantFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.FluidFilter;
import alexiil.mc.lib.attributes.fluid.impl.FilteredFluidInsertable;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.LimitedConsumer;

/** Defines an object that can have fluids inserted into it. */
@FunctionalInterface
public interface FluidInsertable extends LimitedConsumer<FluidVolume> {

    /** Inserts the given stack into this insertable, and returns the excess.
     * 
     * @param fluid The incoming fluid. Must not be modified by this call.
     * @param simulation If {@link Simulation#SIMULATE} then this shouldn't modify anything.
     * @return the excess {@link FluidVolume} that wasn't accepted. This will be independent of this insertable, however
     *         it might be the given object instead of a completely new object. */
    FluidVolume attemptInsertion(FluidVolume fluid, Simulation simulation);

    /** @deprecated This is an override for {@link LimitedConsumer}, for the full javadoc you probably want to call
     *             {@link #attemptInsertion(FluidVolume, Simulation)} directly. */
    @Override
    @Deprecated // Not for removal
    default boolean offer(FluidVolume fluid, Simulation simulation) {
        return attemptInsertion(fluid, simulation).isEmpty();
    }

    /** @deprecated This is an override for {@link LimitedConsumer}, for the full javadoc you probably want to call
     *             {@link #insert(FluidVolume)} directly. */
    @Override
    @Deprecated // Not for removal
    default boolean offer(FluidVolume object) {
        return insert(object).isEmpty();
    }

    /** @deprecated This is an override for {@link LimitedConsumer}, for the full javadoc you probably want to call
     *             {@link #attemptInsertion(FluidVolume, Simulation) attemptInsertion}(FluidVolume, Simulation.SIMULATE)
     *             directly. */
    @Override
    @Deprecated // Not for removal
    default boolean wouldAccept(FluidVolume object) {
        return attemptInsertion(object, Simulation.SIMULATE).isEmpty();
    }

    /** Inserts the given stack into this insertable, and returns the excess.
     * <p>
     * This is equivalent to calling {@link #attemptInsertion(FluidVolume, Simulation)} with a {@link Simulation}
     * parameter of {@link Simulation#ACTION ACTION}.
     * 
     * @param fluid The incoming fluid. Must not be modified by this call.
     * @return the excess {@link FluidVolume} that wasn't accepted. This will be independent of this insertable, however
     *         it might be the given stack instead of a completely new object. */
    default FluidVolume insert(FluidVolume fluid) {
        return attemptInsertion(fluid, Simulation.ACTION);
    }

    // Unfortunately there's no way (at all) to keep both methods
    // due to it being a default method to begin with - we can't
    // make them call each other and expect implementations to
    // override one of them, because they don't at the moment.

    // /** @return The minimum amount of fluid that {@link #attemptInsertion(FluidVolume, Simulation)} will actually
    // * accept. Note that this only provides a guarantee that {@link FluidVolume fluid volumes} with an
    // * {@link FluidVolume#getAmount() amount} less than this will never be accepted.
    // * @deprecated Replaced by {@link #getMinimumAcceptedAmount_F()} */
    // default int getMinimumAcceptedAmount() {
    // return 1;
    // }

    /** @return The minimum amount of fluid that {@link #attemptInsertion(FluidVolume, Simulation)} will actually
     *         accept. Note that this only provides a guarantee that {@link FluidVolume fluid volumes} with an
     *         {@link FluidVolume#getAmount() amount} less than this will never be accepted.
     *         <p>
     *         A null return value indicates that there is no minimum value. */
    @Nullable
    default FluidAmount getMinimumAcceptedAmount() {
        return null;
    }

    /** Returns an {@link FluidFilter} to determine if {@link #attemptInsertion(FluidVolume, Simulation)} could ever
     * accept a fluid. The default implementation is to return {@link ConstantFluidFilter#ANYTHING}, and so it is
     * recommended that custom insertables override this to return a more accurate filter.
     * 
     * @return A filter to determine if the given fluid could ever be inserted. */
    default FluidFilter getInsertionFilter() {
        return ConstantFluidFilter.ANYTHING;
    }

    /** @return A new {@link FluidInsertable} that has an additional filter applied to the fluid inserted into it. */
    default FluidInsertable filtered(FluidFilter filter) {
        return new FilteredFluidInsertable(this, filter);
    }

    /** @return An object that only implements {@link FluidInsertable}, and does not expose any of the other
     *         modification methods that sibling or subclasses offer (like {@link FluidExtractable} or
     *         {@link GroupedFluidInv}. */
    default FluidInsertable getPureInsertable() {
        final FluidInsertable delegate = this;
        return new FluidInsertable() {
            @Override
            public FluidVolume attemptInsertion(FluidVolume fluid, Simulation simulation) {
                return delegate.attemptInsertion(fluid, simulation);
            }

            @Override
            public FluidFilter getInsertionFilter() {
                return delegate.getInsertionFilter();
            }
        };
    }
}
