package KartohaEngine2D.sphere;

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
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PhysicalSphere extends Sphere2 implements Drawable, Collisional, Intersectional, Controllable {

    private float x0, y0;
    private final float r;
    private Vector2 v;
    private float w;
    private final float J;
    private final Vector2 orientationVector;
    private final Space space;
    private final Material material;
    private final float m;
    private BufferedImage sprite;
    private float rotateAngle = 0;


    {
        sprite = null;
    }


    public PhysicalSphere(Space space, Vector2 v, float w, float x0, float y0, float r, Material material) {
        super(x0, y0, r);
        this.x0 = x0;
        this.r = r;
        this.y0 = y0;
        this.space = space;
        this.v = v;
        this.w = w;
        this.material = material;
        this.m = ((float)Math.PI * r * r / 2) * material.p;
        J = 0.5f * m * r * r;
        orientationVector = new Vector2(0, r);
    }

    public synchronized void update() {
        changeSpeed();
        rotate();
        x0 += v.getX() * space.getDT();
        y0 += (v.getY() + v.getY() - space.getG() * space.getDT()) * space.getDT() / 2.0f;

    }

    private synchronized void changeSpeed() {
        v.addY(space.getG() * space.getDT());
    }


    private synchronized void rotate() {
        orientationVector.rotate(w * space.getDT());
        rotateAngle += space.getDT() * w;
        if (rotateAngle > Math.PI * 2){
            rotateAngle -= Math.PI * 2;
        }
    }


    public synchronized void pullFromSphere(SpheresIntersection intersection) {
        if (intersection.getValue() != 0) {
            Point2 nCords = intersection.centralLine.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }

    public synchronized void pullFromLine(SphereToLineIntersection intersection) {
        if (intersection.getValue() != 0){
            Vector2 movementVector = new Vector2(intersection.getCollisionPoint(), getPosition(false));
            Point2 nCords = movementVector.movePoint(new Point2(x0, y0), intersection.getValue());
            this.x0 = nCords.x;
            this.y0 = nCords.y;
        }
    }


    public synchronized Point2 getPosition(boolean mode) {
        float m = mode ? 1.0f : 0.0f;
        return new Point2(x0 + m * v.getX() * space.getDT(), y0 + m * ((v.getY() + v.getY() + space.getG() * space.getDT()) * space.getDT() / 2.0f));
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void setSprite(String path) throws IOException {
        sprite = ImageReader.read(path);
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null){
            Graphics2D g2d = (Graphics2D) g;
            AABB aabb = new AABB(this, false);
            AffineTransform backup = g2d.getTransform();
            AffineTransform tx = AffineTransform.getRotateInstance(rotateAngle, x0, y0);
            g2d.setTransform(tx);

            g2d.drawImage(sprite, Tools.transformFloat(aabb.getMin().x) -1, Tools.transformFloat(aabb.getMin().y) - 1,
                    Tools.transformFloat(aabb.getMax().x - aabb.getMin().x) + 2,
                    Tools.transformFloat(aabb.getMax().y - aabb.getMin().y) + 2, null);

            g2d.setTransform(backup);

        }
    }

    public Vector2 getV() {
        return v;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    @Override
    public void rotate(float a){
    }

    @Override
    public void move(Vector2 movement) {
        x0 += movement.getX();
        y0 += movement.getY();

    }

    @Override
    public void setCords(Point2 newCords) {
        x0 = newCords.x;
        y0 = newCords.y;
    }

    public void setV(Vector2 v) {
        this.v = v;
    }

    public Material getMaterial() {
        return material;
    }

    public float getM() {
        assert m == 0: "Mass is null";
        return m;
    }

    public float getJ() {
        return J;
    }

    public float getR() {
        assert r == 0: "Radius is null";
        return r;
    }
}


