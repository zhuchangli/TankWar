package cc.openhome;

import java.awt.*;

/**
 *@ClassName cc.openhome.Blood
 *@Description 血块类
 *@Author zizl_zq
 *@Date 1/6/19 3:51 PM
 */
public class Blood {

    int x,y,w,h;  // 血块出现的位置,长和宽
    private int step = 0;
    TankClient tc;
    // 血块按照指定的这几个点移动
    int [][] positions = {
            {430,440},{430,450},{440,460},{460,470},{470,450},{450,440},{440,430}
    };

    /**
     * @Description 判断血块是否活着，如果被坦克吃掉了就消失
     * @Param []
     * @return boolean
     */
    public boolean isLive() {
        return live;
    }

    /**
     * @Description 设置血块的生命
     * @Param [live]
     * @return void
     */
    public void setLive(boolean live) {
        this.live = live;
    }

    private boolean live = true;

    public Blood(){
        x = positions[0][0];
        y = positions[0][1];
        w = 10;
        h = 10;
    }

    /**
     * @Description 按着这个步子在移动的
     * @Param []
     * @return void
     */
    public void move(){

        if(step == positions.length){
            step = 0;
        }
        x = positions[step][0];
        y = positions[step][1];
        step++;
    }

    /**
     * @Description 画出血块
     * @Param [g]
     * @return void
     */
    public void draw(Graphics g){
        if(!live){
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.magenta);
        g.fillRect(x,y,w,h);
        g.setColor(c);
        move();
    }

    /**
     * @Description 获取自身位置，为碰撞做准备
     * @Param []
     * @return java.awt.Rectangle
     */
    public Rectangle getRect(){
        return new Rectangle(x,y,w,h);
    }
}
