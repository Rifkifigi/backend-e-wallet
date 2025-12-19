package com.ewallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(EWalletApplication.class, args);
        System.out.println("\n=================================================");
        System.out.println("🚀 E-Wallet Application Started Successfully!");
        System.out.println("=================================================");
        System.out.println("📱 Application: http://localhost:8080");
        System.out.println("💾 H2 Console: http://localhost:8080/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:ewalletdb");
        System.out.println("   Username: sa");
        System.out.println("   Password: (kosong)");
        System.out.println("=================================================\n");
    }
}
