import edu.princeton.cs.algs4.Queue;

import java.util.Observable;

public class MazeBreadthFirstPaths extends MazeExplorer {
   
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    private void bfs(int s) {
        Queue<Integer> q = new Queue<>();

        q.enqueue(s);

        marked[s] = true;

        while (!q.isEmpty()) {
            int v = q.dequeue();           

            for (int w : maze.adj(v)) {    
                if (!marked[w]) {
                    q.enqueue(w);          
                    marked[w] = true;      
                    edgeTo[w] = v;         

                    distTo[w] = distTo[v] + 1;
                    announce();

                    if (w == t) {
                        targetFound = true;
                    }

                    if (targetFound) {
                        return;
                    }
                }
            }
        }



    }


    @Override
    public void solve() {
        bfs(s);
    }
}
