/**
 * Copyright (c) 2011-2015, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.silicon.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import buildcraft.core.lib.gui.BuildCraftContainer;
import buildcraft.core.lib.gui.slots.SlotOutput;
import buildcraft.core.lib.gui.slots.SlotValidated;
import buildcraft.silicon.TileStampingTable;

public class ContainerStampingTable extends BuildCraftContainer {

	private TileStampingTable table;

	public ContainerStampingTable(InventoryPlayer playerInventory, TileStampingTable table) {
		super(table.getSizeInventory());
		this.table = table;

		addSlot(new SlotValidated(table, 0, 15, 18));
		addSlot(new SlotOutput(table, 1, 143, 18));

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 50 + y * 18));
			}
		}

		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 108));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return table.isUseableByPlayer(var1);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); i++) {
			table.sendGUINetworkData(this, (ICrafting) crafters.get(i));
		}
	}

	@Override
	public void updateProgressBar(int par1, int par2) {
		table.getGUINetworkData(par1, par2);
	}
}
