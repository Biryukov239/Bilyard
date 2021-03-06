package KartohaEngine2D.polygons;

import KartohaEngine2D.drawing.ArbitraryFigure;
import KartohaEngine2D.drawing.Drawable;
import KartohaEngine2D.geometry.*;
import KartohaEngine2D.limiters.Collisional;
import KartohaEngine2D.limiters.Intersectional;
import KartohaEngine2D.physics.Material;
import KartohaEngine2D.physics.Space;
import KartohaEngine2D.ui.Controllable;
import KartohaEngine2D.utils.ImageReader;
import KartohaEngine2D.utils.Tools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class PhysicalPolygon extends Polygon2 implements Drawable, Collisional, Intersectional, Controllable {

    private Vector2 v;
    private final Space space;
    private float w;
    private final float m;
    private final float J;
    private final Material material;
    private float rotateAngle;



    public PhysicalPolygon(Space space, Vector2 v, float w, float x0, float y0, ArrayList<Point2> points, Material material) {
        super(x0, y0, points);
        this.space = space;
        this.v = v;
        this.w = w;
        this.material = material;
        PolygonCreator polygonCreator = new PolygonCreator(points);
        this.m = polygonCreator.getSquare() * material.p;
        this.J = polygonCreator.getJDivDensity() * material.p;
    }

    public synchronized void update() {
        updateSpeed();
        rotate();
        updatePoints();
        x0 += v.getX() * space.getDT();
        y0 += ((v.getY() + v.getY() - space.getG()*space.getDT())/2.0f) * space.getDT();
        if (rotateAngle > Math.PI * 2){
            rotateAngle -= Math.PI * 2;
        }
    }

    private void updateSpeed() {
        v.addY(space.getG()*space.getDT());
    }

    private void updatePoints() {
        for (Point2 point : getPoints()){
            point.x += v.getX()* space.getDT();
            point.y += ((v.getY() + v.getY() - space.getG() * space.getDT()) * space.getDT())/ 2.0f;
        }
    }

    private void rotate() {
        Point2 centre = new Point2(x0, y0);
        for (Point2 point : getPoints()){
            point.rotate(centre, w * space.getDT());
        }
        rotateAngle += space.getDT() * w;

    }

    public Point2 getPositionOfCentre(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

    public ArrayList<Line> getLines(boolean mode) {
        ArrayList<Point2> newPoints = getPoints(mode);

        ArrayList<Line> lines = new ArrayList<>();

        for (int i = 0; i<newPoints.size(); i++){
            if (i+1 < newPoints.size()) lines.add(new Line(newPoints.get(i), newPoints.get(i+1)));
            else lines.add(new Line(newPoints.get(0), newPoints.get(i)));
        }
        return lines;
    }

    public ArrayList<Point2> getPoints(boolean mode){
        float m = mode ? 1.0f : 0.0f;
        Point2 centre = new Point2(x0 + v.getX() * m*space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));

        ArrayList<Point2> newPoints = clonePoints();
        for (Point2 point : newPoints) {
            point.x += m * v.getX() * space.getDT();
            point.y += m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT()) / 2.0f;
            point.rotate(centre, w * space.getDT() * m);
        }

        return newPoints;
    }

    public synchronized void pullFromLine(PolygonToLineIntersection intersection) {
        if (intersection.getValue() != 0){
            Vector2 movementVector = new Vector2(intersection.getPointOfPolygon(), intersection.getCollisionPoint());
            if (movementVector.length() != 0f)
                move(movementVector, intersection.getValue());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(material.outlineColor);
        ArbitraryFigure arbitraryFigure = new ArbitraryFigure(getPoints());
        g.drawPolygon(arbitraryFigure.getPolygon());
        g.setColor(material.fillColor);
        g.fillPolygon(arbitraryFigure.getPolygon());
        g.setColor(Color.WHITE);
        g.drawLine(Tools.transformFloat(x0),
                Tools.transformFloat(y0),
                Tools.transformFloat(x0 + v.getX()*space.getDT()),
                Tools.transformFloat(y0 + v.getY()*space.getDT()));
    }

    public float getRotateAngle() {
        return rotateAngle;
    }

    public float getJ() {
        assert J == 0 : "J is null";
        return J;
    }

    public float getW() {
        return w;
    }

    public float getM() {
        assert m == 0 : "Mass is null";
        return m;
    }

    public Vector2 getV() {
        return v;
    }

    public void setV(Vector2 v) {
        this.v = v;
    }

    public void setW(float w) {
        this.w = w;
    }

    @Override
    public void rotate(float a) {
        Point2 centre = new Point2(x0, y0);
        for (Point2 point : getPoints()){
            point.rotate(centre, a);
        }
        rotateAngle += a;
    }

    @Override
    public void move(Vector2 movement) {
        for (Point2 point : getPoints()){
            point.x += movement.getX();
            point.y += movement.getY();
        }
        x0 += movement.getX();
        y0 += movement.getY();
    }

    @Override
    public void setCords(Point2 newCords) {
        Vector2 movement = new Vector2(getPositionOfCentre(false), newCords);
        move(movement);
    }

    public Material getMaterial() {
        return material;
    }
}
