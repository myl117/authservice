package com.myl117.authservice.authservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		System.out.println("DB URL: " + System.getenv("DB_URL"));
		System.out.println("DB Username: " + System.getenv("DB_USERNAME"));

		SpringApplication.run(AuthserviceApplication.class, args);
	}
	
}

/*
 * Routes for auth service:
 * POST /signup
 * 	- Hash password with BCrypt.
 * 	- Save user to DB with status = PENDING_VERIFICATION.
 *  - Send email with verification token.
 * 
 * GET /verify?token=
 * 	- Look up user by token.
 *  - If valid → set status = ACTIVE
 * 
 * POST /signin
 *  - Look up user by credentials
 *  - If not valid, return status code 401
 * 	- else send JWT token
 * 
 * POST /reset
 * 
 * POST /changepassword
 * 
 * GET /status
 *   - Returns auth status and how long before session expires
*/