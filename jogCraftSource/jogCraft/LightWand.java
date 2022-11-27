package jogCraft;

import jogLib.customContent.*;
import jogUtil.data.*;
import jogUtil.data.values.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.*;

import java.util.*;

public class LightWand extends CustomItemType<LightWand.LightWandItem>
{
	enum SpawnTime
	{
		NIGHT, ALWAYS, NEVER
	}
	
	public static final int radius = 20;
	
	static void visualize(Player player, boolean highlight)
	{
		Location location = player.getLocation();
		for (int x = location.getBlockX() - radius; x < location.getBlockX() + radius; x++)
			for (int y = location.getBlockY() - radius; y < location.getBlockY() + radius; y++)
				for (int z = location.getBlockZ() - radius; z < location.getBlockZ() + radius; z++)
				{
					visualizeBlock(location.getWorld().getBlockAt(x, y, z), player, highlight);
				}
	}
	
	static void visualizeBlock(Block block, Player player, boolean highlight)
	{
		SpawnTime time = SpawnTime.NEVER;
		if (empty(block) && empty(block.getRelative(BlockFace.UP)) && solid(block.getRelative(BlockFace.DOWN)))
		{
			if (block.getLightFromBlocks() == 0)
			{
				if (block.getLightFromSky() == 0)
					time = SpawnTime.ALWAYS;
				else
					time = SpawnTime.NIGHT;
			}
		}
		
		if (highlight)
		{
		
		}
		else
		{
			Particle particle;
			if (time.equals(SpawnTime.ALWAYS))
				particle = Particle.FLAME;
			else if (time.equals(SpawnTime.NIGHT))
				particle = Particle.SMOKE_NORMAL;
			else
				return;
			
			Location location = new Location(block.getWorld(), (double)block.getX() + .5, block.getY(), (double)block.getZ() + .5);
			player.spawnParticle(particle, location, 1, 0, 0, 0, 0);
		}
	}
	
	static class Visualizer implements Runnable
	{
		static final long tickInterval = 10;
		
		@Override
		public void run()
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				ItemStack[] items = new ItemStack[2];
				items[0] = player.getEquipment().getItem(EquipmentSlot.HAND);
				items[1] = player.getEquipment().getItem(EquipmentSlot.OFF_HAND);
				for (int index = 0; index < 2; index++)
				{
					if (CustomItemType.isCustomItem(items[index]))
					{
						CustomItem item = CustomItemType.getCustomObject(items[index]);
						if (item instanceof LightWandItem)
						{
							visualize(player, ((LightWandItem) item).highLight());
						}
					}
				}
			}
		}
	}
	
	static boolean empty(Block block)
	{
		if (block.getType().isSolid())
			return false;
		return !block.getType().equals(Material.WATER) && !block.getType().equals(Material.LAVA);
	}
	
	static boolean solid(Block block)
	{
		if ((block.getType().isSolid() && block.getType().isOccluding()) || block.getType().equals(Material.SOUL_SAND))
			return true;
		else
		{
			return (isTrapdoor(block.getType()) || isStairs(block.getType())) && ((Bisected) block.getBlockData()).getHalf().equals(Bisected.Half.TOP);
		}
	}
	
	static final Material[] trapdoors = {
			Material.IRON_TRAPDOOR, Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR,
			Material.DARK_OAK_TRAPDOOR, Material.MANGROVE_TRAPDOOR, Material.CRIMSON_TRAPDOOR, Material.WARPED_TRAPDOOR
	};
	
	static boolean isTrapdoor(Material material)
	{
		for (Material trapdoor : trapdoors)
			if (material.equals(trapdoor))
				return true;
		return false;
	}
	
	static boolean isSlime(Material material)
	{
		return material.equals(Material.SLIME_BLOCK) || material.equals(Material.HONEY_BLOCK);
	}
	
	static final Material[] slabs = {
			Material.PETRIFIED_OAK_SLAB, Material.OAK_SLAB, Material.SPRUCE_SLAB, Material.BIRCH_SLAB, Material.JUNGLE_SLAB, Material.ACACIA_SLAB,
			Material.DARK_OAK_SLAB, Material.MANGROVE_SLAB, Material.CRIMSON_SLAB, Material.WARPED_SLAB,
			
			Material.CUT_COPPER_SLAB, Material.EXPOSED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER_SLAB,
			Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_EXPOSED_CUT_COPPER_SLAB, Material.WAXED_WEATHERED_CUT_COPPER_SLAB, Material.WAXED_OXIDIZED_CUT_COPPER_SLAB,
			
			Material.STONE_SLAB, Material.SMOOTH_STONE_SLAB, Material.SANDSTONE_SLAB, Material.CUT_SANDSTONE_SLAB, Material.COBBLESTONE_SLAB, Material.BRICK_SLAB, Material.STONE_BRICK_SLAB,
			Material.MUD_BRICK_SLAB, Material.NETHER_BRICK_SLAB, Material.QUARTZ_SLAB, Material.RED_SANDSTONE_SLAB, Material.CUT_RED_SANDSTONE_SLAB, Material.PURPUR_SLAB,
			Material.PRISMARINE_SLAB, Material.PRISMARINE_BRICK_SLAB, Material.DARK_PRISMARINE_SLAB,
			
			Material.POLISHED_GRANITE_SLAB, Material.SMOOTH_RED_SANDSTONE_SLAB, Material.MOSSY_STONE_BRICK_SLAB, Material.POLISHED_DIORITE_SLAB, Material.MOSSY_COBBLESTONE_SLAB,
			Material.END_STONE_BRICK_SLAB, Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_QUARTZ_SLAB, Material.GRANITE_SLAB, Material.ANDESITE_SLAB, Material.RED_NETHER_BRICK_SLAB,
			Material.POLISHED_DIORITE_SLAB, Material.DIORITE_SLAB, Material.COBBLED_DEEPSLATE_SLAB, Material.POLISHED_DEEPSLATE_SLAB, Material.DEEPSLATE_BRICK_SLAB, Material.DEEPSLATE_TILE_SLAB,
			
			Material.BLACKSTONE_SLAB, Material.POLISHED_BLACKSTONE_SLAB, Material.POLISHED_BLACKSTONE_BRICK_SLAB
	};
	
	static boolean isSlab(Material material)
	{
		for (Material slab : slabs)
			if (material.equals(slab))
				return true;
		return false;
	}
	
	static final Material[] stairs = {
			Material.OAK_STAIRS, Material.SPRUCE_STAIRS, Material.BIRCH_STAIRS, Material.JUNGLE_STAIRS, Material.ACACIA_STAIRS,
			Material.DARK_OAK_STAIRS, Material.MANGROVE_STAIRS, Material.CRIMSON_STAIRS, Material.WARPED_STAIRS,
			
			Material.CUT_COPPER_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS, Material.WEATHERED_CUT_COPPER_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS,
			Material.WAXED_CUT_COPPER_STAIRS, Material.WAXED_EXPOSED_CUT_COPPER_STAIRS, Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS,
			
			Material.STONE_STAIRS, Material.SANDSTONE_STAIRS, Material.COBBLESTONE_STAIRS, Material.BRICK_STAIRS, Material.STONE_BRICK_STAIRS,
			Material.MUD_BRICK_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.PURPUR_STAIRS,
			Material.PRISMARINE_STAIRS, Material.PRISMARINE_BRICK_STAIRS, Material.DARK_PRISMARINE_STAIRS,
			
			Material.POLISHED_GRANITE_STAIRS, Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS, Material.POLISHED_DIORITE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS,
			Material.END_STONE_BRICK_STAIRS, Material.SMOOTH_SANDSTONE_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.GRANITE_STAIRS, Material.ANDESITE_STAIRS, Material.RED_NETHER_BRICK_STAIRS,
			Material.POLISHED_DIORITE_STAIRS, Material.DIORITE_STAIRS, Material.COBBLED_DEEPSLATE_STAIRS, Material.POLISHED_DEEPSLATE_STAIRS, Material.DEEPSLATE_BRICK_STAIRS, Material.DEEPSLATE_TILE_STAIRS,
			
			Material.BLACKSTONE_STAIRS, Material.POLISHED_BLACKSTONE_STAIRS, Material.POLISHED_BLACKSTONE_BRICK_STAIRS
	};
	
	static boolean isStairs(Material material)
	{
		for (Material stair : stairs)
			if (material.equals(stair))
				return true;
		return false;
	}
	
	LightWand(Plugin plugin)
	{
		super(new NamespacedKey(plugin, "LightWand"), Material.STICK, "Light Wand");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Visualizer(), 0, Visualizer.tickInterval);
	}
	
	@Override
	protected void playerInteract(LightWandItem item, Player player, Block block, Action action, BlockFace face, EquipmentSlot slot, PlayerInteractEvent event)
	{
		if (player.isSneaking() && slot.equals(EquipmentSlot.HAND))
			item.setHighlight(!item.highLight());
	}
	
	@Override
	protected void configureMeta(ItemMeta meta)
	{
		ArrayList<String> lore = new ArrayList<>();
		lore.add("Used to reveal where mobs can spawn.");
		lore.add("Right click in main hand while sneaking to change mode.");
		
		meta.setLore(lore);
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
	}
	
	@Override
	protected void finalizeStack(ItemStack stack)
	{
	
	}
	
	@Override
	protected LightWandItem getObject(ItemMeta meta)
	{
		return new LightWandItem(meta);
	}
	
	@Override
	protected LightWandItem getObject(ItemStack stack)
	{
		return new LightWandItem(stack);
	}
	
	public static class LightWandItem extends CustomItemType.CustomItem
	{
		LightWandItem(ItemMeta meta)
		{
			super(meta);
		}
		
		LightWandItem(ItemStack stack)
		{
			super(stack);
		}
		
		public boolean highLight()
		{
			Data data = getData();
			return ((BooleanValue)data.get("Highlight", new BooleanValue(false))).get();
		}
		
		public void setHighlight(boolean highlight)
		{
		
		}
	}
}