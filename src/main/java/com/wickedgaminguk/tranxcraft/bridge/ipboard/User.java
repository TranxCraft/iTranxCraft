package com.wickedgaminguk.tranxcraft.bridge.ipboard;

public class User {

    //region Private Fields

    private static int count;

    private String username;
    private String email;
    private String ipAddress;
    private String title;
    private String birthday;
    private String displayName;

    private int id;
    private int warnLevel;
    private int lastWarn;
    private int memberGroupId;
    private int posts;

    private Long lastPost;
    private Long joined;

    //endregion

    //region Getters and Setters

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public User setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public int getPosts() {
        return posts;
    }

    public User setPosts(int posts) {
        this.posts = posts;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public User setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public User setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public User setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public int getWarnLevel() {
        return warnLevel;
    }

    public User setWarnLevel(int warnLevel) {
        this.warnLevel = warnLevel;
        return this;
    }

    public int getLastWarn() {
        return lastWarn;
    }

    public User setLastWarn(int lastWarn) {
        this.lastWarn = lastWarn;
        return this;
    }

    public int getMemberGroupId() {
        return memberGroupId;
    }

    public User setMemberGroupId(int memberGroupId) {
        this.memberGroupId = memberGroupId;
        return this;
    }

    public Long getLastPost() {
        return lastPost;
    }

    public User setLastPost(Long lastPost) {
        this.lastPost = lastPost;
        return this;
    }

    public Long getJoined() {
        return joined;
    }

    public User setJoined(Long joined) {
        this.joined = joined;
        return this;
    }

    public static int getCount() {
        return count;
    }

    //endregion
}
