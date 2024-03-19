package info.uaic.proiect.algorithms;

import info.uaic.proiect.algorithms.utils.Pair;
import info.uaic.proiect.models.Dormitory;
import info.uaic.proiect.models.Room;
import info.uaic.proiect.models.Student;

import java.util.*;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 200;
    private static final int MAX_GENERATION = 1000;
    private static final double MUTATION_RATE = 0.01;
    private static final double CROSSOVER_RATE = 0.3;
    private static final int ELITISM = 20;
    private static final double SELECTION_PRESSURE = 2;

    private List<Student> students;
    private Dormitory dormitory;
    private List<int[]> population;
    private int numberOfStudents;
    private int maxNumberOfPreferences = 0;

    private int[] bestSolution;
    private int bestValue = Integer.MAX_VALUE;

    public GeneticAlgorithm(List<Student> students, Dormitory dormitory) {
        this.students = students;
        this.dormitory = dormitory;

        for(Student student : students) {
            if(student.getPreferencesStudents() == null)
                continue;

            if(student.getPreferencesStudents().size() > maxNumberOfPreferences) {
                maxNumberOfPreferences = student.getPreferencesStudents().size();
            }
        }
    }

    public void runGeneticAlgorithm() {
        generateStartingPopulation();

        for (int i = 0; i < MAX_GENERATION; i++) {
            selection();
            mutation();
            crossOver();

            int currentSize = population.size();

            for(int j = 0; j < currentSize; j++) {
                if(evaluate(j) < bestValue) {
                    bestValue = evaluate(j);
                    bestSolution = population.get(j);
                }
            }
        }

        List<Room> rooms = dormitory.getRooms();
        for(int i = 0; i < numberOfStudents; i++) {
            students.get(i).setAssignedRoom(rooms.get(bestSolution[i]));
        }

        System.out.println(bestValue);
    }



    private void generateStartingPopulation() {
        numberOfStudents = students.size();
        population = new ArrayList<>();
        List<Room> rooms = dormitory.getRooms();
        int numberOfRooms = rooms.size();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            Random random = new Random();
            List<Integer> temp = new ArrayList<>();

            for (int j = 0; j < numberOfRooms; j++) {
                for (int k = 0; k < rooms.get(j).getCapacity(); k++) {
                    temp.add(j);
                }
            }
            int[] individ = new int[numberOfStudents];

            int count = 0;
            while (count < numberOfStudents - 1) {
                int index = random.nextInt(temp.size());
                individ[count] = temp.get(index);
                temp.remove(index);
                count++;
            }
            individ[count] = temp.get(0);
            temp.remove(0);
            population.add(individ);
        }

        //System.out.println("hi");
    }

    private int evaluate(int individualIndex) {
        int fitness = 0;

        for (int i = 0; i < numberOfStudents; i++) {
            for (int j = 0; j < numberOfStudents; j++) {
                if (i == j)
                    continue;

                if(population.get(individualIndex)[i] != population.get(individualIndex)[j])
                    continue;

                if(students.get(i).getPreferencesStudents() == null) {
                    fitness += maxNumberOfPreferences;
                    continue;
                }

                int priority = students.get(i).getPreferencesStudents().indexOf(students.get(j));

                if (priority == -1) {
                    fitness += students.get(i).getPreferencesStudents().size();
                    continue;
                }

                fitness += priority;
            }
        }

        return fitness;
    }

    private void selection() {
        List<Double> f = new ArrayList<>();
        double max = Integer.MIN_VALUE;
        int pmaxim = -1;
        double sum = 0;
        int currentPopSize = population.size();

        for (int i = 0; i < currentPopSize; i++) {
            if(evaluate(i) == 0)
                f.add(0.0);
            else
                f.add(1.0 * evaluate(i));

            if (max < f.get(i)) {
                max = f.get(i);
                pmaxim = i;
            }
            sum += f.get(i);
        }

        List<Double> p = new ArrayList<>();
        for (int i = 0; i < currentPopSize; i++) {
            p.add(f.get(i) / sum);
        }

        List<Double> q = new ArrayList<>();
        q.add(p.get(0));
        for (int i = 1; i < currentPopSize; i++) {
            q.add(q.get(i - 1) + p.get(i));
        }

        List<int[]> newPop = new ArrayList<>();
        for (int i = 0; i < ELITISM; i++) {
            newPop.add(population.get(pmaxim));
        }
        for (int i = ELITISM; i < POPULATION_SIZE; i++) {
            double r = Math.random();
            for (int j = 0; j < currentPopSize; j++) {
                if (r < q.get(j)) {
                    newPop.add(population.get(j));
                    break;
                }
            }
        }

        population = newPop;
    }

    void mutation() {
        int popSize = population.size();
        for (int i = 0; i < popSize; i++) {
            Random random = new Random();
            for (int j = 0; j < numberOfStudents; j++) {
                if (Math.random() < MUTATION_RATE) {
                    int temp = population.get(i)[j];
                    int swapPosition = random.nextInt(numberOfStudents);
                    population.get(i)[j] = population.get(i)[swapPosition];
                    population.get(i)[swapPosition] = temp;
                }
            }
        }
    }

    int[] repairSolution(int[] child) {
        List<Integer> roomsWithTooMany = new ArrayList<>(); // salvez index-urile
        List<Integer> roomsWithTooFew = new ArrayList<>(); // salvez index-urile
        List<Room> rooms = dormitory.getRooms();
        int[] studentsInARoom = new int[rooms.size()];
        Random random = new Random();

        for(int i = 0; i < numberOfStudents; i++) {
            studentsInARoom[child[i]]++;

            if(studentsInARoom[child[i]] > rooms.get(child[i]).getCapacity()) { // prea multi in camera
                if(roomsWithTooFew.isEmpty()) {
                    roomsWithTooMany.add(i);
                }
                else {
                    int randomIndex = random.nextInt(roomsWithTooFew.size());
                    studentsInARoom[child[i]]--;

                    child[i] = child[roomsWithTooFew.get(randomIndex)];
                    studentsInARoom[child[roomsWithTooFew.get(randomIndex)]]++;
                }
            }

            if(studentsInARoom[child[i]] < rooms.get(child[i]).getCapacity()) { // prea putini in camera
                if(roomsWithTooMany.isEmpty()) {
                    roomsWithTooFew.add(i);
                }
                else {
                    int randomIndex = random.nextInt(roomsWithTooMany.size());
                    studentsInARoom[child[i]]++;

                    child[roomsWithTooMany.get(randomIndex)] = child[i];
                    studentsInARoom[child[roomsWithTooFew.get(randomIndex)]]--;
                }
            }
        }

        return child;
    }

    void crossOver() {
        if(numberOfStudents <= 1) {
            return;
        }

        int pop_size = population.size();
        List<Pair<Integer, Double>> cxProbabilities = new ArrayList<>();

        for (int i = 0; i < pop_size; i++) {
            cxProbabilities.add(new Pair<>(i, Math.random()));
        }

        cxProbabilities.sort(Comparator.comparingDouble(Pair::getSecond));

        int nrCrossOvers = 0;
        while (cxProbabilities.get(nrCrossOvers).getSecond() <= CROSSOVER_RATE) {
            nrCrossOvers++;
        }
        nrCrossOvers--;
        if (nrCrossOvers % 2 == 0) {
            if (Math.random() <= 0.5) {
                nrCrossOvers++;
            } else {
                nrCrossOvers--;
            }
        }

        for (int i = 0; i <= nrCrossOvers; i += 2) {
            Random random = new Random();
            int firstParentIndex = cxProbabilities.get(i).getFirst();
            int secondParentIndex = cxProbabilities.get(i + 1).getFirst();

            int leftCutIndex = random.nextInt(numberOfStudents - 1); // sa nu il ia pe ultimul
            int rightCutIndex = random.nextInt(leftCutIndex + 1, numberOfStudents); // + 1 sa nu ia aceiasi stanga, -1 sa nu iasa din lista

            int[] firstChild = new int[numberOfStudents];
            int[] secondChild = new int[numberOfStudents];

            for(int j = 0; j < numberOfStudents; j++) {
                if(j >= leftCutIndex && j <= rightCutIndex) {
                    firstChild[j] = population.get(secondParentIndex)[j]; // stanga si dreapta inclusiv [ ]
                    secondChild[j] = population.get(firstParentIndex)[j];
                }

                firstChild[j] = population.get(firstParentIndex)[j];
                secondChild[j] = population.get(secondParentIndex)[j];

                firstChild = repairSolution(firstChild);
                secondChild = repairSolution(secondChild);

                population.add(firstChild);
                population.add(secondChild);
            }
        }
    }
}
