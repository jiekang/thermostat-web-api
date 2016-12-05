package com.redhat.thermostat.web2.endpoint.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserStore {
    private final Map<String, BasicUser> userStore;

    public UserStore() {
        userStore = new HashMap<>();
        userStore.put("alpha", new BasicUser("alpha", "alpha".toCharArray(), new ArrayList<>(Arrays.asList("alpha", "user"))));
        userStore.put("beta", new BasicUser("beta", "beta".toCharArray(), new ArrayList<>(Arrays.asList("beta", "user"))));

    }

    public BasicUser getUser(String userName) {
        return userStore.get(userName);
    }
}
