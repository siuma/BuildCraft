/**
 * Copyright (c) 2011-2015, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport.recipes;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.item.ItemStack;
import buildcraft.BuildCraftTransport;
import buildcraft.api.recipes.CraftingResult;
import buildcraft.core.lib.inventory.StackHelper;
import buildcraft.silicon.ItemRedstoneChipset;
import buildcraft.silicon.TileIntegrationTable;
import buildcraft.silicon.recipes.IntegrationTableRecipe;
import buildcraft.transport.gates.GateDefinition.GateLogic;
import buildcraft.transport.gates.GateDefinition.GateMaterial;
import buildcraft.transport.gates.ItemGate;

public class GateLogicSwapRecipe extends IntegrationTableRecipe {

	public GateLogicSwapRecipe(String id) {
		setContents(id, BuildCraftTransport.pipeGate, 20000, 0);
	}

	@Override
	public boolean isValidInputA(ItemStack inputA) {
		return inputA != null && inputA.getItem() instanceof ItemGate && ItemGate.getMaterial(inputA) != GateMaterial.REDSTONE;
	}

	@Override
	public boolean isValidInputB(ItemStack inputB) {
		return StackHelper.isMatchingItem(inputB, ItemRedstoneChipset.Chipset.RED.getStack());
	}

	@Override
	public CraftingResult<ItemStack> craft(TileIntegrationTable crafter, boolean preview, ItemStack inputA,
			ItemStack inputB) {
		CraftingResult<ItemStack> result = super.craft(crafter, preview, inputA, inputB);

		if (result == null) {
			return null;
		}

		ItemStack output = inputA;

		output.stackSize = 1;
		ItemGate.setLogic(output, ItemGate.getLogic(output) == GateLogic.AND ? GateLogic.OR : GateLogic.AND);

		result.crafted = output;

		return result;
	}

	@Override
	public Collection<Object> getInputs() {
		ArrayList<Object> inputs = new ArrayList<Object>();

		inputs.add(ItemGate.getAllGates());
		inputs.add(ItemRedstoneChipset.Chipset.RED.getStack());

		return inputs;
	}

	@Override
	public Collection<Object> getOutput() {
		ArrayList<Object> gates = new ArrayList<Object>();
		for (ItemStack stack : ItemGate.getAllGates()) {
			ItemStack newStack = stack.copy();
			ItemGate.setLogic(newStack, ItemGate.getLogic(newStack) == GateLogic.AND ? GateLogic.OR : GateLogic.AND);
			gates.add(newStack);
		}

		return gates;
	}
}
