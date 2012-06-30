package com.alta189.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FileList implements List<File> {
	public List<File> files = new ArrayList<File>();

	@Override
	public boolean add(File file) {
		return files.add(file);
	}

	public void add(File directory, String pattern) {
		String pttn = pattern;
		boolean recursive = false;
		String fileSepartor = null;

		if (pattern.contains("/")) {
			fileSepartor = "/";
		} else if (pattern.contains("\\")) {
			fileSepartor = "\\";
		}

		if (fileSepartor != null) {
			recursive = true;
			int i = pattern.lastIndexOf("/") + 1;
			if (i < pattern.length()) {
				pttn = pattern.substring(i);
			}
		}

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				if (recursive) {
					add(file, pattern);
				}
			} else {
				if (matches(file.getName(), pttn)) {
					add(file);
				}
			}
		}
	}

	public void remove(File directory, String pattern) {
		String pttn = pattern;
		boolean recursive = false;
		String fileSepartor = null;

		if (pattern.contains("/")) {
			fileSepartor = "/";
		} else if (pattern.contains("\\")) {
			fileSepartor = "\\";
		}

		if (fileSepartor != null) {
			recursive = true;
			int i = pattern.lastIndexOf("/") + 1;
			if (i < pattern.length()) {
				pttn = pattern.substring(i);
			}
		}

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				if (recursive) {
					remove(file, pattern);
				}
			} else {
				if (matches(file.getName(), pttn)) {
					remove(file);
				}
			}
		}

	}

	public boolean matches(String name, String pattern) {
		return name.matches(pattern.replace(".", "\\.").replace("*", ".*"));
	}

	@Override
	public int size() {
		return files.size();
	}

	@Override
	public boolean isEmpty() {
		return files.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return files.contains(o);
	}

	@Override
	public Iterator<File> iterator() {
		return files.iterator();
	}

	@Override
	public Object[] toArray() {
		return files.toArray();
	}

	@Override
	public <File> File[] toArray(File[] a) {
		return files.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return files.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return files.contains(c);
	}

	@Override
	public boolean addAll(Collection<? extends File> c) {
		return files.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends File> c) {
		return files.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return files.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return files.retainAll(c);
	}

	@Override
	public void clear() {
		files.clear();
	}

	@Override
	public File get(int index) {
		return files.get(index);
	}

	@Override
	public File set(int index, File element) {
		return files.set(index, element);
	}

	@Override
	public void add(int index, File element) {
		files.add(index, element);
	}

	@Override
	public File remove(int index) {
		return files.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return files.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return files.lastIndexOf(o);
	}

	@Override
	public ListIterator<File> listIterator() {
		return files.listIterator();
	}

	@Override
	public ListIterator<File> listIterator(int index) {
		return files.listIterator(index);
	}

	@Override
	public List<File> subList(int fromIndex, int toIndex) {
		return files.subList(fromIndex, toIndex);
	}
}
