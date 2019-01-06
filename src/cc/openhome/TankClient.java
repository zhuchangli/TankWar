package cc.openhome;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description: 坦克大战主界面
 * @author zili_zl
 */
public class TankClient extends Frame{

    /**
     * 整个游戏宽度
     */
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    public final int bknum = 5;

    // 解决画面加载卡顿问题
    Image offScreenImage = null;

    Tank mytank = new Tank(500,500,true, Tank.Direction.STOP,this);

    Wall wall = new Wall(300,300,this);

    Blood b = new Blood();
    // 存储多发炮弹
    List<Missile> missiles = new ArrayList<Missile>();
    // 存储爆炸
    List<Explode> explodes = new ArrayList<Explode>();
    // 存储tank
    List<Tank> tanks = new ArrayList<Tank>();


    /**
     * Description
     * date: 3:57 PM 1/6/19
     * @param  g 画笔显示在屏幕上画出一些对象
     * @return void
     *
     **/

    public void paint(Graphics g){

        // 在屏幕上显示一些文字，有提示的文字
        g.drawString("missiles count: "+missiles.size(),10,60);
        g.drawString("explodes count: "+explodes.size(),10,80);
        g.drawString("tanks count: "+tanks.size(),10,100);
        g.drawString("mytank life: "+mytank.getLife(),10,120);

        // 敌方所有坦克报废后重新生成一批坦克
        if(tanks.size()<= 0){
            for(int i = 0;i < bknum;i++){
                Tank tk = new Tank(50 + (i + 1) * 50,100,false, Tank.Direction.D,this);
                tanks.add(tk);
            }
        }

        // 拿出所有的子弹
        for(int i = 0;i < missiles.size();i++){
            Missile m = missiles.get(i);
            // 子弹击打敌方
            m.hitTanks(tanks);
            // 子弹攻击自己
            m.hitTank(mytank);

            // 判断子弹是否撞墙
            m.collidesWithWall(wall);
            // 第一种解决用bool值解决
            /*
            if(!m.isLive()){
                missiles.remove(m);
            }else {
                m.draw(g);
            }
            */
            // 画出子弹
            m.draw(g);
        }

        // 产生爆炸逐个画出来
        for(int i = 0;i < explodes.size();i++){
            Explode ex = explodes.get(i);
            ex.draw(g);
        }
        // 拿出生成的tank
        for(int i = 0; i < tanks.size();i++){
            Tank t = tanks.get(i);
            // 坦克撞墙
            t.collidesWithWall(wall);
            // 同一个tank相遇
            t.collidesWithTanks(tanks);
            t.draw(g);
        }
        // 画出墙来
        wall.draw(g);
        // tank吃血补充生命值
        mytank.eatBlood(b);
        mytank.draw(g);
        b.draw(g);
    }

    /**
     * @Description: 使用update方法 实现双缓冲
     * @param: [g]
     * @return: void
     */

    public void update(Graphics g){
        // 保证只生成背景色一次
        if(offScreenImage == null){
            offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);

        }
        // 幕后图片，调用paint在画布上作画
        Graphics gOffScreen = offScreenImage.getGraphics();

        // 背景重刷, 重擦
        Color c = gOffScreen.getColor();   // 获取当前颜色
        gOffScreen.setColor(Color.GREEN);  // 设置颜色为颜色绿色
        gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);  // 画一个 800×600的方块，颜色黑色
        gOffScreen.setColor(c);          // 刷完之后把颜色改回去
        paint(gOffScreen);
        //一次把图片画的前面去
        g.drawImage(offScreenImage,0,0,null);

    }

    /**
     * @Description: 做一些初始化工作,只调用一次
     * @param: []
     * @return: void
     */
    public void lauchFram(){

        // 在未画之前开始生成tank
        for(int i = 0;i < 10;i++){
            Tank tk = new Tank(50 + (i + 1) * 50,100,false, Tank.Direction.D,this);
            tanks.add(tk);
        }

        this.setLocation(400,300);           // 打开画面相对屏幕的坐标位置和屏幕的大小
        this.setSize(GAME_WIDTH,GAME_HEIGHT);     // 设置游戏画面的长度和高度
        this.setTitle("TankWar");                 // 游戏界面标题值

        // 关闭点击叉号起作用的
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.setResizable(false);            // 固定画面大小,不可以改变大小
        this.setBackground(Color.GREEN);     // 设置草地颜色
        setVisible(true);                    // 设置可见性

        // 添加键盘监听
        this.addKeyListener(new TankClient.KeyMonitor());

        // 开启一个线程去画一些对象
        new Thread(new TankClient.PaintThread()).start();
    }

    public static void main(String [] args){
        TankClient tc = new TankClient();
        tc.lauchFram();
    }

    /**
     * @Description: 重画方法，让线程工作
     * @param:
     * @return:
     */
    private class PaintThread implements Runnable{

        @Override
        public void run() {
            while (true){
                repaint();
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Description: 监听键盘事件
     * @param:
     * @return:
     */
    private class KeyMonitor extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            mytank.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            mytank.keyReleased(e);
        }
    }
}
