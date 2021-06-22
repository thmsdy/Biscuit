package com.fpghoti.biscuit.biscuit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.PluginCore;
import com.fpghoti.biscuit.audio.AudioScheduler;
import com.fpghoti.biscuit.config.BiscuitConfig;
import com.fpghoti.biscuit.config.BiscuitProperties;
import com.fpghoti.biscuit.logging.BColor;
import com.fpghoti.biscuit.logging.BiscuitLog;
import com.fpghoti.biscuit.rest.MessageText;
import com.fpghoti.biscuit.timer.BiscuitTimer;
import com.fpghoti.biscuit.timer.task.ChatCountTimer;
import com.fpghoti.biscuit.timer.task.DecrementTimer;
import com.fpghoti.biscuit.timer.task.SoftMuteTimer;
import com.fpghoti.biscuit.user.CaptchaUser;
import com.fpghoti.biscuit.user.PreUser;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Biscuit {

	public static Biscuit getBiscuit(Guild g) {
		for(Biscuit b : Main.getBiscuits()) {
			if(b.getGuild().getId().equals(g.getId())) {
				return b;
			}
		}
		return null;
	}

	public static Biscuit getBiscuit(String guildcode) {
		for(Biscuit b : Main.getBiscuits()) {
			if(b.getProperties().getGuildCode().equalsIgnoreCase(guildcode)) {
				return b;
			}
		}
		return null;
	}

	public static ArrayList<PreUser> getPreUsers(User u) {
		ArrayList<PreUser> pres = new ArrayList<PreUser>();
		for(Guild g : Main.getJDA().getGuilds()) {
			if(g.isMember(u)){
				Biscuit b = getBiscuit(g);
				if(b.preUserExists(u)) {
					pres.add(b.getPreUser(u));
				}
			}
		}
		return pres;
	}

	public static Biscuit loadGuild(Guild g) {
		Biscuit biscuit = new Biscuit(Main.getJDA(), g, Main.getLogger());
		biscuit.addTimer(new ChatCountTimer(biscuit));
		biscuit.addTimer(new SoftMuteTimer(biscuit));
		biscuit.addTimer(new DecrementTimer(biscuit));
		biscuit.loadTimers();
		Main.registerBiscuit(biscuit);
		return biscuit;
	}

	private boolean isMain;
	private JDA jda;
	private BiscuitLog logger;
	private Timer timer;
	private List<BiscuitTimer> timers;
	private File captchaDir;
	//private Cage cage;
	private Guild guild;
	private HashMap<String, Integer> inviteUses;
	private BiscuitConfig config;
	private BiscuitProperties properties;
	private BiscuitMessageStore messageStore;
	private AudioPlayer player;
	private AudioScheduler scheduler;

	private CopyOnWriteArrayList<PreUser> users = new CopyOnWriteArrayList<PreUser>();
	private HashMap<Member, Role> rolequeue;

	public Biscuit(JDA jda, Guild guild, BiscuitLog log) {
		this.jda = jda;
		this.guild = guild;
		this.logger = log;
		this.isMain = guild == null;
		this.messageStore = new BiscuitMessageStore(this);
		this.config = new BiscuitConfig(this);
		config.generateConfig();
		this.properties = new BiscuitProperties(this);
		this.rolequeue = new HashMap<Member, Role>();
		this.player = Main.getPlayerManager().createPlayer();

		scheduler = new AudioScheduler(this);
		player.addListener(scheduler);

		timer = new Timer();
		timers = new ArrayList<BiscuitTimer>();
		if(!Main.isPlugin) {
			captchaDir = new File("captcha");
			captchaDir.mkdir();
		}else {
			captchaDir = new File(PluginCore.plugin.getDataFolder(), "captcha");
			captchaDir.mkdir();
		}	
		if(isMain) {
			wipeCaptchaDir();
		}
		if(!isMain) {
			loadPreUsers();
			inviteUses = new HashMap<String, Integer>();
			if(canManageServer() && properties.checkJoinInvite()) {
				guild.retrieveInvites().queue((invs) -> {
					indexInvites(invs);
				});
			}
		}
	}

	public BiscuitConfig getConfig() {
		return config;
	}

	public BiscuitProperties getProperties() {
		return properties;
	}

	public BiscuitMessageStore getMessageStore() {
		return messageStore;
	}

	public Guild getGuild() {
		return guild;
	}

	public boolean canManageServer() {
		return guild.getSelfMember().hasPermission(Permission.MANAGE_SERVER);
	}

	public AudioPlayer getAudioPlayer() {
		return player;
	}

	public AudioScheduler getAudioScheduler() {
		return scheduler;
	}

	public void log(String message) {
		if(properties == null) {
			logger.info(message);
			return;
		}
		logger.info("[" + BColor.CYAN +  properties.getGuildCode().toUpperCase() + BColor.RESET + "]: " + message);
	}

	public void warn(String message) {
		if(properties == null) {
			logger.warn(message);
			return;
		}
		logger.warn("[" + properties.getGuildCode().toUpperCase() + "]: " + message);
	}

	public void error(String message) {
		if(properties == null) {
			logger.error(message);
			return;
		}
		logger.error("[" + properties.getGuildCode().toUpperCase() + "]: " + message);
	}

	public JDA getJDA() {
		return jda;
	}

	public void say(TextChannel channel, String message) {
		MessageText.send(channel, message);
	}

	public void loadTimers() {
		timer.cancel();
		timer = new Timer();
		for(BiscuitTimer t : timers) {
			timer.schedule(t,t.getDelay(), t.getPeriod());
		}
	}

	public void addTimer(BiscuitTimer bt) {
		timers.add(bt);
	}

	public void removeTimer(BiscuitTimer bt) {
		timers.remove(bt);
	}

	public void wipeCaptchaDir() {
		log("Wiping captcha files...");
		boolean good = true;
		File[] files = captchaDir.listFiles();
		for (File file : files){
			if (!file.delete()){
				good = false;
				error("Could not remove captcha file: " + file);
			}
		}
		if(good) {
			log("All captcha files successfully removed!");
		}else {
			error("Some captcha file(s) could not be removed!");
		}
	}

	public ArrayList<TextChannel> getCaptchaLogChannels() {
		ArrayList<TextChannel> ch = new ArrayList<TextChannel>();
		for(TextChannel t : guild.getTextChannels()) {
			if(t.getName().equalsIgnoreCase(properties.getEventLogChannel())) {
				ch.add(t);
			}
		}
		return ch;
	}

	public void eventLog(String msg) {
		if(properties.logEvents()) {
			for(TextChannel t : getCaptchaLogChannels()) {
				MessageText.send(t, msg);
			}
		}
	}

	public Role getDefaultRole() {
		for(Role r : guild.getRoles()) {
			if(r.getName().toLowerCase().contains(properties.getDefaultRole().toLowerCase())) {
				return r;
			}
		}
		return null;
	}

	public Role getCaptchaRewardRole() {
		for(Role r : guild.getRoles()) {
			if(r.getName().toLowerCase().contains(properties.getCaptchaReward().toLowerCase())) {
				return r;
			}
		}
		return null;
	}

	private void loadPreUsers() {
		if(!properties.captchaEnabled()) {
			return;
		}
		for(Member m : guild.getMembers()) {
			User u = m.getUser();
			if(!preUserExists(u)) {
				if(m.getRoles().size() == 1) {
					for(Role role : m.getRoles()){
						if(role.getName().equalsIgnoreCase(properties.getDefaultRole())){
							log(BColor.MAGENTA_BOLD + "Adding pre-join check for user " + u.getName() + " (" + u.getAsMention() + ")...");
							users.add(PreUser.getPreUser(CaptchaUser.getCaptchaUser(u), this));
						}
					}
				}
			}
		}
	}


	private void indexInvites(List<Invite> invs) {
		for(Invite i : invs) {
			String code = i.getCode();
			int uses = i.getUses();
			inviteUses.put(code, uses);
		}
	}

	public HashMap<String, Integer> getInviteUses(){
		return inviteUses;
	}

	public void setInviteUses(HashMap<String, Integer> c) {
		inviteUses = c;
	}

	public void clearInviteUses() {
		inviteUses.clear();
	}

	public void addPreUser(PreUser user) {
		if(!users.contains(user)) {
			users.add(user);
		}else {
			error("CAPTCHA ERROR: Tried to add PreUser when one already exists");
		}
	}

	public void removePreUser(PreUser user) {
		ArrayList<PreUser> temp = new ArrayList<PreUser>(users);
		for(PreUser u : temp) {
			if(u.getUser().getId().equals(user.getUser().getId())) {
				user.setDone();
				users.remove(u);
			}
		}
	}

	public CopyOnWriteArrayList<PreUser> getPreUsers(){
		return users;
	}

	public PreUser getPreUser(User user) {
		for(PreUser u : users) {
			if(u.getUser().getId().equals(user.getId())) {
				return u;
			}
		}
		return null;
	}

	public boolean preUserExists(User user) {
		return getPreUser(user) != null;
	}

	public File getConfigDir() {
		boolean isMain = guild == null;
		if(isMain) {
			if(Main.isPlugin) {
				PluginCore.plugin.getDataFolder().mkdir();
				return PluginCore.plugin.getDataFolder();
			}else {
				return new File("").getAbsoluteFile();
			}
		}else {
			File dir;
			if(!Main.isPlugin) {
				dir = new File("guilds");
			}else {
				dir = new File(PluginCore.plugin.getDataFolder(), "guilds");
			}
			dir.mkdir();
			return dir;
		}
	}

	public HashMap<Member, Role> getRoleQueue() {
		return rolequeue;
	}

	public void remove() {
		log("Removing guild biscuit...");
		for(BiscuitTimer t : timers) {
			t.cancel();
		}
		for(PreUser p : users) {
			p.remove();
		}
		Main.unregisterBiscuit(this);
	}

}
