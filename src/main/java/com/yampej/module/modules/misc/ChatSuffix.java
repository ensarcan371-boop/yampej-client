package com.yampej.module.modules.misc;

import com.yampej.module.Category;
import com.yampej.module.Module;

public class ChatSuffix extends Module {
    public ChatSuffix() {
        super("ChatSuffix", "Add text to chat messages", Category.MISC);
    }

    public String apply(String message) {
        return isEnabled() ? message + " | yampej" : message;
    }
}
