package buildcraft.builders;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import buildcraft.BuildCraftBuilders;
import buildcraft.api.library.ILibraryTypeHandler;
import buildcraft.core.blueprints.BlueprintBase;
import buildcraft.core.blueprints.LibraryId;

public class LibraryBlueprintTypeHandler implements ILibraryTypeHandler {
	private final boolean isBlueprint;

	public LibraryBlueprintTypeHandler(boolean isBlueprint) {
		this.isBlueprint = isBlueprint;
	}

	@Override
	public boolean isHandler(ItemStack stack, boolean store) {
		if (isBlueprint) {
			return stack.getItem() instanceof ItemBlueprintStandard;
		} else {
			return stack.getItem() instanceof ItemBlueprintTemplate;
		}
	}

	@Override
	public String getFileExtension() {
		return isBlueprint ? "bpt" : "tpl";
	}

	@Override
	public int getTextColor() {
		return isBlueprint ? 0x305080 : 0;
	}

	@Override
	public String getName(ItemStack stack) {
		return ItemBlueprint.getId(stack).name;
	}

	@Override
	public ItemStack load(ItemStack stack, NBTTagCompound compound) {
		BlueprintBase blueprint = BlueprintBase.loadBluePrint((NBTTagCompound) compound.copy());
		blueprint.id.name = compound.getString("__filename");
		blueprint.id.extension = getFileExtension();
		BuildCraftBuilders.serverDB.add(blueprint.id, compound);
		return blueprint.getStack();
	}

	@Override
	public boolean store(ItemStack stack, NBTTagCompound compound) {
		LibraryId id = ItemBlueprint.getId(stack);
		if (id == null) {
			return false;
		}

		NBTTagCompound nbt = BuildCraftBuilders.serverDB.load(id);
		if (nbt == null) {
			return false;
		}

		for (Object o : nbt.func_150296_c()) {
			compound.setTag((String) o, nbt.getTag((String) o));
		}
		id.write(compound);
		return true;
	}
}
