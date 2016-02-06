package org.dogepool.practicalrx.domain;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.util.JSONPObject;
import com.couchbase.client.java.document.json.JsonObject;

public class User {

    public static final User USER = new User(0L, "user0", "Test User", "Story of my life.\nEnd of Story.", "12434");
    public static final User OTHERUSER = new User(1L, "richUser", "Richie Rich", "I'm rich I have dogecoin", "45678");

    public final long id;
    public final String nickname;
    public final String displayName;
    public final String bio;
    public final String avatarId;
    public final String type = "user";

    public User(long id, String nickname, String displayName, String bio, String avatarId) {
        this.id = id;
        this.nickname = nickname;
        this.displayName = displayName;
        this.bio = bio;
        this.avatarId = avatarId;
    }

    public JsonObject toJsonObject(){
        JsonObject jso = JsonObject.create();
        jso.put("id",id);
        jso.put("nickname",nickname);
        jso.put("displayName", displayName);
        jso.put("bio", bio);
        jso.put("avatarId", avatarId);
        jso.put("type", "user");
        return jso;
    }

    public static User fromJsonObject(JsonObject jso){
        return new User(jso.getInt("id"), jso.getString("nickname"), jso.getString("displayName"), jso.getString("bio"),
                jso.getString("avatarId"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (id != user.id) {
            return false;
        }
        if (!nickname.equals(user.nickname)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nickname.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return displayName + " (" + nickname + ")";
    }
}
