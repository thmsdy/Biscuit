package com.fpghoti.biscuit;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpghoti.biscuit.commands.BaseCommand;
import com.fpghoti.biscuit.commands.CommandListener;
import com.fpghoti.biscuit.commands.client.AddCommand;
import com.fpghoti.biscuit.commands.client.ChanIDCommand;
import com.fpghoti.biscuit.commands.client.ChnameCommand;
import com.fpghoti.biscuit.commands.client.DivideCommand;
import com.fpghoti.biscuit.commands.client.DontNotifyCommand;
import com.fpghoti.biscuit.commands.client.HelpCommand;
import com.fpghoti.biscuit.commands.client.MultiplyCommand;
import com.fpghoti.biscuit.commands.client.NotSpammerCommand;
import com.fpghoti.biscuit.commands.client.NotifyCommand;
import com.fpghoti.biscuit.commands.client.PingCommand;
import com.fpghoti.biscuit.commands.client.PowerCommand;
import com.fpghoti.biscuit.commands.client.RecentSpammersCommand;
import com.fpghoti.biscuit.commands.client.SoftMuteCommand;
import com.fpghoti.biscuit.commands.client.SquareRootCommand;
import com.fpghoti.biscuit.commands.client.SubtractCommand;
import com.fpghoti.biscuit.commands.client.UIDCommand;
import com.fpghoti.biscuit.commands.client.UnSoftMuteCommand;
import com.fpghoti.biscuit.commands.console.SayCommand;
import com.fpghoti.biscuit.commands.console.ShutdownConsoleCommand;
import com.fpghoti.biscuit.config.ConfigRetrieval;
import com.fpghoti.biscuit.config.PropertiesRetrieval;
import com.fpghoti.biscuit.global.Properties;
import com.fpghoti.biscuit.listener.DMListener;
import com.fpghoti.biscuit.listener.JoinListener;
import com.fpghoti.biscuit.listener.MessageDeleteListener;
import com.fpghoti.biscuit.listener.MessageEditListener;
import com.fpghoti.biscuit.listener.MessageReceiveListener;
import com.fpghoti.biscuit.timer.task.BotMsgRemoveTimer;
import com.fpghoti.biscuit.timer.task.ChatCountTimer;
import com.fpghoti.biscuit.timer.task.DecrementTimer;
import com.fpghoti.biscuit.timer.task.FastMsgRemoveTimer;
import com.fpghoti.biscuit.timer.task.SlowMsgRemoveTimer;
import com.fpghoti.biscuit.timer.task.SoftMuteTimer;

import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

	private static JDA jda;

	public RollingFileAppender<String> we;
	public SizeAndTimeBasedRollingPolicy<String> wes;

	public static final Logger log = LoggerFactory.getLogger("ch.qos.logback.core.ConsoleAppender");
	public static Scanner sc;
	public static Biscuit biscuit;
	public static boolean ready = false;
	public static boolean isPlugin =  false;
	public static boolean shutdownStarted = false;


	public static void main(String[] args){
		ConfigRetrieval.generateConfig();
		log.info("========================= Welcome to Biscuit =========================");
		startJDA();
		biscuit = new Biscuit(jda, log);
		startCommandListener();

		jda.addEventListener(new MessageReceiveListener());
		jda.addEventListener(new MessageEditListener());
		jda.addEventListener(new MessageDeleteListener());
		jda.addEventListener(new JoinListener());
		jda.addEventListener(new DMListener());

		String link = "NULL";
		Properties.naughtyList = ConfigRetrieval.getFromConfig("NaughtyList");
		Properties.customdefaultrole = ConfigRetrieval.getFromConfig("UseCustomDefaultRole").equalsIgnoreCase("true");
		Properties.roleName = ConfigRetrieval.getFromConfig("DefaultRoleName");

		biscuit.addTimer(new ChatCountTimer());
		biscuit.addTimer(new BotMsgRemoveTimer());
		biscuit.addTimer(new FastMsgRemoveTimer());
		biscuit.addTimer(new SlowMsgRemoveTimer());
		biscuit.addTimer(new SoftMuteTimer());
		biscuit.addTimer(new DecrementTimer());

		biscuit.loadTimers();

		List<BaseCommand> commands = biscuit.getCommandManager().getCommands();

		//Client

		commands.add(new HelpCommand());
		commands.add(new PingCommand());
		commands.add(new SoftMuteCommand());
		commands.add(new UnSoftMuteCommand());
		commands.add(new NotSpammerCommand());
		commands.add(new RecentSpammersCommand());
		commands.add(new ChanIDCommand());
		commands.add(new UIDCommand());
		commands.add(new ChnameCommand());
		commands.add(new NotifyCommand());
		commands.add(new DontNotifyCommand());
		commands.add(new SquareRootCommand());
		commands.add(new AddCommand());
		commands.add(new SubtractCommand());
		commands.add(new MultiplyCommand());
		commands.add(new DivideCommand());
		commands.add(new PowerCommand());

		//Console

		commands.add(new SayCommand());
		commands.add(new ShutdownConsoleCommand());

		link = "https://discordapp.com/oauth2/authorize?&client_id=" + jda.getSelfUser().getId() + "&scope=bot";
		log.info("Connection successful!");
		log.info("Startup successful!");
		log.info("You can add this bot to Discord using this link:");
		log.info(link);
		log.info("======================================================================");
		log.info("CHAT LOGS BEGIN HERE:");
		ready = true;
	}

	private static void startJDA() {
		String token = PropertiesRetrieval.getToken();
		log.info("Connecting bot to Discord.");
		try{
			jda = new JDABuilder(AccountType.BOT).setToken(token).build();
			try {
				jda.awaitReady();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			jda.setAutoReconnect(true);
		}catch(Exception e){
			e.printStackTrace();
			log.error("There was an issue connecting to Discord. Bot shutting down!");
			shutdown();
		}
	}

	private static void startCommandListener() {
		if(!isPlugin) {
			sc = new Scanner(System.in);
			CommandListener cl = new CommandListener();
			cl.sc = sc;
			cl.jda = jda;
			jda.addEventListener(cl);
			new Thread(cl).start();
		}else {
			CommandListener cl = new CommandListener();
			cl.jda = jda;
			jda.addEventListener(cl);
		}
	}

	public static Biscuit getBiscuit() {
		return biscuit;
	}


	public static void shutdown() {
		if(!shutdownStarted) {
			shutdownStarted = true;
			log.info("Shutting down...");
			biscuit.wipeCaptchaDir();
			if(jda != null) {
				jda.shutdown();
			}
			if(!isPlugin) {
				sc.close();
				System.exit(0);
			}
		}
	}



}
