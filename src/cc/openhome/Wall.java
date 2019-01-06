package cc.openhome;

import java.awt.*;
/**
 *@Description 增加游戏好玩度，增加一堵墙
 *@Author zizl_zq
 *@Date 1/6/19 3:51 PM
 */
public class Wall {
    public static final int WIDTH = 200;
    public static final int HEIGHT = 10;

    int x,y;
    private TankClient tc;
    public Wall(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    /**
     * @Description 画出一个tank
     * @Param [g]
     * @return void
     **/
    public void draw(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.black);
        g.fillRect(x,y,WIDTH,HEIGHT);
        g.setColor(c);
    }

    /**
     * @Description 获取墙的位置
     * @Param []
     * @return java.awt.Rectangle
     **/
    public Rectangle getRect(){
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }

}

