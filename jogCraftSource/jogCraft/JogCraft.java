package jogCraft;

import jogLib.*;
import org.bukkit.*;

public class JogCraft extends JogPlugin
{
	static LightWand lightWand;
	
	@Override
	public void onEnable()
	{
		new FarmHand(this);
		lightWand = new LightWand(this);
		
		new JogCraftCategory(JogLib.commandConsole);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PathWanderer(), 0, PathWanderer.tickInterval);
	}
	
	public static LightWand lightWand()
	{
		return lightWand;
	}
}