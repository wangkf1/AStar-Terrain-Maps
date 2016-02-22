
import java.awt.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarExp_999298402_998848125 implements AIModule {

    private double getGValue(Map<Point, Double> gValues, Point pt) {
        if (!gValues.containsKey(pt)) {
            return Double.POSITIVE_INFINITY;
        }
        return gValues.get(pt);
    }

    private double getFValue(Map<Point, Double> fValues, Point pt) {
        if (!fValues.containsKey(pt)) {
            return Double.POSITIVE_INFINITY;
        }
        return fValues.get(pt);
    }

    public List<Point> createPath(TerrainMap map) {
        HashMap<Point, Point> predecessors = new HashMap<Point, Point>();
        HashSet<Point> closedSet = new HashSet<Point>();
        final HashMap<Point, Double> gValues = new HashMap<Point, Double>();
        final HashMap<Point, Double> fValues = new HashMap<Point, Double>();
        
        PriorityQueue<Point> openSet = new PriorityQueue<Point>(1, new Comparator<Point>(){
            @Override
            public int compare(Point one, Point two) {
                double twoScore;
                double oneScore = (Double)fValues.get(one);
                if (oneScore < (twoScore = ((Double)fValues.get(two)).doubleValue())) {
                    return -1;
                }
                if (oneScore > twoScore) {
                    return 1;
                }
                if (one.x < two.x) {
                    return -1;
                }
                if (one.x > two.x) {
                    return 1;
                }
                if (one.y < two.y) {
                    return -1;
                }
                if (one.y > two.y) {
                    return 1;
                }
                return 0;
            }
        });

        gValues.put(map.getStartPoint(), 0.0);
        fValues.put(map.getStartPoint(), this.getHeuristic(map, map.getStartPoint(), map.getEndPoint()));
        openSet.add(map.getStartPoint());

        while (!openSet.isEmpty()) {
            Point current = (Point)openSet.poll();
            openSet.remove(current);
            if (current.equals(map.getEndPoint())) {
                break;
            }
            closedSet.add(current);

            Point [] neighbors = map.getNeighbors(current);
            for (int j = 0; j < neighbors.length; j++) {
                double g_value;

                if (closedSet.contains(neighbors[j]) || (g_value = (Double)gValues.get(current) + map.getCost(current, neighbors[j])) >= this.getGValue(gValues, neighbors[j])) 
                    continue;
                predecessors.put(neighbors[j], current);
                if (gValues.containsKey(neighbors[j])) {
                    openSet.remove(neighbors[j]);
                }
                gValues.put(neighbors[j], g_value);
                fValues.put(neighbors[j], g_value + this.getHeuristic(map, neighbors[j], map.getEndPoint()));
                openSet.add(neighbors[j]);
            }
        }
        
        LinkedList<Point> path = new LinkedList<Point>();
        Point current = map.getEndPoint();
        while (current != null) {
            path.add(0, current);
            current = predecessors.get(current);
        }
        return path;
    }

    private double getHeuristic(final TerrainMap map, final Point pt1, final Point pt2){
        double z1 = map.getTile(pt1);
        double z2 = map.getTile(pt2);
        double dist = Math.max(Math.abs(pt1.x-pt2.x),  Math.abs(pt1.y-pt2.y));
        double avgStep = ((z2-z1)/dist);

        if(z2==z1){
            return dist;
        }
        else if(z2 > z1){
            if(avgStep<=1){
                return (z2-z1)*Math.exp(1) + (dist - z2 + z1);
            }
            else{
                return Math.exp(avgStep)*dist;
            }
        }
        else{
            if(avgStep<=1){
                return (z1-z2)*Math.exp(-1) + (dist - z1 + z2);
            }
            else{
                return Math.exp(-avgStep)*dist;
            }
        }
    }
}