package me.stqlth.birthdaybot.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.birthdaybot.utils.DatabaseMethods;
import me.stqlth.birthdaybot.utils.EmbedSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.ZoneId;

public class View extends Command {

	private DatabaseMethods db;

	public View(DatabaseMethods databaseMethods) {
		this.name = "view";
		this.aliases = new String[]{"show"};
		this.help = "View another user's birthday";
		this.guildOnly = true;
		this.arguments = "<name>";
		this.category = new Category("Utilities");
		this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};

		this.db = databaseMethods;
	}

	@Override
	protected void execute(CommandEvent event) {
		TextChannel channel = event.getTextChannel();

		String[] args = event.getMessage().getContentRaw().split(" ");
		if (args.length != 3) return;

		Member target;

		try {
			target = event.getMessage().getMentionedMembers().get(0);
		} catch (Exception ignored) {
			target = event.getGuild().getMembers().stream().filter(member -> member.getEffectiveName().toLowerCase().contains(args[2].toLowerCase())).findFirst().orElse(null);
			if (target == null)
				target = event.getGuild().getMembers().stream().filter(member -> member.getUser().getName().toLowerCase().contains(args[2].toLowerCase())).findFirst().orElse(null);
		}

		if (target == null) {
			EmbedSender.sendEmbed(channel, null, "I can't find that user!", Color.RED);
			return;
		}

		if (!db.doesUserExist(target.getUser()) || db.getUserBirthday(target.getUser()) == null) {
			EmbedSender.sendEmbed(channel, null, "**" + target.getUser().getName() + "** does not have a birthday set! :(", Color.RED);
			return;
		}

		String birthday = db.getUserBirthday(target.getUser()); //we set birthday here so we don't try to get a user's birthday who isn't in the database in the block above
		String[] values = birthday.split("-");
		ZoneId zoneId = db.getUserZoneId(target.getUser());

		int day = Integer.parseInt(values[2]);
		int month = Integer.parseInt(values[1]);

		String date = getMonth(month) + " " + day + " " + zoneId.getId();
		EmbedSender.sendEmbed(channel, null, target.getUser().getName() + "'s birthday is on **" + date + "**.", Color.decode("#1CFE86"));
	}

	private static String getMonth(int month) {
		switch (month) {
			case 1:  return "January";
			case 2:  return "February";
			case 3:  return "March";
			case 4:  return "April";
			case 5:  return "May";
			case 6:  return "June";
			case 7:  return "July";
			case 8:  return "August";
			case 9:  return "September";
			case 10: return "October";
			case 11: return "November";
			case 12: return "December";
			default: return "Invalid month";
		}
	}

}