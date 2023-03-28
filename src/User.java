public class User {
    int xpos, ypos;
    int respect, feared, health;
    String name;

    User(String name, int xpos, int ypos){
        this.xpos = xpos;
        this.ypos = ypos;
        this.name = name;
    }

    void setRespect(int newRespect){
        respect = newRespect;
    }

    void setHealth(int newHealth){
        health = newHealth;
    }

    void setFeared(int newFeared){
        feared = newFeared;
    }
}
