import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {
    //basic config
    //800*600 1020*760 1360*760 1400*1040
    //1600*900 1920*1080 2560*1440 4080*2160
    static int SCREEN_WIDTH = 800;
    static int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS =
            SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE;
    //speed | update gap
    static int DELAY = 200;
    //snake storage
    final int[] x =new int[GAME_UNITS];
    final int[] y =new int[GAME_UNITS];
    int bodyParts = 4;
    //spawn detection
    int tempSnake = 1;
    //score & part of the body length
    int foodEaten = 0;
    //food position
    int foodX = 0;
    int foodY = 0;
    //direction
    char runningDirection = 'R';
    //serve for single input judgement
    char reservedDirection = 'R';
    //determine the whole game state
    boolean runningState = false;
    //determine pause state
    boolean pauseTrigger = false;
    //determine whether the game is at the initial window
    boolean welcomeTrigger = true;
    //serve for difficulty change by time
    int tempDifficultyCounts = 0;
    //colorful snake temp
    boolean redColorDecrease = true;
    boolean greenColorDecrease = true;
    boolean blueColorDecrease = true;
    int redColor = 102;
    int greenColor = 204;
    int blueColor = 255;
    //game clock
    Timer timer;
    //random generate for food position
    Random random;
    //main panel with initialization
    Panel(){
        random = new Random();
        //set resolution
        this.setPreferredSize(
                new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        //set background color
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());
        startGame();
    }
    public void startGame(){
        //new game initialization with default snake & food
        newSnake();
        newFood();
        //set running state
        runningState = true;
        timer = new Timer(DELAY, this);
        //start clock and stop when game over
    }
    public void paintComponent(Graphics g){
        //set graphics
        super.paintComponent(g);
        drawGame(g);
    }
    public void drawGame(Graphics g){
        //draw grid
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0,
                    i * UNIT_SIZE, SCREEN_HEIGHT);
        }
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(0, i * UNIT_SIZE,
                    SCREEN_WIDTH, i * UNIT_SIZE);
        }
        //draw walls
        g.setColor(new Color(212, 242, 231));
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
            g.fillRect(i * UNIT_SIZE, 0,
                    UNIT_SIZE, UNIT_SIZE);
            g.fillRect(i * UNIT_SIZE, SCREEN_HEIGHT - UNIT_SIZE,
                    UNIT_SIZE, UNIT_SIZE);
        }
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.fillRect(0, i * UNIT_SIZE,
                    UNIT_SIZE, UNIT_SIZE);
            g.fillRect(SCREEN_WIDTH - UNIT_SIZE, i * UNIT_SIZE,
                    UNIT_SIZE, UNIT_SIZE);
        }
        if(welcomeTrigger){
            gameWelcome(g);
        }
        else{
            //draw food
            g.setColor(new Color(0, 255, 127));
            g.fillRoundRect(foodX + 2, foodY + 2,
                    UNIT_SIZE - 4, UNIT_SIZE - 4,
                    UNIT_SIZE - 4, UNIT_SIZE - 4);
            //draw snake with unique head color
            redColor = 102;
            greenColor = 204;
            blueColor = 255;
            redColorDecrease = true;
            greenColorDecrease = true;
            blueColorDecrease = true;
            for(int i = 0; i < bodyParts + foodEaten; i++){
                if(i == 0){
                    g.setColor(Color.blue);
                }
                else{
                    if(redColorDecrease){
                        redColor -= 3;
                        if(redColor < 10){
                            redColorDecrease = false;
                        }
                    }
                    else{
                        redColor += 3;
                        if(redColor > 245){
                            redColorDecrease = true;
                        }
                    }
                    if(greenColorDecrease){
                        greenColor -= 4;
                        if(greenColor < 10){
                            greenColorDecrease = false;
                        }
                    }
                    else{
                        greenColor += 4;
                        if(greenColor > 245){
                            greenColorDecrease = true;
                        }
                    }
                    if(blueColorDecrease){
                        blueColor -= 5;
                        if(blueColor < 10){
                            blueColorDecrease = false;
                        }
                    }
                    else{
                        blueColor += 5;
                        if(blueColor > 245){
                            blueColorDecrease = true;
                        }
                    }
                    g.setColor(new Color(redColor, greenColor, blueColor));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        //draw dead snake head
        if(!runningState){
            g.setColor(Color.red);
            g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
            gameOver(g);
        }
        //pause without stop clock
        if(pauseTrigger){
            gamePause(g);
        }
    }
    public void newSnake(){
        if(tempSnake > SCREEN_WIDTH / UNIT_SIZE){
            JOptionPane.showMessageDialog(this,
                    "Spawn snake error!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        for(int i = bodyParts; i > 0; i--){
            //set default position
            x[i - 1] = tempSnake * UNIT_SIZE;
            y[i - 1] = (SCREEN_HEIGHT / UNIT_SIZE / 2)
                    * UNIT_SIZE;
            tempSnake ++;
        }
        //reset to default
        tempSnake = 1;
    }
    public void newFood(){
        //set random position in map except the walls
        foodX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE) - 2)
                * UNIT_SIZE + UNIT_SIZE;
        foodY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE) - 2)
                * UNIT_SIZE + UNIT_SIZE;
        //detect pre-collision with snake head & respawn
        if((x[0] == foodX) && (y[0] == foodY)){
            newFood();
        }
    }
    public void moveSnake(){
        //copy the former one from the tail to the neck
        for(int i = bodyParts + foodEaten; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        //set the head position
        switch(runningDirection){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
            break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkFoods(){
        if((x[0] == foodX) && (y[0] == foodY)){
            //score & snake length increase
            foodEaten++;
            //increase difficulty
            if(DELAY > 50){
                DELAY--;
            }
            //respawn food
            newFood();
        }
    }
    public void checkCollisions(){
        //check snake body with snake head
        for(int i = 1; i < bodyParts + foodEaten; i++){
            if ((x[i] == x[0]) && (y[i] == y[0])) {
                runningState = false;
                break;
            }
        }
        //check the whole snake with the walls
        if((x[0] == 0) || (x[0] == SCREEN_WIDTH - UNIT_SIZE)
        || (y[0] == 0) || (y[0] == SCREEN_HEIGHT - UNIT_SIZE)){
            runningState = false;
        }
        if(!runningState){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //ending marks
        g.setColor(new Color(139,0,0));
        g.setFont(new Font("Consolas", Font.BOLD, 70));
        FontMetrics metricsUp = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (SCREEN_WIDTH -
                        metricsUp.stringWidth("Game Over")) / 2,
                SCREEN_HEIGHT / 2 - 50);
        //final scores
        g.setColor(new Color(255,250,250));
        g.setFont(new Font("Consolas", Font.BOLD, 30));
        FontMetrics metricsDown = getFontMetrics(g.getFont());
        g.drawString("Your Score: " + foodEaten,
                (SCREEN_WIDTH -
                        metricsDown.stringWidth(
                                "Your Score: " + foodEaten)) / 2,
                SCREEN_HEIGHT / 2 + 50);
    }
    public void gamePause(Graphics g){
        //pause marks
        g.setColor(new Color(255,250,250));
        g.setFont(new Font("Consolas", Font.BOLD, 70));
        FontMetrics metricsUp = getFontMetrics(g.getFont());
        g.drawString("Game Pause",
                (SCREEN_WIDTH -
                        metricsUp.stringWidth("Game Pause")) / 2,
                SCREEN_HEIGHT / 2 - 50);
        //pause scores
        g.setColor(new Color(255,250,250));
        g.setFont(new Font("Consolas", Font.BOLD, 30));
        FontMetrics metricsDown = getFontMetrics(g.getFont());
        g.drawString("Current Score: " + foodEaten,
                (SCREEN_WIDTH -
                        metricsDown.stringWidth(
                                "Current Score: " + foodEaten)) / 2,
                SCREEN_HEIGHT / 2 + 50);
    }
    public void gameWelcome(Graphics g){
        g.setColor(new Color(255,250,250));
        g.setFont(new Font("Consolas", Font.BOLD, 70));
        FontMetrics metricsUp = getFontMetrics(g.getFont());
        g.drawString("Welcome",
                (SCREEN_WIDTH -
                        metricsUp.stringWidth("Welcome")) / 2,
                SCREEN_HEIGHT / 2 - 50);
        //pause scores
        g.setColor(new Color(255,250,250));
        g.setFont(new Font("Consolas", Font.BOLD, 30));
        FontMetrics metricsDown = getFontMetrics(g.getFont());
        g.drawString("Press space to start",
                (SCREEN_WIDTH -
                        metricsDown.stringWidth(
                                "Press space to start")) / 2,
                SCREEN_HEIGHT / 2 + 50);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //frame change
        if((!pauseTrigger) && runningState) {
            reservedDirection = runningDirection;
            moveSnake();
            checkFoods();
            checkCollisions();
            //increase difficulty by time
            if (tempDifficultyCounts == 5) {
                tempDifficultyCounts = 0;
                if(DELAY > 50){
                    DELAY--;
                }
            }
            else{
                tempDifficultyCounts++;
            }
        }
        repaint();
    }
    public class GameKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            //get key pressed information
            switch(e.getKeyCode()){
                //4 directions
                case KeyEvent.VK_LEFT:
                    if(reservedDirection != 'R'){
                        runningDirection = 'L';
                    }
                    if(reservedDirection == 'L'){
                        if(!welcomeTrigger){
                            if((pauseTrigger) && (runningState)) {
                                pauseTrigger = false;
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(reservedDirection != 'L'){
                        runningDirection = 'R';
                    }
                    if(reservedDirection == 'R'){
                        if(!welcomeTrigger){
                            if((pauseTrigger) && (runningState)) {
                                pauseTrigger = false;
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(reservedDirection != 'D'){
                        runningDirection = 'U';
                    }
                    if(reservedDirection == 'U'){
                        if(!welcomeTrigger){
                            if((pauseTrigger) && (runningState)) {
                                pauseTrigger = false;
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(reservedDirection != 'U'){
                        runningDirection = 'D';
                    }
                    if(reservedDirection == 'D'){
                        if(!welcomeTrigger){
                            if((pauseTrigger) && (runningState)) {
                                pauseTrigger = false;
                            }
                        }
                    }
                    break;
                case KeyEvent.VK_P:
                    //pause
                    if(!welcomeTrigger){
                        pauseTrigger = (!pauseTrigger) && (runningState);
                    }
                    break;
                    //restart
                case KeyEvent.VK_R:
                    timer.stop();
                    welcomeTrigger = false;
                    DELAY = 200;
                    foodEaten = 0;
                    runningDirection = 'R';
                    pauseTrigger = false;
                    tempDifficultyCounts = 0;
                    startGame();
                    timer.start();
                    break;
                case KeyEvent.VK_SPACE:
                    welcomeTrigger = false;
                    if(!pauseTrigger){
                        timer.start();
                    }
                    break;
            }
        }
    }
}
