package KartohaEngine2D.ui;

import KartohaEngine2D.geometry.Point2;
import KartohaEngine2D.geometry.Vector2;
import KartohaEngine2D.physics.Material;
import KartohaEngine2D.physics.Space;
import KartohaEngine2D.polygons.PhysicalPolygon;
import KartohaEngine2D.utils.ImageReader;
import billiard_example.Cue;
import billiard_example.Pocket;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

final class Main implements MouseListener {

    public static void main(String[] args) throws InterruptedException, IOException {


        Scene scene = new Scene(new Space(0.0025f, 000f), new Color(9, 73, 26, 255), 1600, 1000);

        scene.getSpace().addSphere(new Vector2(0, 0), 0, 500, 385, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 540, 360, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 540, 400, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 570, 380, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 570, 420, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 570, 340, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 605, 320, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 605, 360, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 605, 400, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 605, 440, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0, 0), 0, 400, 385, 20, Material.Constantin);

        for (int i=0;i<=9;i++) {
            scene.getSpace().getSpheres().get(i).setSprite("ball1.png");
        }
        scene.getSpace().getSpheres().get(10).setSprite("ball2.png");

        scene.getSpace().addBlock(-100, -100, 2000, 200, Material.Wood);
        scene.getSpace().addBlock(900, -100, 200, 2000, Material.Wood);
        scene.getSpace().addBlock(-100, -100, 200, 2000, Material.Wood);
        scene.getSpace().addBlock(-50, 620, 2000, 2000, Material.Wood);

        Point2 point1 = new Point2(200, 505),
                point2 = new Point2(200, 495),
                point3 = new Point2(600, 495),
                point4 = new Point2(600, 505);
        ArrayList<Point2> points = new ArrayList<>();
        points.add(point1);
        points.add(point4);
        points.add(point3);
        points.add(point2);

        Pocket pocket1 = new Pocket(scene, 30, 130, 130),
                pocket2 = new Pocket(scene, 30, 870, 130),
                pocket3 = new Pocket(scene, 30, 130, 590),
                pocket4 = new Pocket(scene, 30, 870, 590),
                pocket5 = new Pocket(scene, 30, 500, 130),
                pocket6 = new Pocket(scene, 30, 500, 590);
        Cue cue = new Cue(ImageReader.read("kii.png"), scene, new Point2(200, 200), 300, 20);

        while (2 + 2 == 4) scene.update();

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        System.out.println(e.getX());
        System.out.println(e.getY());
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
