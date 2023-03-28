import jdk.dynalink.linker.support.Guards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;


public class BasicGameApp implements Runnable, KeyListener, MouseListener {
    // Global variables

    public int WIDTH = 600;
    public int HEIGHT = 600;

    public int gridRatio = WIDTH/9;

    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public int userX = 250;
    public int userY = 250;
//    public int userX = 250;
//    public int userY = 250;

    // boolean to check if an NPC is speaking
    public boolean speaking = false;
    public String interactionText = "";
    public int counter = 0;



    public BufferStrategy bufferStrategy;

    // Okonkwo's starting position
    public int startingX = 250;
    public int startingY = 250;

    // map
    public int[][] map = new int[500][500];
    /*
    0 = Okonkwo
    1 = grass
    2 = water
    3 = beach/sand

    100+ = NPC numbers

    family NPCs:
    101 = Nwoye
    102 = Enzima
    103 = Ekwefi

    tribe:
    104 = Obierka
    105 = Chielo
    106 = Enoch

    European colonizers:
    107 = The District Commissioner
    108 = Mr. Smith
    109 = Mr. Brown
    110 = Guards/physical opposition
     */

    // shading arrays
    int[][] grassshades = new int[map.length][map[0].length];
    int[][] sandshades = new int[map.length][map[0].length];


    public Image OkonkwoFace, NwoyeFace, EnzimaFace, EkwefiFace, ObierkaFace, ChieloFace, EnochFace,
        DistrictCommissionerFace, MrSmithFace, MrBrownFace, GuardsFace, WombatFace;


    public int currBlock = 1;

    public int speed = 1;

    public int interactionsRemaining = 10;


    // user
    public User Okonkwo = new User("Okonkwo", startingX, startingY);

    // general meters
    public int tribeUnity, colonizerInfluence;

    // NPCs
    public NPC Nwoye, Enzima, Ekwefi,
            Obierka, Chielo, Enoch, DistrictCommissioner, MrSmith, MrBrown, Guards1, Guards2, Guards3;

    // boolean for close up vs overall map
    public boolean fullView = false;


    //public NPC


    public void run() {
        // setting up user
        // meters (out of 100)
        Okonkwo.setFeared(50);
        Okonkwo.setHealth(100);
        Okonkwo.setRespect(50);


        // general meters
        tribeUnity = 30;
        colonizerInfluence = 60;


        // setting up map
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(!(j == userX && i == userY)){
                    map[i][j] = 1;
                }
                if(i < 24 || i > map.length-24 || j < 24 || j > map[i].length-24){
                    map[i][j] = 3;
                }
                if(i < 21 || i > map.length-21 || j < 21 || j > map[i].length-21){
                    map[i][j] = 2;
                }
//                double shade = Math.random();
//                g.setColor(Color.getHSBColor(80 + (int)(70*shade), 100, 35 + (int)(65*shade)));
                double shade = Math.random();
                grassshades[i][j] = 225 + (int)(8*shade);
                double sandshade = Math.random();
                sandshades[i][j] = 170 + (int)(20*sandshade);
            }
        }

        map[map.length-23][23] = 200; // easter egg

        // user
        OkonkwoFace = Toolkit.getDefaultToolkit().getImage("okonkwo.png");

        // easter egg
        WombatFace = Toolkit.getDefaultToolkit().getImage("wombat.png");


        // setting up NPCs
        Nwoye = new NPC();
        Nwoye.x = 300;
        Nwoye.y = 250;
        Nwoye.name = "Nwoye";
        map[Nwoye.y][Nwoye.x] = 101;
        Nwoye.voiceLines = new String[]{"Nwoye: I don't know what to believe anymore. \n            Our traditions represent only violence; there is no room at all for compassion.", "Nwoye: I need some time to think, we can talk later."};
        NwoyeFace = Toolkit.getDefaultToolkit().getImage("nwoye.png");

        Enzima = new NPC();
        Enzima.x = 400;
        Enzima.y = 300;
        Enzima.name = "Ezinma";
        map[Enzima.y][Enzima.x] = 102;
        Enzima.voiceLines = new String[]{"Ezinma: I hate being told what to do, \n            but it might be time to put my own needs aside for the greater good of the tribe.", "Ezinma: I have errands to run, we can talk later."};
        EnzimaFace = Toolkit.getDefaultToolkit().getImage("ezinma.png");

        Ekwefi = new NPC();
        Ekwefi.x = 150;
        Ekwefi.y = 100;
        Ekwefi.name = "Ekwefi";
        map[Ekwefi.y][Ekwefi.x] = 103;
        Ekwefi.voiceLines = new String[]{"Ekwefi: It is important that our tribe sticks together in these tough times. \n            We need to recognize the importance of community.", "Ekwefi: I have work to do, come back when you need me."};
        EkwefiFace = Toolkit.getDefaultToolkit().getImage("ekwefi.png");

        Obierka = new NPC();
        Obierka.x = 450;
        Obierka.y = 350;
        Obierka.name = "Obierika";
        map[Obierka.y][Obierka.x] = 104;
        Obierka.voiceLines = new String[]{"Obierika: Our beliefs are what hold our tribe together. \n            We need to protect them.", "Obierika: I have to attend to some business, we can talk later."};
        ObierkaFace = Toolkit.getDefaultToolkit().getImage("obierika.png");

        Chielo = new NPC();
        Chielo.x = 90;
        Chielo.y = 200;
        Chielo.name = "Chielo";
        map[Chielo.y][Chielo.x] = 105;
        Chielo.voiceLines = new String[]{"Chielo: I feel a deep connection to our spiritual traditions and our generation-old wisdom. \n            No one can take it away from us.", "Chielo: Hush. Not now."};
        ChieloFace = Toolkit.getDefaultToolkit().getImage("chielo.png");

        Enoch = new NPC();
        Enoch.x = 100;
        Enoch.y = 250;
        Enoch.name = "Enoch";
        map[Enoch.y][Enoch.x] = 106;
        Enoch.voiceLines = new String[]{"Enoch: The Europeans have shown me a better way of life, \n            where my past is of no concern.", "Enoch: I have nothing to say to you."};
        EnochFace = Toolkit.getDefaultToolkit().getImage("enoch.png");

        DistrictCommissioner = new NPC();
        DistrictCommissioner.x = 450;
        DistrictCommissioner.y = 50;
        DistrictCommissioner.name = "The District Commissioner";
        map[DistrictCommissioner.y][DistrictCommissioner.x] = 107;
        DistrictCommissioner.voiceLines = new String[]{"The District Commissioner: I am here to bring order to your tribe \n            and instill more productive ideals into your culture. Do not oppose me.", "The District Commissioner: Stop wasting my time."};
        DistrictCommissionerFace = Toolkit.getDefaultToolkit().getImage("districtcommissioner.png");

        MrSmith = new NPC();
        MrSmith.x = 260;
        MrSmith.y = 400;
        MrSmith.name = "Mr. Smith";
        map[MrSmith.y][MrSmith.x] = 108;
        MrSmith.voiceLines = new String[]{"Mr. Smith: The people of this tribe need European guidance. They are disorderly and unproductive.", "Mr. Smith: We have nothing to discuss. Go."};
        MrSmithFace = Toolkit.getDefaultToolkit().getImage("mrsmith.png");

        MrBrown = new NPC();
        MrBrown.x = 150;
        MrBrown.y = 100;
        MrBrown.name = "Mr. Brown";
        map[MrBrown.y][MrBrown.x] = 109;
        MrBrown.voiceLines = new String[]{"Mr. Brown: It is my duty as a Christian to spread the word of God to your tribe through education and understanding.", "Mr. Brown: I'm very busy with missionary work, come back later."};
        MrBrownFace = Toolkit.getDefaultToolkit().getImage("mrbrown.png");

        Guards1 = new NPC();
        Guards1.x = 300;
        Guards1.y = 300;
        Guards1.name = "Commissioner's Guards";
        Guards1.voiceLines = new String[]{"Commissioner's Guards: Your land belongs to us now, Okonkwo! \n            We're watching you, don't step out of line.", "Commissioner's Guards: Get out of here before we run out of patience."};
        map[Guards1.y][Guards1.x] = 110;

        Guards2 = new NPC();
        Guards2.x = 250;
        Guards2.y = 100;
        Guards2.name = "Commissioner's Guards";
        //Guards2.voiceLines = new String[]{"Commissioner's Guards: ", "Commissioner's Guards: "};
        map[Guards2.y][Guards2.x] = 110;

        Guards3 = new NPC();
        Guards3.x = 200;
        Guards3.y = 273;
        Guards3.name = "Commissioner's Guards";
        map[Guards3.y][Guards3.x] = 110;
        //Guards3.voiceLines = new String[]{"Commissioner's Guards: ", "Commissioner's Guards: "};
        GuardsFace = Toolkit.getDefaultToolkit().getImage("guards.png");

        // runs 60x per second (draws graphics)
        while(true){
            render();
        }
    }

    public void render(){
        // start
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        //g.clearRect(0, 0, WIDTH, HEIGHT);

        if(!fullView) {

            //draw map
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {


                    if (map[userY + i - 4][userX + j - 4] == 0) { // user
//                        g.setColor(Color.black);
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(OkonkwoFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }


                    if (map[userY + i - 4][userX + j - 4] == 1) { // grass
//                        g.setColor(Color.green);
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
//                        g.setColor(Color.getHSBColor(hue[i][j], 100, brightness[i][j]));
                        g.setColor(new Color(0, grassshades[userY + i - 4][userX + j - 4], 0));
                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 2) { // water
                        g.setColor(Color.blue);
                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 3) { // beach/sand
                        //g.setColor(Color.YELLOW);
                        g.setColor(new Color(255, sandshades[userY + i - 4][userX + j - 4], 0));
                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                    }




                    if (map[userY + i - 4][userX + j - 4] == 101) { // Nwoye
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(NwoyeFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 102) { // Enzima
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(EnzimaFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 103) { // Ekwefi
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(EkwefiFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 104) { // Obierka
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(ObierkaFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 105) { // Chielo
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(ChieloFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 106) { // Enoch
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(EnochFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 107) { // The District Commissioner
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(DistrictCommissionerFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 108) { // Mr. Smith
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(MrSmithFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 109) { // Mr. Brown
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(MrBrownFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }
                    if (map[userY + i - 4][userX + j - 4] == 110) { // Guards
//                        g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                        g.fillRect(j * gridRatio, i * gridRatio, gridRatio, gridRatio);
                        g.drawImage(GuardsFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }

                    if(map[userY + i - 4][userX + j - 4] == 200){ // easter egg
                        g.drawImage(WombatFace, j * gridRatio, i * gridRatio, gridRatio, gridRatio, null);
                    }

                }
            }

            if(currBlock == 200){
                currBlock = 3;
                OkonkwoFace = Toolkit.getDefaultToolkit().getImage("wombat.png");
            }



            if(speaking){ // display NPC messages
                g.setColor(Color.black);
                //g.drawString(interactionText, 20, 100);
                int veryTemp = 100;
                for (String line : interactionText.split("\n"))
                    g.drawString(line, 20, veryTemp += g.getFontMetrics().getHeight());
                counter++;
            }
            if(counter >= 3000){
                speaking = false;
                counter = 0;
                interactionsRemaining--;
            }
//            g.setColor(Color.black);
//            g.drawString("This is a test", 100, 100);





        }




        if(interactionsRemaining <= 0){
            g.setColor(Color.white);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
            g.setFont(newFont);
            g.setColor(Color.black);
            if(colonizerInfluence > 100){
                g.drawString("Okonkwo's actions led the Europeans to immediately initiate a large scale", 20, 100);
                g.drawString("plan that effectively eradicated any form of resistance from the tribe.", 20, 150);
                g.drawString("Okonkwo's friends and family were put under colonizer control.", 20, 200);
                g.drawString("The District Commissioner's book sold one million copies.", 20, 250);
            }
            else if(colonizerInfluence < 30 && tribeUnity < 30){
                g.drawString("Okonkwo was able to drive the European colonizers out of his tribe.", 20, 100);
                g.drawString("However, his community was left in shambles and never recovered.", 20, 150);
                g.drawString("Nwoye left Africa and became famous for his scientific achievements.", 20, 200);
                g.drawString("Specifically, his work with the marsupial \"Vombatus ursinus\" was notable.", 20, 250);
            }
            else if(colonizerInfluence < 30){
                g.drawString("Now unified, Okonkwo's tribe was able to drive the colonizers away.", 20, 100);
                g.drawString("Although there were some that were still doubtful of their culture,", 20, 150);
                g.drawString("their community was able to live in harmony.", 20, 200);
                g.drawString("Through their new trading methods provided by the Europeans,", 20, 250);
                g.drawString("Okonkwo's family was able to build a yam farming empire,", 20, 300);
                g.drawString("eventually owning more than 10% of all wealth on the planet.", 20, 350);
            }
            else{
                g.drawString("While the Europeans did not expand their influence in the area,", 20, 100);
                g.drawString("they continued to slowly increase the cultural divide within Umuofia.", 20, 150);
                g.drawString("Over the following years Okonkwo regained the respect of his tribe.", 20, 200);
            }


        }


        // end
        g.dispose();
        bufferStrategy.show();
    }


    private void setUpGraphics() {

        frame = new JFrame("Application Template");

        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        System.out.println("DONE graphic setup");
    }

    public BasicGameApp() {

        setUpGraphics();


    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        //g.clearRect(0, 0, WIDTH, HEIGHT);

        if (e.getKeyChar() == ' ') {

            if(!fullView) {
                fullView = true;
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[i].length; j++) {


                        if (map[i][j] == 0) { // user
//                            g.setColor(Color.black); // temporary, will swap out with pictures
//                            g.fillRect(j + 50, i + 50, 1, 1);
                            g.drawImage(OkonkwoFace, j + 50, i + 50, 1, 1, null);
                        }


                        if (map[i][j] == 1) { // grass
                            g.setColor(Color.green);
                            g.fillRect(j + 50, i + 50, 1, 1);
                        }
                        if (map[i][j] == 2) { // water
                            g.setColor(Color.blue);
                            g.fillRect(j + 50, i + 50, 1, 1);
                        }
                        if (map[i][j] == 3) { // beach/sand
                            g.setColor(Color.YELLOW);
                            g.fillRect(j + 50, i + 50, 1, 1);
                        }

                    }
                }




//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(Nwoye.x + 50, Nwoye.y + 50, 20, 20);
                g.drawImage(NwoyeFace, Nwoye.x + 50, Nwoye.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(Enzima.x + 50, Enzima.y + 50, 20, 20);
                g.drawImage(EnzimaFace, Enzima.x + 50, Enzima.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(Ekwefi.x + 50, Ekwefi.y + 50, 20, 20);
                g.drawImage(EkwefiFace, Ekwefi.x + 50, Ekwefi.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(Obierka.x + 50, Obierka.y + 50, 20, 20);
                g.drawImage(ObierkaFace, Obierka.x + 50, Obierka.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(Chielo.x + 50, Chielo.y + 50, 20, 20);
                g.drawImage(ChieloFace, Chielo.x + 50, Chielo.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(Enoch.x + 50, Enoch.y + 50, 20, 20);
                g.drawImage(EnochFace, Enoch.x + 50, Enoch.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(DistrictCommissioner.x + 50, DistrictCommissioner.y + 50, 20, 20);
                g.drawImage(DistrictCommissionerFace, DistrictCommissioner.x + 50, DistrictCommissioner.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(MrSmith.x + 50, MrSmith.y + 50, 20, 20);
                g.drawImage(MrSmithFace, MrSmith.x + 50, MrSmith.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(MrBrown.x + 50, MrBrown.y + 50, 20, 20);
                g.drawImage(MrBrownFace, MrBrown.x + 50, MrBrown.y + 50, 20, 20, null);

//                g.setColor(new Color(100, 10, 10)); // temporary, will swap out with pictures
//                g.fillRect(Guards1.x + 50, Guards1.y + 50, 20, 20);
//                g.fillRect(Guards2.x + 50, Guards2.y + 50, 20, 20);
//                g.fillRect(Guards3.x + 50, Guards3.y + 50, 20, 20);
                g.drawImage(GuardsFace, Guards1.x + 50, Guards1.y + 50, 20, 20, null);
                g.drawImage(GuardsFace, Guards2.x + 50, Guards2.y + 50, 20, 20, null);
                g.drawImage(GuardsFace, Guards3.x + 50, Guards3.y + 50, 20, 20, null);



//                g.setColor(Color.black); // user
//                g.fillRect(userX+50, userY+50, 20, 20);
                g.drawImage(OkonkwoFace, userX+50, userY+50, 20, 20, null); // user
            }




        }



        if(e.getKeyChar() == 'w'){
            int tempY = userY-speed;
            if(map[tempY][userX] == 1 || map[tempY][userX] == 3 || map[tempY][userX] == 200){
                //map[userY][userX] = map[tempY][userX];
                map[userY][userX] = currBlock;
                currBlock = map[tempY][userX];
                map[tempY][userX] = 0;
                userY = tempY;
            }


            if(map[tempY][userX] == 101){ // Nwoye
                if(Nwoye.timesInteracted < Nwoye.voiceLines.length){
                    interactionText = Nwoye.voiceLines[Nwoye.timesInteracted];
                }else{
                    interactionText = Nwoye.voiceLines[Nwoye.voiceLines.length-1];
                }

                if(!speaking){
                    Nwoye.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 5;
                }
                speaking = true;

            }
            if(map[tempY][userX] == 102){ // Enzima
                if(Enzima.timesInteracted < Enzima.voiceLines.length){
                    interactionText = Enzima.voiceLines[Enzima.timesInteracted];
                }else{
                    interactionText = Enzima.voiceLines[Enzima.voiceLines.length-1];
                }

                if(!speaking){
                    Enzima.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 103){ // Ekwefi
                if(Ekwefi.timesInteracted < Ekwefi.voiceLines.length){
                    interactionText = Ekwefi.voiceLines[Ekwefi.timesInteracted];
                }else{
                    interactionText = Ekwefi.voiceLines[Ekwefi.voiceLines.length-1];
                }

                if(!speaking){
                    Ekwefi.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 104){ // Obierka
                if(Obierka.timesInteracted < Obierka.voiceLines.length){
                    interactionText = Obierka.voiceLines[Obierka.timesInteracted];
                }else{
                    interactionText = Obierka.voiceLines[Obierka.voiceLines.length-1];
                }

                if(!speaking){
                    Obierka.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 105){ // Chielo
                if(Chielo.timesInteracted < Chielo.voiceLines.length){
                    interactionText = Chielo.voiceLines[Chielo.timesInteracted];
                }else{
                    interactionText = Chielo.voiceLines[Chielo.voiceLines.length-1];
                }

                if(!speaking){
                    Chielo.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 15;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 106){ // Enoch
                if(Enoch.timesInteracted < Enoch.voiceLines.length){
                    interactionText = Enoch.voiceLines[Enoch.timesInteracted];
                }else{
                    interactionText = Enoch.voiceLines[Enoch.voiceLines.length-1];
                }

                if(!speaking){
                    Enoch.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 107){ // DistrictCommissioner
                if(DistrictCommissioner.timesInteracted < DistrictCommissioner.voiceLines.length){
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.timesInteracted];
                }else{
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.voiceLines.length-1];
                }

                if(!speaking){
                    DistrictCommissioner.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 15;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 108){ // MrSmith
                if(MrSmith.timesInteracted < MrSmith.voiceLines.length){
                    interactionText = MrSmith.voiceLines[MrSmith.timesInteracted];
                }else{
                    interactionText = MrSmith.voiceLines[MrSmith.voiceLines.length-1];
                }

                if(!speaking){
                    MrSmith.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 109){ // MrBrown
                if(MrBrown.timesInteracted < MrBrown.voiceLines.length){
                    interactionText = MrBrown.voiceLines[MrBrown.timesInteracted];
                }else{
                    interactionText = MrBrown.voiceLines[MrBrown.voiceLines.length-1];
                }

                if(!speaking){
                    MrBrown.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 5;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 110){ // Guards
                if(Guards1.timesInteracted < Guards1.voiceLines.length){
                    interactionText = Guards1.voiceLines[Guards1.timesInteracted];
                }else{
                    interactionText = Guards1.voiceLines[Guards1.voiceLines.length-1];
                }

                if(!speaking){
                    Guards1.timesInteracted++;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
        }


        if(e.getKeyChar() == 'a'){
            int tempX = userX-speed;
            if(map[userY][tempX] == 1 || map[userY][tempX] == 3 || map[userY][tempX] == 200){
//                map[userY][userX] = map[userY][tempX];
                map[userY][userX] = currBlock;
                currBlock = map[userY][tempX];
                map[userY][tempX] = 0;
                userX = tempX;
            }

/*
            if(map[userY][tempX] == 101){ // Nwoye
                if(Nwoye.timesInteracted < Nwoye.voiceLines.length){
                    interactionText = Nwoye.voiceLines[Nwoye.timesInteracted];
                }else{
                    interactionText = Nwoye.voiceLines[Nwoye.voiceLines.length-1];
                }

                if(!speaking){
                    Nwoye.timesInteracted++;
                }
                speaking = true;
            }

 */

            if(map[userY][tempX] == 101){ // Nwoye
                if(Nwoye.timesInteracted < Nwoye.voiceLines.length){
                    interactionText = Nwoye.voiceLines[Nwoye.timesInteracted];
                }else{
                    interactionText = Nwoye.voiceLines[Nwoye.voiceLines.length-1];
                }

                if(!speaking){
                    Nwoye.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 5;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 102){ // Enzima
                if(Enzima.timesInteracted < Enzima.voiceLines.length){
                    interactionText = Enzima.voiceLines[Enzima.timesInteracted];
                }else{
                    interactionText = Enzima.voiceLines[Enzima.voiceLines.length-1];
                }

                if(!speaking){
                    Enzima.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 103){ // Ekwefi
                if(Ekwefi.timesInteracted < Ekwefi.voiceLines.length){
                    interactionText = Ekwefi.voiceLines[Ekwefi.timesInteracted];
                }else{
                    interactionText = Ekwefi.voiceLines[Ekwefi.voiceLines.length-1];
                }

                if(!speaking){
                    Ekwefi.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 104){ // Obierka
                if(Obierka.timesInteracted < Obierka.voiceLines.length){
                    interactionText = Obierka.voiceLines[Obierka.timesInteracted];
                }else{
                    interactionText = Obierka.voiceLines[Obierka.voiceLines.length-1];
                }

                if(!speaking){
                    Obierka.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 105){ // Chielo
                if(Chielo.timesInteracted < Chielo.voiceLines.length){
                    interactionText = Chielo.voiceLines[Chielo.timesInteracted];
                }else{
                    interactionText = Chielo.voiceLines[Chielo.voiceLines.length-1];
                }

                if(!speaking){
                    Chielo.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 15;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 106){ // Enoch
                if(Enoch.timesInteracted < Enoch.voiceLines.length){
                    interactionText = Enoch.voiceLines[Enoch.timesInteracted];
                }else{
                    interactionText = Enoch.voiceLines[Enoch.voiceLines.length-1];
                }

                if(!speaking){
                    Enoch.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 107){ // DistrictCommissioner
                if(DistrictCommissioner.timesInteracted < DistrictCommissioner.voiceLines.length){
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.timesInteracted];
                }else{
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.voiceLines.length-1];
                }

                if(!speaking){
                    DistrictCommissioner.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 15;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 108){ // MrSmith
                if(MrSmith.timesInteracted < MrSmith.voiceLines.length){
                    interactionText = MrSmith.voiceLines[MrSmith.timesInteracted];
                }else{
                    interactionText = MrSmith.voiceLines[MrSmith.voiceLines.length-1];
                }

                if(!speaking){
                    MrSmith.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 109){ // MrBrown
                if(MrBrown.timesInteracted < MrBrown.voiceLines.length){
                    interactionText = MrBrown.voiceLines[MrBrown.timesInteracted];
                }else{
                    interactionText = MrBrown.voiceLines[MrBrown.voiceLines.length-1];
                }

                if(!speaking){
                    MrBrown.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 5;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 110){ // Guards
                if(Guards1.timesInteracted < Guards1.voiceLines.length){
                    interactionText = Guards1.voiceLines[Guards1.timesInteracted];
                }else{
                    interactionText = Guards1.voiceLines[Guards1.voiceLines.length-1];
                }

                if(!speaking){
                    Guards1.timesInteracted++;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
        }


        if(e.getKeyChar() == 's'){
            int tempY = userY+speed;
            if(map[tempY][userX] == 1 || map[tempY][userX] == 3 || map[tempY][userX] == 200){
//                map[userY][userX] = map[tempY][userX];
                map[userY][userX] = currBlock;
                currBlock = map[tempY][userX];
                map[tempY][userX] = 0;
                userY = tempY;
            }


            if(map[tempY][userX] == 101){ // Nwoye
                if(Nwoye.timesInteracted < Nwoye.voiceLines.length){
                    interactionText = Nwoye.voiceLines[Nwoye.timesInteracted];
                }else{
                    interactionText = Nwoye.voiceLines[Nwoye.voiceLines.length-1];
                }

                if(!speaking){
                    Nwoye.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 5;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 102){ // Enzima
                if(Enzima.timesInteracted < Enzima.voiceLines.length){
                    interactionText = Enzima.voiceLines[Enzima.timesInteracted];
                }else{
                    interactionText = Enzima.voiceLines[Enzima.voiceLines.length-1];
                }

                if(!speaking){
                    Enzima.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 103){ // Ekwefi
                if(Ekwefi.timesInteracted < Ekwefi.voiceLines.length){
                    interactionText = Ekwefi.voiceLines[Ekwefi.timesInteracted];
                }else{
                    interactionText = Ekwefi.voiceLines[Ekwefi.voiceLines.length-1];
                }

                if(!speaking){
                    Ekwefi.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 104){ // Obierka
                if(Obierka.timesInteracted < Obierka.voiceLines.length){
                    interactionText = Obierka.voiceLines[Obierka.timesInteracted];
                }else{
                    interactionText = Obierka.voiceLines[Obierka.voiceLines.length-1];
                }

                if(!speaking){
                    Obierka.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 105){ // Chielo
                if(Chielo.timesInteracted < Chielo.voiceLines.length){
                    interactionText = Chielo.voiceLines[Chielo.timesInteracted];
                }else{
                    interactionText = Chielo.voiceLines[Chielo.voiceLines.length-1];
                }

                if(!speaking){
                    Chielo.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 15;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 106){ // Enoch
                if(Enoch.timesInteracted < Enoch.voiceLines.length){
                    interactionText = Enoch.voiceLines[Enoch.timesInteracted];
                }else{
                    interactionText = Enoch.voiceLines[Enoch.voiceLines.length-1];
                }

                if(!speaking){
                    Enoch.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 107){ // DistrictCommissioner
                if(DistrictCommissioner.timesInteracted < DistrictCommissioner.voiceLines.length){
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.timesInteracted];
                }else{
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.voiceLines.length-1];
                }

                if(!speaking){
                    DistrictCommissioner.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 15;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 108){ // MrSmith
                if(MrSmith.timesInteracted < MrSmith.voiceLines.length){
                    interactionText = MrSmith.voiceLines[MrSmith.timesInteracted];
                }else{
                    interactionText = MrSmith.voiceLines[MrSmith.voiceLines.length-1];
                }

                if(!speaking){
                    MrSmith.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 109){ // MrBrown
                if(MrBrown.timesInteracted < MrBrown.voiceLines.length){
                    interactionText = MrBrown.voiceLines[MrBrown.timesInteracted];
                }else{
                    interactionText = MrBrown.voiceLines[MrBrown.voiceLines.length-1];
                }

                if(!speaking){
                    MrBrown.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 5;
                }
                speaking = true;
            }
            if(map[tempY][userX] == 110){ // Guards
                if(Guards1.timesInteracted < Guards1.voiceLines.length){
                    interactionText = Guards1.voiceLines[Guards1.timesInteracted];
                }else{
                    interactionText = Guards1.voiceLines[Guards1.voiceLines.length-1];
                }

                if(!speaking){
                    Guards1.timesInteracted++;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
        }


        if(e.getKeyChar() == 'd'){
            int tempX = userX+speed;
            if(map[userY][tempX] == 1 || map[userY][tempX] == 3 || map[userY][tempX] == 200){
//                map[userY][userX] = map[userY][tempX];
                map[userY][userX] = currBlock;
                currBlock = map[userY][tempX];
                map[userY][tempX] = 0;
                userX = tempX;
            }


//            if(map[userY][tempX] == 101){ // Nwoye
////                g.drawString("This is a test", 100, 100);
//                speaking = true;
//                interactionText = "This is a test";
//            }
            /*if(map[userY][tempX] == 101){ // Nwoye
                if(Nwoye.timesInteracted < Nwoye.voiceLines.length){
                    interactionText = Nwoye.voiceLines[Nwoye.timesInteracted];
                }else{
                    interactionText = Nwoye.voiceLines[Nwoye.voiceLines.length-1];
                }

                if(!speaking){
                    Nwoye.timesInteracted++;
                }
                speaking = true;
            }*/
            if(map[userY][tempX] == 101){ // Nwoye
                if(Nwoye.timesInteracted < Nwoye.voiceLines.length){
                    interactionText = Nwoye.voiceLines[Nwoye.timesInteracted];
                }else{
                    interactionText = Nwoye.voiceLines[Nwoye.voiceLines.length-1];
                }

                if(!speaking){
                    Nwoye.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 5;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 102){ // Enzima
                if(Enzima.timesInteracted < Enzima.voiceLines.length){
                    interactionText = Enzima.voiceLines[Enzima.timesInteracted];
                }else{
                    interactionText = Enzima.voiceLines[Enzima.voiceLines.length-1];
                }

                if(!speaking){
                    Enzima.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 103){ // Ekwefi
                if(Ekwefi.timesInteracted < Ekwefi.voiceLines.length){
                    interactionText = Ekwefi.voiceLines[Ekwefi.timesInteracted];
                }else{
                    interactionText = Ekwefi.voiceLines[Ekwefi.voiceLines.length-1];
                }

                if(!speaking){
                    Ekwefi.timesInteracted++;
                    tribeUnity += 10;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 104){ // Obierka
                if(Obierka.timesInteracted < Obierka.voiceLines.length){
                    interactionText = Obierka.voiceLines[Obierka.timesInteracted];
                }else{
                    interactionText = Obierka.voiceLines[Obierka.voiceLines.length-1];
                }

                if(!speaking){
                    Obierka.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 105){ // Chielo
                if(Chielo.timesInteracted < Chielo.voiceLines.length){
                    interactionText = Chielo.voiceLines[Chielo.timesInteracted];
                }else{
                    interactionText = Chielo.voiceLines[Chielo.voiceLines.length-1];
                }

                if(!speaking){
                    Chielo.timesInteracted++;
                    tribeUnity += 15;
                    colonizerInfluence -= 15;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 106){ // Enoch
                if(Enoch.timesInteracted < Enoch.voiceLines.length){
                    interactionText = Enoch.voiceLines[Enoch.timesInteracted];
                }else{
                    interactionText = Enoch.voiceLines[Enoch.voiceLines.length-1];
                }

                if(!speaking){
                    Enoch.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 107){ // DistrictCommissioner
                if(DistrictCommissioner.timesInteracted < DistrictCommissioner.voiceLines.length){
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.timesInteracted];
                }else{
                    interactionText = DistrictCommissioner.voiceLines[DistrictCommissioner.voiceLines.length-1];
                }

                if(!speaking){
                    DistrictCommissioner.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 15;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 108){ // MrSmith
                if(MrSmith.timesInteracted < MrSmith.voiceLines.length){
                    interactionText = MrSmith.voiceLines[MrSmith.timesInteracted];
                }else{
                    interactionText = MrSmith.voiceLines[MrSmith.voiceLines.length-1];
                }

                if(!speaking){
                    MrSmith.timesInteracted++;
                    tribeUnity -= 10;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 109){ // MrBrown
                if(MrBrown.timesInteracted < MrBrown.voiceLines.length){
                    interactionText = MrBrown.voiceLines[MrBrown.timesInteracted];
                }else{
                    interactionText = MrBrown.voiceLines[MrBrown.voiceLines.length-1];
                }

                if(!speaking){
                    MrBrown.timesInteracted++;
                    tribeUnity -= 5;
                    colonizerInfluence += 5;
                }
                speaking = true;
            }
            if(map[userY][tempX] == 110){ // Guards
                if(Guards1.timesInteracted < Guards1.voiceLines.length){
                    interactionText = Guards1.voiceLines[Guards1.timesInteracted];
                }else{
                    interactionText = Guards1.voiceLines[Guards1.voiceLines.length-1];
                }

                if(!speaking){
                    Guards1.timesInteracted++;
                    colonizerInfluence += 10;
                }
                speaking = true;
            }
        }








        if(e.getKeyChar() == 'k'){
            if(speed == 1){
                speed = 3;
            }else if(speed == 3){
                speed = 1;
            }
        }


        g.dispose();
        bufferStrategy.show();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar() == ' '){
            fullView = false;
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}