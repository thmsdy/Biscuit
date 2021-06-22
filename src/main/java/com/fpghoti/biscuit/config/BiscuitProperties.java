package com.fpghoti.biscuit.config;

import com.fpghoti.biscuit.Main;
import com.fpghoti.biscuit.biscuit.Biscuit;
import com.fpghoti.biscuit.util.Util;

public class BiscuitProperties {
    
    Biscuit biscuit;
    
    public BiscuitProperties(Biscuit b) {
        this.biscuit = b;
    }

    public String getToken(){
        String key = "Bot-Token";
        return Main.getMainBiscuit().getConfig().getFromConfig(key);
    }
    
    public String getCommandSignifier(){
        String key = "Command-Signifier";
        return Main.getMainBiscuit().getConfig().getFromConfig(key);
    }
    
    public boolean musicBotEnabled(){
        String key = "Enable-Music-Bot";
        return Main.getMainBiscuit().getConfig().getFromConfig(key).equalsIgnoreCase("true");
    }
    
    public boolean logMusicPlayer(){
        String key = "Log-Music-Player";
        return Main.getMainBiscuit().getConfig().getFromConfig(key).equalsIgnoreCase("true");
    }
    
    public boolean allowMusicBot(){
        if(!musicBotEnabled()) {
            return false;
        }
        String key = "Allow-Music-Bot";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().allowMusicBot();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public String getGuildCode(){
        String key = "Guild-Code";
        if(biscuit.getGuild() == null) {
            return "MAIN";
        }
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return biscuit.getConfig().makeCode(biscuit.getGuild().getName());
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public String getDoneEmote(){
        String key = "Done-Emote";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getDoneEmote();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public String getMusicControllerRole(){
        String key = "Music-Controller-Role";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getMusicControllerRole();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public String getKickDMInvite(){
        String key = "Kick-DM-Invite";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getKickDMInvite();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public boolean captchaEnabled(){
        String key = "Captcha";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().captchaEnabled();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public boolean customDefaultRole(){
        String key = "UseCustomDefaultRole";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().customDefaultRole();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public boolean dmBeforeKick(){
        String key = "DM-Before-Kick";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().dmBeforeKick();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public String getCaptchaReward(){
        String key = "Captcha-Reward-Role";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getCaptchaReward();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public String getDefaultRole(){
        String key = "DefaultRoleName";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getDefaultRole();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public String getModRole(){
        String key = "ModRole";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getModRole();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public String getAdminRole(){
        String key = "AdminRole";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getAdminRole();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public String getEventLogChannel(){
        String key = "Event-Log-Channel";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getEventLogChannel();
        }
        return biscuit.getConfig().getFromConfig(key);
    }
    
    public boolean logEvents(){
        String key = "LogEvents";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().logEvents();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public boolean spamPunishAllow(){
        String key = "AllowSpamPunish";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().spamPunishAllow();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public boolean checkJoinInvite(){
        String key = "Check-Join-Invite";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().checkJoinInvite();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public boolean noCaptchaKick(){
        String key = "No-Captcha-Kick";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().noCaptchaKick();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public Integer noCaptchaKickTime(){
        String key = "No-Captcha-Kick-Time";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().noCaptchaKickTime();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        if(!Util.isDigit(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }
    
    public boolean logChat(){
        String key = "ChatLog";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().logChat();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public String[] getNaughtyWords(){
        String key = "NaughtyList";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getNaughtyWords();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public boolean restrictCmdChannels(){
        String key = "Restrict-Cmd-Channels";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().restrictCmdChannels();
        }
        String value = biscuit.getConfig().getFromConfig(key);
        return value.equalsIgnoreCase("true");
    }
    
    public String[] getCmdChannels(){
        String key = "CmdChannels";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getCmdChannels();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] getDontLogChannels(){
        String key = "Channels-To-Not-Chatlog";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getCmdChannels();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] getToggleRoles(){
        String key = "ToggleRoles";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getToggleRoles();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" , ", ",").replace(", ", ",").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] getBoostExclusiveRoles(){
        String key = "Boost-Exclusive-Roles";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getBoostExclusiveRoles();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" , ", ",").replace(", ", ",").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] getBoosterRoles(){
        String key = "Treat-Like-Booster";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getBoosterRoles();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" , ", ",").replace(", ", ",").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] getMusicChannels(){
        String key = "Music-Channels";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getMusicChannels();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" , ", ",").replace(", ", ",").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] blockedUnicodeEmotes(){
        String key = "Block-Unicode-Emotes";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().blockedUnicodeEmotes();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }

    public String[] blockedCustomEmotes(){
        String key = "Block-Custom-Emotes";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().blockedCustomEmotes();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] getCustomCmds(){
        String key = "Custom-Command-Names";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getCustomCmds();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] disabledCommands(){
        String key = "DisabledCommands";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().disabledCommands();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] disabledUserCommands(){
        String key = "DisabledUserCommands";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().disabledUserCommands();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
    
    public String[] getToggleChannels(){
        String key = "Toggle-Role-React-Channels";
        if(biscuit.getConfig().getFromConfig(key).equalsIgnoreCase("[global]") && biscuit.getGuild() != null) {
            return Main.getMainBiscuit().getProperties().getToggleChannels();
        }
        String [] list = biscuit.getConfig().getFromConfig(key).replace(" ", "").split(",");
        if(list.length == 1 && list[0].equals("")) {
            String[] blank = {};
            return blank;
        }
        return list;
    }
}