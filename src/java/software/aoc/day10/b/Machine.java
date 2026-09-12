package software.aoc.day10.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Machine {
    private final int[] targets;
    private final List<int[]> buttons;

    private Machine(int[] targets, List<int[]> buttons) {
        this.targets = targets;
        this.buttons = List.copyOf(buttons);
    }

    public static Machine parse(String line) {
        Matcher targetMatcher = Pattern.compile("\\{([^}]+)\\}").matcher(line);
        if (!targetMatcher.find()) {
            throw new IllegalArgumentException("No se encontraron objetivos de voltaje: " + line);
        }
        String[] targetStrs = targetMatcher.group(1).split(",");
        int[] parsedTargets = new int[targetStrs.length];
        for (int i = 0; i < targetStrs.length; i++) {
            parsedTargets[i] = Integer.parseInt(targetStrs[i].trim());
        }

        List<int[]> parsedButtons = new ArrayList<>();
        Matcher btnMatcher = Pattern.compile("\\(([^)]*)\\)").matcher(line);
        while (btnMatcher.find()) {
            String content = btnMatcher.group(1).trim();
            if (content.isEmpty()) continue;
            String[] parts = content.split(",");
            int[] btn = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                btn[i] = Integer.parseInt(parts[i].trim());
            }
            parsedButtons.add(btn);
        }

        return new Machine(parsedTargets, parsedButtons);
    }

    public int getMinPresses() {
        int numCounters = targets.length;
        int numButtons = buttons.size();

        double[][] a = new double[numCounters][numButtons];
        for (int j = 0; j < numButtons; j++) {
            for (int idx : buttons.get(j)) {
                a[idx][j] += 1.0;
            }
        }

        List<Simplex.Constraint> baseConstraints = new ArrayList<>();
        for (int i = 0; i < numCounters; i++) {
            baseConstraints.add(new Simplex.Constraint(a[i].clone(), Simplex.Relation.EQ, targets[i]));
        }

        double[] cost = new double[numButtons];
        Arrays.fill(cost, 1.0);

        double result = BranchAndBound.solveMinIntegerSum(numButtons, baseConstraints, cost);
        if (Double.isInfinite(result)) {
            throw new IllegalStateException("No se encontro una configuracion valida.");
        }
        return (int) Math.round(result);
    }

    private static class BranchAndBound {
        static double solveMinIntegerSum(int numVars, List<Simplex.Constraint> baseConstraints, double[] cost) {
            double[] best = {Double.POSITIVE_INFINITY};
            recurse(numVars, baseConstraints, cost, new ArrayList<>(), best);
            return best[0];
        }

        private static void recurse(int numVars, List<Simplex.Constraint> baseConstraints,
                                    double[] cost, List<Simplex.Constraint> extra, double[] best) {
            List<Simplex.Constraint> all = new ArrayList<>(baseConstraints);
            all.addAll(extra);

            Simplex.Result r = Simplex.solve(numVars, all, cost);
            if (r.status != Simplex.Status.OPTIMAL) {
                return;
            }
            if (r.objective >= best[0] - 1e-6) {
                return;
            }

            int fracIdx = -1;
            for (int j = 0; j < numVars; j++) {
                double v = r.x[j];
                if (Math.abs(v - Math.round(v)) > 1e-5) {
                    fracIdx = j;
                    break;
                }
            }

            if (fracIdx == -1) {
                double total = Math.round(r.objective);
                if (total < best[0]) {
                    best[0] = total;
                }
                return;
            }

            double v = r.x[fracIdx];
            long floorV = (long) Math.floor(v + 1e-7);
            long ceilV = (long) Math.ceil(v - 1e-7);

            double[] coeffs = new double[numVars];
            coeffs[fracIdx] = 1.0;

            List<Simplex.Constraint> left = new ArrayList<>(extra);
            left.add(new Simplex.Constraint(coeffs.clone(), Simplex.Relation.LE, floorV));
            recurse(numVars, baseConstraints, cost, left, best);

            List<Simplex.Constraint> right = new ArrayList<>(extra);
            right.add(new Simplex.Constraint(coeffs.clone(), Simplex.Relation.GE, ceilV));
            recurse(numVars, baseConstraints, cost, right, best);
        }
    }

    private static class Simplex {
        enum Relation { LE, GE, EQ }
        enum Status { OPTIMAL, INFEASIBLE, UNBOUNDED }

        static class Constraint {
            double[] coeffs;
            Relation relation;
            double rhs;

            Constraint(double[] coeffs, Relation relation, double rhs) {
                this.coeffs = coeffs;
                this.relation = relation;
                this.rhs = rhs;
            }
        }

        static class Result {
            Status status;
            double objective;
            double[] x;
        }

        private static final double BIG_M = 1.0e7;
        private static final double EPS = 1.0e-9;

        static Result solve(int numVars, List<Constraint> rawConstraints, double[] cost) {
            int n = rawConstraints.size();

            double[][] baseA = new double[n][numVars];
            double[] rhs = new double[n];
            Relation[] rel = new Relation[n];

            for (int i = 0; i < n; i++) {
                Constraint c = rawConstraints.get(i);
                double[] coeffs = c.coeffs.clone();
                double r = c.rhs;
                Relation relation = c.relation;
                if (r < 0) {
                    for (int j = 0; j < numVars; j++) coeffs[j] = -coeffs[j];
                    r = -r;
                    if (relation == Relation.LE) relation = Relation.GE;
                    else if (relation == Relation.GE) relation = Relation.LE;
                }
                baseA[i] = coeffs;
                rhs[i] = r;
                rel[i] = relation;
            }

            List<int[]> extraCols = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                switch (rel[i]) {
                    case LE -> extraCols.add(new int[]{0, i});
                    case GE -> { extraCols.add(new int[]{1, i}); extraCols.add(new int[]{2, i}); }
                    case EQ -> extraCols.add(new int[]{2, i});
                }
            }

            int totalCols = numVars + extraCols.size();
            double[][] a = new double[n][totalCols];
            double[] b = rhs.clone();
            for (int i = 0; i < n; i++) {
                System.arraycopy(baseA[i], 0, a[i], 0, numVars);
            }

            double[] colCost = new double[totalCols];
            for (int j = 0; j < numVars; j++) colCost[j] = -cost[j];

            int[] basis = new int[n];
            Arrays.fill(basis, -1);
            boolean[] isArtificial = new boolean[totalCols];

            int colIdx = numVars;
            for (int[] slot : extraCols) {
                int type = slot[0];
                int row = slot[1];
                if (type == 0) {
                    a[row][colIdx] = 1.0;
                    colCost[colIdx] = 0.0;
                    basis[row] = colIdx;
                } else if (type == 1) {
                    a[row][colIdx] = -1.0;
                    colCost[colIdx] = 0.0;
                } else {
                    a[row][colIdx] = 1.0;
                    colCost[colIdx] = -BIG_M;
                    isArtificial[colIdx] = true;
                    if (basis[row] == -1) basis[row] = colIdx;
                }
                colIdx++;
            }

            for (int i = 0; i < n; i++) {
                if (basis[i] == -1) throw new IllegalStateException("Falta columna base en la fila " + i);
            }

            int maxIter = 20000;
            for (int iter = 0; iter < maxIter; iter++) {
                double[] cB = new double[n];
                for (int i = 0; i < n; i++) cB[i] = colCost[basis[i]];

                int entering = -1;
                double bestReduced = EPS * 100;
                for (int j = 0; j < totalCols; j++) {
                    double zj = 0.0;
                    for (int i = 0; i < n; i++) {
                        if (cB[i] != 0.0) zj += cB[i] * a[i][j];
                    }
                    double reduced = colCost[j] - zj;
                    if (reduced > bestReduced) {
                        bestReduced = reduced;
                        entering = j;
                    }
                }

                if (entering == -1) break;

                int leaving = -1;
                double bestRatio = Double.POSITIVE_INFINITY;
                for (int i = 0; i < n; i++) {
                    if (a[i][entering] > 1e-9) {
                        double ratio = b[i] / a[i][entering];
                        if (ratio < bestRatio - 1e-12 || (Math.abs(ratio - bestRatio) < 1e-9 && (leaving == -1 || basis[i] < basis[leaving]))) {
                            bestRatio = ratio;
                            leaving = i;
                        }
                    }
                }

                if (leaving == -1) {
                    Result unbounded = new Result();
                    unbounded.status = Status.UNBOUNDED;
                    return unbounded;
                }

                double pivot = a[leaving][entering];
                for (int j = 0; j < totalCols; j++) a[leaving][j] /= pivot;
                b[leaving] /= pivot;

                for (int i = 0; i < n; i++) {
                    if (i == leaving) continue;
                    double factor = a[i][entering];
                    if (Math.abs(factor) < 1e-12) continue;
                    for (int j = 0; j < totalCols; j++) {
                        a[i][j] -= factor * a[leaving][j];
                    }
                    b[i] -= factor * b[leaving];
                }

                basis[leaving] = entering;
            }

            for (int i = 0; i < n; i++) {
                if (isArtificial[basis[i]] && b[i] > 1e-6) {
                    Result infeasible = new Result();
                    infeasible.status = Status.INFEASIBLE;
                    return infeasible;
                }
            }

            double[] x = new double[numVars];
            for (int i = 0; i < n; i++) {
                if (basis[i] < numVars) x[basis[i]] = b[i];
            }

            double objective = 0.0;
            for (int j = 0; j < numVars; j++) objective += cost[j] * x[j];

            Result result = new Result();
            result.status = Status.OPTIMAL;
            result.objective = objective;
            result.x = x;
            return result;
        }
    }
}