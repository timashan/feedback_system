package sample;

import java.util.ArrayList;

/**
 * This class holds all the answer slots for a particular question
 * @author Kavishka Timashan
 */
public class Answers{
    private ArrayList<Slot> slotList = new ArrayList();

    /**
     * Constructor
     * Sets up all the answer slots (slot 1 to slot 5)
     * @param s1
     * @param s2
     * @param s3
     * @param s4
     * @param s5
     */
    public Answers(String s1, String s2, String s3, String s4, String s5) {
        this.slotList.add(new Slot(s1));
        this.slotList.add(new Slot(s2));
        this.slotList.add(new Slot(s3));
        this.slotList.add(new Slot(s4));
        this.slotList.add(new Slot(s5));

    }

    /**
     * Sets up the answer slots 1 and 2
     * @param slot1
     * @param slot2
     */
    public Answers(String slot1, String slot2) {
        this(slot1,slot2,null,null,null);
    }

    /**
     * Sets up the answer slots 1,2 and 3
     * @param slot1
     * @param slot2
     * @param slot3
     */
    public Answers(String slot1, String slot2, String slot3) {
        this(slot1,slot2,slot3,null,null);
    }

    /**
     * Sets up the answer slots 1,2,3 and 4
     * @param slot1
     * @param slot2
     * @param slot3
     * @param Slot4
     */
    public Answers(String slot1, String slot2, String slot3, String Slot4) {
        this(slot1,slot2,slot3,Slot4,null);
    }

    /**
     * Sets up values 1-5 if no parameters are provided
     * Used by rate questions
     */
    public Answers() {
        this("1","2","3","4","5");
    }

    /**
     * Returns all the answer slots as a arrayList
     * @return slotList (ArrayList)
     */
    public ArrayList<Slot> getSlotList() {
        return slotList;
    }

    /**
     * Returns slot 1
     * @return Slot
     */
    public Slot getSlot1() {
        return slotList.get(0);
    }

    /**
     * Returns slot 2
     * @return Slot
     */
    public Slot getSlot2() {
        return slotList.get(1);
    }

    /**
     * Returns slot 3
     * @return Slot
     */
    public Slot getSlot3() {
        return slotList.get(2);
    }

    /**
     * Returns slot 4
     * @return Slot
     */
    public Slot getSlot4() {
        return slotList.get(3);
    }

    /**
     * Returns slot 5
     * @return Slot
     */
    public Slot getSlot5() {
        return slotList.get(4);
    }

    /**
     * This class consists of the String value, and the boolean representing whether it's selected
     * Innder class of the Answers outer class
     * @author Kavishka Timashan
     */
    public class Slot{
        private String answer;
        private boolean select;

        /**
         * Constructor
         * @param answer
         */
        public Slot(String answer) {
            this.answer = answer;
            this.select = false;
        }

        /**
         * Returns the answer
         * @return answer (String)
         */
        public String getAnswer() {
            return answer;
        }

        /**
         * Returns the true/false if the answer was selected
         * @return select (Boolean)
         */
        public boolean isSelect() {
            return select;
        }

        /**
         * Sets select boolean to true
         * @param select
         */
        public void setSelect(boolean select) {
            this.select = select;
        }
    }
}
