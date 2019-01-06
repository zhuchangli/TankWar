package cc.openhome;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

/**
 *
 * @Description 坦克类
 * @Author zizl_zq
 * @Date 1/6/19 3:51 PM
 */
public class Tank {

    // tank 生命值
    public static final int LIFETIME = 100;
    // 移动的速度
    public static final int xSpeed = 5;
    public static final int ySpeed = 5;

    // tank 的宽度
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    int x ,y;
    private boolean bL = false;
    private boolean bR = false;
    private boolean bU = false;
    private boolean bD = false;
    enum Direction {L,LU,U,RU,R,RD,D,LD,STOP};  // 有八个移动方向和停止

    private Direction dir = Direction.STOP;   // tank的方向
    private Direction ptdir = Direction.D;    // 炮筒的方向

    private TankClient tkc;

    private int oldx,oldy;

    private boolean good;

    private BloodBar bb = new BloodBar();  // 显示声明值进度条

    /**
     * @Description: tank 分敌方和我方tank
     * @param: []
     * @return: boolean
     */
    public boolean isGood() {
        return good;
    }
    // 产生随机数 static 只产生一份,是共享变量
    private static Random r = new Random();

    public void setLive(boolean live) {
        this.live = live;
    }

    /**
     * @Description: 当tank撞在墙上是需要停下来的,在撞见墙的上一步开始停下来改变方向
     * @param: []
     * @return: void
     */
    public void stay(){
        x = oldx;
        y = oldy;
    }

    public boolean isLive() {
        return live;
    }

    private boolean live = true;
    // 产生移动的步长
    private int step = r.nextInt(12) + 3;

    // 生命值
    private int life = 100;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Tank(int x,int y,boolean good){
        this.x = x;
        this.y = y;
        this.good = good;
    }
    public Tank(int x, int y, boolean good, Direction dir, TankClient tkc){
        this(x,y,good);
        this.dir = dir;
        this.tkc = tkc;
    }

    /**
     * @Description: 画tank
     * @param: [g]
     * @return: void
     */
    public void draw(Graphics g){
        if(!live) {
            if(!good){
                tkc.tanks.remove(this);
            }
            return;
        }
        Color c = g.getColor();         // 保存以前颜色
        if(good) g.setColor(Color.red); // 玩家tank画成红色
        else g.setColor(Color.blue);    // 敌方tank画成蓝色

        // 坐标四个参数，确定图案大小
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);

        if(good) bb.draw(g);
        // 画炮筒
        switch (ptdir ){
            case L:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x,y + Tank.HEIGHT / 2);
                break;
            case LU:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x,y );
                break;
            case U:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x + Tank.WIDTH / 2,y);
                break;
            case RU:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x + Tank.WIDTH,y);
                break;
            case R:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x + Tank.WIDTH ,y + Tank.HEIGHT /2);
                break;
            case RD:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x + Tank.WIDTH,y + Tank.HEIGHT );
                break;
            case D:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x + Tank.WIDTH /2,y + Tank.HEIGHT);
                break;
            case LD:
                g.drawLine(x+Tank.WIDTH / 2,y + Tank.HEIGHT / 2,x,y + Tank.HEIGHT);
                break;
        }
        // 移动
        move();
    }

    /**
     * @Description: 根据方向来确定行进方向
     * @param: []
     * @return: void
     */
    void move(){
        this.oldx = x;
        this.oldy = y;
        switch (dir ){
            case L:
                x -= xSpeed;
                break;
            case LU:
                x -= xSpeed;
                y -= ySpeed;
                break;
            case U:
                y -= ySpeed;
                break;
            case RU:
                x += xSpeed;
                y -= ySpeed;
                break;
            case R:
                x += xSpeed;
                break;
            case RD:
                x += xSpeed;
                y += ySpeed;
                break;
            case D:
                y += ySpeed;
                break;
            case LD:
                x -= xSpeed;
                y += ySpeed;
                break;
            case STOP:
                break;
        }
        // 炮筒方向,解决静止也能发炮
        if(dir != Direction.STOP){
            ptdir = dir;
        }

        // 限制tank 不能越界
        if(x < 0) x = 0;
        if(y < 30) y = 30;
        if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

        // 敌方tank在八个方向里面随机选择方向,找出下一步移动方向
        if(!good){
            // enum 转换为数组
            Direction [] dirs = Direction.values();
            if(step == 0){
                step = r.nextInt(12) + 3;
                int rn = r.nextInt(dirs.length);
                dir = dirs[rn];
            }
            step--;
            // 解决子弹发射太快问题 这个条件下发射一枚炮弹
            if(r.nextInt(40) > 38) fire();

        }

    }

    /**
     * @Description: 键盘按下事件
     * @param: [e]
     * @return: void
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locateDirection();
    }

    /**
     * @Description: 处理松开按键事件
     * @param: [e]
     * @return: void
     */
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_F2:
                if(good){
                    this.live = true;
                    this.life = LIFETIME;
                }
            case KeyEvent.VK_CONTROL:     // control键发射炮弹 释放按键时发射炮弹控制 发射频率
                fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_SPACE:
                superFire();
                break;
        }
        locateDirection();
    }

    /**
     * @Description: 根据按下的键盘确定移动方向
     * @param: []
     * @return: void
     */
    void locateDirection(){
        if(bL && !bR && !bU && !bD) dir = Direction.L;
        else if(bL && !bR && bU && !bD) dir = Direction.LU;
        else if(!bL && !bR && bU && !bD) dir = Direction.U;
        else if(!bL && bR && bU && !bD) dir = Direction.RU;
        else if(!bL && bR && !bU && !bD) dir = Direction.R;
        else if(!bL && bR && !bU && bD) dir = Direction.RD;
        else if(!bL && !bR && !bU && bD) dir = Direction.D;
        else if(bL && !bR && !bU && bD) dir = Direction.LD;
        else if(!bL && !bR && !bU && !bD) dir = Direction.STOP;
    }

    /**
     * @Description: 开火打出一发子弹
     * @param: []
     * @return: cc.openhome.Missile
     */
    public Missile fire(){
        int x = this.x + (Tank.WIDTH - Missile.WIDTH) / 2;
        int y = this.y + (Tank.HEIGHT - Missile.HEIGHT) / 2;
        Missile m = new Missile(x,y,good,ptdir,this.tkc);
        tkc.missiles.add(m);
        return m;
    }

    /**
     * @Description: 朝固定方向打出一颗子弹
     * @param: [dir]
     * @return: cc.openhome.Missile
     */
    public Missile fire(Direction dir){
        int x = this.x + (Tank.WIDTH - Missile.WIDTH) / 2;
        int y = this.y + (Tank.HEIGHT - Missile.HEIGHT) / 2;
        Missile m = new Missile(x,y,good,dir,this.tkc);
        tkc.missiles.add(m);
        return m;
    }

    /**
     * @Description: 获取子弹的方块
     * @param: []
     * @return: java.awt.Rectangle
     */
    public Rectangle getRect(){
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }

    /**
     * @Description: tank 撞见墙了作处理
     * @param: [w]
     * @return: boolean
     */
    public boolean collidesWithWall(Wall w){
        // 撞见墙 让坦克掉头,或停下来
        if(this.getRect().intersects(w.getRect())){
            this.stay();
            return true;
        }
        return false;
    }

    /**
     * @Description: 处理tank遇见tank时
     * @param: [tanks]
     * @return: boolean
     */
    public boolean collidesWithTanks(List<Tank> tanks){
        for(int i = 0;i < tanks.size();i++){
            Tank t = tanks.get(i);
            if(this != t){
                if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
                    this.stay();
                    t.stay();
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * @Description: 超级子弹 一次可以朝多个方向打出子弹
     * @param: []
     * @return: void
     */
    public void superFire(){
        Direction [] dirs = Direction.values();
        for(int i = 0;i < 8;i++){
            fire(dirs[i]);
        }
    }

    /**
     * @Description: 成tank的生命值
     * @param:
     * @return:
     */
    private class BloodBar{
        public void draw(Graphics g){
            Color c = g.getColor();
            g.setColor(Color.red);
            g.drawRect(x,y - 15,WIDTH,10);
            int w = WIDTH * life / 100;
            g.fillRect(x,y - 15,w,10);
            g.setColor(c);
        }
    }

    /**
     * @Description: 给tank补充生命值
     * @param: [b]
     * @return: boolean
     */
    public boolean eatBlood(Blood b){
        // 当前tank活着,还有血,子弹和血块相撞
        if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
            if(life < LIFETIME){
                life += 20;
            }
            b.setLive(false);
            return true;
        }
        return false;
    }
}
