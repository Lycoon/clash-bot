package com.lycoon.clashbot.utils;

import com.lycoon.clashbot.lang.LangUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class CoreUtils
{
	static Duration rateTime = Duration.ofSeconds(6);
	static long threshold = rateTime.toMillis();
	static HashMap<Long, ZonedDateTime> generating = new HashMap<>();
	
	public static long getLastTimeDifference(long id)
	{
		if (generating.containsKey(id))
		{
			Duration diff = Duration.between(generating.get(id), ZonedDateTime.now());
			return diff.toMillis();
		}
		return threshold;
	}
	
	public static void addUserToGenerating(long id)
	{
		generating.put(id, ZonedDateTime.now());
	}
	
	public static void removeUserFromGenerating(long id)
	{
		generating.remove(id);
	}
	
	public static boolean checkThrottle(MessageReceivedEvent event, Locale lang)
	{
		ResourceBundle i18n = LangUtils.getTranslations(lang);
		
		NumberFormat nf = NumberFormat.getNumberInstance(lang);
		DecimalFormat df = (DecimalFormat)nf;
		df.applyPattern("#.#");
		
		long timeDifference = getLastTimeDifference(event.getAuthor().getIdLong());
		boolean isValid = timeDifference >= threshold;
		
		if (!isValid)
			ErrorUtils.sendError(event.getChannel(),
					i18n.getString("exception.rate.exceeded"), 
					MessageFormat.format(
							i18n.getString("exception.rate.exceeded.left"), 
							df.format((threshold - timeDifference) / 1000.0)));
		
		return isValid;
	}
}
