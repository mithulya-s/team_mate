package services;

import base.Participant;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class FormationRunner {

    private final int attempts; // how many independent solutions to try
    private final int threads;  // thread pool size
    private final long baseSeed;

    public FormationRunner(int attempts, int threads) {
        this.attempts = attempts;
        this.threads = threads;
        this.baseSeed = System.nanoTime();
    }

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
            List<Future<TeamBuilder.TeamFormationResult>> futures =
                    pool.invokeAll(tasks);

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
