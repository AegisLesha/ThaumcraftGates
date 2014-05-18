package ganymedes01.thaumcraftgates.triggers;

import ganymedes01.thaumcraftgates.lib.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import buildcraft.api.gates.ITileTrigger;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.TriggerParameter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Thaumcraft Gates
 * 
 * @author ganymedes01
 * 
 */

public class AspectAmountTrigger implements ITileTrigger {

	@SideOnly(Side.CLIENT)
	private IIcon icon;

	private final String uniqueTag, description;
	private final int amount;

	public AspectAmountTrigger(int amount) {
		uniqueTag = Reference.MOD_ID + ":" + "aspectTrigger" + amount;
		description = "Aspect " + (amount > 0 ? ">= " : amount < 0 ? "<= " : "= ") + Math.abs(amount);
		this.amount = amount;
	}

	@Override
	public String getUniqueTag() {
		return uniqueTag;
	}

	@Override
	public IIcon getIcon() {
		return icon;
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		icon = reg.registerIcon(Reference.MOD_ID + ":triggers/aspectTrigger" + Math.abs(amount));
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter) {
		if (tile != null && tile instanceof IAspectContainer) {
			AspectList aspects = ((IAspectContainer) tile).getAspects();
			if (aspects != null) {
				Aspect aspect = aspects.getAspectsSortedAmount()[0];

				// If trigger has a parameter
				if (parameter != null && parameter.getItemStack() != null) {
					Item paramItem = parameter.getItemStack().getItem();
					if (paramItem != null && paramItem instanceof IEssentiaContainerItem) {
						AspectList itemAspects = ((IEssentiaContainerItem) paramItem).getAspects(parameter.getItemStack());
						if (itemAspects != null)
							aspect = itemAspects.getAspectsSortedAmount()[0];
					}
				}

				return testAspect(aspects, aspect, amount);
			}
		}
		return false;
	}

	private boolean testAspect(AspectList aspects, Aspect aspect, int amount) {
		return amount > 0 ? aspects.getAmount(aspect) >= amount : aspects.getAmount(aspect) <= Math.abs(amount);
	}

	@Override
	public boolean hasParameter() {
		return true;
	}

	@Override
	public boolean requiresParameter() {
		return false;
	}

	@Override
	public ITriggerParameter createParameter() {
		return new TriggerParameter();
	}

	@Override
	public ITrigger rotateLeft() {
		return this;
	}
}