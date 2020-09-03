package com.lycoon.clashbot.commands;

import java.awt.Color;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.lycoon.clashbot.core.ErrorEmbed;
import com.lycoon.clashbot.lang.LangUtils;
import com.lycoon.clashbot.utils.DBUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SetCommand
{
	public static void executePlayer(MessageReceivedEvent event, String tag)
	{
		DBUtils.setPlayerTag(event.getAuthor().getIdLong(), tag);
		ResourceBundle i18n = LangUtils.getTranslations(event.getAuthor().getIdLong());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setTitle(MessageFormat.format(i18n.getString("set.player.success"), tag));
		builder.setFooter(i18n.getString("set.player.tip"));
		
		event.getChannel().sendMessage(builder.build()).queue();
		builder.clear();
	}
	
	public static void executeClan(MessageReceivedEvent event, String tag)
	{
		DBUtils.setClanTag(event.getAuthor().getIdLong(), tag);
		ResourceBundle i18n = LangUtils.getTranslations(event.getAuthor().getIdLong());
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setTitle(MessageFormat.format(i18n.getString("set.clan.success"), tag));
		builder.setFooter(i18n.getString("set.clan.tip"));
		
		event.getChannel().sendMessage(builder.build()).queue();
		builder.clear();
	}
	
	public static void executeLang(MessageReceivedEvent event, String language)
	{
		long id = event.getAuthor().getIdLong();
		if (LangUtils.isSupportedLanguage(language))
		{
			DBUtils.setUserLang(id, language);
			Locale lang = new Locale(language);
			ResourceBundle i18n = LangUtils.getTranslations(lang);
			
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.GREEN);
			builder.setTitle(i18n.getString("lang.flag")+
					" " +MessageFormat.format(i18n.getString("lang.success"), lang.getDisplayLanguage(lang)));
			builder.setDescription(i18n.getString("lang.info.other"));
			
			event.getChannel().sendMessage(builder.build()).queue();
			builder.clear();
		}
		else
		{
			Locale lang = LangUtils.getLanguage(id);
			ResourceBundle i18n = LangUtils.getTranslations(lang);
			
			ErrorEmbed.sendError(event.getChannel(), 
					MessageFormat.format(i18n.getString("lang.error"), language), 
					i18n.getString("lang.info.supported")+ "\n" +LangUtils.getSupportedLanguages(lang),
					i18n.getString("lang.suggest.contact"));
		}
	}
}