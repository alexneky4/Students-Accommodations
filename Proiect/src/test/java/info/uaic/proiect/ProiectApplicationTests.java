package info.uaic.proiect;

import info.uaic.proiect.algorithms.GeneticAlgorithm;
import info.uaic.proiect.models.Dormitory;
import info.uaic.proiect.models.Room;
import info.uaic.proiect.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ProiectApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	void testGeneticAlgorithm() {
		List<Student> students = new ArrayList<>();

		Student s1 = new Student("s1");
		Student s2 = new Student("s2");
		Student s3 = new Student("s3");
		Student s4 = new Student("s4");
		Student s5 = new Student("s5");
		Student s6 = new Student("s6");
		Student s7 = new Student("s7");
		Student s8 = new Student("s8");
		Student s9 = new Student("s9");

		s1.setPreferencesStudents(new ArrayList<>(Arrays.asList(s5,s2,s6,s4)));
		s2.setPreferencesStudents(new ArrayList<>(Arrays.asList(s1,s9,s4,s3)));
		s3.setPreferencesStudents(new ArrayList<>(Arrays.asList(s6,s8,s7)));
		s4.setPreferencesStudents(new ArrayList<>(Arrays.asList(s9,s2)));
		s5.setPreferencesStudents(new ArrayList<>(Arrays.asList(s3)));
		s6.setPreferencesStudents(new ArrayList<>(Arrays.asList(s4,s2)));
		s7.setPreferencesStudents(new ArrayList<>(Arrays.asList(s1)));

		students.add(s1);
		students.add(s2);
		students.add(s3);
		students.add(s4);
		students.add(s5);
		students.add(s6);
		students.add(s7);
		students.add(s8);
		students.add(s9);

		Dormitory dormitory = new Dormitory("d1");

		List<Room> rooms = new ArrayList<>();
		rooms.add(new Room(1, 3));
		rooms.add(new Room(2, 3));
		rooms.add(new Room(3, 3));

		dormitory.setRooms(rooms);

		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(students, dormitory);
		geneticAlgorithm.runGeneticAlgorithm();

		System.out.println(students);
		assert true;
	}

	@Test
	void testGeneticAlgorithm2InaRoom() {
		List<Student> students = new ArrayList<>();

		Student charlie = new Student("Charlie");
		Student peter = new Student("Peter");
		Student elise = new Student("Elise");
		Student paul = new Student("Paul");
		Student kelly = new Student("Kelly");
		Student sam = new Student("Sam");

		charlie.setPreferencesStudents(new ArrayList<>(Arrays.asList(peter, paul, sam, kelly, elise)));
		peter.setPreferencesStudents(new ArrayList<>(Arrays.asList(kelly, elise, sam, paul, charlie)));
		elise.setPreferencesStudents(new ArrayList<>(Arrays.asList(peter, sam, kelly, charlie ,paul)));
		paul.setPreferencesStudents(new ArrayList<>(Arrays.asList(elise, charlie, sam, peter, kelly)));
		kelly.setPreferencesStudents(new ArrayList<>(Arrays.asList(peter, charlie, sam, elise, paul)));
		sam.setPreferencesStudents(new ArrayList<>(Arrays.asList(charlie, paul, kelly, elise, peter)));

		Dormitory dormitory = new Dormitory("d1");

		List<Room> rooms = new ArrayList<>();
		rooms.add(new Room(1, 2));
		rooms.add(new Room(2, 2));
		rooms.add(new Room(3, 2));

		students.add(charlie);
		students.add(peter);
		students.add(elise);
		students.add(paul);
		students.add(kelly);
		students.add(sam);

		dormitory.setRooms(rooms);

		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(students, dormitory);
		geneticAlgorithm.runGeneticAlgorithm();

		System.out.println(students);
		assert true;
	}


}
