package com.holmal.app.holmal.dummy;

import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dummy Test Data
 *
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public abstract class DummyContent {

    public Household dummyHaushalt;

    public Household getDummyHaushalt() {
        dummyHaushalt = new Household("dummyHaushalt", new ArrayList<Person>());
        return dummyHaushalt;
    }

    public void setDummyHaushalt(Household dummyHaushalt) {
        this.dummyHaushalt = dummyHaushalt;
    }

    public Person getLukas() {
        lukas = new Person("Lukas", 1);
        //lukas.addHaushalt(dummyHaushalt);
        return lukas;
    }

    public static void setLukas(Person lukas) {
        DummyContent.lukas = lukas;
    }

    public Person getMarie() {
        return marie;
    }

    public static void setMarie(Person marie) {
        DummyContent.marie = marie;
    }

    public Person getSusi() {
        return susi;
    }

    public static void setSusi(Person susi) {
        DummyContent.susi = susi;
    }

    public Person getTim() {
        return tim;
    }

    public static void setTim(Person tim) {
        DummyContent.tim = tim;
    }

    public static Person lukas;
    public static Person marie;
    public static Person susi;
    public static Person tim;

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
