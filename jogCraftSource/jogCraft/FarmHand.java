package jogCraft;

import org.bukkit.*;
import org.bukkit.block.data.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;

public class FarmHand implements Listener
{
	FarmHand(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getBlockData() instanceof Ageable ageable &&
			event.getItem() == null && event.getHand().equals(EquipmentSlot.HAND))
		{
			Material drop = getDrop(event.getClickedBlock().getType());
			if (drop != null && ageable.getAge() == ageable.getMaximumAge())
			{
				ageable.setAge(0);
				event.getClickedBlock().setBlockData(ageable);
				Location location = event.getClickedBlock().getLocation();
				location.add(.5, 1, .5);
				location.getWorld().dropItem(location, new ItemStack(drop));
			}
		}
	}
	
	static final Material[][] plants = { //block type, drop type
		{Material.WHEAT, Material.WHEAT},
		{Material.BEETROOTS, Material.BEETROOT},
		{Material.CARROTS, Material.CARROT},
		{Material.POTATOES, Material.POTATO},
		{Material.COCOA, Material.COCOA_BEANS}
	};
	
	static Material getDrop(Material block)
	{
		for (Material[] plant : plants)
		{
			if (block.equals(plant[0]))
				return plant[1];
		}
		return null;
	}
}