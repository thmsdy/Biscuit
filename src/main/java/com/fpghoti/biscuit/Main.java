package com.fpghoti.biscuit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.captcha.BCage;
import com.fpghoti.biscuit.commands.CommandManager;
import com.fpghoti.biscuit.commands.client.AddCommand;
import com.fpghoti.biscuit.commands.client.ChanIDCommand;
import com.fpghoti.biscuit.commands.client.DivideCommand;
import com.fpghoti.biscuit.commands.client.GetConfigCommand;
import com.fpghoti.biscuit.commands.client.GuildIDCommand;
import com.fpghoti.biscuit.commands.client.ToggleRoleCommand;
import com.fpghoti.biscuit.commands.client.HelpCommand;
import com.fpghoti.biscuit.commands.client.MultiplyCommand;
import com.fpghoti.biscuit.commands.client.NotSpammerCommand;
import com.fpghoti.biscuit.commands.client.MakeInviteCommand;
import com.fpghoti.biscuit.commands.client.PingCommand;
import com.fpghoti.biscuit.commands.client.PowerCommand;
import com.fpghoti.biscuit.commands.client.RecentSpammersCommand;
import com.fpghoti.biscuit.commands.client.SaveConfigCommand;
import com.fpghoti.biscuit.commands.client.SoftMuteCommand;
import com.fpghoti.biscuit.commands.client.SquareRootCommand;
import com.fpghoti.biscuit.commands.client.SubtractCommand;
import com.fpghoti.biscuit.commands.client.UIDCommand;
import com.fpghoti.biscuit.commands.client.UnSoftMuteCommand;
import com.fpghoti.biscuit.commands.client.WikiCommand;
import com.fpghoti.biscuit.commands.client.music.ClearCommand;
import com.fpghoti.biscuit.commands.client.music.ForceSkipCommand;
import com.fpghoti.biscuit.commands.client.music.ForceSkipToCommand;
import com.fpghoti.biscuit.commands.client.music.ClearUserSongsCommand;
import com.fpghoti.biscuit.commands.client.music.LoopMusicCommand;
import com.fpghoti.biscuit.commands.client.music.MoveToCommand;
import com.fpghoti.biscuit.commands.client.music.NowPlayingCommand;
import com.fpghoti.biscuit.commands.client.music.PauseCommand;
import com.fpghoti.biscuit.commands.client.music.PlayCommand;
import com.fpghoti.biscuit.commands.client.music.PlayFirstCommand;
import com.fpghoti.biscuit.commands.client.music.QueueCommand;
import com.fpghoti.biscuit.commands.client.music.RemoveCommand;
import com.fpghoti.biscuit.commands.client.music.ShuffleCommand;
import com.fpghoti.biscuit.commands.client.music.SkipAllCommand;
import com.fpghoti.biscuit.commands.client.music.SkipCommand;
import com.fpghoti.biscuit.commands.client.music.TogglePauseCommand;
import com.fpghoti.biscuit.commands.client.music.UnpauseCommand;
import com.fpghoti.biscuit.commands.client.music.VolumeCommand;
import com.fpghoti.biscuit.commands.console.GuildSayCommand;
import com.fpghoti.biscuit.commands.console.SayCommand;
import com.fpghoti.biscuit.commands.console.ShutdownConsoleCommand;
import com.fpghoti.biscuit.listener.CommandListener;
import com.fpghoti.biscuit.listener.DMListener;
import com.fpghoti.biscuit.listener.GuildListener;
import com.fpghoti.biscuit.listener.JoinListener;
import com.fpghoti.biscuit.listener.LeaveListener;
import com.fpghoti.biscuit.listener.MessageDeleteListener;
import com.fpghoti.biscuit.listener.MessageEditListener;
import com.fpghoti.biscuit.listener.MessageReceiveListener;
import com.fpghoti.biscuit.listener.ReactionListener;
import com.fpghoti.biscuit.listener.RoleListener;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.logging.BiscuitLog;
import com.github.cage.Cage;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main {

	private static JDA jda;
	private static ShardManager shardm;

	public RollingFileAppender<String> we;
	public SizeAndTimeBasedRollingPolicy<String> wes;

	private static final BiscuitLog log = new BiscuitLog();

	private static ArrayList<Biscuit> biscuits;
	private static Biscuit mainBiscuit;
	public static Scanner sc;
	public static boolean ready = false;
	public static boolean isPlugin =  false;
	public static boolean shutdownStarted = false;
	private static Cage cage;

	private static AudioPlayerManager playerManager;

	public static void main(String[] args){
		final Properties properties = new Properties();
		try {
			properties.load(Main.class.getClassLoader().getResourceAsStream("info.properties"));
		} catch (IOException e) {
			log.error("Could not determine Biscuit version.");
		}
		String version = properties.getProperty("version");

		AnsiConsole.systemInstall();
		log.info(BColor.CYAN_BOLD + "========================= WELCOME TO BISCUIT =========================");
		log.info("Running version: " + BColor.MAGENTA_BOLD + version);

		playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);

		mainBiscuit = new Biscuit(null, null, log);
		startJDA();
		
		cage = new BCage();

		jda.addEventListener(new GuildListener());
		jda.addEventListener(new MessageReceiveListener());
		jda.addEventListener(new MessageEditListener());
		jda.addEventListener(new MessageDeleteListener());
		jda.addEventListener(new JoinListener());
		jda.addEventListener(new LeaveListener());
		jda.addEventListener(new DMListener());
		jda.addEventListener(new ReactionListener());
		jda.addEventListener(new RoleListener());
		biscuits = new ArrayList<Biscuit>();
		for(Guild g : jda.getGuilds()) {
			Biscuit.loadGuild(g);
		}
		startCommandListener();

		String link = "NULL";

		//Client

		CommandManager.addCommand(new HelpCommand());
		CommandManager.addCommand(new PingCommand());
		CommandManager.addCommand(new SoftMuteCommand());
		CommandManager.addCommand(new UnSoftMuteCommand());
		CommandManager.addCommand(new NotSpammerCommand());
		CommandManager.addCommand(new RecentSpammersCommand());
		CommandManager.addCommand(new ChanIDCommand());
		CommandManager.addCommand(new UIDCommand());
		CommandManager.addCommand(new ToggleRoleCommand());
		CommandManager.addCommand(new SquareRootCommand());
		CommandManager.addCommand(new AddCommand());
		CommandManager.addCommand(new SubtractCommand());
		CommandManager.addCommand(new MultiplyCommand());
		CommandManager.addCommand(new DivideCommand());
		CommandManager.addCommand(new PowerCommand());
		CommandManager.addCommand(new MakeInviteCommand());
		CommandManager.addCommand(new GetConfigCommand());
		CommandManager.addCommand(new SaveConfigCommand());
		CommandManager.addCommand(new GuildIDCommand());
		CommandManager.addCommand(new WikiCommand());
		
		//Music Client
		
		CommandManager.addCommand(new PlayCommand());
		CommandManager.addCommand(new PlayFirstCommand());
		CommandManager.addCommand(new ForceSkipCommand());
		CommandManager.addCommand(new PauseCommand());
		CommandManager.addCommand(new UnpauseCommand());
		CommandManager.addCommand(new TogglePauseCommand());
		CommandManager.addCommand(new QueueCommand());
		CommandManager.addCommand(new SkipCommand());
		CommandManager.addCommand(new NowPlayingCommand());
		CommandManager.addCommand(new LoopMusicCommand());
		CommandManager.addCommand(new VolumeCommand());
		CommandManager.addCommand(new ShuffleCommand());
		CommandManager.addCommand(new RemoveCommand());
		CommandManager.addCommand(new ClearCommand());
		CommandManager.addCommand(new MoveToCommand());
		CommandManager.addCommand(new ForceSkipToCommand());
		CommandManager.addCommand(new ClearUserSongsCommand());
		CommandManager.addCommand(new SkipAllCommand());

		//Console

		CommandManager.addCommand(new SayCommand());
		CommandManager.addCommand(new GuildSayCommand());
		CommandManager.addCommand(new ShutdownConsoleCommand());

		link = "https://discord.com/oauth2/authorize?&client_id=" + jda.getSelfUser().getId() + "&permissions=8&scope=bot";
		log.info("Connection successful!");
		log.info("Startup successful!");
		log.info("You can add this bot to Discord using this link:");
		log.info(link);
		log.info(BColor.CYAN_BOLD + "======================================================================");
		log.info("CHAT LOGS BEGIN HERE:");
		ready = true;
	}

	private static void startJDA() {
		String token = mainBiscuit.getProperties().getToken();
		log.info("Connecting bot to Discord.");
		try{
			shardm = DefaultShardManagerBuilder.createDefault(token)
					.setChunkingFilter(ChunkingFilter.ALL)
					.setMemberCachePolicy(MemberCachePolicy.ALL)
					.enableIntents(GatewayIntent.getIntents(GatewayIntent.DEFAULT))
					.enableIntents(GatewayIntent.GUILD_MEMBERS)
					.build();
			jda = shardm.getShardById(0);
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

	public static Biscuit getMainBiscuit() {
		return mainBiscuit;
	}

	private static void startCommandListener() {
		if(!isPlugin) {
			sc = new Scanner(System.in);
			CommandListener cl = new CommandListener(sc, log);
			jda.addEventListener(cl);
			new Thread(cl).start();
		}else {
			CommandListener cl = new CommandListener(null, log);
			jda.addEventListener(cl);
		}
	}

	public static ArrayList<Biscuit> getBiscuits() {
		return biscuits;
	}

	public static AudioPlayerManager getPlayerManager() {
		return playerManager;
	}

	public static void shutdown() {
		if(!shutdownStarted) {
			shutdownStarted = true;
			mainBiscuit.log("Shutting down...");
			ArrayList<Biscuit> list = new ArrayList<Biscuit>(biscuits);
			for(Biscuit b : list) {
				b.remove();
			}
			mainBiscuit.wipeCaptchaDir();
			if(jda != null) {
				jda.shutdown();
			}
			if(!isPlugin) {
				sc.close();
				System.exit(0);
			}
		}
	}

	public static JDA getJDA() {
		return jda;
	}
	
	public static Cage getCage() {
		return cage;
	}

	public static BiscuitLog getLogger() {
		return log;
	}

	public static void registerBiscuit(Biscuit b) {
		biscuits.add(b);
	}

	public static void unregisterBiscuit(Biscuit b) {
		biscuits.remove(b);
	}

}
