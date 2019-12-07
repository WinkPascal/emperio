package com.swinkels.emperio.security;

import java.time.Duration;
import java.time.Instant;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class Hashing {
	public static void createHash() {
		   String password = "Hello World!";
		    Instant beginHash = Instant.now();

		    Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
		    System.out.println(String.format("Creating hash for password '%s'.", password));

		    String hash = argon2.hash(4, 1024 * 1024, 8, password);
		    System.out.println(String.format("Encoded hash is '%s'.", hash));

		    Instant endHash = Instant.now();
		    System.out.println(String.format(
		        "Process took %f s",
		        Duration.between(beginHash, endHash).toMillis() / 1024.0
		        ));

		    Instant beginVerify = Instant.now();
		    System.out.println("Verifying hash...");

		    boolean success = argon2.verify(hash, password);
		    System.out.println(success ? "Success!" : "Failure!");

		    Instant endVerify = Instant.now();
		    System.out.println(String.format(
		        "Process took %f s",
		        Duration.between(beginVerify, endVerify).toMillis() / 1024.0
		        ));
	}

	public boolean readHash() {
		String password = "pascal";
		Instant beginVerify = Instant.now();

		System.out.println("Verifying hash...");
		Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);

		String hash = argon2.hash(4, 1024 * 1024, 8, password);
		boolean success = argon2.verify(hash, password);
		System.out.println(success ? "Success!" : "Failure!");

		Instant endVerify = Instant.now();
		System.out.println(
				String.format("Process took %f s", Duration.between(beginVerify, endVerify).toMillis() / 1024.0));
		return success;
	}

}
