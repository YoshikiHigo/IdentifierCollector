package yoshikihigo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class IdentifierComparator {

	public static void main(final String[] args) {
		final List<File> projects = getProjects(args[0]);
		final List<SortedSet<String>> identifiersList = new ArrayList<>();
		for (final File project : projects) {
			final CollectFromProject collector = new CollectFromProject(project);
			collector.collect();
			identifiersList.add(collector.identifiers);
		}

		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(
				args[1]))) {

			for (int i = 0; i < identifiersList.size(); i++) {
				for (int j = 0; j < identifiersList.size(); j++) {

					if ((i != j) && (0 < identifiersList.get(i).size())
							&& (0 < identifiersList.get(j).size())) {
						final float coefficient = getCoefficient(
								identifiersList.get(i), identifiersList.get(j));
						writer.write(projects.get(i).getName());
						writer.write(", ");
						writer.write(projects.get(j).getName());
						writer.write(", ");
						writer.write(Float.toString(coefficient));
						writer.newLine();
					}
				}
			}

		} catch (final IOException e) {

		}

	}

	private static List<File> getProjects(final String path) {
		final File root = new File(path);
		final File[] files = root.listFiles();
		final List<File> projects = new ArrayList<>();
		for (final File file : files) {
			if (file.isDirectory()) {
				projects.add(file);
			}
		}
		return projects;
	}

	private static float getCoefficient(final SortedSet<String> identifiers1,
			final SortedSet<String> identifiers2) {

		final SortedSet<String> intersection = new TreeSet<>();
		intersection.addAll(identifiers1);
		intersection.retainAll(identifiers2);

		return (float) intersection.size() / (float) identifiers1.size();
	}
}
