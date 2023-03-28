public class NPC {
    String name;
    int x, y;
    int timesInteracted;

    String[] voiceLines;

    NPC(String name, int x, int y, String[] voiceLines){
        this.name = name;
        this.x = x;
        this.y = y;
        this.voiceLines = voiceLines;
    }

    NPC(){

    }


}
