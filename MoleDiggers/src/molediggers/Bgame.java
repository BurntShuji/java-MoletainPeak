package molediggers;

import java.awt.Image;
//import static java.awt.Image.SCALE_SMOOTH;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sounds.Sound; //This is to import code from another package

/**
 * OBJOPROG FINALS OUTPUT
 * BS COMPUTER ENGINEERING 
 */
public class Bgame extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Bgame.class.getName());
    //Additional Variable Declarations
    private String p1, p2; //Name of Players
    private int s1 = 0, s2 = 0;//Score point Main GAme
    private int turn = 1;//default turn
    private DatabaseQ data = new DatabaseQ();//Access to Database
    
    private String cAnswer = "";//Holds the Answer 
    private boolean GoldQuestion = false;//Question For Gold
    private String questiondifficulty;//Default of Question
    Random rand = new Random();
    Timer moveTime;
    Timer whamTime;
    Timer countdown;
    int timeleft = 60;//Timer
    int moleTile;
    int trapTile;
    int goldTile;
    boolean isTrue = false;
    //int[] Mole = new int[9];
    int whamScore = 0; //This is for Whac-A-Mole Scoring
    int lives = 3; //This is for Whac-A-Mole Life
    ImageIcon moleIcon; //ImageIcons ___ are variables for the images
    ImageIcon trapIcon; 
    ImageIcon goldIcon;
    int roundCount=0;
    //Object Declarations
    private Leaderboard winners = new Leaderboard(); 
     private Sound soundEffects = new Sound();; //This is for the game sound effects
    
    public Bgame(String p1, String p2) {
        initComponents();
        this.p1=p1;
        this.p2=p2;
        P1NameHolder.setText(p1);
        P2NameHolder.setText(p2);
        turns();
        try{ //Image initialization
            Image moleImg = new ImageIcon(getClass().getResource("/molediggers/Mole.png")).getImage();
            this.moleIcon = new ImageIcon(moleImg.getScaledInstance(105, 97, Image.SCALE_SMOOTH));
            Image trapImg = new ImageIcon(getClass().getResource("/molediggers/Trap.png")).getImage();
            this.trapIcon = new ImageIcon(trapImg.getScaledInstance(105, 97, Image.SCALE_SMOOTH));
            Image goldImg = new ImageIcon(getClass().getResource("/molediggers/Gold.png")).getImage();
            this.goldIcon = new ImageIcon(goldImg.getScaledInstance(105, 97, Image.SCALE_SMOOTH));
        } catch (Exception e){
            System.out.println("ERROR: Could not find image files!");
        }
    
    moveTime = new Timer(900, new ActionListener(){ //This is a 0.9 secs delay before the tiles switch places
            @Override
                    public void actionPerformed(ActionEvent e){
                        moleTile = rand.nextInt(9) + 1;
                        do{
                            trapTile = rand.nextInt(9) + 1;
                        } while (trapTile == moleTile); //this ensures the two tiles won't spawn at the same tile
                        goldTile = -1;
                        if(rand.nextInt(100) < 10){ //this to make the gold appear at 10% chance
                            do{
                                goldTile = rand.nextInt(9) + 1;
                            } while (goldTile == moleTile || goldTile == trapTile);
                            isTrue = true;
                        }
                        setButtonIcon(moleTile, moleIcon);
                        setButtonIcon(trapTile, trapIcon);
                        if(isTrue){
                            setButtonIcon(goldTile, goldIcon);
                            isTrue = false;
                        }
                        MoleCondition(moleTile);
                    }            
        });

        //Initializing Whac-A-Mole Timer
        whamTime = new Timer(1500, new ActionListener(){//1.5sec.
           @Override
                public void actionPerformed(ActionEvent a){
                    timeleft--;
                    TimerHolder.setText( timeleft + "s");
                    if (timeleft <= 0) {
                    whamTime.stop();
                    moveTime.stop();
                    
                    lives = 3;
                    whamScore = 0;
                    
                    LivesHolder.setText("Lives: " + lives);
                    ScoreHolder.setText("SCORE: " + whamScore);
                    
                    Sturn();
                    startTimer();
                    }     
                } 
        });
        //Initializing Window Pop-up
        setLocationRelativeTo(null); //Load at center of the screen
        setResizable(false); // Disabling window resizing
        whamTime.stop();
        moveTime.stop();
        StartCount();
    }
    private void StartCount(){//This is for the interval of game 3sec
        soundEffects.countSfx(); 
        countdown = new Timer(1000, null);
        final int[] count = {3};
        countdown.addActionListener(e -> {
            if (count[0] > 0) {
                jLabel11.setText(String.valueOf(count[0]));
                count[0]--;
            } else {
                jLabel11.setText(""); 
                countdown.stop();
                moveTime.start();
                whamTime.start();
                  if(roundCount == 0){ //This is to avoid too much songs being looped after player switch
                    soundEffects.bgm();
                }
                ++roundCount;
            }
        });
        countdown.start();
    }
    private void turns(){//Turn Indicator for the textField
        String name = (turn == 1) ? p1 : p2;
        TurnIndicatorHolder.setText(name + "'S Turn");
    }

    private void Points(int pts) {//Main GAme Points
        if (turn == 1) {
            s1 += pts;
            Pt1Holder.setText(String.valueOf(s1));
        } else {
            s2 += pts;
            Pt2Holder.setText(String.valueOf(s2));
        }
        Winner();
    }

    private void Winner() {//Indicates the winner when reaches 30points
        if (s1 >= 30 || s2 >= 30) {
            String winner = (s1 >= 30) ? p1 : p2;
            int winningScore = (s1 >= 30) ? s1 : s2; 
            //JOptionPane.showMessageDialog(this, winner + " WINS!");
            moveTime.stop();
            whamTime.stop();
            SubmitBtn.setEnabled(false);
            winners.addScore(winner, winningScore);
            winners.getTopScores();
            int reply = JOptionPane.showConfirmDialog(this, winner + " WINS!");
            if(reply == JOptionPane.OK_OPTION){
                Menu leadb = new Menu();
                leadb.setVisible(true);
                this.dispose();
        }
    }
    }
    private void reset(){//Reset of Time Lives whamScore
        whamScore=0;
        lives=3;
        startTimer();
    }
    private void resetQuestDif(){//For the reset of question difficulty
        questiondifficulty="Easy";//Default difficulty
    }
    private void Sturn() {//SwitchTurn
        resetMoles(); //To clear the Whac A Mole board before another player's turn
        turn = (turn == 1) ? 2 : 1;
        turns();
        resetQuestDif();
        UpDifficulty();
        
    }
    
    private void startTimer() {//WhamTimer
        timeleft = 60;
        
        TimerHolder.setText(timeleft + "s");
        StartCount();
    }
    
    private void Aquest(String difficulty, boolean gold) {//Question Provider
        String GoldDifficulty = difficulty;
        if (gold) {
            if (difficulty.equals("Easy")) {// gold must be medium or Hard Questions
                GoldDifficulty = "Medium";
            } else {
                GoldDifficulty = "Hard";
            }
        }
        Question q = data.getRandomQuestion(difficulty);
        if (q == null) return;
        QuestionHolder.setText(q.quest);
        cAnswer = q.ans;
        GoldQuestion = gold;
        AnswerInput.setText("");
    }
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        WhacAMoleBg = new javax.swing.JPanel();
        ScoreHolder = new javax.swing.JLabel();
        TimerHolder = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        Molebtn1 = new javax.swing.JButton();
        Molebtn3 = new javax.swing.JButton();
        Molebtn2 = new javax.swing.JButton();
        Molebtn5 = new javax.swing.JButton();
        Molebtn4 = new javax.swing.JButton();
        Molebtn6 = new javax.swing.JButton();
        Molebtn7 = new javax.swing.JButton();
        Molebtn8 = new javax.swing.JButton();
        Molebtn9 = new javax.swing.JButton();
        LivesHolder = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        P1NameHolder = new javax.swing.JTextField();
        Pt1Holder = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        P2NameHolder = new javax.swing.JTextField();
        Pt2Holder = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        TurnIndicatorHolder = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        SubmitBtn = new javax.swing.JButton();
        QuestionHolder = new javax.swing.JTextField();
        AnswerInput = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(46, 35, 28));
        jPanel1.setPreferredSize(new java.awt.Dimension(965, 585));

        jPanel2.setBackground(new java.awt.Color(214, 174, 96));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Vineta BT", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(46, 35, 28));
        jLabel1.setText("MOLE-TAIN PEAK");

        jLabel11.setFont(new java.awt.Font("Vineta BT", 1, 36)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(156, 156, 156)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        WhacAMoleBg.setBackground(new java.awt.Color(79, 61, 50));
        WhacAMoleBg.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        ScoreHolder.setFont(new java.awt.Font("SimSun-ExtB", 1, 45)); // NOI18N
        ScoreHolder.setForeground(new java.awt.Color(255, 255, 255));
        ScoreHolder.setText("SCORE:0");

        TimerHolder.setFont(new java.awt.Font("SimSun-ExtB", 1, 50)); // NOI18N
        TimerHolder.setForeground(new java.awt.Color(255, 255, 255));
        TimerHolder.setText("00s");

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 337));

        Molebtn1.setHideActionText(true);
        Molebtn1.addActionListener(this::Molebtn1ActionPerformed);

        Molebtn3.setHideActionText(true);
        Molebtn3.addActionListener(this::Molebtn3ActionPerformed);

        Molebtn2.setHideActionText(true);
        Molebtn2.addActionListener(this::Molebtn2ActionPerformed);

        Molebtn5.setHideActionText(true);
        Molebtn5.addActionListener(this::Molebtn5ActionPerformed);

        Molebtn4.setHideActionText(true);
        Molebtn4.addActionListener(this::Molebtn4ActionPerformed);

        Molebtn6.setHideActionText(true);
        Molebtn6.addActionListener(this::Molebtn6ActionPerformed);

        Molebtn7.setHideActionText(true);
        Molebtn7.addActionListener(this::Molebtn7ActionPerformed);

        Molebtn8.setHideActionText(true);
        Molebtn8.addActionListener(this::Molebtn8ActionPerformed);

        Molebtn9.setHideActionText(true);
        Molebtn9.addActionListener(this::Molebtn9ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Molebtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Molebtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(Molebtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(Molebtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(Molebtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16)
                                .addComponent(Molebtn6, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(Molebtn7, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(Molebtn8, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(Molebtn9, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Molebtn2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(Molebtn3, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(Molebtn1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Molebtn5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(Molebtn4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(Molebtn6, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Molebtn7, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(Molebtn8, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addComponent(Molebtn9, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        LivesHolder.setFont(new java.awt.Font("SimSun-ExtB", 1, 24)); // NOI18N
        LivesHolder.setForeground(new java.awt.Color(255, 255, 255));
        LivesHolder.setText("Lives: 3");

        javax.swing.GroupLayout WhacAMoleBgLayout = new javax.swing.GroupLayout(WhacAMoleBg);
        WhacAMoleBg.setLayout(WhacAMoleBgLayout);
        WhacAMoleBgLayout.setHorizontalGroup(
            WhacAMoleBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WhacAMoleBgLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(WhacAMoleBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(WhacAMoleBgLayout.createSequentialGroup()
                        .addGroup(WhacAMoleBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(WhacAMoleBgLayout.createSequentialGroup()
                                .addComponent(LivesHolder)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(ScoreHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TimerHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        WhacAMoleBgLayout.setVerticalGroup(
            WhacAMoleBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WhacAMoleBgLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(WhacAMoleBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(WhacAMoleBgLayout.createSequentialGroup()
                        .addComponent(ScoreHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LivesHolder))
                    .addComponent(TimerHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("SimSun-ExtG", 1, 18)); // NOI18N
        jLabel2.setText("PEAK: 30 POINTS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(180, 180, 180))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setFont(new java.awt.Font("SimSun-ExtG", 1, 18)); // NOI18N
        jLabel3.setText("PLAYER 1:");

        jLabel4.setFont(new java.awt.Font("SimSun-ExtG", 1, 18)); // NOI18N
        jLabel4.setText("POINTS:");

        P1NameHolder.addActionListener(this::P1NameHolderActionPerformed);

        Pt1Holder.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Pt1Holder.setText("  ");
        Pt1Holder.addActionListener(this::Pt1HolderActionPerformed);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(P1NameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pt1Holder)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(P1NameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pt1Holder, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("SimSun-ExtG", 1, 18)); // NOI18N
        jLabel5.setText("PLAYER 2:");

        jLabel6.setFont(new java.awt.Font("SimSun-ExtG", 1, 18)); // NOI18N
        jLabel6.setText("POINTS:");

        P2NameHolder.addActionListener(this::P2NameHolderActionPerformed);

        Pt2Holder.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Pt2Holder.setText("  ");
        Pt2Holder.addActionListener(this::Pt2HolderActionPerformed);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(P2NameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(5, 5, 5)
                .addComponent(Pt2Holder)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(P2NameHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pt2Holder, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        TurnIndicatorHolder.setEditable(false);
        TurnIndicatorHolder.setFont(new java.awt.Font("Vineta BT", 1, 24)); // NOI18N
        TurnIndicatorHolder.setForeground(new java.awt.Color(36, 45, 28));
        TurnIndicatorHolder.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TurnIndicatorHolder.addActionListener(this::TurnIndicatorHolderActionPerformed);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TurnIndicatorHolder)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TurnIndicatorHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("SimSun-ExtG", 1, 18)); // NOI18N
        jLabel7.setText("QUESTION");

        jLabel8.setFont(new java.awt.Font("SimSun-ExtG", 1, 18)); // NOI18N
        jLabel8.setText("ANSWER:");

        SubmitBtn.setText("SUBMIT");
        SubmitBtn.addActionListener(this::SubmitBtnActionPerformed);

        QuestionHolder.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N

        jLabel9.setText("Direction: The First Letter of your answer must be capitalize");

        jLabel10.setText("and for two word answer use underscore(_) as space. (ex. Try_catch)");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(QuestionHolder))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AnswerInput)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SubmitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addGap(28, 28, 28)))))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addGap(2, 2, 2)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(QuestionHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SubmitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(WhacAMoleBg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(WhacAMoleBg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void setButtonIcon(int tile, ImageIcon icon){ //Reusable code for Whac-A-Mole
        switch(tile){
            case 1: 
                Molebtn1.setIcon(icon);
                break;
            case 2: 
                Molebtn2.setIcon(icon);
                break;
            case 3: 
                Molebtn3.setIcon(icon);
                break;
            case 4: 
                Molebtn4.setIcon(icon);
                break;
            case 5: 
                Molebtn5.setIcon(icon);
                break;
            case 6: 
                Molebtn6.setIcon(icon);
                break;
            case 7: 
                Molebtn7.setIcon(icon);
                break;
            case 8: 
                Molebtn8.setIcon(icon);
                break;
            case 9: 
                Molebtn9.setIcon(icon);
                break;
        }
    }
    
    public void resetMoles(){ //This is to remove the images for Whac-A-Mole
        Molebtn1.setIcon(null);
        Molebtn2.setIcon(null);
        Molebtn3.setIcon(null);
        Molebtn4.setIcon(null);
        Molebtn5.setIcon(null);
        Molebtn6.setIcon(null);
        Molebtn7.setIcon(null);
        Molebtn8.setIcon(null);
        Molebtn9.setIcon(null);
    }
    
    public void MoleCondition(int mole){ //This is to continue the Whac-A-Mole
        resetMoles();
        moleTile = rand.nextInt(9) + 1;
        do{
            trapTile = rand.nextInt(9) + 1;
        } while (trapTile == moleTile); //this ensures the two tiles won't spawn at the same tile
        goldTile = -1;
        if(rand.nextInt(100) < 10){ //this to make the gold appear at 10% chance
            do{
                goldTile = rand.nextInt(9) + 1;
            } while (goldTile == moleTile || goldTile == trapTile);
            isTrue = true;
        }
        setButtonIcon(moleTile, moleIcon);
        setButtonIcon(trapTile, trapIcon);
        if(isTrue){
            setButtonIcon(goldTile, goldIcon);
            isTrue = false;
            }
        }
    private String difficulty="Easy";
    private void UpDifficulty() {
        if (whamScore >= 300) {
            difficulty = "Hard";
        } else if (whamScore >= 200) {
            difficulty = "Medium";
        } else if (whamScore >= 50) {
            difficulty = "Easy";
        }
    }
    private void ifClicked(JButton btn){ 
        Icon clickedBtn = btn.getIcon();
        soundEffects = new Sound();
        if(btn.getIcon() == null) return;
        int earnedPoints = 0;
        
        if(clickedBtn.equals(moleIcon)){
            soundEffects.moleSfx(); //This is the sfx when mole is clicked
            whamScore += 10;
            earnedPoints = 1;
            UpDifficulty();//This updates Difficulty
            int tpt = 50;//targetpoints
            if (difficulty.equals("Easy")) {
                tpt = 50;
            }    
            else if (difficulty.equals("Medium")) {
                tpt = 200;
            } 
            else if (difficulty.equals("Hard")) {
                tpt = 300;
            }
            
            if (whamScore == tpt) {
                UpDifficulty();//This Increases Difficulty
                Aquest(difficulty, false);
                whamTime.stop();
                moveTime.stop();   
                
            }
            
        } else if(clickedBtn.equals(trapIcon)){
            soundEffects.trapSfx(); //This is the Sfx if the trap is clicked    
            if(whamScore > 0){
                whamScore -= 10;
            }
            if(lives>0){
                --lives;
            }
            if (lives == 0){
                
                whamTime.stop();
                moveTime.stop();
                Sturn();
                reset();       
            }
            earnedPoints = -1;
        }
        else if(clickedBtn.equals(goldIcon)){
            soundEffects.goldSfx(); //This is the Sfx if the gold is clicked
            int choice = JOptionPane.showOptionDialog(
            this,
            "Gold found! Choose:",
            "Gold Event",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Pass (+2)", "Play (Question)"},
            null
        );
            if (choice == 0) {
                Points(2);
                whamTime.stop();
                moveTime.stop();
                Sturn();
                reset();
            } else if (choice == 1) {
                whamTime.stop();
                moveTime.stop();
                Aquest(difficulty, true);
                return; 
            }
        }
        ScoreHolder.setText("SCORE: " + whamScore);
        LivesHolder.setText("Lives: " + lives);
        btn.setIcon(null);
    } 
    
    private void Molebtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn1ActionPerformed
        ifClicked(Molebtn1);
    }//GEN-LAST:event_Molebtn1ActionPerformed

    private void Molebtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn3ActionPerformed
        ifClicked(Molebtn3);
    }//GEN-LAST:event_Molebtn3ActionPerformed

    private void Molebtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn2ActionPerformed
        ifClicked(Molebtn2);
    }//GEN-LAST:event_Molebtn2ActionPerformed

    private void Molebtn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn5ActionPerformed
        ifClicked(Molebtn5);
    }//GEN-LAST:event_Molebtn5ActionPerformed

    private void Molebtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn4ActionPerformed
        ifClicked(Molebtn4);
    }//GEN-LAST:event_Molebtn4ActionPerformed

    private void Molebtn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn6ActionPerformed
        ifClicked(Molebtn6);
    }//GEN-LAST:event_Molebtn6ActionPerformed

    private void Molebtn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn7ActionPerformed
        ifClicked(Molebtn7);
    }//GEN-LAST:event_Molebtn7ActionPerformed

    private void Molebtn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn8ActionPerformed
        ifClicked(Molebtn8);
    }//GEN-LAST:event_Molebtn8ActionPerformed

    private void Molebtn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Molebtn9ActionPerformed
        ifClicked(Molebtn9);
    }//GEN-LAST:event_Molebtn9ActionPerformed

    private void P1NameHolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P1NameHolderActionPerformed
        
        P1NameHolder.setEditable(false);
    }//GEN-LAST:event_P1NameHolderActionPerformed

    private void P2NameHolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_P2NameHolderActionPerformed
        
        P2NameHolder.setEditable(false);
    }//GEN-LAST:event_P2NameHolderActionPerformed

    private void Pt2HolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Pt2HolderActionPerformed
        Pt2Holder.setEditable(false);
    }//GEN-LAST:event_Pt2HolderActionPerformed

    private void Pt1HolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Pt1HolderActionPerformed
        Pt1Holder.setEditable(false);
    }//GEN-LAST:event_Pt1HolderActionPerformed

    private void TurnIndicatorHolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TurnIndicatorHolderActionPerformed
        TurnIndicatorHolder.setEditable(false);
    }//GEN-LAST:event_TurnIndicatorHolderActionPerformed

    private void SubmitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitBtnActionPerformed
        
        String userAns = AnswerInput.getText().trim();
        if (userAns.isEmpty()) return;
        boolean correct = userAns.equalsIgnoreCase(cAnswer.trim());
        if (correct) {
            soundEffects.correctSfx();
            Points(GoldQuestion ? 5 : 1);
        } else{
            soundEffects.wrongSfx();
        }
        AnswerInput.setText("");
        QuestionHolder.setText("");
        cAnswer = "";
        GoldQuestion = false;
        whamTime.start();
        moveTime.start();
        
        if (whamScore >= 50 && !correct) {
        whamTime.stop();
        moveTime.stop();
        lives = 3;
        whamScore = 0;             
        LivesHolder.setText("Lives: " + lives);
        ScoreHolder.setText("SCORE: " + whamScore);
        Sturn(); 
        reset(); 
        }
    }//GEN-LAST:event_SubmitBtnActionPerformed
    
    public static void main(String args[]) {
       java.awt.EventQueue.invokeLater(() -> {
        new Bgame("Player 1", "Player 2").setVisible(true);
    });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AnswerInput;
    private javax.swing.JLabel LivesHolder;
    private javax.swing.JButton Molebtn1;
    private javax.swing.JButton Molebtn2;
    private javax.swing.JButton Molebtn3;
    private javax.swing.JButton Molebtn4;
    private javax.swing.JButton Molebtn5;
    private javax.swing.JButton Molebtn6;
    private javax.swing.JButton Molebtn7;
    private javax.swing.JButton Molebtn8;
    private javax.swing.JButton Molebtn9;
    private javax.swing.JTextField P1NameHolder;
    private javax.swing.JTextField P2NameHolder;
    private javax.swing.JTextField Pt1Holder;
    private javax.swing.JTextField Pt2Holder;
    private javax.swing.JTextField QuestionHolder;
    private javax.swing.JLabel ScoreHolder;
    private javax.swing.JButton SubmitBtn;
    private javax.swing.JLabel TimerHolder;
    private javax.swing.JTextField TurnIndicatorHolder;
    private javax.swing.JPanel WhacAMoleBg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    // End of variables declaration//GEN-END:variables
}