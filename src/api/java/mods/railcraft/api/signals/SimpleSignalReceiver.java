/*
 * ******************************************************************************
 *  Copyright 2011-2015 CovertJaguar
 *
 *  This work (the API) is licensed under the "MIT" License, see LICENSE.md for details.
 * ***************************************************************************
 */
package mods.railcraft.api.signals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class SimpleSignalReceiver extends SignalReceiver {
    private SignalAspect aspect = SignalAspect.BLINK_RED;

    public SimpleSignalReceiver(String locTag, TileEntity tile) {
        super(locTag, tile, 1);
    }

    public SignalAspect getAspect() {
        return aspect;
    }

    public void setAspect(SignalAspect aspect) {
        this.aspect = aspect;
    }

    @Override
    public void onControllerAspectChange(SignalController con, SignalAspect aspect) {
        if (this.aspect != aspect) {
            this.aspect = aspect;
            super.onControllerAspectChange(con, aspect);
        }
    }

    @Override
    protected void saveNBT(NBTTagCompound data) {
        super.saveNBT(data);
        data.setByte("aspect", (byte) aspect.ordinal());
    }

    @Override
    protected void loadNBT(NBTTagCompound data) {
        super.loadNBT(data);
        aspect = SignalAspect.values()[data.getByte("aspect")];
    }

    public void writePacketData(DataOutputStream data) throws IOException {
        data.writeByte(aspect.ordinal());
    }

    public void readPacketData(DataInputStream data) throws IOException {
        aspect = SignalAspect.values()[data.readByte()];
    }

    @Override
    public String toString() {
        return String.format("Receiver:%s (%s)", aspect,  super.toString());
    }
}
