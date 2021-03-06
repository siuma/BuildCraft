package buildcraft.transport.stripes;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import buildcraft.api.transport.IStripesActivator;
import buildcraft.api.transport.IStripesHandler;
import buildcraft.core.lib.utils.BlockUtils;

public class StripesHandlerBucket implements IStripesHandler {
	private static final ItemStack emptyBucket = new ItemStack(Items.bucket, 1);

	private ItemStack getFilledBucket(FluidStack fluidStack, Block underblock) {
		if (underblock == Blocks.lava) {
			return new ItemStack(Items.lava_bucket, 1);
		} else if (underblock == Blocks.water) {
			return new ItemStack(Items.water_bucket, 1);
		} else {
			return FluidContainerRegistry.fillFluidContainer(fluidStack, emptyBucket);
		}
	}

	@Override
	public StripesHandlerType getType() {
		return StripesHandlerType.ITEM_USE;
	}
	
	@Override
	public boolean shouldHandle(ItemStack stack) {
		return stack.getItem() instanceof ItemBucket;
	}

	@Override
	public boolean handle(World world, int x, int y, int z,
			ForgeDirection direction, ItemStack stack, EntityPlayer player,
			IStripesActivator activator) {
		Block block = world.getBlock(x, y, z);
		if (block == Blocks.air) {
			Block underblock = world.getBlock(x, y - 1, z);

			if (((ItemBucket) stack.getItem()).tryPlaceContainedLiquid(world, x, y - 1, z)) {
				activator.sendItem(emptyBucket, direction.getOpposite());
				stack.stackSize--;
				if (stack.stackSize > 0) {
					activator.sendItem(stack, direction.getOpposite());
				}
				
				return true;
			} else {
				if (!FluidContainerRegistry.isEmptyContainer(stack)) {
					activator.sendItem(stack, direction.getOpposite());
					return true;
				}

				FluidStack fluidStack = BlockUtils.drainBlock(underblock, world, x, y - 1, z, true);
				ItemStack filledBucket = getFilledBucket(fluidStack, underblock);

				if (fluidStack == null || filledBucket == null) {
					activator.sendItem(stack, direction.getOpposite());
					return true;
				}

				activator.sendItem(filledBucket, direction.getOpposite());
				stack.stackSize--;
				if (stack.stackSize > 0) {
					activator.sendItem(stack, direction.getOpposite());
				}

				return true;
			}
		}
		return false;
	}

}
