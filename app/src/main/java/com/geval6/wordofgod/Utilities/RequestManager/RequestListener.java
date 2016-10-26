package com.geval6.wordofgod.Utilities.RequestManager;

import java.util.HashMap;

public interface RequestListener {
    void onBeginRequest();
    void onRequestCompleted(RequestIdentifier requestIdentifier, Object... object);
}
