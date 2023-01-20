package com.github.theprogmatheus.zonadelivery.server;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLine implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		try (Scanner scanner = new Scanner(System.in)) {
			String commandLine;
			while (scanner.hasNext() && (commandLine = scanner.nextLine()) != null) {
				System.out.println("Executar linha de comando: " + commandLine);
			}
		}
	}
}
