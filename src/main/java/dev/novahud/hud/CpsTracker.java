package dev.novahud.hud;

import java.util.ArrayDeque;
import java.util.Deque;

public class CpsTracker {

    private static final Deque<Long> leftClicks = new ArrayDeque<>();
    private static final Deque<Long> rightClicks = new ArrayDeque<>();

    public static void registerLeft() {
        leftClicks.addLast(System.currentTimeMillis());
    }

    public static void registerRight() {
        rightClicks.addLast(System.currentTimeMillis());
    }

    public static int getLeft() {
        return count(leftClicks);
    }

    public static int getRight() {
        return count(rightClicks);
    }

    private static int count(Deque<Long> deque) {
        long now = System.currentTimeMillis();
        while (!deque.isEmpty() && now - deque.peekFirst() > 1000) {
            deque.pollFirst();
        }
        return deque.size();
    }
}
