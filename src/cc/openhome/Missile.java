package cc.openhome;

import cc.openhome.Explode;
import cc.openhome.Wall;

import java.awt.*;
import java.util.List;
/**
 * @descpition 子弹类
 * @author zizl_zq
 * @date 1/6/19 3:51 PM
 */
public class Missile {

    // 子弹的移动速度
    public static final int xSpeed = 10;
    public static final int ySpeed = 10;

    // 子弹的宽度,高度
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    private int x,y;
    private Tank.Direction dir;

    private boolean good;
    private TankClient tc;


    private boolean live = true;

    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, Boolean good, Tank.Direction dir, TankClient tc){
        this(x,y,dir);
        this.good = good;
        this.tc = tc;
    }

    public boolean isLive() {
        return live;
    }
    public void setLive(boolean live) {
        this.live = live;
    }


    public void draw(Graphics g){
        if(!live)
        {
            tc.missiles.remove(this);
            return;
        }
        Color c = g.getColor();
        if(!good){
            g.setColor(Color.blue);
        }
        if(good){
            g.setColor(Color.black);
        }
        // 坐标四个参数，确定图案大小
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);

        // 移动
        move();
    }

    /**
     * @Description 子弹移动方法
     * @Param []
     * @return void
     */
    void move(){
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
        }
        // 判断炮弹出界
        if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT){
            live = false;
        }
    }

    // 获取子弹的方块
    public Rectangle getRect(){
        return new Rectangle(x,y,WIDTH,HEIGHT);

    }
    /**
     * @Description 子弹打击tank
     * @Param [tk]
     * @return boolean
     */
    public boolean hitTank(Tank tk){
        // 1,判断子弹是否活着 2,判断是否击中,3,判断坦克是否活着,4,判断是否是一伙的
        if(this.live && this.getRect().intersects(tk.getRect()) && tk.isLive() && this.good != tk.isGood()){
            if(tk.isGood()){
                tk.setLife(tk.getLife() - 20);
                if(tk.getLife() <= 0){
                    tk.setLive(false);
                }

            }else {
                tk.setLive(false);
            }
            this.setLive(false);
            Explode e = new Explode(x,y,tc);
            tc.explodes.add(e);
            return true;
        }
        return false;
    }

    /**
     * @Description 击打所有的tank
     * @Param [tanks]
     * @return boolean
     */
    public boolean hitTanks(List<Tank> tanks){
        for(int i = 0;i < tanks.size();i++){
            if(hitTank(tanks.get(i))){
                return true;
            }
        }
        return false;
    }

    /**
     * @Description 子弹撞在墙上
     * @Param [w]
     * @return boolean
     */
    public boolean collidesWithWall(Wall w){
        // 子弹撞到墙上，让它消失
        if(this.getRect().intersects(w.getRect())){
            setLive(false);
            return true;
        }
        return false;
    }
}
