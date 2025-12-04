package services;

import base.Participant;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/*
 Executes multiple concurrent attempts at team formation.
 This class:
    - Runs formation tasks across threads with randomized shuffling.
    - Collects results and selects the best team distribution.
    - Concurrency is used to form balanced and component teams.
    - ExecutorService is used for controlled parallel execution.
 */

public class FormationRunner {

    private final int attempts;
    private final int threads;
    private final long baseSeed;

    public FormationRunner(int attempts, int threads) {
        this.attempts = attempts;
        this.threads = threads;
        this.baseSeed = System.nanoTime();
    }


    /*
     - This runs multiple team formation attempts in parallel.
     - Each attempt shuffles participants with a different seed and finally collects results and picks the best distribution.
     */

    public TeamBuilder.TeamFormationResult runFormationThreads(List<Participant> participants, int teamSize) {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Callable<TeamBuilder.TeamFormationResult>> tasks = new ArrayList<>();

        for (int i = 0; i < attempts; i++) {
            final int attemptIndex = i;

            tasks.add(() -> {
                List<Participant> copy = new ArrayList<>(participants);
                Collections.shuffle(copy, new Random(baseSeed + attemptIndex));
                return TeamBuilder.formTeams(copy, teamSize);
            });
        }

        try {
            List<Future<TeamBuilder.TeamFormationResult>> futures = pool.invokeAll(tasks);

            List<TeamBuilder.TeamFormationResult> results =
                    futures.stream()
                            .map(f -> {
                                try { return f.get(); }
                                catch (Exception e) { return null; }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

            pool.shutdown();

            return pickBest(results);

        } catch (InterruptedException e) {
            pool.shutdownNow();
            return TeamBuilder.formTeams(participants, teamSize);
        }
    }

    private TeamBuilder.TeamFormationResult pickBest(List<TeamBuilder.TeamFormationResult> results) {

        return results.stream()
                .min(Comparator
                        .comparingInt(this::pooledSize)
                        .thenComparingDouble(this::skillVariance)
                ).orElse(results.get(0));
    }

    private int pooledSize(TeamBuilder.TeamFormationResult r) {
        return r.getPooledParticipants().size();
    }
    private double skillVariance(TeamBuilder.TeamFormationResult r) {
        List<Double> means = r.getFormedTeams().stream()
                .map(t -> t.getMembers().stream()
                        .mapToInt(Participant::getSkillLevel)
                        .average()
                        .orElse(0))
                .toList();

        double avg = means.stream().mapToDouble(d -> d).average().orElse(0);
        return means.stream()
                .mapToDouble(m -> Math.pow(m - avg, 2))
                .average()
                .orElse(0);
    }
}
