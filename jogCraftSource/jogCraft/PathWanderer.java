package jogCraft;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

public class PathWanderer implements Runnable
{
	static final long tickInterval = 5;
	static final Material[] pathBlocks = {
		Material.DIRT_PATH, Material.MUD_BRICK_SLAB, Material.MUD_BRICK_STAIRS, Material.MUD_BRICKS
	};
	static final Material[] conditionalPaths = {//path block, material required beneath
		Material.OAK_STAIRS,
		Material.OAK_SLAB,
		Material.JUNGLE_STAIRS,
		Material.JUNGLE_SLAB,
	};
	static final Material[] roadFoundations = {Material.DIRT, Material.GRASS_BLOCK};
	static final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, (int)tickInterval + 1, 1, true, false, false);
	
	static boolean isPath(Block block)
	{
		if (!block.getRelative(BlockFace.UP).getType().isAir())
			return false;
		Material material = block.getType();
		Material materialBelow = block.getRelative(BlockFace.DOWN).getType();
		for (Material pathBlock : pathBlocks)
		{
			if (material.equals(pathBlock))
				return true;
		}
		for (Material conditionalPath : conditionalPaths)
		{
			if (material.equals(conditionalPath) && isRoadFoundation(materialBelow))
				return true;
		}
		return false;
	}
	
	static boolean isRoadFoundation(Material material)
	{
		for (Material roadFoundation : roadFoundations)
			if (roadFoundation.equals(material))
				return true;
		return false;
	}
	
	static void applySpeed(LivingEntity entity)
	{
		entity.addPotionEffect(speed);
	}
	
	static void wander(Entity entity)
	{
		if (entity instanceof LivingEntity)
		{
			Location location = entity.getLocation();
			
			Block block1 = location.getBlock(); //if player is on dirt-Path, stairs, or slabs
			Block block2 = location.getBlock().getRelative(BlockFace.DOWN); //if player is standing on solid block
			Block block3 = location.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN); //if player is jumping along the path
			
			if (isPath(block1) || isPath(block2) || isPath(block3))
				applySpeed((LivingEntity)entity);
		}
	}
	
	@Override
	public void run()
	{
		Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(PathWanderer::wander));
	}
}