package me.stqlth.birthdaybot.commands.staff.clear;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.birthdaybot.utils.DatabaseMethods;
import me.stqlth.birthdaybot.utils.EmbedSender;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class ClearBirthdayRole extends Command {

	private DatabaseMethods db;

	public ClearBirthdayRole(DatabaseMethods databaseMethods) {
		this.name = "clearbirthdayrole";
		this.help = "Clears the birthday role";
		this.guildOnly = true;
		this.hidden = true;
		this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};

		this.db = databaseMethods;
	}

	@Override
	protected void execute(CommandEvent event) {
		TextChannel channel = event.getTextChannel();

		Member sender = event.getMember();
		Permission req = Permission.ADMINISTRATOR;

		if (!sender.hasPermission(req)) {
			EmbedSender.sendEmbed(channel, null, "Only Admins may use this command!", Color.RED);
			return;
		}

		db.clearBirthdayRole(event);
		EmbedSender.sendEmbed(channel, null, "**BirthdayBot** will no longer give users the birthday role! :(", Color.decode("#1CFE86"));
	}
}
