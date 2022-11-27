package jogCraft;

import jogLib.command.executor.*;
import jogLib.command.filter.*;
import jogUtil.commander.*;
import jogUtil.commander.argument.*;
import jogUtil.commander.command.*;
import org.bukkit.entity.*;

public class JogCraftCategory extends Category
{
	JogCraftCategory(Category parent)
	{
		super(parent, "JogCraft", "JogCraft commands.");
		
		new GetLightWand(this);
	}
	
	public static class GetLightWand extends Command
	{
		GetLightWand(Category parent)
		{
			super(parent, "GetLightWand", "Gives you a light wand.");
			addFilter(new PlayerFilter());
			addFilter(new OperatorFilter());
		}
		
		@Override
		protected void execute(AdaptiveInterpretation adaptiveInterpretation, Executor executor)
		{
			Player player = ((PlayerExecutor)executor).sender();
			player.getInventory().addItem(JogCraft.lightWand().createStack());
		}
	}
}